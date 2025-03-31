package com.example.salesorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for order detail operations")
public class OrderDetailDTO {
    
    @Schema(description = "ID of the order detail (only used in responses)", example = "1")
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Schema(description = "Name of the product", example = "Smartphone XYZ", required = true)
    private String productName;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Schema(description = "Quantity of the product", example = "2", required = true)
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    @Schema(description = "Price per unit of the product", example = "499.99", required = true)
    private BigDecimal unitPrice;
    
    @Schema(description = "Subtotal for this line item (calculated automatically)", example = "999.98")
    private BigDecimal subtotal;
}
