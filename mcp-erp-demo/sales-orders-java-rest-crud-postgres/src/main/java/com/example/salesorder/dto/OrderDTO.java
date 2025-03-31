package com.example.salesorder.dto;

import com.example.salesorder.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for order operations")
public class OrderDTO {
    
    @Schema(description = "ID of the order (only used in responses)", example = "1")
    private Long id;
    
    @Schema(description = "Unique order number (generated automatically for new orders)", example = "ORD-2025-0001")
    private String orderNumber;
    
    @NotBlank(message = "Customer name is required")
    @Schema(description = "Name of the customer", example = "John Doe", required = true)
    private String customerName;
    
    @PastOrPresent(message = "Order date cannot be in the future")
    @Schema(description = "Date when the order was placed (defaults to current time if not provided)", example = "2025-03-10T10:30:00")
    private LocalDateTime orderDate;
    
    @Schema(description = "Current status of the order (defaults to PENDING for new orders)", example = "PENDING")
    private OrderStatus status;
    
    @Schema(description = "Total amount of the order (calculated automatically from order details)", example = "299.99")
    private BigDecimal totalAmount;
    
    @NotEmpty(message = "At least one order detail is required")
    @Valid
    @Schema(description = "List of order details", required = true)
    @Builder.Default
    private List<OrderDetailDTO> orderDetails = new ArrayList<>();
}
