package com.example.salesorder.service;

import com.example.salesorder.dto.OrderDTO;
import com.example.salesorder.dto.OrderDetailDTO;
import com.example.salesorder.model.Order;
import com.example.salesorder.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    
    /**
     * Create a new order
     * 
     * @param orderDTO the order data
     * @return the created order
     */
    OrderDTO createOrder(OrderDTO orderDTO);
    
    /**
     * Get an order by ID
     * 
     * @param id the order ID
     * @return the order if found
     */
    OrderDTO getOrderById(Long id);
    
    /**
     * Get an order by order number
     * 
     * @param orderNumber the order number
     * @return the order if found
     */
    OrderDTO getOrderByOrderNumber(String orderNumber);
    
    /**
     * Get all orders
     * 
     * @return list of all orders
     */
    List<OrderDTO> getAllOrders();
    
    /**
     * Update an existing order
     * 
     * @param id the order ID
     * @param orderDTO the updated order data
     * @return the updated order
     */
    OrderDTO updateOrder(Long id, OrderDTO orderDTO);
    
    /**
     * Delete an order
     * 
     * @param id the order ID
     */
    void deleteOrder(Long id);
    
    /**
     * Find orders by customer name (partial match, case-insensitive)
     * 
     * @param customerName the customer name to search for
     * @return list of matching orders
     */
    List<OrderDTO> findOrdersByCustomerName(String customerName);
    
    /**
     * Find orders by status
     * 
     * @param status the order status
     * @return list of matching orders
     */
    List<OrderDTO> findOrdersByStatus(OrderStatus status);
    
    /**
     * Find orders by date range
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of matching orders
     */
    List<OrderDTO> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Update order status
     * 
     * @param id the order ID
     * @param status the new status
     * @return the updated order
     */
    OrderDTO updateOrderStatus(Long id, OrderStatus status);

    /**
     * Generates a markdown-based invoice for a given order
     * @param orderId The ID of the order to generate the invoice for
     * @return A markdown string containing the formatted invoice
     */
    String generateInvoiceMarkdown(Long orderId);
}
