package com.oms.repository;

import com.oms.entity.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Integer> {
    List<StockHistory> findByStockIdOrderByCreatedAtDesc(Integer stockId);
    List<StockHistory> findAllByOrderByCreatedAtDesc();
}
