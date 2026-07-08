package com.oms.service;

import com.oms.dto.DeliveryUpdateDto;
import com.oms.entity.Delivery;
import com.oms.entity.Order;
import com.oms.repository.DeliveryRepository;
import com.oms.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    public Delivery getDeliveryByOrderId(Integer orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery details not found for order id " + orderId));
    }

    @Transactional
    public Delivery createDelivery(Order order) {
        Delivery delivery = Delivery.builder()
                .order(order)
                .status("PENDING")
                .updatedAt(LocalDateTime.now())
                .build();
        return deliveryRepository.save(delivery);
    }

    @Transactional
    public Delivery updateDelivery(Integer orderId, DeliveryUpdateDto updateDto) {
        Delivery delivery = getDeliveryByOrderId(orderId);
        if (updateDto.getDeliveryPartner() != null) {
            delivery.setDeliveryPartner(updateDto.getDeliveryPartner());
        }
        if (updateDto.getTrackingNumber() != null) {
            delivery.setTrackingNumber(updateDto.getTrackingNumber());
        }
        if (updateDto.getStatus() != null) {
            delivery.setStatus(updateDto.getStatus());
        }
        if (updateDto.getEstimatedDelivery() != null) {
            delivery.setEstimatedDelivery(updateDto.getEstimatedDelivery());
        }
        delivery.setUpdatedAt(LocalDateTime.now());
        return deliveryRepository.save(delivery);
    }
}
