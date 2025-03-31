# Customer Express REST CRUD API with PostgreSQL

A RESTful CRUD API for managing customer data, built with Express.js and PostgreSQL.

## Prerequisites

- Node.js (v12 or higher)
- npm or yarn
- PostgreSQL database

## Setup and Installation

1. Clone the repository
   ```
   git clone <repository-url>
   cd customer-express-rest-crud-postgres
   ```

2. Install dependencies
   ```
   npm install
   ```

3. Set up the database
   - Create a PostgreSQL database named `erp_customer_master`
   - Run the schema file to create the necessary tables:
     ```
     psql -U postgres -d erp_customer_master -f db/schema.sql
     ```

4. Configure environment variables
   Create a `.env` file in the root directory with the following variables:
   ```
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=erp_customer_master
   DB_USER=postgres
   DB_PASSWORD=admin
   PORT=3001
   ```

## Running the Application

1. Start the server
   ```
   npm run dev
   ```

2. The API will be available at `http://localhost:3001/api/customers`

## API Endpoints

### GET /api/customers
Retrieve all customers

```bash
curl -X GET http://localhost:3001/api/customers
```

### GET /api/customers/:id
Retrieve a specific customer by ID

```bash
curl -X GET http://localhost:3001/api/customers/123
```

### POST /api/customers
Create a new customer

```bash
curl -X POST http://localhost:3001/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "id": "CUST001",
    "companyName": "Acme Corporation",
    "contactName": "John Doe",
    "primaryPhone": "555-123-4567",
    "primaryEmail": "john.doe@acme.com",
    "physicalAddress": "123 Main St, Anytown, USA",
    "website": "https://acme.com"
  }'
```

### PUT /api/customers/:id
Update an existing customer

```bash
curl -X PUT http://localhost:3001/api/customers/CUST001 \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "Acme Corporation",
    "contactName": "Jane Smith",
    "primaryPhone": "555-987-6543",
    "primaryEmail": "jane.smith@acme.com",
    "physicalAddress": "456 Oak Ave, Anytown, USA",
    "website": "https://acme.com"
  }'
```

### DELETE /api/customers/:id
Delete a customer

```bash
curl -X DELETE http://localhost:3001/api/customers/CUST001
```

## Customer Data Structure

| Field           | Type        | Description                       |
|-----------------|-------------|-----------------------------------|
| id              | VARCHAR(50) | Unique identifier (Primary Key)   |
| company_name    | VARCHAR(255)| Name of the company               |
| contact_name    | VARCHAR(255)| Name of the primary contact       |
| primary_phone   | VARCHAR(20) | Main contact phone number         |
| primary_email   | VARCHAR(255)| Main contact email address        |
| physical_address| TEXT        | Physical location of the company  |
| website         | VARCHAR(255)| Company website URL (optional)    |
| created_at      | TIMESTAMP   | Record creation timestamp         |
| updated_at      | TIMESTAMP   | Record last update timestamp      |
