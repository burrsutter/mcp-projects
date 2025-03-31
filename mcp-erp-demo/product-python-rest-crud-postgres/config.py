# Server Configuration
PORT = 8001
HOST = "0.0.0.0"  # Allows external connections

# Database Configuration
DB_HOST = "localhost"
DB_PORT = 5432
DB_NAME = "products"
DB_USER = "postgres"
DB_PASSWORD = "postgres"

# API Configuration
API_PREFIX = "/api/v1"
DEBUG = True  # Set to False in production

# Security
SECRET_KEY = "your-secret-key-here"  # Change this in production 