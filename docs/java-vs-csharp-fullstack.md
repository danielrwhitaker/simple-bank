# Full-stack Java/Spring Boot vs. C#/.NET

Current as of **September 16, 2026**. This is a practical comparison for an early-career developer in Lexington, Kentucky who can commute to Louisville, prefers remote work, already has a Java/Spring Boot/PostgreSQL/React application, and has Azure student credit.

## Bottom line

Both are strong, job-viable enterprise stacks. Neither choice blocks React, PostgreSQL, Docker, Azure, AWS, REST, OAuth/OIDC, or modern testing.

- **C#/.NET is the more unified and beginner-efficient stack.** The `dotnet` CLI, ASP.NET Core, Entity Framework Core, ASP.NET Core Identity, first-party migrations, and Azure tooling fit together with fewer ecosystem decisions.
- **Java/Spring is the more ecosystem-flexible backend stack.** Spring Boot, Spring Data, Spring Security, Maven/Gradle, and the JVM have enormous enterprise reach and many interchangeable components, but that flexibility creates more concepts and configuration.
- **For this candidate, the practical edge is C#/.NET as the next skill—not as a rewrite.** Finish the existing Java project's deployment story on Azure, then build one small ASP.NET Core API with EF Core and Identity. That produces two credible signals without discarding completed work.
- **Keep React/TypeScript as the frontend for either backend.** Blazor is useful, but React/TypeScript transfers across Java and .NET jobs and matches the existing project.

## Component-by-component comparison

| Component | Java/Spring Boot | C#/.NET | Practical edge |
|---|---|---|---|
| Language | Java is explicit, conservative, strongly typed, and widely understood. Records and modern language features reduce older boilerplate, but Java still tends to be verbose. | C# is strongly typed but has more convenience features: properties, records, nullable-reference analysis, LINQ, pattern matching, and `async`/`await`. | **C# for day-to-day ergonomics.** Java for a smaller, more conservative language surface. |
| Backend framework | Spring Boot auto-configures production-grade Spring applications. Typical APIs use controllers, dependency injection, validation, services, and repositories. | ASP.NET Core supports controllers and lower-ceremony Minimal APIs in the same framework. Dependency injection, configuration, logging, and hosting are built in. | **C# for the easiest small API.** Tie for large systems. |
| REST/API development | Mature and highly capable. Annotation-driven controllers are readable once the Spring model is learned, but filters, security, serialization, and auto-configuration add conceptual weight. | Minimal APIs have a very low ceremony floor; controllers provide a familiar enterprise structure when the application grows. | **C# for ease.** Capability is effectively tied. |
| Database access | Spring Data JPA reduces repository boilerplate and normally uses Hibernate. Java also offers JDBC, jOOQ, MyBatis, and other mature choices. Schema migration is commonly added separately with Flyway or Liquibase. | EF Core combines ORM, LINQ queries, change tracking, database providers, and first-party migrations. Dapper is a common lighter option. | **C# for one cohesive workflow.** Java when interchangeable persistence approaches matter. |
| PostgreSQL / SQL Server | PostgreSQL, MySQL, Oracle, and SQL Server are normal choices. Java is not tied to Oracle Database. | SQL Server has the smoothest Microsoft integration, but EF Core supports PostgreSQL, MySQL, SQLite, and others through providers. C# is not tied to SQL Server. | Tie. Choose the database required by the job or product. |
| Schema migrations | Usually Flyway/Liquibase plus JPA. This separation is explicit and production-friendly, but it is another dependency and workflow to learn. | EF Core generates source-controlled migrations and tracks applied migrations as part of the standard toolchain. Microsoft still advises reviewing and testing generated migrations. | **C# for learning speed and integration.** |
| Browser frontend | Spring MVC can render server-side templates such as Thymeleaf. Rich client applications normally add React, Angular, or Vue with JavaScript/TypeScript. | ASP.NET Core offers Razor Pages/MVC and Blazor, which can use C# for server-rendered and interactive UI. It also pairs normally with React, Angular, or Vue. | **C# if one-language UI via Blazor matters.** **Tie for employable SPA work:** use TypeScript. |
| Frontend job portability | React/Angular/TypeScript is independent of Spring and appears in Java full-stack roles. | React/Angular/Vue/TypeScript is also common in .NET roles. Blazor is useful but narrows the set of matching jobs. | **TypeScript SPA + either backend** is the strongest general signal. |
| Authentication | Spring Security covers authentication, authorization, common-exploit protection, OAuth2/OIDC clients and resource servers, JWT, and method-level security. It is powerful but has a steeper mental model. | ASP.NET Core Identity directly handles users, password hashing, roles, claims, tokens, email confirmation, two-factor authentication, external logins, and API endpoints. Policy-based authorization is built in. | **C# for an integrated first implementation.** Tie for serious OAuth/OIDC systems. |
| Security practicality | Spring Security is safe when used as designed, but custom filter chains and custom JWT code are easy places for a beginner to make mistakes. | Identity provides more of the ordinary account lifecycle out of the box, reducing custom security code. It still requires correct cookie/token, CORS, CSRF, secret, and deployment configuration. | **C# for less custom auth code.** Neither removes the need to learn web security. |
| Validation and error handling | Bean Validation annotations, exception handlers, and Spring's web stack are mature. | Data annotations, endpoint filters/model validation, exceptions, and standardized `ProblemDetails` support are cohesive. | Slight **C# ease**; no important capability gap. |
| Testing | `spring-boot-starter-test` brings Spring test support, JUnit Jupiter, AssertJ, and Hamcrest. Spring Boot also integrates with Testcontainers. | The .NET test platform supports xUnit, NUnit, MSTest, and others through `dotnet test`; ASP.NET Core provides first-party integration-testing guidance and an in-memory test host. | Tie. C# has a slightly more uniform command-line experience. |
| Build and packages | Spring supports Maven and Gradle. Both are mature, but choosing and understanding one is additional cognitive load. | One SDK and CLI covers project creation, restore, build, run, test, publish, and package operations; NuGet is the normal package source. | **C# for tooling simplicity.** |
| IDEs | IntelliJ IDEA is excellent; VS Code and Eclipse are viable. Some advanced Spring support depends on the IDE/edition. | Visual Studio is deeply integrated on Windows; Rider and VS Code are viable. The command line remains cross-platform. | **C# on Windows for lowest friction.** Personal preference can erase this difference. |
| Runtime and performance | The JVM has mature JIT compilation, garbage collectors, profiling, virtual threads, and decades of production tuning. Spring/GraalVM native images can improve startup and memory with compatibility tradeoffs. | .NET has a mature JIT, garbage collector, async runtime, profiling, and Native AOT. Native AOT can improve startup and memory but restricts dynamic features; not all ASP.NET Core models are AOT-compatible. | No universal winner. **Benchmark the real workload.** For a portfolio CRUD/API application, performance should not decide the language. |
| Containers | Spring Boot supports Dockerfiles and Cloud Native Buildpacks for Docker-compatible images. | Microsoft publishes official runtime/SDK images; the .NET SDK can also publish a container image without maintaining a Dockerfile. | Slight **C# ease**; both are fully practical. |
| Observability/operations | Spring Boot Actuator supplies health, readiness/liveness, metrics, audit, and management endpoints; Micrometer/OpenTelemetry are common. | ASP.NET Core has built-in logging, health checks, metrics/OpenTelemetry integrations, configuration, and diagnostics. | Tie. |
| Azure deployment | Azure App Service officially supports Java SE/Spring Boot, Tomcat, and JBoss. Java can use Azure databases, managed identity, monitoring, and GitHub Actions. | .NET is a first-class Azure path with especially cohesive Visual Studio/CLI, Identity/Entra ID, App Service, Functions, Azure SQL, monitoring, and deployment integrations. | **C# for Azure ergonomics**, but Java is fully supported. |
| AWS deployment | Java/Spring has a long-standing AWS enterprise footprint. AWS officially supports Java platforms and containers. | AWS officially supports modern .NET on Linux, Windows, containers, Lambda, and Elastic Beanstalk. | Java has a cultural/ecosystem tendency toward AWS; **no technical lock-in**. |
| Cloud portability | A JAR or OCI container runs across major clouds. Avoiding cloud-specific service APIs preserves portability. | A self-contained app or OCI container also runs across major clouds. Avoiding cloud-specific service APIs preserves portability. | Tie. Architecture creates lock-in more than language does. |
| Maintenance | Java's explicitness and stable conventions make large teams predictable. Spring's annotations and auto-configuration can obscure behavior until the framework model is understood. | C# often expresses the same behavior with less boilerplate. The integrated stack reduces dependency decisions, but annual .NET releases require a deliberate support/update policy. | Slight **C# productivity edge**; both maintain well with conventional architecture and tests. |
| Release lifecycle | Java has multiple LTS releases and multiple OpenJDK vendors; support and licensing terms depend on the distribution/vendor. Oracle currently lists Java 21 and 25 as LTS. | .NET releases annually with defined support windows. .NET 10 is LTS through November 2028; ASP.NET Core and EF Core follow the .NET lifecycle. | **C# for lifecycle clarity.** Java can offer longer support through the selected vendor. |
| Learning from Java | Already learned and demonstrated in this repository. Spring concepts transfer to dependency injection, controllers, services, ORM, and security elsewhere. | C# syntax and ASP.NET architecture will feel familiar to a Java learner, while LINQ, properties, `async`/`await`, and the integrated toolchain add useful new concepts. | **C# is a low-cost second ecosystem after Java.** |

## What a complete portfolio stack looks like

### Java-centered

```text
React + TypeScript
        ↓ HTTPS / JSON
Spring Boot + Spring MVC
Spring Security (prefer OAuth/OIDC or a proven identity provider)
Spring Data JPA + Hibernate
PostgreSQL + Flyway/Liquibase
JUnit + Spring Boot Test + Testcontainers
Gradle or Maven
Docker / OCI image
Azure App Service, AWS, or another container/PaaS host
```

Strengths: strong enterprise/backend signal, broad portability, mature ecosystem, excellent fit for financial and transaction-heavy systems.

Costs: more framework concepts, more annotation/configuration knowledge, and more separate ecosystem choices.

### C#-centered

```text
React + TypeScript (or Blazor when the target job uses it)
        ↓ HTTPS / JSON
ASP.NET Core Minimal APIs or controllers
ASP.NET Core Identity / OAuth / OIDC
Entity Framework Core
PostgreSQL or SQL Server + EF migrations
xUnit/NUnit/MSTest + ASP.NET integration tests
dotnet CLI + NuGet
Docker / OCI image
Azure App Service, AWS, or another container/PaaS host
```

Strengths: cohesive first-party workflow, low API ceremony, excellent Windows/Azure tooling, and fewer separate technology decisions.

Costs: the Microsoft ecosystem can encourage Azure-specific choices; Blazor-only frontend experience transfers less widely than TypeScript experience.

## Current hiring signal: Lexington, Louisville, and remote

This is a **small, current snapshot—not a labor-market census**. Job pages change or disappear, and titles such as “junior” are inconsistent.

### Direct local evidence

- Churchill Downs/TwinSpires currently has a role titled **Jr. Software Engineer** in both Lexington and Louisville using Angular, Java, Spring Boot/Spring MVC, MySQL, AWS, and GitHub. However, the same posting asks for **five or more years** of career progression, so its title is not truly beginner-level. It still proves that the Java/Spring/Angular/AWS stack exists directly in the user's commute area. [Churchill Downs posting](https://jobs.churchilldowns.com/job/Louisville-Jr_-Software-Engineer-KY-40222/1371647700/)
- Brooksource currently lists a Louisville **Jr. Software Engineer** contract-to-hire role asking for about two years of experience with .NET Core/C#, Vue, REST, GraphQL, and microservices. This is a clearer early-career .NET signal, although it is hybrid and still asks for professional experience. [Brooksource posting](https://jobs.brooksource.com/jobs/job/a1wcv0000011yv7eae-jr-software-engineer-louisville-kentucky/)
- Dismas Charities lists a Louisville software-developer role centered on C#/.NET MVC, Web API, SQL Server, HTML/JavaScript, Azure DevOps, and 1–5 years of experience; it becomes hybrid after training. [Dismas posting](https://dismas.applicantpool.com/jobs/1301977.html)
- Slingshot Software in Louisville advertises a full-stack .NET developer path using C#, .NET, Vue, SQL Server, and AWS, illustrating that C# is not limited to Azure. [Slingshot careers](https://www.yslingshot.com/careers/net-developer/)
- More experienced local roles exist on both sides: Cognizant has advertised Louisville .NET/Azure and Java/Spring Boot/Angular roles, while Intuit has advertised Lexington Java/Spring Boot/AWS work. These confirm both ecosystems locally, but they do not establish entry-level availability. [Cognizant .NET](https://careers.cognizant.com/us-en/jobs/00069408631/full-stack-backend-engineer-net-cloud/) [Cognizant Java](https://careers.cognizant.com/global-en/jobs/00070157251/senior-java-full-stack-developer/) [Intuit Java listing](https://www.linkedin.com/jobs/view/staff-software-engineer-at-intuit-4455522135)

### Interpretation

- The current local sample gives **C#/.NET a modest early-career practicality edge**, especially in Louisville and Microsoft-oriented internal-business systems.
- Java is present locally, including a rare posting spanning both Lexington and Louisville, but the observed Java roles skew more experienced despite inconsistent titles.
- Remote work increases the geographic pool for both stacks but also increases applicant competition. A deployed, tested application and the ability to explain design/security decisions signal more than adding a language name to a résumé.
- GitHub's 2025 data shows both Java and C# among its major languages, while TypeScript became the most-used language by contributor count. This supports keeping TypeScript regardless of backend choice; it does **not** by itself measure available jobs. [GitHub Octoverse 2025](https://github.blog/news-insights/octoverse/octoverse-a-new-developer-joins-github-every-second-as-ai-leads-typescript-to-1/)

## Cost and the Azure student benefit

Azure for Students currently includes **$100 of credit usable within 12 months**, no credit card requirement, free service allowances, and annual renewal while eligible. The credit applies to Azure services, not specifically to C#. [Azure for Students](https://azure.microsoft.com/en-us/free/students)

Therefore:

- Deploying the existing Java Spring Boot application to Azure is a valid use of the credit and demonstrates cloud portability.
- Building a small C# service afterward makes the Microsoft/Azure connection more visible to recruiters.
- Do not run parallel always-on environments merely to compare languages. One small App Service/container, one managed database if affordable, budget alerts, and cleanup after demonstrations are enough.

## Recommendation for this repository and candidate

1. **Do not rewrite Simple Bank in C#.** It already demonstrates Spring Boot, REST, JPA, PostgreSQL, validation, BCrypt/JWT-based authentication, React, TypeScript, and tests. A rewrite would mostly reproduce the same product rather than add a new signal.
2. **Deploy this Java application to Azure.** Azure App Service explicitly supports Spring Boot/Java, and GitHub Actions can deploy Java packages. This completes the “built, tested, and deployed” story. [Azure Java quickstart](https://learn.microsoft.com/en-us/azure/app-service/quickstart-java) [Azure GitHub Actions deployment](https://learn.microsoft.com/en-us/azure/app-service/deploy-github-actions)
3. **Then build a deliberately small C# comparison service**, such as accounts plus transactions without another frontend. Use ASP.NET Core, EF Core migrations, ASP.NET Core Identity, PostgreSQL or SQL Server, one integration test, and Azure deployment.
4. **Keep React/TypeScript shared knowledge.** Do not learn Blazor first unless several target postings ask for it.
5. After applying/searching for several weeks, compare interview responses from Java and .NET applications. That real feedback is better evidence than global language rankings.

The shortest job-viable combination from the current position is:

```text
Primary proof:    Java + Spring Boot + PostgreSQL + React/TypeScript + Azure
Secondary signal: C# + ASP.NET Core + EF Core + Identity + Azure
Shared skills:    SQL, HTTP/REST, Git, tests, Docker, CI/CD, cloud configuration
```

## Primary technical sources

- [Spring Boot servlet web applications](https://docs.spring.io/spring-boot/reference/web/servlet.html)
- [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/)
- [Spring Security features](https://docs.spring.io/spring-security/reference/features/index.html)
- [Spring Security OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/)
- [Spring Boot testing](https://docs.spring.io/spring-boot/reference/testing/)
- [Spring Boot Testcontainers](https://docs.spring.io/spring-boot/reference/testing/testcontainers.html)
- [Spring Boot container images](https://docs.spring.io/spring-boot/reference/packaging/container-images/)
- [Spring Boot native images](https://docs.spring.io/spring-boot/reference/packaging/native-image/introducing-graalvm-native-images.html)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/reference/actuator/)
- [ASP.NET Core Minimal APIs](https://learn.microsoft.com/en-us/aspnet/core/fundamentals/minimal-apis?view=aspnetcore-10.0)
- [Entity Framework Core](https://learn.microsoft.com/en-us/ef/core/)
- [EF Core migrations](https://learn.microsoft.com/en-us/ef/core/managing-schemas/migrations/)
- [ASP.NET Core Identity](https://learn.microsoft.com/en-us/aspnet/core/security/authentication/identity?view=aspnetcore-10.0)
- [Identity for SPA/API backends](https://learn.microsoft.com/en-us/aspnet/core/security/authentication/identity-api-authorization?view=aspnetcore-10.0)
- [ASP.NET Core authorization](https://learn.microsoft.com/en-us/aspnet/core/security/authorization/introduction?view=aspnetcore-10.0)
- [Blazor overview](https://learn.microsoft.com/en-us/aspnet/core/blazor/?view=aspnetcore-10.0)
- [.NET CLI](https://learn.microsoft.com/en-us/dotnet/core/tools/)
- [.NET testing](https://learn.microsoft.com/en-us/dotnet/core/testing/)
- [ASP.NET Core integration testing](https://learn.microsoft.com/en-us/aspnet/core/fundamentals/minimal-apis/test-min-api?view=aspnetcore-10.0)
- [.NET container publishing](https://learn.microsoft.com/en-us/dotnet/core/containers/sdk-publish)
- [.NET Native AOT](https://learn.microsoft.com/en-us/dotnet/core/deploying/native-aot)
- [.NET support policy](https://dotnet.microsoft.com/en-us/platform/support/policy/dotnet-core)
- [Oracle Java support roadmap](https://www.oracle.com/java/technologies/java-se-support-roadmap.html)
- [Azure App Service language support](https://learn.microsoft.com/en-us/azure/app-service/)
- [AWS Elastic Beanstalk supported platforms](https://docs.aws.amazon.com/elasticbeanstalk/latest/platforms/platforms-supported.html)
