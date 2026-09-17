# Simple Bank

A Spring Boot and React banking application for creating accounts, viewing balances, depositing and withdrawing money, and reviewing transaction history.

## Requirements

- Java 25
- Node.js and npm
- PostgreSQL database
- `SUPABASE_DB_URL` environment variable containing the JDBC connection URL
- `JWT_SECRET` environment variable containing a Base64-encoded 256-bit secret

## Run the backend

```powershell
cd backend
.\gradlew.bat bootRun
```

## Run the frontend

```powershell
cd frontend
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

1. `POST /api/users` registers a user.
2. `POST /api/users/login` returns a JWT.
3. `POST /api/accounts` creates an account for the authenticated user.
4. `GET /api/accounts/{id}` returns an owned account.
5. `POST /api/accounts/{id}/deposit` deposits money.
6. `POST /api/accounts/{id}/withdraw` withdraws money.
7. `GET /api/accounts/{id}/transactions` returns transaction history.

Protected requests require `Authorization: Bearer <token>`.

## Submission files

- PostgreSQL schema: `database/schema.sql`
- Postman collection: `postman/Simple Bank.postman_collection.json`
- UI screenshots: `screenshots/`
