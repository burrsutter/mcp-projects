from typing import Optional, List
from datetime import datetime
from pydantic import BaseModel, Field, validator

class ProductBase(BaseModel):
    sku: str = Field(..., min_length=3, max_length=50, description="Stock Keeping Unit, unique identifier for the product")
    name: str = Field(..., min_length=1, max_length=255, description="Product name")
    description: Optional[str] = Field(None, description="Detailed product description")
    category: Optional[str] = Field(None, max_length=100, description="Product category")
    price: float = Field(..., gt=0, description="Selling price of the product")
    cost: Optional[float] = Field(None, ge=0, description="Cost price of the product")
    stock_quantity: int = Field(0, ge=0, description="Current stock quantity")
    manufacturer: Optional[str] = Field(None, max_length=255, description="Product manufacturer")
    supplier: Optional[str] = Field(None, max_length=255, description="Product supplier")
    weight: Optional[float] = Field(None, ge=0, description="Product weight")
    dimensions: Optional[str] = Field(None, max_length=100, description="Product dimensions (LxWxH)")
    is_active: int = Field(1, ge=0, le=1, description="Product status (1 for active, 0 for inactive)")

    @validator('price', 'cost', 'weight')
    def round_to_two_decimals(cls, v):
        if v is not None:
            return round(v, 2)
        return v

class ProductCreate(ProductBase):
    pass

class ProductUpdate(BaseModel):
    sku: Optional[str] = Field(None, min_length=3, max_length=50, description="Stock Keeping Unit, unique identifier for the product")
    name: Optional[str] = Field(None, min_length=1, max_length=255, description="Product name")
    description: Optional[str] = Field(None, description="Detailed product description")
    category: Optional[str] = Field(None, max_length=100, description="Product category")
    price: Optional[float] = Field(None, gt=0, description="Selling price of the product")
    cost: Optional[float] = Field(None, ge=0, description="Cost price of the product")
    stock_quantity: Optional[int] = Field(None, ge=0, description="Current stock quantity")
    manufacturer: Optional[str] = Field(None, max_length=255, description="Product manufacturer")
    supplier: Optional[str] = Field(None, max_length=255, description="Product supplier")
    weight: Optional[float] = Field(None, ge=0, description="Product weight")
    dimensions: Optional[str] = Field(None, max_length=100, description="Product dimensions (LxWxH)")
    is_active: Optional[int] = Field(None, ge=0, le=1, description="Product status (1 for active, 0 for inactive)")

    @validator('price', 'cost', 'weight')
    def round_to_two_decimals(cls, v):
        if v is not None:
            return round(v, 2)
        return v

class ProductInDB(ProductBase):
    id: int
    created_at: datetime
    updated_at: Optional[datetime] = None

    class Config:
        orm_mode = True

class Product(ProductInDB):
    pass

class ProductList(BaseModel):
    items: List[Product]
    total: int
    page: int
    page_size: int
    pages: int

    class Config:
        orm_mode = True
