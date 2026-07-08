package com.oms.repository;

import com.oms.entity.CustomerIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerIssueRepository extends JpaRepository<CustomerIssue, Integer> {
    List<CustomerIssue> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);
    List<CustomerIssue> findAllByOrderByCreatedAtDesc();
}
