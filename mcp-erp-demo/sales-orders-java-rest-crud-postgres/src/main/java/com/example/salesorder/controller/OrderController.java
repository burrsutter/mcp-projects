package com.example.salesorder.controller;

import com.example.salesorder.dto.OrderDTO;
import com.example.salesorder.model.OrderStatus;
import com.example.salesorder.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing sales orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new sales order with order details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<OrderDTO> createOrder(
            @Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Returns a single order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> getOrderById(
            @Parameter(description = "ID of the order to retrieve", required = true)
            @PathVariable Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Returns a list of all orders")
    @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number", description = "Returns a single order by its order number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> getOrderByOrderNumber(
            @Parameter(description = "Order number to retrieve", required = true)
            @PathVariable String orderNumber) {
        OrderDTO order = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an order", description = "Updates an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> updateOrder(
            @Parameter(description = "ID of the order to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order", description = "Deletes an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID of the order to delete", required = true)
            @PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer")
    @Operation(summary = "Find orders by customer name", description = "Returns orders matching the customer name (partial match, case-insensitive)")
    @ApiResponse(responseCode = "200", description = "List of matching orders retrieved successfully")
    public ResponseEntity<List<OrderDTO>> findOrdersByCustomerName(
            @Parameter(description = "Customer name to search for", required = true)
            @RequestParam String name) {
        List<OrderDTO> orders = orderService.findOrdersByCustomerName(name);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Find orders by status", description = "Returns orders with the specified status")
    @ApiResponse(responseCode = "200", description = "List of matching orders retrieved successfully")
    public ResponseEntity<List<OrderDTO>> findOrdersByStatus(
            @Parameter(description = "Status to filter by", required = true)
            @PathVariable OrderStatus status) {
        List<OrderDTO> orders = orderService.findOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Find orders by date range", description = "Returns orders placed between the specified dates")
    @ApiResponse(responseCode = "200", description = "List of matching orders retrieved successfully")
    public ResponseEntity<List<OrderDTO>> findOrdersByDateRange(
            @Parameter(description = "Start date (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (ISO format)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<OrderDTO> orders = orderService.findOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Updates the status of an existing order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @Parameter(description = "ID of the order to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "New status", required = true)
            @RequestParam OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "Generate invoice markdown", description = "Generates a markdown-based invoice for a given order")
    @GetMapping(value = "/invoice/{id}", produces = MediaType.TEXT_MARKDOWN_VALUE)
    public ResponseEntity<String> generateInvoiceMarkdown(
            @Parameter(description = "Order ID", required = true) @PathVariable Long id) {
        String markdown = orderService.generateInvoiceMarkdown(id);
        return ResponseEntity.ok(markdown);
    }
}
