package com.oms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustmentDto {
    @NotNull
    private Integer productId;

    @NotNull
    private Integer changeQuantity;

    @NotBlank
    private String type;

    private String reason;
}
