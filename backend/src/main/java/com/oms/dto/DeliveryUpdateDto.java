package com.oms.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DeliveryUpdateDto {
    private String deliveryPartner;
    private String trackingNumber;
    private String status;
    private LocalDate estimatedDelivery;
}
