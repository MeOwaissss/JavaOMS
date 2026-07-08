package com.oms.service;

import com.oms.entity.WhatsAppLog;
import com.oms.repository.WhatsAppLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WhatsAppService {

    @Autowired
    private WhatsAppLogRepository whatsAppLogRepository;

    public void sendBookingNotificationToAdmin(String orderNumber, String customerName, String totalAmount) {
        String adminPhone = "+1234567890";
        String message = String.format("OMS Booking Alert: New Order %s booked by Customer %s. Total amount: $%s. Please login to confirm.", 
                orderNumber, customerName, totalAmount);
        
        WhatsAppLog log = WhatsAppLog.builder()
                .recipientNumber(adminPhone)
                .messageBody(message)
                .status("SENT")
                .build();
        
        whatsAppLogRepository.save(log);
    }

    public List<WhatsAppLog> getLogs() {
        return whatsAppLogRepository.findAllByOrderBySentAtDesc();
    }
}
