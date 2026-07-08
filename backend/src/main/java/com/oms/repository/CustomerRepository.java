package com.oms.repository;

import com.oms.entity.Customer;
import com.oms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUser(User user);
    Optional<Customer> findByUserUsername(String username);
}
