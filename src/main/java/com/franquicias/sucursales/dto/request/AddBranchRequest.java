package com.franquicias.sucursales.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddBranchRequest {
    @NotBlank
    private String name;
}
