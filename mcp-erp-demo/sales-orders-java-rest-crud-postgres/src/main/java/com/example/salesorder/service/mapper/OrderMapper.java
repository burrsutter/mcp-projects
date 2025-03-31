package com.example.salesorder.service.mapper;

import com.example.salesorder.dto.OrderDTO;
import com.example.salesorder.dto.OrderDetailDTO;
import com.example.salesorder.model.Order;
import com.example.salesorder.model.OrderDetail;
import com.example.salesorder.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    /**
     * Convert Order entity to OrderDTO
     */
    public OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderDetailDTO> orderDetailDTOs = order.getOrderDetails().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerName(order.getCustomerName())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDetails(orderDetailDTOs)
                .build();
    }

    /**
     * Convert OrderDetail entity to OrderDetailDTO
     */
    public OrderDetailDTO toDTO(OrderDetail orderDetail) {
        if (orderDetail == null) {
            return null;
        }

        return OrderDetailDTO.builder()
                .id(orderDetail.getId())
                .productName(orderDetail.getProductName())
                .quantity(orderDetail.getQuantity())
                .unitPrice(orderDetail.getUnitPrice())
                .subtotal(orderDetail.getSubtotal())
                .build();
    }

    /**
     * Convert OrderDTO to Order entity for creating a new order
     */
    public Order toEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        Order order = Order.builder()
                .customerName(orderDTO.getCustomerName())
                .orderDate(orderDTO.getOrderDate() != null ? orderDTO.getOrderDate() : LocalDateTime.now())
                .status(orderDTO.getStatus() != null ? orderDTO.getStatus() : OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO) // Will be calculated from order details
                .build();

        // Generate order number if not provided
        if (orderDTO.getOrderNumber() == null || orderDTO.getOrderNumber().isEmpty()) {
            order.setOrderNumber(generateOrderNumber());
        } else {
            order.setOrderNumber(orderDTO.getOrderNumber());
        }

        // Convert and add order details
        if (orderDTO.getOrderDetails() != null) {
            orderDTO.getOrderDetails().forEach(detailDTO -> {
                OrderDetail detail = toEntity(detailDTO);
                order.addOrderDetail(detail);
            });
        }

        // Calculate total amount
        calculateTotalAmount(order);

        return order;
    }

    /**
     * Convert OrderDetailDTO to OrderDetail entity
     */
    public OrderDetail toEntity(OrderDetailDTO orderDetailDTO) {
        if (orderDetailDTO == null) {
            return null;
        }

        return OrderDetail.builder()
                .productName(orderDetailDTO.getProductName())
                .quantity(orderDetailDTO.getQuantity())
                .unitPrice(orderDetailDTO.getUnitPrice())
                .build();
    }

    /**
     * Update an existing Order entity with data from OrderDTO
     */
    public void updateEntityFromDTO(OrderDTO orderDTO, Order order) {
        if (orderDTO == null || order == null) {
            return;
        }

        if (orderDTO.getCustomerName() != null) {
            order.setCustomerName(orderDTO.getCustomerName());
        }

        if (orderDTO.getOrderDate() != null) {
            order.setOrderDate(orderDTO.getOrderDate());
        }

        if (orderDTO.getStatus() != null) {
            order.setStatus(orderDTO.getStatus());
        }

        // Handle order details update
        if (orderDTO.getOrderDetails() != null && !orderDTO.getOrderDetails().isEmpty()) {
            // Remove all existing order details
            order.getOrderDetails().clear();

            // Add new order details from DTO
            orderDTO.getOrderDetails().forEach(detailDTO -> {
                OrderDetail detail = toEntity(detailDTO);
                order.addOrderDetail(detail);
            });

            // Recalculate total amount
            calculateTotalAmount(order);
        }
    }

    /**
     * Calculate total amount of an order based on its order details
     */
    private void calculateTotalAmount(Order order) {
        BigDecimal total = order.getOrderDetails().stream()
                .map(detail -> detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        order.setTotalAmount(total);
    }

    /**
     * Generate a unique order number based on current date and time
     */
    private String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return "ORD-" + now.format(formatter);
    }
}
