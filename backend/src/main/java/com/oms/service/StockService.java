package com.oms.service;

import com.oms.dto.StockAdjustmentDto;
import com.oms.entity.Product;
import com.oms.entity.Stock;
import com.oms.entity.StockHistory;
import com.oms.repository.StockHistoryRepository;
import com.oms.repository.StockRepository;
import com.oms.exception.ResourceNotFoundException;
import com.oms.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Autowired
    private ProductService productService;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Stock getStockByProductId(Integer productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock not found for product id " + productId));
    }

    public List<Stock> getLowStocks() {
        return stockRepository.findAll().stream()
                .filter(stock -> stock.getQuantity() <= stock.getMinimumRequired())
                .collect(Collectors.toList());
    }

    @Transactional
    public Stock adjustStock(StockAdjustmentDto adjustment) {
        Product product = productService.getProductById(adjustment.getProductId());
        Stock stock = getStockByProductId(product.getId());

        int oldQuantity = stock.getQuantity();
        int change = adjustment.getChangeQuantity();
        String type = adjustment.getType().toUpperCase();

        int newQuantity;
        if ("ADD".equals(type)) {
            newQuantity = oldQuantity + change;
        } else if ("REMOVE".equals(type)) {
            if (oldQuantity < change) {
                throw new BadRequestException("Insufficient stock. Current: " + oldQuantity + ", Request: " + change);
            }
            newQuantity = oldQuantity - change;
        } else if ("ADJUST".equals(type)) {
            newQuantity = change;
            change = newQuantity - oldQuantity;
        } else {
            throw new BadRequestException("Invalid stock adjustment type: " + type);
        }

        stock.setQuantity(newQuantity);
        Stock savedStock = stockRepository.save(stock);

        StockHistory history = StockHistory.builder()
                .stock(savedStock)
                .changeQuantity(change)
                .type(type)
                .reason(adjustment.getReason())
                .build();
        stockHistoryRepository.save(history);

        return savedStock;
    }

    public List<StockHistory> getStockHistoryByProduct(Integer productId) {
        Stock stock = getStockByProductId(productId);
        return stockHistoryRepository.findByStockIdOrderByCreatedAtDesc(stock.getId());
    }

    public List<StockHistory> getAllStockHistory() {
        return stockHistoryRepository.findAllByOrderByCreatedAtDesc();
    }
}
