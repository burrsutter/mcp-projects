package com.example.salesorder.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")
@Schema(description = "Order detail entity representing a line item in an order")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the order detail", example = "1")
    private Long id;

    @NotBlank(message = "Product id is required")
    @Column(name = "product_id", nullable = false)
    @Schema(description = "product identifier", example = "MUG-SMART-001")
    private String productId;
    
    @NotBlank(message = "Product name is required")
    @Column(name = "product_name", nullable = false)
    @Schema(description = "Name of the product", example = "RetroTech Smart Ceramic Mug")
    private String productName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(name = "quantity", nullable = false)
    @Schema(description = "Quantity of the product", example = "2")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    @Column(name = "unit_price", nullable = false)
    @Schema(description = "Price per unit of the product", example = "499.99")
    private BigDecimal unitPrice;

    @Column(name = "subtotal", nullable = false)
    @Schema(description = "Subtotal for this line item (quantity * unitPrice)", example = "999.98")
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Timestamp when the order detail was created", example = "2025-03-10T10:30:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the order detail was last updated", example = "2025-03-10T10:35:00")
    private LocalDateTime updatedAt;

    // Calculate subtotal before persist or update
    @PrePersist
    @PreUpdate
    public void calculateSubtotal() {
        if (quantity != null && unitPrice != null) {
            subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
