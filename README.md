# Tabcorp Demo Project

This is the Tabcorp demo microservice project built with Spring Boot. It processes transactions and provides reporting endpoints to calculate the total cost per customer, the total cost per product, and the number of transactions sold to customers from Australia.

The project uses PostgreSQL for its database with Flyway for database migrations. For testing, an in-memory H2 database is used (configured in `src/test/resources/application-test.properties`).

## Prerequisites

Before running the project locally, ensure you have the following installed:

- **Java 17:** The project is built with Java 17 and uses Gradle's toolchain support.
- **PostgreSQL:** The application connects to a PostgreSQL database.
    - PostgreSQL must be installed and running on port `5432`.
    - A database named `tabcorp-db` should exist, with the default user: postgres:pass.
    - If you use a different database name, username, or password, update the values in the `src/main/resources/application.properties` file accordingly.

## Database Setup

The project uses Flyway to manage the database schema and seed initial data. On startup, Flyway runs the migration scripts found in the `src/main/resources/db/migration` directory. These scripts will:

- Create the necessary tables for `customer`, `product`, and `transactions`.
- Insert the initial data into the `customer` and `product` tables using `ON CONFLICT DO NOTHING` to avoid duplicate entries.

## Running the Project Locally

1. **Clone the repository:**
   ```
   git clone https://github.com/your-username/your-repo-name.git
   cd your-repo-name
   ```

2. **Ensure PostgreSQL is running:**
   - Make sure your PostgreSQL server is running on port `5432`.
   - Ensure that the database `tabcorp-db` exists, or update the database details in `src/main/resources/application.properties`.


3. **Make sure Gradle is installed, then build and run the application using Gradle:**
    ```bash
    ./gradlew bootRun
   ```

This will start the application on http://localhost:8080.

## Endpoints and cURL Commands
### 1. Submit a Transaction (Endpoint: /transactions)
   Send a transaction request as a JSON payload containing exactly 4 fields:

- `transaction_time`: A date/time (format: `yyyy-MM-dd HH:mm`) that is either the current date or a future date.

- `customer_id`: The ID of an existing customer.

- `quantity`: The quantity for the transaction.

- `product_code`: The code of an existing product.

**Example cURL command:**
```declarative
    curl -X POST "http://localhost:8080/transactions" \
      -H "Content-Type: application/json" \
      -d '{
            "transaction_time": "2025-05-10 10:00",
            "customer_id": 10001,
            "quantity": 2,
            "product_code": "PRODUCT_001"
          }'
```

### 2. Report Endpoints
   The report endpoints require Basic Authentication. The default credentials are:
   - username: *user*
   - password: *password*

#### a. Total Cost per Customer
This endpoint returns a JSON object with customer IDs as keys and the total cost (calculated as `quantity * product.cost`) per customer as values.
```declarative
curl -u user:password -X GET "http://localhost:8080/report/customer-costs"
```

#### b. Total Cost per Product
This endpoint returns a JSON object with product codes as keys and the total cost per product as values.
```declarative
curl -u user:password -X GET "http://localhost:8080/report/product-costs"
```

#### c. Number of Transactions for Australian Customers
This endpoint returns the count of transactions for customers whose location is Australia.
```declarative
curl -u user:password -X GET "http://localhost:8080/report/australia-transactions"
```

## Testing
The project includes both unit and integration tests. Integration tests use an in-memory H2 database to mimic the production environment. 
Tests can be run using:
```bash
  ./gradlew test