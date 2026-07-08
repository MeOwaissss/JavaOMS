package com.oms.controller;

import com.oms.dto.OrderRequest;
import com.oms.entity.Order;
import com.oms.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getOrders(Principal principal) {
        String username = principal.getName();
        try {
            return ResponseEntity.ok(orderService.getOrdersByCustomerUsername(username));
        } catch (Exception e) {
            return ResponseEntity.ok(orderService.getAllOrders());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Order> placeOrder(Principal principal, @Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.placeOrder(principal.getName(), orderRequest));
    }

    @PostMapping("/admin/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> placeOrderForCustomer(@PathVariable Integer customerId, @Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.placeOrderForCustomer(customerId, orderRequest));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
