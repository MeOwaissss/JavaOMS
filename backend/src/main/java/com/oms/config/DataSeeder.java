package com.oms.config;

import com.oms.entity.Role;
import com.oms.entity.User;
import com.oms.repository.RoleRepository;
import com.oms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.oms.repository.CustomerRepository customerRepository;

    @Autowired
    private com.oms.repository.CategoryRepository categoryRepository;

    @Autowired
    private com.oms.repository.ProductRepository productRepository;

    @Autowired
    private com.oms.repository.StockRepository stockRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            roleRepository.save(admin);
        }
        if (roleRepository.findByName("ROLE_CUSTOMER").isEmpty()) {
            Role customer = new Role();
            customer.setName("ROLE_CUSTOMER");
            roleRepository.save(customer);
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@oms.com")
                    .enabled(true)
                    .role(adminRole)
                    .build();
            userRepository.save(admin);
        }

        if (userRepository.findByUsername("rahul").isEmpty()) {
            Role customerRole = roleRepository.findByName("ROLE_CUSTOMER").orElse(null);
            User rahulUser = User.builder()
                    .username("rahul")
                    .password(passwordEncoder.encode("123456"))
                    .email("rahul@gmail.com")
                    .enabled(true)
                    .role(customerRole)
                    .build();
            rahulUser = userRepository.save(rahulUser);

            com.oms.entity.Customer rahulCustomer = com.oms.entity.Customer.builder()
                    .user(rahulUser)
                    .firstName("Rahul")
                    .lastName("Demo")
                    .phone("1234567890")
                    .address("Demo Address")
                    .city("Demo City")
                    .state("Demo State")
                    .zipCode("000000")
                    .build();
            customerRepository.save(rahulCustomer);
        }

        // Seed Categories and Products if Fashion category doesn't exist
        java.util.List<com.oms.entity.Category> allCategories = categoryRepository.findAll();
        boolean hasFashion = allCategories.stream().anyMatch(c -> c.getName().equals("Fashion"));
        if (!hasFashion) {
            com.oms.entity.Category fashion = com.oms.entity.Category.builder().name("Fashion").description("Clothing and Accessories").build();
            com.oms.entity.Category electronics = com.oms.entity.Category.builder().name("Electronics").description("Gadgets and Devices").build();
            com.oms.entity.Category books = com.oms.entity.Category.builder().name("Books").description("Fiction and Non-Fiction").build();

            fashion = categoryRepository.save(fashion);
            electronics = categoryRepository.save(electronics);
            books = categoryRepository.save(books);

            // Add Products
            com.oms.entity.Product p1 = com.oms.entity.Product.builder()
                .name("Leather Jacket")
                .description("Premium black leather jacket for winter.")
                .price(java.math.BigDecimal.valueOf(120.00))
                .category(fashion)
                .sku("FAS-JAC-001")
                .imageUrl("https://images.unsplash.com/photo-1551028719-00167b16eac5?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80")
                .gstPercent(java.math.BigDecimal.valueOf(18.00))
                .build();

            com.oms.entity.Product p2 = com.oms.entity.Product.builder()
                .name("Running Sneakers")
                .description("Lightweight running shoes with optimal grip.")
                .price(java.math.BigDecimal.valueOf(80.00))
                .category(fashion)
                .sku("FAS-SHO-002")
                .imageUrl("https://images.unsplash.com/photo-1542291026-7eec264c27ff?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80")
                .gstPercent(java.math.BigDecimal.valueOf(18.00))
                .build();

            com.oms.entity.Product e1 = com.oms.entity.Product.builder()
                .name("Wireless Noise-Cancelling Headphones")
                .description("Over-ear headphones with superior sound.")
                .price(java.math.BigDecimal.valueOf(250.00))
                .category(electronics)
                .sku("ELE-HDP-001")
                .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80")
                .gstPercent(java.math.BigDecimal.valueOf(18.00))
                .build();

            com.oms.entity.Product e2 = com.oms.entity.Product.builder()
                .name("Smartwatch Series 8")
                .description("Advanced health monitoring smartwatch.")
                .price(java.math.BigDecimal.valueOf(399.00))
                .category(electronics)
                .sku("ELE-WAT-002")
                .imageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80")
                .gstPercent(java.math.BigDecimal.valueOf(18.00))
                .build();

            com.oms.entity.Product b1 = com.oms.entity.Product.builder()
                .name("The Great Gatsby")
                .description("Classic novel by F. Scott Fitzgerald.")
                .price(java.math.BigDecimal.valueOf(15.00))
                .category(books)
                .sku("BOK-FIC-001")
                .imageUrl("https://images.unsplash.com/photo-1544947950-fa07a98d237f?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80")
                .gstPercent(java.math.BigDecimal.valueOf(5.00))
                .build();

            p1 = productRepository.save(p1);
            p2 = productRepository.save(p2);
            e1 = productRepository.save(e1);
            e2 = productRepository.save(e2);
            b1 = productRepository.save(b1);

            // Add Stock
            stockRepository.save(com.oms.entity.Stock.builder().product(p1).quantity(50).minimumRequired(10).build());
            stockRepository.save(com.oms.entity.Stock.builder().product(p2).quantity(30).minimumRequired(5).build());
            stockRepository.save(com.oms.entity.Stock.builder().product(e1).quantity(20).minimumRequired(5).build());
            stockRepository.save(com.oms.entity.Stock.builder().product(e2).quantity(15).minimumRequired(3).build());
            stockRepository.save(com.oms.entity.Stock.builder().product(b1).quantity(100).minimumRequired(20).build());
        }
    }
}
