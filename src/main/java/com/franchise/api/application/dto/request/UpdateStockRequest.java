package com.franchise.api.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateStockRequest {
    @Min(0)
    private int stock;
}
