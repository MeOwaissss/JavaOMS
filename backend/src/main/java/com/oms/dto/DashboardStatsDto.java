package com.oms.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardStatsDto {
    private long totalProducts;
    private long totalOrders;
    private BigDecimal totalRevenue;
    private long totalCustomers;
    private List<?> recentOrders;
    private Map<String, BigDecimal> monthlyRevenue;
    private Map<String, Long> categoryShares;
}
