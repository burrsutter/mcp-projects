-- Drop the table if it exists
DROP TABLE IF EXISTS products;

-- Create the products table
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

-- Create indexes for better performance
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_is_active ON products(is_active);

-- Add comments to the table and columns for documentation
COMMENT ON TABLE products IS 'Stores product information for the ERP system';
COMMENT ON COLUMN products.id IS 'Unique identifier for the product';
COMMENT ON COLUMN products.sku IS 'Stock Keeping Unit, unique identifier for the product';
COMMENT ON COLUMN products.name IS 'Product name';
COMMENT ON COLUMN products.description IS 'Detailed product description';
COMMENT ON COLUMN products.category IS 'Product category';
COMMENT ON COLUMN products.price IS 'Selling price of the product';
COMMENT ON COLUMN products.cost IS 'Cost price of the product';
COMMENT ON COLUMN products.stock_quantity IS 'Current stock quantity';
COMMENT ON COLUMN products.manufacturer IS 'Product manufacturer';
COMMENT ON COLUMN products.supplier IS 'Product supplier';
COMMENT ON COLUMN products.weight IS 'Product weight';
COMMENT ON COLUMN products.dimensions IS 'Product dimensions (LxWxH)';
COMMENT ON COLUMN products.is_active IS 'Product status (1 for active, 0 for inactive)';
COMMENT ON COLUMN products.created_at IS 'Timestamp when the product record was created';
COMMENT ON COLUMN products.updated_at IS 'Timestamp when the product record was last updated';

-- Create a trigger to automatically update the updated_at column
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_products_updated_at
BEFORE UPDATE ON products
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
