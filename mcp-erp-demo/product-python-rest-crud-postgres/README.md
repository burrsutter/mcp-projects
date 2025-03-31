# ERP Product Catalog API

A Python REST API for managing an ERP Product catalog with CRUD operations using FastAPI and PostgreSQL.

## Features

- **RESTful API**: Built with FastAPI for high performance
- **Database Integration**: PostgreSQL with SQLAlchemy ORM
- **CRUD Operations**: Create, Read, Update, Delete operations for products
- **Filtering & Pagination**: Advanced filtering and pagination support
- **Data Validation**: Request and response validation with Pydantic
- **OpenAPI Documentation**: Auto-generated API documentation with Swagger UI
- **Error Handling**: Comprehensive error handling and responses

## Tech Stack

- **Python 3.9+**
- **FastAPI**: Modern, fast web framework for building APIs
- **SQLAlchemy**: SQL toolkit and ORM
- **Pydantic**: Data validation and settings management
- **PostgreSQL**: Relational database
- **Uvicorn**: ASGI server for FastAPI
- **Alembic**: Database migration tool

## Installation

### Prerequisites

- Python 3.9 or higher
- PostgreSQL database

### Setup

1. Clone the repository:

```bash
git clone https://github.com/yourusername/erp-product-catalog-api.git
cd erp-product-catalog-api
```

2. Create a virtual environment and activate it:

```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

3. Install dependencies:

```bash
pip install -r requirements.txt
```

4. Create a PostgreSQL database:

```bash
createdb erp_product_catalog
```

or

```bash
psql -U postgres -W
create database erp_product_catalog;
\l
```

```bash
\c erp_product_catalog
```



5. Configure environment variables in `.env` file:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=erp_product_catalog
DB_USER=postgres
DB_PASSWORD=admin
API_HOST=0.0.0.0
API_PORT=8000
DEBUG=True
```

## Running the Application

Start the application:

```bash
python run.py
```

The API will be available at http://localhost:8000

## API Documentation

Once the application is running, you can access the API documentation at:

- Swagger UI: http://localhost:8000/docs

## Database Schema

The product table schema is defined in `schema.sql`:

```sql
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    price NUMERIC(10, 2) NOT NULL,
    cost NUMERIC(10, 2),
    stock_quantity INTEGER DEFAULT 0,
    manufacturer VARCHAR(255),
    supplier VARCHAR(255),
    weight NUMERIC(10, 2),
    dimensions VARCHAR(100),
    is_active INTEGER DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /products/ | Create a new product |
| GET | /products/ | Get all products (with filtering and pagination) |
| GET | /products/{id} | Get product by ID |
| GET | /products/sku/{sku} | Get product by SKU |
| PUT | /products/{id} | Update a product |
| DELETE | /products/{id} | Delete a product |

## Testing the API

### Using cURL

Examples of cURL commands for testing the API are provided in `curl-commands.md`.

### Using Java Client

A Java REST client example is provided in `JavaRestClient.java`. This class demonstrates how to interact with the API programmatically using Java's HttpClient.

## Project Structure

```
.
├── app/
│   ├── __init__.py
│   ├── main.py                 # Main application file
│   ├── database/               # Database connection and configuration
│   ├── models/                 # SQLAlchemy models
│   ├── routes/                 # API routes and endpoints
│   ├── schemas/                # Pydantic schemas for request/response validation
│   ├── utils/                  # Utility functions
│   └── tests/                  # Unit and integration tests
├── .env                        # Environment variables
├── requirements.txt            # Project dependencies
├── run.py                      # Script to run the application
├── schema.sql                  # SQL schema definition
├── curl-commands.md            # cURL examples for testing
├── JavaRestClient.java         # Java client example
├── openapi.yaml                # OpenAPI specification
└── README.md                   # Project documentation
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.
