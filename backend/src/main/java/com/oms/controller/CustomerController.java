package com.oms.controller;

import com.oms.dto.CustomerUpdateDto;
import com.oms.entity.Customer;
import com.oms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private com.oms.service.AuthService authService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Customer> getProfile(Principal principal) {
        return ResponseEntity.ok(customerService.getCustomerByUsername(principal.getName()));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Customer> updateProfile(Principal principal, @RequestBody CustomerUpdateDto updateDto) {
        return ResponseEntity.ok(customerService.updateCustomerProfile(principal.getName(), updateDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> adminCreateCustomer(@RequestBody com.oms.dto.RegisterRequest registerRequest) {
        // We reuse the registration logic, which creates the User and Customer entity.
        // It returns User, from which we could fetch Customer. For simplicity, we just return ok.
        // To be perfectly RESTful we would fetch the created customer.
        authService.registerUser(registerRequest);
        return ResponseEntity.ok(customerService.getCustomerByUsername(registerRequest.getUsername()));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> adminUpdateCustomer(@PathVariable Integer id, @RequestBody CustomerUpdateDto updateDto) {
        return ResponseEntity.ok(customerService.adminUpdateCustomer(id, updateDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}
