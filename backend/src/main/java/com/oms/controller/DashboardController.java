package com.oms.controller;

import com.oms.dto.DashboardStatsDto;
import com.oms.entity.Order;
import com.oms.entity.OrderItem;
import com.oms.repository.CustomerRepository;
import com.oms.repository.OrderRepository;
import com.oms.repository.ProductRepository;
import com.oms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDto> getStats() {
        long totalProducts = productRepository.count();
        long totalCustomers = customerRepository.count();
        List<Order> orders = orderRepository.findAll();
        long totalOrders = orders.size();

        BigDecimal totalRevenue = orders.stream()
                .filter(o -> !"CANCELLED".equals(o.getStatus()))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Order> recentOrders = orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .limit(5)
                .collect(Collectors.toList());

        Map<String, BigDecimal> monthlyRevenue = new LinkedHashMap<>();
        Map<String, Long> categoryShares = new HashMap<>();

        for (Order o : orders) {
            if (!"CANCELLED".equals(o.getStatus())) {
                String month = o.getCreatedAt().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                monthlyRevenue.put(month, monthlyRevenue.getOrDefault(month, BigDecimal.ZERO).add(o.getTotalAmount()));
            }

            for (OrderItem item : o.getOrderItems()) {
                String cat = item.getProduct().getCategory().getName();
                categoryShares.put(cat, categoryShares.getOrDefault(cat, 0L) + item.getQuantity());
            }
        }

        DashboardStatsDto dto = DashboardStatsDto.builder()
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .totalCustomers(totalCustomers)
                .recentOrders(recentOrders)
                .monthlyRevenue(monthlyRevenue)
                .categoryShares(categoryShares)
                .build();

        return ResponseEntity.ok(dto);
    }
}
