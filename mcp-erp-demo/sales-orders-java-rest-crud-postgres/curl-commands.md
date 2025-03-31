# Sales Order API - cURL Command Examples

This document provides example cURL commands for interacting with the Sales Order API.

## Base URL

All examples use the base URL: `http://localhost:8081`

## POST - Create a New Order

Create a new sales order with order details:

```bash
curl -X POST "http://localhost:8081/api/orders" \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "orderDetails": [
      {
        "productName": "Smartphone XYZ",
        "quantity": 2,
        "unitPrice": 499.99
      },
      {
        "productName": "Wireless Headphones",
        "quantity": 1,
        "unitPrice": 149.99
      }
    ]
  }'
```

## GET - Retrieve Orders

### Get All Orders

Retrieve a list of all orders:

```bash
curl -X GET "http://localhost:8081/api/orders" \
  -H "Accept: application/json"
```

### Get Order by ID

Retrieve a specific order by its ID:

```bash
curl -X GET "http://localhost:8081/api/orders/1" \
  -H "Accept: application/json"
```

### Get Order by Order Number

Retrieve a specific order by its order number:

```bash
curl -X GET "http://localhost:8081/api/orders/number/ORD-2025-0001" \
  -H "Accept: application/json"
```

### Find Orders by Customer Name

Search for orders by customer name (partial match, case-insensitive):

```bash
curl -X GET "http://localhost:8081/api/orders/customer?name=John" \
  -H "Accept: application/json"
```

### Find Orders by Status

Retrieve orders with a specific status:

```bash
curl -X GET "http://localhost:8081/api/orders/status/PENDING" \
  -H "Accept: application/json"
```

### Find Orders by Date Range

Retrieve orders placed between specific dates:

```bash
curl -X GET "http://localhost:8081/api/orders/date-range?startDate=2025-01-01T00:00:00&endDate=2025-03-31T23:59:59" \
  -H "Accept: application/json"
```

## PUT - Update an Order

Update an existing order:

```bash
curl -X PUT "http://localhost:8081/api/orders/1" \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe Updated",
    "orderDetails": [
      {
        "productName": "Smartphone XYZ",
        "quantity": 3,
        "unitPrice": 499.99
      },
      {
        "productName": "Wireless Headphones",
        "quantity": 2,
        "unitPrice": 149.99
      },
      {
        "productName": "Phone Case",
        "quantity": 1,
        "unitPrice": 29.99
      }
    ]
  }'
```

## PATCH - Update Order Status

Update the status of an existing order:

```bash
curl -X PATCH "http://localhost:8081/api/orders/1/status?status=PROCESSING" \
  -H "Accept: application/json"
```

## DELETE - Delete an Order

Delete an order by its ID:

```bash
curl -X DELETE "http://localhost:8081/api/orders/1"
```

## Response Examples

### Successful Order Creation (201 Created)

```json
{
  "id": 1,
  "orderNumber": "ORD-2025-0001",
  "customerName": "John Doe",
  "orderDate": "2025-03-13T14:30:00",
  "status": "PENDING",
  "totalAmount": 1149.97,
  "orderDetails": [
    {
      "id": 1,
      "productName": "Smartphone XYZ",
      "quantity": 2,
      "unitPrice": 499.99,
      "subtotal": 999.98
    },
    {
      "id": 2,
      "productName": "Wireless Headphones",
      "quantity": 1,
      "unitPrice": 149.99,
      "subtotal": 149.99
    }
  ]
}
```

### Error Response (404 Not Found)

```json
{
  "timestamp": "2025-03-13T14:35:00",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found with id: 999"
}
```

### Error Response (400 Bad Request)

```json
{
  "timestamp": "2025-03-13T14:40:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": {
    "customerName": "Customer name is required",
    "orderDetails": "At least one order detail is required"
  }
}
```
