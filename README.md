# ProExpense Backend

REST API for managing personal and shared expenses. Users own **accounts** that can be shared with other users. Each account has its own **cards** and **categories**. **Expenses** are recorded against an account and a card, and credit purchases can be split into installments.

## Tech stack

- **Java 25**
- **Spring Boot 4.1** (Web MVC, Data JPA, Validation, DevTools)
- **PostgreSQL 18** (via Docker Compose)
- **Hibernate** (schema managed with `ddl-auto: update`)
- **Lombok**
- **Maven** (wrapper included)

## Project structure

```
.
├── docker-compose.yaml        # PostgreSQL database
├── .env.example               # Environment variables template
└── backend/
    ├── pom.xml
    └── src/main/java/com/mat/proexpense_backend/
        ├── controller/        # REST endpoints
        ├── service/           # Business rules
        ├── repository/        # Spring Data JPA repositories
        ├── entitties/         # JPA entities
        ├── dto/               # Request / response records
        ├── enums/             # PaymentMethod, CardPaymentMethod, RecurrenceUnit
        ├── validation/        # Custom bean validation constraints
        └── exception/         # Global error handling
```

## Getting started

### Prerequisites

- JDK 25
- Docker and Docker Compose

### 1. Configure environment variables

Copy the template and fill in the values:

```bash
cp .env.example .env
```

| Variable       | Used by        | Description                                    | Example                                     |
|----------------|----------------|------------------------------------------------|---------------------------------------------|
| `DB_USER`      | Docker Compose | PostgreSQL user created in the container       | `admin`                                     |
| `DB_PASSWORD`  | Both           | PostgreSQL password                            | `admin`                                     |
| `DB_NAME`      | Docker Compose | Database created in the container              | `proexpense`                                |
| `DB_PORT`      | Docker Compose | Host port mapped to the container's `5432`     | `5432`                                      |
| `DB_URL`       | Application    | JDBC connection URL                            | `jdbc:postgresql://localhost:5432/proexpense` |
| `DB_USERNAME`  | Application    | Database user used by the application          | `admin`                                     |
| `DB_POOL_SIZE` | Application    | Hikari maximum pool size (default: `5`)        | `5`                                         |

The application reads `.env` from either the repository root or the `backend/` folder.

### 2. Start the database

```bash
docker compose up -d
```

### 3. Run the application

```bash
cd backend
./mvnw spring-boot:run
```

The API is available at `http://localhost:8090`.

### Running tests

```bash
cd backend
./mvnw test
```

## Domain overview

| Entity          | Description                                                                                                |
|-----------------|------------------------------------------------------------------------------------------------------------|
| `User`          | A person using the system.                                                                                 |
| `Account`       | A group of expenses owned by a user. It can be shared with other users through members.                    |
| `AccountMember` | Links a user to an account. Members can be activated or deactivated.                                       |
| `Card`          | A card belonging to an account. It supports `CREDIT`, `DEBIT` or `BOTH`, and has an optional limit.        |
| `Category`      | An expense category scoped to an account. Names are unique per account.                                    |
| `Expense`       | A purchase linked to an account, a card and an optional category. It can be split into installments.       |

Cards and categories can be deactivated. Inactive ones can't be used on new or updated expenses.

## Expenses and installments

- An expense's `paymentMethod` (`CREDIT` or `DEBIT`) must be supported by the selected card.
- Only `CREDIT` purchases can be split into installments, with **2 to 120** installments allowed.
- Each installment is stored as its own `Expense`, with a `currentInstallment` number (1..N) and its own `expenseDate`.
- **Amount splitting:** the total is divided with 2 decimal places, rounding down. The leftover cents go to the first installment.
  - Example: `100.00` in 3 installments → `33.34`, `33.33`, `33.33`.

### Installment due dates

When registering an installment purchase, pick **one** of these modes. Sending fields from both modes is rejected.

| Mode             | Fields                                                    | Behavior                                                                                                   |
|------------------|-----------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| Interval         | `recurrenceUnit` (`DAYS`, `MONTHS`, `YEARS`), `recurrenceInterval` (1–365) | Each installment is `interval × unit` after the previous one. With no fields sent, the default is every 1 month. |
| Fixed day        | `recurrenceDayOfMonth` (1–31)                             | The first installment falls on `expenseDate`. Later ones fall on the given day of each following month. Shorter months use their last day. |

Recurrence fields are not allowed on purchases that aren't split into installments.

### Example: register a purchase in 3 monthly installments

```http
POST /api/v1/expenses
Content-Type: application/json

{
  "name": "Notebook",
  "description": "Work laptop",
  "amount": 4500.00,
  "paymentMethod": "CREDIT",
  "expenseDate": "2026-11-18",
  "ownerId": "<user-id>",
  "accountId": "<account-id>",
  "categoryId": "<category-id>",
  "cardId": "<card-id>",
  "isInInstallments": true,
  "installmentsNumber": 3,
  "recurrenceDayOfMonth": 10
}
```

## API reference

Base path: `/api/v1`

### Users

| Method | Path          | Description        |
|--------|---------------|--------------------|
| GET    | `/users`      | List all users     |
| GET    | `/users/{id}` | Get a user by id   |
| POST   | `/users`      | Create a user      |

### Accounts

| Method | Path                                             | Description                                         |
|--------|--------------------------------------------------|-----------------------------------------------------|
| POST   | `/accounts`                                      | Create an account                                   |
| POST   | `/accounts/users`                                | Add a user to an account                            |
| GET    | `/accounts/users/{userId}`                       | List the accounts a user belongs to                 |
| GET    | `/accounts/{accountId}/users/active`             | List an account's active members                    |
| PATCH  | `/accounts/{accountId}/users/{userId}/activate`  | Activate a member                                   |
| PATCH  | `/accounts/{accountId}/users/{userId}/deactivate`| Deactivate a member                                 |
| GET    | `/accounts/access?userId=&accountId=`            | Check whether a user has access to an account       |

### Cards

| Method | Path                               | Description                          |
|--------|------------------------------------|--------------------------------------|
| GET    | `/cards/{id}`                      | Get a card by id                     |
| GET    | `/cards/accounts/{accountId}`      | List all cards of an account         |
| GET    | `/cards/accounts/{accountId}/active` | List active cards of an account    |
| POST   | `/cards`                           | Create a card                        |
| PUT    | `/cards/{id}`                      | Update a card                        |

### Categories

| Method | Path                                    | Description                               |
|--------|-----------------------------------------|-------------------------------------------|
| GET    | `/categories/{id}`                      | Get a category by id                      |
| GET    | `/categories/accounts/{accountId}`      | List all categories of an account         |
| GET    | `/categories/accounts/{accountId}/active` | List active categories of an account    |
| POST   | `/categories`                           | Create a category                         |
| PUT    | `/categories/{id}`                      | Update a category                         |

### Expenses

| Method | Path              | Description                                                      |
|--------|-------------------|------------------------------------------------------------------|
| GET    | `/expenses`       | List all expenses                                                |
| GET    | `/expenses/{id}`  | Get an expense by id                                             |
| POST   | `/expenses`       | Register an expense (creates one record per installment)         |
| PUT    | `/expenses/{id}`  | Update an expense                                                |
| DELETE | `/expenses/{id}`  | Delete an expense                                                |

## Error responses

Errors follow the [RFC 9457 Problem Details](https://www.rfc-editor.org/rfc/rfc9457) format. Validation errors include an `errors` map with the messages for each field:

```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Um ou mais campos são inválidos",
  "instance": "/api/v1/expenses",
  "errors": {
    "installmentsNumber": ["Compras parceladas devem ter entre 2 e 120 parcelas"]
  }
}
```

> Validation and error messages returned by the API are in Portuguese (pt-BR).
