package com.example.salesorder.repository;

import com.example.salesorder.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    
    List<OrderDetail> findByOrderId(Long orderId);
    
    List<OrderDetail> findByProductNameContainingIgnoreCase(String productName);
    
    @Query("SELECT od FROM OrderDetail od WHERE od.quantity > :minQuantity")
    List<OrderDetail> findByQuantityGreaterThan(int minQuantity);
    
    @Query("SELECT od FROM OrderDetail od WHERE od.unitPrice BETWEEN :minPrice AND :maxPrice")
    List<OrderDetail> findByUnitPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT od FROM OrderDetail od WHERE od.subtotal > :minSubtotal")
    List<OrderDetail> findBySubtotalGreaterThan(BigDecimal minSubtotal);
}
