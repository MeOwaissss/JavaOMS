package com.oms.repository;

import com.oms.entity.IssueReply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IssueReplyRepository extends JpaRepository<IssueReply, Integer> {
    List<IssueReply> findByIssueIdOrderByCreatedAtAsc(Integer issueId);
}
