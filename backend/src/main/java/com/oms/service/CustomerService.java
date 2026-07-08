package com.oms.service;

import com.oms.dto.CustomerUpdateDto;
import com.oms.entity.Customer;
import com.oms.exception.ResourceNotFoundException;
import com.oms.exception.BadRequestException;
import com.oms.repository.CustomerRepository;
import com.oms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private com.oms.repository.UserRepository userRepository;

    @Transactional
    public Customer getCustomerByUsername(String username) {
        return customerRepository.findByUserUsername(username)
                .orElseGet(() -> {
                    com.oms.entity.User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found for username: " + username));
                    Customer newCustomer = Customer.builder()
                            .user(user)
                            .firstName("")
                            .lastName("")
                            .phone("")
                            .build();
                    return customerRepository.save(newCustomer);
                });
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer updateCustomerProfile(String username, CustomerUpdateDto updateDto) {
        Customer customer = getCustomerByUsername(username);
        
        if (updateDto.getFirstName() != null) customer.setFirstName(updateDto.getFirstName());
        if (updateDto.getLastName() != null) customer.setLastName(updateDto.getLastName());
        if (updateDto.getPhone() != null) customer.setPhone(updateDto.getPhone());
        if (updateDto.getAddress() != null) customer.setAddress(updateDto.getAddress());
        if (updateDto.getCity() != null) customer.setCity(updateDto.getCity());
        if (updateDto.getState() != null) customer.setState(updateDto.getState());
        if (updateDto.getZipCode() != null) customer.setZipCode(updateDto.getZipCode());

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer adminUpdateCustomer(Integer id, CustomerUpdateDto updateDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        if (updateDto.getFirstName() != null) customer.setFirstName(updateDto.getFirstName());
        if (updateDto.getLastName() != null) customer.setLastName(updateDto.getLastName());
        if (updateDto.getPhone() != null) customer.setPhone(updateDto.getPhone());
        if (updateDto.getAddress() != null) customer.setAddress(updateDto.getAddress());
        if (updateDto.getCity() != null) customer.setCity(updateDto.getCity());
        if (updateDto.getState() != null) customer.setState(updateDto.getState());
        if (updateDto.getZipCode() != null) customer.setZipCode(updateDto.getZipCode());

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (!orderRepository.findByCustomerIdOrderByCreatedAtDesc(id).isEmpty()) {
            throw new BadRequestException("Cannot delete customer because they have existing orders. Historical data must be preserved.");
        }

        customerRepository.delete(customer);
    }
}
