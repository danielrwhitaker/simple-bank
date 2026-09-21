# Simple Bank

A full-stack banking demo built with Spring Boot, React, and PostgreSQL. Users can register, sign in, create an account, view its balance, deposit or withdraw money, and review transaction history.

## Live application

[Open Simple Bank on AWS](https://d24qq15c0l36zb.cloudfront.net/)

The React frontend is hosted through Amazon S3 and CloudFront. The Spring Boot API runs on AWS Lambda behind API Gateway.

## Stack

- Java 25, Spring Boot, and Gradle
- React, TypeScript, and Vite
- PostgreSQL
- JWT authentication with BCrypt password hashing
- AWS S3, CloudFront, API Gateway, and Lambda

## Run locally

Set these backend environment variables:

- `SUPABASE_DB_URL`
- `SUPABASE_DB_USERNAME`
- `SUPABASE_DB_PASSWORD`
- `JWT_SECRET` — Base64-encoded 256-bit secret

Set `VITE_API_URL=http://localhost:8080` in `frontend/.env`.

```powershell
cd backend
.\gradlew.bat bootRun

cd ..\frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

## Verify

```powershell
cd backend
.\gradlew.bat test

cd ..\frontend
npm run build
npm run lint
```

## API workflow

1. Register with `POST /api/users`.
2. Sign in with `POST /api/users/login` to receive a JWT.
3. Create an account with `POST /api/accounts`.
4. View an account with `GET /api/accounts/{id}`.
5. Deposit or withdraw with `POST /api/accounts/{id}/deposit` or `/withdraw`.
6. View history with `GET /api/accounts/{id}/transactions`.

Protected requests require `Authorization: Bearer <token>`.
