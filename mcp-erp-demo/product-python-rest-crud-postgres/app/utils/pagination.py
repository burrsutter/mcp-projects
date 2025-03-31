import math
from typing import TypeVar, Generic, List, Optional
from pydantic import BaseModel

T = TypeVar('T')

class PaginationParams:
    def __init__(self, page: int = 1, page_size: int = 10):
        self.page = max(1, page)
        self.page_size = min(max(1, page_size), 100)  # Limit page size to 100
        self.offset = (self.page - 1) * self.page_size

def paginate_query(query, pagination: PaginationParams):
    """
    Apply pagination to a SQLAlchemy query.
    
    Args:
        query: SQLAlchemy query object
        pagination: PaginationParams instance
        
    Returns:
        Tuple of (paginated query, total count)
    """
    total = query.count()
    items = query.offset(pagination.offset).limit(pagination.page_size).all()
    
    return items, total

def get_pagination_metadata(total: int, pagination: PaginationParams):
    """
    Calculate pagination metadata.
    
    Args:
        total: Total number of items
        pagination: PaginationParams instance
        
    Returns:
        Dictionary with pagination metadata
    """
    pages = math.ceil(total / pagination.page_size) if total > 0 else 0
    
    return {
        "total": total,
        "page": pagination.page,
        "page_size": pagination.page_size,
        "pages": pages
    }
