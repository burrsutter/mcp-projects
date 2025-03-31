package com.example.salesorder.controller;

import com.example.salesorder.dto.OrderDTO;
import com.example.salesorder.dto.OrderDetailDTO;
import com.example.salesorder.model.OrderStatus;
import com.example.salesorder.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        // Arrange
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .productName("Test Product")
                .quantity(2)
                .unitPrice(new BigDecimal("99.99"))
                .build();

        OrderDTO inputOrderDTO = OrderDTO.builder()
                .customerName("John Doe")
                .orderDetails(Collections.singletonList(orderDetailDTO))
                .build();

        OrderDTO outputOrderDTO = OrderDTO.builder()
                .id(1L)
                .orderNumber("ORD-20250311")
                .customerName("John Doe")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("199.98"))
                .orderDetails(Collections.singletonList(orderDetailDTO))
                .build();

        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(outputOrderDTO);

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputOrderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.orderNumber", is("ORD-20250311")))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.orderDetails", hasSize(1)))
                .andExpect(jsonPath("$.orderDetails[0].productName", is("Test Product")))
                .andExpect(jsonPath("$.orderDetails[0].quantity", is(2)));
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        // Arrange
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .id(1L)
                .productName("Test Product")
                .quantity(2)
                .unitPrice(new BigDecimal("99.99"))
                .subtotal(new BigDecimal("199.98"))
                .build();

        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .orderNumber("ORD-20250311")
                .customerName("John Doe")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("199.98"))
                .orderDetails(Collections.singletonList(orderDetailDTO))
                .build();

        when(orderService.getOrderById(1L)).thenReturn(orderDTO);

        // Act & Assert
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.orderNumber", is("ORD-20250311")))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.orderDetails", hasSize(1)))
                .andExpect(jsonPath("$.orderDetails[0].productName", is("Test Product")))
                .andExpect(jsonPath("$.orderDetails[0].quantity", is(2)));
    }

    @Test
    void getAllOrders_ShouldReturnListOfOrders() throws Exception {
        // Arrange
        OrderDetailDTO orderDetail1 = OrderDetailDTO.builder()
                .id(1L)
                .productName("Product 1")
                .quantity(2)
                .unitPrice(new BigDecimal("99.99"))
                .subtotal(new BigDecimal("199.98"))
                .build();

        OrderDetailDTO orderDetail2 = OrderDetailDTO.builder()
                .id(2L)
                .productName("Product 2")
                .quantity(1)
                .unitPrice(new BigDecimal("149.99"))
                .subtotal(new BigDecimal("149.99"))
                .build();

        OrderDTO order1 = OrderDTO.builder()
                .id(1L)
                .orderNumber("ORD-20250311-1")
                .customerName("John Doe")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("199.98"))
                .orderDetails(Collections.singletonList(orderDetail1))
                .build();

        OrderDTO order2 = OrderDTO.builder()
                .id(2L)
                .orderNumber("ORD-20250311-2")
                .customerName("Jane Smith")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PROCESSING)
                .totalAmount(new BigDecimal("149.99"))
                .orderDetails(Collections.singletonList(orderDetail2))
                .build();

        List<OrderDTO> orders = Arrays.asList(order1, order2);

        when(orderService.getAllOrders()).thenReturn(orders);

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].customerName", is("John Doe")))
                .andExpect(jsonPath("$[0].status", is("PENDING")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].customerName", is("Jane Smith")))
                .andExpect(jsonPath("$[1].status", is("PROCESSING")));
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        // Arrange
        OrderDetailDTO orderDetailDTO = OrderDetailDTO.builder()
                .id(1L)
                .productName("Test Product")
                .quantity(2)
                .unitPrice(new BigDecimal("99.99"))
                .subtotal(new BigDecimal("199.98"))
                .build();

        OrderDTO orderDTO = OrderDTO.builder()
                .id(1L)
                .orderNumber("ORD-20250311")
                .customerName("John Doe")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.SHIPPED)
                .totalAmount(new BigDecimal("199.98"))
                .orderDetails(Collections.singletonList(orderDetailDTO))
                .build();

        when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.SHIPPED))).thenReturn(orderDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/orders/1/status")
                        .param("status", "SHIPPED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("SHIPPED")));
    }
}
