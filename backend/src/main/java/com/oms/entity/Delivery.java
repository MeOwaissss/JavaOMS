package com.oms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    @Column(name = "delivery_partner", length = 100)
    private String deliveryPartner;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(nullable = false, length = 50)
    private String status = "PENDING";

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
