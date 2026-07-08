package com.oms.service;

import com.oms.dto.OrderItemRequest;
import com.oms.dto.OrderRequest;
import com.oms.dto.StockAdjustmentDto;
import com.oms.entity.*;
import com.oms.repository.CustomerRepository;
import com.oms.repository.OrderItemRepository;
import com.oms.repository.OrderRepository;
import com.oms.repository.UserRepository;
import com.oms.exception.ResourceNotFoundException;
import com.oms.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private StockService stockService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private NotificationService notificationService;

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    public List<Order> getOrdersByCustomerUsername(String username) {
        Customer customer = customerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer details not found"));
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId());
    }

    @Transactional
    public Order placeOrder(String username, OrderRequest orderRequest) {
        Customer customer = customerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer details not found"));
        return createOrderForCustomer(customer, orderRequest);
    }

    @Transactional
    public Order placeOrderForCustomer(Integer customerId, OrderRequest orderRequest) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));
        return createOrderForCustomer(customer, orderRequest);
    }

    private Order createOrderForCustomer(Customer customer, OrderRequest orderRequest) {

        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Order order = Order.builder()
                .orderNumber(orderNumber)
                .customer(customer)
                .totalAmount(BigDecimal.ZERO)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest req : orderRequest.getItems()) {
            Product product = productService.getProductById(req.getProductId());
            Stock stock = stockService.getStockByProductId(product.getId());

            if (stock.getQuantity() < req.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName() + 
                        ". Available: " + stock.getQuantity());
            }

            StockAdjustmentDto stockAdj = new StockAdjustmentDto();
            stockAdj.setProductId(product.getId());
            stockAdj.setChangeQuantity(req.getQuantity());
            stockAdj.setType("REMOVE");
            stockAdj.setReason("Order booked: " + orderNumber);
            stockService.adjustStock(stockAdj);

            BigDecimal linePrice = product.getPrice();
            BigDecimal subtotal = linePrice.multiply(BigDecimal.valueOf(req.getQuantity()));
            BigDecimal gstAmount = subtotal.multiply(product.getGstPercent()).divide(BigDecimal.valueOf(100));

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(req.getQuantity())
                    .price(linePrice)
                    .gstAmount(gstAmount)
                    .build();

            orderItemRepository.save(orderItem);
            items.add(orderItem);

            totalAmount = totalAmount.add(subtotal).add(gstAmount);
        }

        savedOrder.setTotalAmount(totalAmount);
        savedOrder.setOrderItems(items);
        savedOrder = orderRepository.save(savedOrder);

        invoiceService.generateAndSaveInvoice(savedOrder);
        deliveryService.createDelivery(savedOrder);

        whatsAppService.sendBookingNotificationToAdmin(orderNumber, 
                customer.getFirstName() + " " + customer.getLastName(), 
                totalAmount.toString());

        notificationService.createNotification(customer.getUser(), 
                "Your order " + orderNumber + " has been successfully booked!");

        return savedOrder;
    }

    @Transactional
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = getOrderById(orderId);
        String oldStatus = order.getStatus();
        status = status.toUpperCase();

        if (oldStatus.equals(status)) {
            return order;
        }

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        deliveryService.updateDelivery(orderId, new com.oms.dto.DeliveryUpdateDto() {{
            setStatus(updatedOrder.getStatus());
        }});

        if ("CANCELLED".equals(status)) {
            for (OrderItem item : order.getOrderItems()) {
                StockAdjustmentDto stockAdj = new StockAdjustmentDto();
                stockAdj.setProductId(item.getProduct().getId());
                stockAdj.setChangeQuantity(item.getQuantity());
                stockAdj.setType("ADD");
                stockAdj.setReason("Order cancelled: " + order.getOrderNumber());
                stockService.adjustStock(stockAdj);
            }
        }

        notificationService.createNotification(order.getCustomer().getUser(), 
                "Your order " + order.getOrderNumber() + " status is updated to: " + status);

        return updatedOrder;
    }
}
