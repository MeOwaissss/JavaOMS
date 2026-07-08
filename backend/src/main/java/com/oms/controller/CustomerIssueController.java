package com.oms.controller;

import com.oms.dto.IssueRequest;
import com.oms.dto.ReplyRequest;
import com.oms.entity.CustomerIssue;
import com.oms.entity.IssueReply;
import com.oms.service.CustomerIssueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class CustomerIssueController {

    @Autowired
    private CustomerIssueService customerIssueService;

    @GetMapping
    public ResponseEntity<List<CustomerIssue>> getIssues(Principal principal) {
        String username = principal.getName();
        try {
            return ResponseEntity.ok(customerIssueService.getIssuesByCustomerUsername(username));
        } catch (Exception e) {
            return ResponseEntity.ok(customerIssueService.getAllIssues());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerIssue> getIssueById(@PathVariable Integer id) {
        return ResponseEntity.ok(customerIssueService.getIssueById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerIssue> createIssue(Principal principal, @Valid @RequestBody IssueRequest issueRequest) {
        return ResponseEntity.ok(customerIssueService.createIssue(principal.getName(), issueRequest));
    }

    @PostMapping("/{id}/replies")
    public ResponseEntity<IssueReply> replyToIssue(
            @PathVariable Integer id,
            Principal principal,
            @Valid @RequestBody ReplyRequest replyRequest) {
        return ResponseEntity.ok(customerIssueService.replyToIssue(id, principal.getName(), replyRequest));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<IssueReply>> getIssueReplies(@PathVariable Integer id) {
        return ResponseEntity.ok(customerIssueService.getIssueReplies(id));
    }
}
