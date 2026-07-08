package com.oms.service;

import com.oms.dto.IssueRequest;
import com.oms.dto.ReplyRequest;
import com.oms.entity.Customer;
import com.oms.entity.CustomerIssue;
import com.oms.entity.IssueReply;
import com.oms.entity.User;
import com.oms.repository.CustomerIssueRepository;
import com.oms.repository.CustomerRepository;
import com.oms.repository.IssueReplyRepository;
import com.oms.repository.UserRepository;
import com.oms.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerIssueService {

    @Autowired
    private CustomerIssueRepository customerIssueRepository;

    @Autowired
    private IssueReplyRepository issueReplyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CustomerIssue> getAllIssues() {
        return customerIssueRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<CustomerIssue> getIssuesByCustomerUsername(String username) {
        Customer customer = customerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer details not found"));
        return customerIssueRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId());
    }

    public CustomerIssue getIssueById(Integer id) {
        return customerIssueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id " + id));
    }

    @Transactional
    public CustomerIssue createIssue(String username, IssueRequest req) {
        Customer customer = customerRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Customer details not found"));

        CustomerIssue issue = CustomerIssue.builder()
                .customer(customer)
                .subject(req.getSubject())
                .description(req.getDescription())
                .status("OPEN")
                .createdAt(LocalDateTime.now())
                .build();

        return customerIssueRepository.save(issue);
    }

    @Transactional
    public IssueReply replyToIssue(Integer issueId, String username, ReplyRequest req) {
        CustomerIssue issue = getIssueById(issueId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        IssueReply reply = IssueReply.builder()
                .issue(issue)
                .user(user)
                .message(req.getMessage())
                .createdAt(LocalDateTime.now())
                .build();

        if ("ROLE_ADMIN".equals(user.getRole().getName())) {
            issue.setStatus("RESPONDED");
            customerIssueRepository.save(issue);
        }

        return issueReplyRepository.save(reply);
    }

    public List<IssueReply> getIssueReplies(Integer issueId) {
        return issueReplyRepository.findByIssueIdOrderByCreatedAtAsc(issueId);
    }
}
