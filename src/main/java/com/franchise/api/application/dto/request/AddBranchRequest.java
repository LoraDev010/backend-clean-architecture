package com.franchise.api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddBranchRequest {
    @NotBlank
    private String name;
}
