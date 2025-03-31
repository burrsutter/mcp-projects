# ERP Product Catalog API - cURL Commands

This document provides examples of how to interact with the ERP Product Catalog API using cURL commands.

## Prerequisites

- The API server is running at `http://localhost:8000`
- PostgreSQL database is set up and running
- You have cURL installed on your system

## API Endpoints

### Create a Product (POST)

```bash
curl -X POST \
  http://localhost:8000/products/ \
  -H 'Content-Type: application/json' \
  -d '{
    "sku": "PROD-001",
    "name": "Ergonomic Office Chair",
    "description": "Comfortable office chair with lumbar support and adjustable height",
    "category": "Office Furniture",
    "price": 199.99,
    "cost": 120.00,
    "stock_quantity": 50,
    "manufacturer": "ErgoDesigns",
    "supplier": "Office Supplies Inc.",
    "weight": 15.5,
    "dimensions": "60x65x120",
    "is_active": 1
}'
```

#### Additional Product Examples

##### Example 1: Computer Monitor

```bash
curl -X POST \
  http://localhost:8000/products/ \
  -H 'Content-Type: application/json' \
  -d '{
    "sku": "TECH-101",
    "name": "UltraWide 34-inch Monitor",
    "description": "34-inch curved ultrawide monitor with 3440x1440 resolution, 144Hz refresh rate, and HDR support",
    "category": "Computer Hardware",
    "price": 499.99,
    "cost": 350.00,
    "stock_quantity": 25,
    "manufacturer": "TechVision",
    "supplier": "Tech Distributors LLC",
    "weight": 7.2,
    "dimensions": "81x36x51",
    "is_active": 1
}'
```

##### Example 2: Office Software

```bash
curl -X POST \
  http://localhost:8000/products/ \
  -H 'Content-Type: application/json' \
  -d '{
    "sku": "SOFT-202",
    "name": "Enterprise Office Suite",
    "description": "Complete office productivity suite with word processing, spreadsheets, presentations, and email client. Annual subscription with cloud storage.",
    "category": "Software",
    "price": 299.99,
    "cost": 150.00,
    "stock_quantity": 500,
    "manufacturer": "SoftWorks Inc.",
    "supplier": "Digital Solutions",
    "weight": 0.1,
    "dimensions": "14x12x2",
    "is_active": 1
}'
```

##### Example 3: Printer

```bash
curl -X POST \
  http://localhost:8000/products/ \
  -H 'Content-Type: application/json' \
  -d '{
    "sku": "PRINT-303",
    "name": "LaserJet Pro Color Printer",
    "description": "High-speed color laser printer with duplex printing, wireless connectivity, and mobile printing capabilities",
    "category": "Office Equipment",
    "price": 349.99,
    "cost": 220.00,
    "stock_quantity": 30,
    "manufacturer": "PrintTech",
    "supplier": "Office Supplies Inc.",
    "weight": 12.3,
    "dimensions": "45x39x28",
    "is_active": 1
}'
```

##### Example 4: Office Desk

```bash
curl -X POST \
  http://localhost:8000/products/ \
  -H 'Content-Type: application/json' \
  -d '{
    "sku": "FURN-404",
    "name": "L-Shaped Corner Desk",
    "description": "Spacious L-shaped corner desk with cable management system and adjustable shelving",
    "category": "Office Furniture",
    "price": 249.99,
    "cost": 175.00,
    "stock_quantity": 15,
    "manufacturer": "FurniCraft",
    "supplier": "Office Furnishings Ltd.",
    "weight": 35.8,
    "dimensions": "170x150x75",
    "is_active": 1
}'
```

##### Example 5: Network Equipment

```bash
curl -X POST \
  http://localhost:8000/products/ \
  -H 'Content-Type: application/json' \
  -d '{
    "sku": "NET-505",
    "name": "Enterprise WiFi 6 Router",
    "description": "High-performance WiFi 6 router with mesh capabilities, advanced security features, and multi-gigabit connectivity",
    "category": "Networking",
    "price": 189.99,
    "cost": 110.00,
    "stock_quantity": 40,
    "manufacturer": "NetConnect",
    "supplier": "Tech Distributors LLC",
    "weight": 1.2,
    "dimensions": "24x24x4",
    "is_active": 1
}'
```

### Get All Products (GET)

```bash
curl -X GET \
  http://localhost:8000/products/
```

### Get All Products with Pagination and Filtering (GET)

```bash
curl -X GET \
  'http://localhost:8000/products/?page=1&page_size=10&category=Office%20Furniture&min_price=100&max_price=300'
```

### Get Product by ID (GET)

```bash
# Replace {id} with the actual product ID
curl -X GET \
  http://localhost:8000/products/{id}
```

Example:

```bash
curl -X GET \
  http://localhost:8000/products/1
```

### Get Product by SKU (GET)

```bash
# Replace {sku} with the actual product SKU
curl -X GET \
  http://localhost:8000/products/sku/{sku}
```

Example:

```bash
curl -X GET \
  http://localhost:8000/products/sku/PROD-001
```

### Update a Product (PUT)

```bash
# Replace {id} with the actual product ID
curl -X PUT \
  http://localhost:8000/products/{id} \
  -H 'Content-Type: application/json' \
  -d '{
    "price": 179.99,
    "stock_quantity": 45,
    "description": "Comfortable ergonomic office chair with lumbar support, adjustable height, and armrests"
}'
```

Example:

```bash
curl -X PUT \
  http://localhost:8000/products/1 \
  -H 'Content-Type: application/json' \
  -d '{
    "price": 179.99,
    "stock_quantity": 45,
    "description": "Comfortable ergonomic office chair with lumbar support, adjustable height, and armrests"
}'
```

### Delete a Product (DELETE)

```bash
# Replace {id} with the actual product ID
curl -X DELETE \
  http://localhost:8000/products/{id}
```

Example:

```bash
curl -X DELETE \
  http://localhost:8000/products/1
```

## Testing Workflow Example

Here's a complete workflow example for testing the API:

1. Create a new product:

```bash
curl -X POST \
  http://localhost:8000/products/ \
  -H 'Content-Type: application/json' \
  -d '{
    "sku": "PROD-002",
    "name": "Adjustable Standing Desk",
    "description": "Electric standing desk with memory settings and smooth height adjustment",
    "category": "Office Furniture",
    "price": 499.99,
    "cost": 300.00,
    "stock_quantity": 20,
    "manufacturer": "DeskCraft",
    "supplier": "Office Supplies Inc.",
    "weight": 45.0,
    "dimensions": "160x80x120",
    "is_active": 1
}'
```

2. Get all products to verify the creation:

```bash
curl -X GET \
  http://localhost:8000/products/
```

3. Update the product:

```bash
# Assuming the product ID is 2
curl -X PUT \
  http://localhost:8000/products/2 \
  -H 'Content-Type: application/json' \
  -d '{
    "price": 449.99,
    "stock_quantity": 25,
    "description": "Premium electric standing desk with memory settings, smooth height adjustment, and cable management"
}'
```

4. Get the product by ID to verify the update:

```bash
curl -X GET \
  http://localhost:8000/products/2
```

5. Delete the product:

```bash
curl -X DELETE \
  http://localhost:8000/products/2
```

6. Try to get the deleted product to verify deletion:

```bash
curl -X GET \
  http://localhost:8000/products/2
```
