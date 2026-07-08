package com.oms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IssueRequest {
    @NotBlank
    private String subject;

    @NotBlank
    private String description;
}
