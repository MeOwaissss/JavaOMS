package com.oms.controller;

import com.oms.entity.WhatsAppLog;
import com.oms.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/whatsapp-logs")
@PreAuthorize("hasRole('ADMIN')")
public class WhatsAppLogController {

    @Autowired
    private WhatsAppService whatsAppService;

    @GetMapping
    public ResponseEntity<List<WhatsAppLog>> getLogs() {
        return ResponseEntity.ok(whatsAppService.getLogs());
    }
}
