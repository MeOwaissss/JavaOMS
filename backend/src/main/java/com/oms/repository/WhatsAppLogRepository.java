package com.oms.repository;

import com.oms.entity.WhatsAppLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WhatsAppLogRepository extends JpaRepository<WhatsAppLog, Integer> {
    List<WhatsAppLog> findAllByOrderBySentAtDesc();
}
