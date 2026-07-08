package com.oms.controller;

import com.oms.dto.DeliveryUpdateDto;
import com.oms.entity.Delivery;
import com.oms.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Delivery> getDeliveryByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(deliveryService.getDeliveryByOrderId(orderId));
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable Integer orderId, @RequestBody DeliveryUpdateDto updateDto) {
        return ResponseEntity.ok(deliveryService.updateDelivery(orderId, updateDto));
    }
}
