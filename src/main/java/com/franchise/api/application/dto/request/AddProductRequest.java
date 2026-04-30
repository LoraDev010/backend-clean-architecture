package com.franchise.api.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddProductRequest {
    @NotBlank
    private String name;
    @Min(0)
    private int stock;
}
