package com.example.salesorder.repository;

import com.example.salesorder.model.Order;
import com.example.salesorder.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
    
    List<Order> findByStatus(OrderStatus status);
    
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.totalAmount >= :minAmount AND o.totalAmount <= :maxAmount")
    List<Order> findByTotalAmountRange(double minAmount, double maxAmount);
    
    @Query("SELECT o FROM Order o JOIN o.orderDetails od WHERE od.productName = :productName")
    List<Order> findByProductName(String productName);
}
