package com.oms.controller;

import com.oms.entity.Order;
import com.oms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<Order>> getReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer categoryId) {
        return ResponseEntity.ok(reportService.getFilteredOrders(startDate, endDate, customerId, categoryId));
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcelReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer categoryId) {
        List<Order> orders = reportService.getFilteredOrders(startDate, endDate, customerId, categoryId);
        byte[] excelBytes = reportService.exportToExcel(orders);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders-report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
