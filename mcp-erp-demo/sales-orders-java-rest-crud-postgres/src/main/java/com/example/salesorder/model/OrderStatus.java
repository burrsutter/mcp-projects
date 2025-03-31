package com.example.salesorder.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration of possible order statuses")
public enum OrderStatus {
    @Schema(description = "Order has been created but not processed")
    PENDING,
    
    @Schema(description = "Order has been confirmed and is being processed")
    PROCESSING,
    
    @Schema(description = "Order has been shipped to the customer")
    SHIPPED,
    
    @Schema(description = "Order has been delivered to the customer")
    DELIVERED,
    
    @Schema(description = "Order has been cancelled")
    CANCELLED
}
