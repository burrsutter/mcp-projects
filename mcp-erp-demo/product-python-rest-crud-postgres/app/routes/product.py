from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, Path, status
from sqlalchemy.orm import Session
from sqlalchemy.exc import IntegrityError

from app.database.connection import get_db
from app.models.product import Product
from app.schemas.product import ProductCreate, ProductUpdate, Product as ProductSchema, ProductList
from app.utils.pagination import PaginationParams, paginate_query, get_pagination_metadata

router = APIRouter(
    prefix="/products",
    tags=["products"],
    responses={404: {"description": "Product not found"}},
)

@router.post("/", response_model=ProductSchema, status_code=status.HTTP_201_CREATED)
def create_product(product: ProductCreate, db: Session = Depends(get_db)):
    """
    Create a new product in the catalog.
    """
    db_product = Product(**product.dict())
    
    try:
        db.add(db_product)
        db.commit()
        db.refresh(db_product)
        return db_product
    except IntegrityError:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Product with SKU '{product.sku}' already exists"
        )

@router.get("/", response_model=ProductList)
def read_products(
    page: int = Query(1, ge=1, description="Page number"),
    page_size: int = Query(10, ge=1, le=100, description="Items per page"),
    sku: Optional[str] = Query(None, description="Filter by SKU"),
    name: Optional[str] = Query(None, description="Filter by name (partial match)"),
    category: Optional[str] = Query(None, description="Filter by category"),
    min_price: Optional[float] = Query(None, ge=0, description="Minimum price"),
    max_price: Optional[float] = Query(None, ge=0, description="Maximum price"),
    is_active: Optional[int] = Query(None, ge=0, le=1, description="Filter by active status (1=active, 0=inactive)"),
    db: Session = Depends(get_db)
):
    """
    Retrieve products with filtering and pagination.
    """
    query = db.query(Product)
    
    # Apply filters
    if sku:
        query = query.filter(Product.sku == sku)
    if name:
        query = query.filter(Product.name.ilike(f"%{name}%"))
    if category:
        query = query.filter(Product.category == category)
    if min_price is not None:
        query = query.filter(Product.price >= min_price)
    if max_price is not None:
        query = query.filter(Product.price <= max_price)
    if is_active is not None:
        query = query.filter(Product.is_active == is_active)
    
    # Apply pagination
    pagination = PaginationParams(page=page, page_size=page_size)
    items, total = paginate_query(query, pagination)
    
    # Prepare response
    pagination_data = get_pagination_metadata(total, pagination)
    
    return {
        "items": items,
        **pagination_data
    }

@router.get("/{product_id}", response_model=ProductSchema)
def read_product(
    product_id: int = Path(..., gt=0, description="The ID of the product to retrieve"),
    db: Session = Depends(get_db)
):
    """
    Retrieve a specific product by ID.
    """
    db_product = db.query(Product).filter(Product.id == product_id).first()
    if db_product is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Product with ID {product_id} not found"
        )
    return db_product

@router.get("/sku/{sku}", response_model=ProductSchema)
def read_product_by_sku(
    sku: str = Path(..., min_length=3, max_length=50, description="The SKU of the product to retrieve"),
    db: Session = Depends(get_db)
):
    """
    Retrieve a specific product by SKU.
    """
    db_product = db.query(Product).filter(Product.sku == sku).first()
    if db_product is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Product with SKU '{sku}' not found"
        )
    return db_product

@router.put("/{product_id}", response_model=ProductSchema)
def update_product(
    product_id: int = Path(..., gt=0, description="The ID of the product to update"),
    product: ProductUpdate = ...,
    db: Session = Depends(get_db)
):
    """
    Update a product's information.
    """
    db_product = db.query(Product).filter(Product.id == product_id).first()
    if db_product is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Product with ID {product_id} not found"
        )
    
    # Update product attributes
    update_data = product.dict(exclude_unset=True)
    for key, value in update_data.items():
        setattr(db_product, key, value)
    
    try:
        db.commit()
        db.refresh(db_product)
        return db_product
    except IntegrityError:
        db.rollback()
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"Product with SKU '{product.sku}' already exists"
        )

@router.delete("/{product_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_product(
    product_id: int = Path(..., gt=0, description="The ID of the product to delete"),
    db: Session = Depends(get_db)
):
    """
    Delete a product.
    """
    db_product = db.query(Product).filter(Product.id == product_id).first()
    if db_product is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Product with ID {product_id} not found"
        )
    
    db.delete(db_product)
    db.commit()
    return None
