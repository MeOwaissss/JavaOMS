package com.oms.service;

import com.oms.entity.Order;
import com.oms.entity.OrderItem;
import com.oms.repository.OrderRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getFilteredOrders(String startDate, String endDate, Integer customerId, Integer categoryId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = startDate != null ? LocalDate.parse(startDate, formatter) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate, formatter) : null;

        return orderRepository.findAll().stream().filter(order -> {
            LocalDate orderDate = order.getCreatedAt().toLocalDate();
            if (start != null && orderDate.isBefore(start)) return false;
            if (end != null && orderDate.isAfter(end)) return false;
            if (customerId != null && !order.getCustomer().getId().equals(customerId)) return false;
            if (categoryId != null) {
                boolean match = order.getOrderItems().stream()
                        .anyMatch(item -> item.getProduct().getCategory().getId().equals(categoryId));
                if (!match) return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    public byte[] exportToExcel(List<Order> orders) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Orders Report");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"Order Number", "Date", "Customer", "Items Count", "Total Amount", "Status"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(order.getOrderNumber());
                row.createCell(1).setCellValue(order.getCreatedAt().toString());
                row.createCell(2).setCellValue(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
                row.createCell(3).setCellValue(order.getOrderItems().size());
                row.createCell(4).setCellValue(order.getTotalAmount().doubleValue());
                row.createCell(5).setCellValue(order.getStatus());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export Excel report", e);
        }
    }
}
