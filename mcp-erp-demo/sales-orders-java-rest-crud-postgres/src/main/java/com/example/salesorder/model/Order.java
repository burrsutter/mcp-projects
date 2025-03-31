package com.example.salesorder.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Order entity representing a sales order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the order", example = "1")
    private Long id;

    @NotBlank(message = "Order number is required")
    @Column(name = "orderNumber", unique = true, nullable = false)
    @Schema(description = "Unique order number", example = "ORD-2025-0001")
    private String orderNumber;

    @NotBlank(message = "Customer id is required")
    @Column(name = "customer_id", nullable = false)
    @Schema(description = "ID of the customer", example = "John Doe")
    private String customerId;


    @NotBlank(message = "Customer name is required")
    @Column(name = "customer_name", nullable = false)
    @Schema(description = "Name of the customer", example = "John Doe")
    private String customerName;

    @NotNull(message = "Order date is required")
    @PastOrPresent(message = "Order date cannot be in the future")
    @Column(name = "order_date", nullable = false)
    @Schema(description = "Date when the order was placed", example = "2025-03-10T10:30:00")
    private LocalDateTime orderDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Schema(description = "Current status of the order", example = "PENDING")
    private OrderStatus status;

    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be zero or positive")
    @Column(name = "total_amount", nullable = false)
    @Schema(description = "Total amount of the order", example = "299.99")
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "List of order details")
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Schema(description = "Timestamp when the order was created", example = "2025-03-10T10:30:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Schema(description = "Timestamp when the order was last updated", example = "2025-03-10T10:35:00")
    private LocalDateTime updatedAt;

    // Helper method to add order detail
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    // Helper method to remove order detail
    public void removeOrderDetail(OrderDetail orderDetail) {
        orderDetails.remove(orderDetail);
        orderDetail.setOrder(null);
    }
}
