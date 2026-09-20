# AWS vs. Azure: full deployment and production cycle

Current as of **September 16, 2026**. This comparison is specific to this repository: a Java 25 Spring Boot backend built with Gradle, a React/TypeScript/Vite frontend, and PostgreSQL.

## Bottom line

The software-delivery cycle is about **85% the same** on AWS and Azure:

```text
write code -> test -> build immutable artifact -> provision infrastructure
-> deploy staging -> migrate database -> health/smoke check -> promote
-> monitor -> scale -> roll back or restore -> retire resources
```

The meaningful difference is abstraction:

- **Azure is the shorter first production path for this project:** Static Web Apps + App Service Java SE + Azure Database for PostgreSQL + Key Vault + Azure Monitor. App Service directly supports Java 25, and the user's Azure for Students credit makes real practice inexpensive.
- **AWS exposes more infrastructure sooner:** Amplify Hosting + Elastic Beanstalk Java SE + RDS PostgreSQL + Secrets Manager + CloudWatch. Elastic Beanstalk manages the EC2, Auto Scaling, and load-balancer layer, but those resources and their interactions remain visible.
- **Containers are optional on both.** ECR + App Runner/ECS and ACR + Container Apps are useful when container portability is itself a goal. They add a registry, image lifecycle, and container security work that this application does not yet need.

For this repository, start with **Azure App Service**, not a container rewrite. Learn AWS later by mapping the same concepts.

## The simplest credible production architectures

### Azure

```text
GitHub
  |-- GitHub Actions: test/build frontend -> Azure Static Web Apps
  `-- GitHub Actions: test/build JAR -> App Service staging slot -> swap

Browser -> Static Web Apps (React/Vite, HTTPS, custom domain)
              `-- /api/* proxy -> App Service (Spring Boot / Java 25)
                                      |-- Key Vault via managed identity
                                      `-- private connection -> PostgreSQL Flexible Server

Logs/metrics/traces -> Azure Monitor + Application Insights
Infrastructure      -> Bicep (or Terraform)
```

Azure Static Web Apps can proxy `/api/*` to a linked App Service, which matches this frontend's existing relative API paths and avoids browser CORS configuration. That integration requires the **Static Web Apps Standard plan** and is not available in pull-request preview environments. [Microsoft: link App Service as a Static Web Apps API](https://learn.microsoft.com/en-us/azure/static-web-apps/apis-app-service)

### AWS

```text
GitHub
  |-- Amplify build: test/build frontend -> Amplify Hosting
  `-- GitHub Actions or CodePipeline: test/build JAR -> Elastic Beanstalk

Browser -> Amplify Hosting (React/Vite, CDN, HTTPS, custom domain)
              `-- /api/* reverse proxy -> Elastic Beanstalk
                                            |-- Secrets Manager via IAM role
                                            `-- private connection -> RDS PostgreSQL

Logs/metrics/traces -> CloudWatch (+ X-Ray when tracing is enabled)
Infrastructure      -> CloudFormation/CDK (or Terraform)
```

Amplify Hosting supports Git-based continuous deployment, atomic frontend releases, preview branches, custom domains, and SPA rewrites. It can also reverse-proxy a path to an external API endpoint. [AWS: Amplify Hosting overview](https://docs.aws.amazon.com/amplify/latest/userguide/welcome.html) [AWS: redirects, rewrites, and reverse proxy](https://docs.aws.amazon.com/amplify/latest/userguide/redirects.html)

## Service mapping: equivalent versus structurally different

| Need | AWS | Azure | How equivalent are they? |
|---|---|---|---|
| Static React hosting | Amplify Hosting | Static Web Apps | **Close equivalents.** Both build from Git, use a CDN, manage HTTPS/custom domains, and support preview environments. Their API-linking rules and pricing differ. |
| Direct Spring Boot JAR hosting | Elastic Beanstalk Java SE | App Service Java SE | **Same job, different structure.** App Service presents an app on a plan. Beanstalk presents an environment backed by EC2, Auto Scaling, proxy/load balancer, security groups, and an application version. |
| Simple managed container web app | App Runner | Container Apps | **Close conceptual equivalents.** Both run OCI images, autoscale, expose HTTPS, and integrate with managed identity/secrets. Container Apps has first-class revisions and traffic weights; App Runner has a simpler service model but weaker progressive-delivery controls. |
| Larger container platform | ECS on Fargate | Container Apps or AKS | **Not one-to-one.** ECS is an orchestrator with explicit services/tasks/networking; Container Apps hides more Kubernetes machinery. AKS is closer to EKS than ECS. |
| Container registry | ECR | ACR | **Direct equivalents.** |
| Managed PostgreSQL | RDS for PostgreSQL | Azure Database for PostgreSQL Flexible Server | **Direct equivalents** for backups, HA, patching, encryption, private networking, and replicas, with different SKUs and identity systems. |
| Secret store | Secrets Manager / Parameter Store | Key Vault | **Close equivalents.** IAM roles versus managed identities/RBAC are the main vocabulary and policy difference. |
| Identity | IAM roles and policies | Microsoft Entra ID, managed identity, Azure RBAC | **Same purpose, structurally different.** Azure splits directory identity and resource authorization more visibly; AWS centers resource access in IAM. |
| Logs/metrics/alerts | CloudWatch | Azure Monitor / Log Analytics | **Direct operational equivalents.** Query languages, defaults, and pricing differ. |
| Distributed tracing | X-Ray / CloudWatch tracing | Application Insights | **Equivalent outcome.** Both need an agent or OpenTelemetry instrumentation for useful application traces. |
| DNS and certificates | Route 53 + ACM | Azure DNS + App Service/Static Web Apps managed certificates | **Equivalent outcome.** Azure's PaaS certificate flow is more bundled; AWS makes DNS, certificate, and load-balancer bindings separate resources. |
| Native IaC | CloudFormation or CDK | ARM/Bicep | **Equivalent outcome.** CloudFormation/CDK manages stacks; Bicep deploys through Azure Resource Manager and normally groups lifecycle by resource group. |
| Native pipeline | CodePipeline/CodeBuild | Azure Pipelines | **Direct equivalents.** GitHub Actions is simpler here because the code is already on GitHub. |

## Lifecycle comparison

### 1. Local development and validation

Cloud choice changes nothing at this stage. The current repository's verification commands remain:

```powershell
cd C:\Users\drobe\IdeaProjects\simple-bank\backend
.\gradlew.bat test
.\gradlew.bat bootJar

cd C:\Users\drobe\IdeaProjects\simple-bank\frontend
npm ci
npm run lint
npm run build
```

Outputs are a Spring Boot executable JAR under `backend\build\libs\` and static frontend files under `frontend\dist\`.

The production rule on either cloud is: **build once and promote the same version**. Identify the backend artifact and frontend release by the Git commit SHA; do not rebuild different bits for staging and production.

### 2. Frontend build and hosting

| Step | AWS | Azure |
|---|---|---|
| Locate frontend in this monorepo | Set Amplify's monorepo app root to `frontend`; Amplify supports monorepo build settings. [AWS](https://docs.aws.amazon.com/amplify/latest/userguide/monorepo-configuration.html) | Set Static Web Apps `app_location` to `frontend` and output to `dist`; its GitHub workflow builds and publishes React apps. [Microsoft](https://learn.microsoft.com/en-us/azure/static-web-apps/deploy-react) |
| Install/build | `npm ci`, `npm run lint`, `npm run build` in Amplify build settings | Same commands in GitHub Actions / Static Web Apps action |
| SPA routes | Add the Amplify 200 rewrite to `index.html` | Configure the Static Web Apps navigation fallback |
| Atomic release | Amplify publishes only after the complete frontend deployment is ready | Static Web Apps publishes the completed artifact and handles cache invalidation |
| Preview | Connect feature branches / pull-request previews | GitHub Actions can create preproduction environments |
| TLS/domain | Amplify default domain plus managed certificate or custom certificate [AWS](https://docs.aws.amazon.com/amplify/latest/userguide/custom-domains.html) | Default TLS plus free managed certificate for a custom domain [Microsoft](https://learn.microsoft.com/en-us/azure/static-web-apps/custom-domain) |

**Repository-specific issue:** `vite.config.ts` proxies `/api` only during `npm run dev`; that proxy is absent from `frontend/dist`. Production must therefore either:

1. preserve same-origin `/api` through the managed proxies described above; or
2. change the frontend to use a configured API base URL and configure CORS on Spring Boot.

The first option is the smaller deployment change. Azure's official App Service linking is especially cohesive, but its Standard-plan cost must be compared with simply serving `frontend/dist` from Spring Boot as one App Service artifact.

### 3. Backend artifact and runtime

#### AWS: Elastic Beanstalk first

Elastic Beanstalk accepts an executable JAR on its **Java SE** platform and currently supports Amazon Corretto 25. It provisions and manages the environment's EC2 instances, reverse proxy, Auto Scaling group, and optional load balancer. [AWS: Java deployment](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/create_deploy_Java.html) [AWS: supported platforms](https://docs.aws.amazon.com/elasticbeanstalk/latest/platforms/platforms-supported.html)

Advantages:

- no Dockerfile or registry is required;
- Java 25 matches this project's toolchain;
- deployment policies include rolling, immutable, and traffic splitting;
- it teaches visible AWS networking, load balancing, scaling, and IAM.

Costs:

- a production-style load-balanced environment has more billable resources and configuration;
- platform and instance maintenance are more visible than App Service;
- safe blue/green commonly means two temporary environments.

#### Azure: App Service first

App Service accepts the executable JAR on its Java SE runtime and currently supports Microsoft/Adoptium OpenJDK 25. [Microsoft: App Service language support](https://learn.microsoft.com/en-us/azure/app-service/language-support-policy) GitHub Actions can build and deploy JAR/WAR packages, and Deployment Center can generate a workflow. [Microsoft: deploy App Service with GitHub Actions](https://learn.microsoft.com/en-us/azure/app-service/deploy-github-actions)

Advantages:

- fewer visible infrastructure pieces;
- native staging slots, warm-up, swap, and swap-back;
- managed identity and Key Vault references fit naturally;
- the App Service-managed Application Insights Java agent can collect requests, dependencies, logs, metrics, and heartbeats without application-code instrumentation. [Microsoft: monitor App Service](https://learn.microsoft.com/en-us/azure/azure-monitor/app/codeless-app-service)

Costs:

- useful production capabilities such as deployment slots, private endpoints, and autoscale require eligible paid tiers;
- an App Service plan reserves compute even when an app is idle;
- hiding infrastructure makes the first deployment easier but teaches fewer load-balancer/VM concepts.

### 4. Container path: when it is actually useful

Containers are not required for this project's first deployment. Add them when a target job asks for Docker, the app needs an OS/runtime not supplied by PaaS, or identical packaging across clouds matters.

| Phase | AWS container path | Azure container path |
|---|---|---|
| Build image | Multi-stage Docker build or buildpacks | Same OCI image; Dockerfile or buildpacks |
| Registry | ECR | ACR |
| Simple runtime | App Runner | Container Apps |
| Release unit | App Runner service deployment | Container Apps revision |
| Progressive delivery | App Runner lacks Container Apps-style per-revision traffic weighting; use a separate service/DNS strategy or move to ECS | Multiple revisions can receive weighted traffic for canary or blue/green releases [Microsoft](https://learn.microsoft.com/en-us/azure/container-apps/revisions-manage) |
| Scale-to-zero | App Runner keeps provisioned capacity according to its service model | Container Apps Consumption can scale Java apps to zero [Microsoft](https://learn.microsoft.com/en-us/azure/container-apps/java-overview) |
| Next complexity tier | ECS on Fargate: explicit task definitions, services, load balancer, subnets, security groups, and deployment controller | Container Apps still hides nodes; AKS is the step to Kubernetes-level control |

App Runner can build from source, but its documented managed Java runtimes are older than this repository's Java 25 requirement; an ECR image avoids that mismatch. App Runner otherwise manages running, scaling, and load balancing the service. [AWS: App Runner Java runtime](https://docs.aws.amazon.com/apprunner/latest/dg/service-source-code-java.html) [AWS: image-based App Runner services](https://docs.aws.amazon.com/apprunner/latest/dg/service-source-image.html)

### 5. Infrastructure as code

Use one native IaC file set per cloud; do not hand-create the final environment in the portal and hope to remember it.

- **AWS:** CloudFormation YAML is the lowest-dependency choice. A stack creates, updates, and deletes related resources as a unit; change sets preview updates. CDK is useful later if the template becomes unwieldy, but it synthesizes CloudFormation and adds a toolchain. [AWS: how CloudFormation works](https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/cloudformation-overview.html)
- **Azure:** Bicep is the lowest-dependency Azure-native choice. It deploys through ARM, supports modules and `what-if`, and does not require maintaining a separate state file. [Microsoft: Bicep overview](https://learn.microsoft.com/en-us/azure/azure-resource-manager/bicep/overview)
- **Cross-cloud:** Terraform is reasonable only if maintaining both deployments is a real goal. For one learning deployment, two providers and remote state are extra machinery.

Azure's resource group is a particularly convenient lab boundary: delete the group to remove its contained resources. AWS achieves similar cleanup only when every resource is in the CloudFormation stack and no resource has a retain policy or external lifecycle.

### 6. CI/CD

The smallest credible GitHub Actions pipeline is the same on both clouds:

```text
pull request
  -> backend tests
  -> frontend lint + build

main branch after review
  -> repeat gates
  -> build versioned JAR and frontend artifact
  -> apply reviewed IaC
  -> deploy staging
  -> run migration once
  -> wait for health + smoke test
  -> promote/swap
  -> retain prior application version for rollback
```

Use **OIDC federation**, not permanent cloud keys in GitHub secrets:

- AWS trusts GitHub as an IAM OIDC provider and lets the workflow assume a narrowly scoped role. [AWS IAM](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-idp_oidc.html)
- Azure recommends GitHub OIDC with a user-assigned identity for App Service deployment when basic authentication is disabled. [Microsoft](https://learn.microsoft.com/en-us/azure/app-service/deploy-github-actions)

Native alternatives are CodePipeline/CodeBuild -> Elastic Beanstalk on AWS and Azure Pipelines -> App Service on Azure. Both work, but moving a GitHub project off GitHub Actions adds a second CI system without improving this portfolio deployment. [AWS: CodePipeline GitHub connections](https://docs.aws.amazon.com/codepipeline/latest/userguide/connections-github.html) [Microsoft: Java App Service with Azure Pipelines](https://learn.microsoft.com/en-us/azure/devops/pipelines/ecosystems/java-webapp)

### 7. PostgreSQL and schema migrations

| Concern | RDS for PostgreSQL | Azure PostgreSQL Flexible Server |
|---|---|---|
| Basic production topology | Private DB subnet/security group; app connects through the VPC | Private access/private endpoint; app connects through VNet Integration |
| Authentication | PostgreSQL password in Secrets Manager first; IAM DB authentication later if justified | PostgreSQL password in Key Vault first; Entra token authentication later if justified |
| HA | Multi-AZ provisions synchronous standby capacity in another AZ [AWS](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/Welcome.html) | Same-zone or zone-redundant HA uses a synchronous standby [Microsoft](https://learn.microsoft.com/en-us/azure/postgresql/flexible-server/concepts-high-availability) |
| Backups | Automated backups/PITR plus manual snapshots | Automated backups/PITR with configurable retention and backup redundancy |
| Scaling | Change instance/storage; replicas for reads | Change compute/storage; replicas for reads |

The repository is **not migration-ready for production**:

- `spring.jpa.hibernate.ddl-auto=update` lets Hibernate modify production schema implicitly;
- `database/schema.sql` is not a versioned release history;
- there is no Flyway or Liquibase dependency.

Before a real production release, introduce versioned migrations and set Hibernate to validation rather than mutation. That is an application readiness issue, not an AWS/Azure difference.

Run each migration **once per release**, before sending full traffic to the new code. A single-instance learning deployment can let Flyway/Liquibase migrate at startup because they use a schema-history lock. With multiple replicas or canary traffic, run migrations as a dedicated release step/job. Use backward-compatible expand/migrate/contract changes because neither Beanstalk rollback nor an App Service slot swap reverses the database.

Private database networking complicates CI equally on both clouds: a public GitHub-hosted runner cannot directly reach a private database. Use a cloud-local migration runner (for example, CodeBuild/ECS task or Container Apps Job), a self-hosted runner inside the network, or execute migrations from the application release process with careful locking.

### 8. Secrets and workload identity

Do not store `SUPABASE_DB_URL`, a database password, or `JWT_SECRET` in source control or a long-lived GitHub secret.

#### AWS

- store application secrets in Secrets Manager (or non-secret configuration in Parameter Store);
- grant the Beanstalk EC2 instance profile or App Runner instance role only the required secret ARNs;
- inject/read secrets at runtime;
- remember that environment-variable materialization may require an environment refresh after rotation. [AWS: Beanstalk secrets in environment variables](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/AWSHowTo.secrets.env-vars.html)

#### Azure

- enable an App Service managed identity;
- grant it `get` access to the specific Key Vault secrets through Azure RBAC;
- use Key Vault references in App Service settings, keeping credentials out of the deployed artifact. [Microsoft: App Service Key Vault references](https://learn.microsoft.com/en-us/azure/app-service/app-service-key-vault-references)

Azure's managed-identity-to-Key-Vault path is slightly more cohesive. AWS IAM roles provide the same passwordless application-to-secret access, but role, instance profile, trust policy, and secret resource policy vocabulary can feel more fragmented.

Database passwordless authentication exists on both platforms—15-minute IAM authentication tokens on RDS and Entra tokens on Flexible Server—but it changes the JDBC connection/token-refresh design. [AWS: RDS IAM DB authentication](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.IAMDBAuth.Connecting.html) [Microsoft: PostgreSQL Entra authentication](https://learn.microsoft.com/en-us/azure/postgresql/security/security-entra-concepts) Use the managed secret store first; add passwordless DB auth when identity is the learning objective.

### 9. Networking, TLS, and DNS

#### AWS

1. Amplify serves the public frontend over its managed CDN.
2. Route 53 or another DNS provider maps the domain.
3. Amplify provisions a managed certificate for its custom domain.
4. A load-balanced Beanstalk environment terminates backend HTTPS at its Application Load Balancer using an ACM certificate. [AWS: Beanstalk HTTPS](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/configuring-https.html)
5. Beanstalk instances live in application subnets; RDS lives in private DB subnets.
6. Security groups allow database port 5432 only from the application security group.

#### Azure

1. Static Web Apps serves the public frontend globally.
2. Azure DNS or another provider maps the domain.
3. Static Web Apps/App Service supplies managed TLS certificates for supported custom domains. [Microsoft: App Service certificates](https://learn.microsoft.com/en-us/azure/app-service/configure-ssl-certificate)
4. App Service VNet Integration controls **outbound** access from the app to private resources.
5. Private Endpoint controls **inbound** private access to an Azure service; it is not a substitute for VNet Integration. [Microsoft: App Service private endpoints](https://learn.microsoft.com/en-us/azure/app-service/networking/private-endpoint)
6. PostgreSQL Flexible Server uses private networking and private DNS.

The security outcome is the same. AWS requires more explicit subnet, route, load-balancer, certificate, and security-group assembly; Azure bundles more of the public ingress and certificate work into the PaaS resources.

### 10. Health, scaling, and deployment safety

| Capability | Elastic Beanstalk | App Service | App Runner | Container Apps |
|---|---|---|---|---|
| Health check | Configure an HTTP path returning 200; enhanced health also evaluates environment/instance signals | Pings a configured path every minute, removes unhealthy instances from rotation, and can replace persistently unhealthy workers [Microsoft](https://learn.microsoft.com/en-us/azure/app-service/monitor-instances-health-check) | TCP by default; configure an HTTP path and thresholds [AWS](https://docs.aws.amazon.com/apprunner/latest/dg/manage-configure-healthcheck.html) | Startup, liveness, and readiness probes |
| Horizontal scaling | EC2 Auto Scaling min/max and policies | Manual scale, metric/schedule autoscale, or supported-tier HTTP automatic scaling | Concurrency-driven autoscaling with min/max instances [AWS](https://docs.aws.amazon.com/apprunner/latest/dg/manage-autoscaling.html) | HTTP/event KEDA rules; Consumption can scale to zero |
| Warm staging | Separate environment or immutable deployment | Deployment slot is warmed before swap | No slot primitive | New revision is provisioned before traffic moves |
| Blue/green | Clone environment and swap CNAMEs [AWS](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/using-features.CNAMESwap.html) | Swap staging and production slots | Separate services/DNS | Two active revisions with traffic weights |
| Canary | Native traffic-splitting deployment | Route a percentage to a slot | No native weighted revisions | Weighted traffic across revisions |
| Failed rollout | Immutable/traffic-split deployment can leave old fleet serving and discard unhealthy new fleet | Failed slot preparation stops the swap; swap back after a bad promotion | Deploy a known-good prior source/image version | Move traffic back to the known-good revision |

The application currently lacks a dedicated health endpoint such as Spring Boot Actuator's readiness check. A TCP check or `/` returning 200 proves only that something is listening. For credible production health, add a lightweight readiness endpoint before configuring either platform's rollout gate.

Elastic Beanstalk offers more deployment-policy choices directly: all-at-once, rolling, rolling with an extra batch, immutable, and traffic splitting. Immutable and traffic-split releases temporarily duplicate compute capacity. [AWS: Beanstalk deployment policies](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/using-features.rolling-version-deploy.html)

App Service slots keep production online while staging restarts and warms. A swap moves the prior production instances into staging, so rollback is another swap. Slots require eligible paid tiers, and auto-swap is not supported for Linux web apps/containers. [Microsoft: App Service deployment slots](https://learn.microsoft.com/en-us/azure/app-service/deploy-staging-slots)

### 11. Observability and incident response

#### AWS baseline

- stream Beanstalk application, proxy, deployment, and health logs to CloudWatch Logs;
- set explicit log retention instead of keeping logs forever;
- alarm on 5xx rate, latency, unhealthy hosts, CPU/memory, RDS connections/storage, and failed deployments;
- enable X-Ray/OpenTelemetry only when request traces provide value. [AWS: Beanstalk and CloudWatch Logs](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/AWSHowTo.cloudwatchlogs.html) [AWS: Beanstalk monitoring](https://docs.aws.amazon.com/elasticbeanstalk/latest/dg/incident-response.html)

#### Azure baseline

- send App Service platform/application logs to Azure Monitor / Log Analytics;
- enable the App Service-managed Application Insights Java agent;
- alert on 5xx rate, latency, unhealthy instances, CPU/memory, PostgreSQL connections/storage, and failed deployments;
- set workspace retention and sampling to control telemetry cost. [Microsoft: App Service monitoring](https://learn.microsoft.com/en-us/azure/azure-monitor/app/codeless-app-service)

Both platforms can supply logs, metrics, alerts, traces, audit activity, and dashboards. Azure's Application Insights experience is more unified for one web app. AWS's CloudWatch/X-Ray/EventBridge/CloudTrail pieces are powerful but appear as more separate services.

### 12. Rollback, backups, and disaster recovery

Separate three different recovery problems:

1. **Bad application release:** swap back/redeploy the previous artifact.
2. **Bad schema or data write:** restore/fix PostgreSQL; application rollback alone cannot help.
3. **Region failure:** use a second region, replicated data, global routing, and a rehearsed failover plan.

RDS automated backups provide point-in-time recovery within the configured retention period; manual snapshots persist independently until deleted. RDS retention can be 0–35 days for a DB instance, and zero disables automated backups. [AWS: RDS backups](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_WorkingWithAutomatedBackups.html) [AWS: backup retention](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_WorkingWithAutomatedBackups.BackupRetention.html)

Azure PostgreSQL Flexible Server supports 7–35 day backup retention and locally, zone-, or geo-redundant backup choices depending on region/configuration. Network mode and backup redundancy include decisions that cannot simply be changed later, so put them in IaC from the start. [Microsoft: PostgreSQL server creation options](https://learn.microsoft.com/en-us/azure/postgresql/configure-maintain/quickstart-create-server)

Multi-AZ/zone-redundant standby is **high availability, not backup**: accidental deletes and corrupt writes replicate. On either cloud, perform a restore drill to a separate database and verify the application can read the restored data. An enabled-backup checkbox is not evidence of recoverability.

A student portfolio does not need active-active multi-region production. Document the recovery objectives and prove a database restore first; add a second region when a real availability requirement justifies doubling resources and operational work.

### 13. Cost control and cleanup

As of the date of this report:

- Azure for Students provides **$100 for 12 months**, no credit card requirement, service allowances, and annual renewal while eligible. [Azure for Students](https://azure.microsoft.com/en-us/free/students)
- New AWS customers can receive **$100 at signup plus up to $100 more through activities**; the Free account plan lasts up to six months or until credit exhaustion. Eligibility depends on the account. [AWS Free Tier](https://aws.amazon.com/free/free-tier-faqs/)

Credits do not make an oversized production topology free. The managed PostgreSQL server and always-on compute are likely to dominate this small application's cost. NAT gateways, load balancers, private endpoints, retained snapshots, log ingestion, and DNS zones can also outlive the app or surprise a learner.

Do this immediately on either platform:

- create a low monthly budget with alerts at several thresholds;
- tag resources with project, environment, owner, and expiry date;
- use the smallest supported dev SKU and one region;
- stop/pause or delete demos when not in use;
- cap log retention and autoscaling maximums;
- avoid a NAT gateway or multi-region topology until required.

Azure Cost Management and AWS Budgets alert on spend; treat alerts as warnings, not a guaranteed hard spending stop. [Microsoft: cost alerts](https://learn.microsoft.com/en-us/azure/cost-management-billing/costs/cost-mgt-alerts-monitor-usage-spending) [AWS: budget monitoring](https://docs.aws.amazon.com/cost-management/latest/userguide/billing-security-logging.html)

#### Cleanup checklist

**Azure:** delete the dedicated resource group, then confirm that separately scoped DNS zones, Key Vault soft-deleted resources, shared Log Analytics workspaces, and retained backups are intentional. An empty App Service plan still incurs charges because it reserves workers. [Microsoft: App Service plan management](https://learn.microsoft.com/en-us/azure/app-service/app-service-plan-manage)

**AWS:** delete the CloudFormation stack/Beanstalk environment, then verify RDS final/manual snapshots, retained automated backups, S3 buckets, Amplify branches, ECR images, CloudWatch log groups, load balancers, NAT gateways, Elastic IPs, Route 53 hosted zones, and domain registration. Retained resources and resources created outside the stack remain billable.

## Operational-complexity scorecard

| Factor | AWS Beanstalk path | Azure App Service path |
|---|---:|---:|
| First successful deployment | 3/5 complexity | **2/5 complexity** |
| Production networking concepts exposed | **More** | Fewer |
| Direct Java 25 deployment | Yes | Yes |
| Container required | No | No |
| Safe release mechanism | Strong, but policy/environment oriented | **Strong and easy to visualize with slots** |
| Same-origin `/api` with separate static frontend | Amplify reverse proxy | **Static Web Apps linked backend; Standard plan** |
| Secretless workload access to secret store | IAM role | Managed identity |
| Private DB setup | More explicit VPC/subnet/security-group work | VNet/private DNS still nontrivial, but more guided |
| Monitoring experience for one Java app | Capable, spread across CloudWatch/X-Ray | **More unified with Azure Monitor/Application Insights** |
| Cost predictability for a beginner | More separate billable pieces | Fewer visible pieces, but App Service plan/slots can be costly |
| Current user benefit | Depends on AWS account eligibility | **Known $100 student credit for 12 months** |
| Job signal | Strong AWS/IAM/VPC/Beanstalk signal | Strong Azure/App Service/managed-identity signal |

## What must change before calling this repository production-ready

These are cloud-neutral gaps found in the current repository, not reasons to redesign it:

1. Add versioned schema migrations; replace `ddl-auto=update` with validation in production.
2. Add a real readiness/health endpoint.
3. Decide how production `/api` routing works; the Vite development proxy does not ship.
4. Put infrastructure and environment configuration in IaC.
5. Add CI gates and OIDC-based deployment identity.
6. Store `JWT_SECRET` and database credentials in the cloud secret store.
7. Configure HTTPS, private database access, backup retention, logs, alerts, and budget alerts.
8. Rehearse one bad-release rollback and one database restore.

No microservices, Kubernetes, service mesh, multi-region active-active design, or custom deployment platform is warranted for this application.

## Recommendation

Deploy this repository to Azure first:

```text
React/Vite      -> Azure Static Web Apps
Spring Boot JAR -> Azure App Service Java 25
PostgreSQL      -> Azure Database for PostgreSQL Flexible Server
Secrets         -> Key Vault + managed identity
Delivery        -> GitHub Actions + OIDC + staging slot
Operations      -> Azure Monitor + Application Insights
Infrastructure  -> Bicep
```

This is not because Java belongs on Azure. It is because App Service is the shortest credible JAR path, the current frontend can preserve `/api` through the linked-backend feature, and the known student credit supports real practice.

Then learn AWS by deploying the **same architecture**, not by rewriting the application:

```text
Static Web Apps -> Amplify Hosting
App Service     -> Elastic Beanstalk
Flexible Server -> RDS for PostgreSQL
Key Vault       -> Secrets Manager
Azure Monitor   -> CloudWatch
Bicep           -> CloudFormation
```

That second deployment teaches the differences that employers care about—AWS IAM, VPCs, security groups, load balancers, RDS, CloudWatch, and release policies—while proving that the Java artifact and production discipline are portable.
