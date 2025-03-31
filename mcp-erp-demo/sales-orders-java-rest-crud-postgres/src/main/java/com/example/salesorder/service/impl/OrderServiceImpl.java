package com.example.salesorder.service.impl;

import com.example.salesorder.dto.OrderDTO;
import com.example.salesorder.dto.OrderDetailDTO;
import com.example.salesorder.exception.ResourceNotFoundException;
import com.example.salesorder.model.Order;
import com.example.salesorder.model.OrderDetail;
import com.example.salesorder.model.OrderStatus;
import com.example.salesorder.repository.OrderRepository;
import com.example.salesorder.service.OrderService;
import com.example.salesorder.service.mapper.OrderMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with order number: " + orderNumber));
        return orderMapper.toDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        
        orderMapper.updateEntityFromDTO(orderDTO, existingOrder);
        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> findOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameContainingIgnoreCase(customerName).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public String generateInvoiceMarkdown(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        StringBuilder markdown = new StringBuilder();
        
        // Header
        markdown.append("# Invoice\n\n");
        markdown.append("## Order Details\n");
        markdown.append(String.format("- **Order Number:** %s\n", order.getOrderNumber()));
        markdown.append(String.format("- **Order Date:** %s\n", 
            order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        markdown.append(String.format("- **Status:** %s\n\n", order.getStatus()));

        // Customer Information
        markdown.append("## Customer Information\n");
        markdown.append(String.format("- **Customer ID:** %s\n", order.getCustomerId()));
        markdown.append(String.format("- **Customer Name:** %s\n\n", order.getCustomerName()));

        // Order Items
        markdown.append("## Order Items\n");
        markdown.append("| Product ID | Product Name | Quantity | Unit Price | Subtotal |\n");
        markdown.append("|------------|--------------|----------|------------|----------|\n");
        
        for (OrderDetail detail : order.getOrderDetails()) {
            markdown.append(String.format("| %s | %s | %d | $%.2f | $%.2f |\n",
                detail.getProductId(),
                detail.getProductName(),
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getSubtotal()));
        }

        // Total
        markdown.append("\n## Total\n");
        markdown.append(String.format("**Total Amount:** $%.2f\n", order.getTotalAmount()));

        // Footer
        markdown.append("\n---\n");
        markdown.append("*This is a computer-generated invoice. No signature required.*\n");
        markdown.append(String.format("*Generated on: %s*", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        return markdown.toString();
    }
}
