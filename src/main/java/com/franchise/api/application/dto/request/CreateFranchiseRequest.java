package com.franchise.api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateFranchiseRequest {
    @NotBlank
    private String name;
}
