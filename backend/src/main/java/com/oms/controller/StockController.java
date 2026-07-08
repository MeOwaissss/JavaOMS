package com.oms.controller;

import com.oms.dto.StockAdjustmentDto;
import com.oms.entity.Stock;
import com.oms.entity.StockHistory;
import com.oms.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetMapping("/low")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Stock>> getLowStocks() {
        return ResponseEntity.ok(stockService.getLowStocks());
    }

    @PostMapping("/adjust")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Stock> adjustStock(@Valid @RequestBody StockAdjustmentDto adjustmentDto) {
        return ResponseEntity.ok(stockService.adjustStock(adjustmentDto));
    }

    @GetMapping("/{productId}/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockHistory>> getStockHistoryByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(stockService.getStockHistoryByProduct(productId));
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockHistory>> getAllStockHistory() {
        return ResponseEntity.ok(stockService.getAllStockHistory());
    }
}
