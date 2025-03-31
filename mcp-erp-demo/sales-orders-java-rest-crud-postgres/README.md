# Sales Order API

A RESTful API for managing sales orders with order details, built with Spring Boot, PostgreSQL, and OpenAPI documentation.

## Features

- CRUD operations for sales orders and order details
- Automatic schema generation with JPA/Hibernate
- OpenAPI documentation
- Comprehensive error handling
- Unit and integration tests

## Technologies

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- PostgreSQL
- Lombok
- SpringDoc OpenAPI
- JUnit 5
- Maven

## Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL

## Database Setup

1. Create a PostgreSQL database named `salesorders`:

```sql
CREATE DATABASE erp_sales_orders;
```

2. Update the database connection details in `src/main/resources/application.properties` if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/erp_sales_orders
spring.datasource.username=postgres
spring.datasource.password=admin
```

## Running the Application

1. Clone the repository:

```bash
git clone https://github.com/burrsutter/mcp-erp-demo.git
cd mcp-erp-demo/sales-orders-java-rest-crud-postgres
```

2. Build the application:

```bash
mvn clean package
```

3. Run the application:

```bash
mvn spring-boot:run
```

The application will start on port 8081.

## API Documentation

Once the application is running, you can access the API documentation at:

- Swagger UI: http://localhost:8081/swagger-ui.html
- OpenAPI JSON: http://localhost:8081/api-docs
- OpenAPI YAML: http://localhost:8081/api-docs.yaml

## API Endpoints

### Orders

- `POST /api/orders` - Create a new order
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/number/{orderNumber}` - Get order by order number
- `PUT /api/orders/{id}` - Update an order
- `DELETE /api/orders/{id}` - Delete an order
- `GET /api/orders/customer?name={customerName}` - Find orders by customer name
- `GET /api/orders/status/{status}` - Find orders by status
- `GET /api/orders/date-range?startDate={startDate}&endDate={endDate}` - Find orders by date range
- `PATCH /api/orders/{id}/status?status={status}` - Update order status

## Testing

Run the tests with:

```bash
mvn test
```

## Generating OpenAPI YAML

To generate the OpenAPI YAML file:

1. Start the application
2. Run the following Maven command:

```bash
mvn springdoc-openapi:generate
```

The OpenAPI YAML file will be generated in the `target` directory as `openapi.yaml`.

## Project Structure

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── salesorder
│   │               ├── SalesOrderApiApplication.java
│   │               ├── config
│   │               │   └── OpenApiConfig.java
│   │               ├── controller
│   │               │   └── OrderController.java
│   │               ├── dto
│   │               │   ├── OrderDTO.java
│   │               │   └── OrderDetailDTO.java
│   │               ├── exception
│   │               │   ├── ErrorResponse.java
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── model
│   │               │   ├── Order.java
│   │               │   ├── OrderDetail.java
│   │               │   └── OrderStatus.java
│   │               ├── repository
│   │               │   ├── OrderDetailRepository.java
│   │               │   └── OrderRepository.java
│   │               └── service
│   │                   ├── OrderService.java
│   │                   ├── impl
│   │                   │   └── OrderServiceImpl.java
│   │                   └── mapper
│   │                       └── OrderMapper.java
│   └── resources
│       ├── application.properties
│       └── db
│           └── migration
└── test
    ├── java
    │   └── com
    │       └── example
    │           └── salesorder
    │               ├── SalesOrderApiApplicationTests.java
    │               └── controller
    │                   └── OrderControllerTest.java
    └── resources
        └── application.properties
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.
