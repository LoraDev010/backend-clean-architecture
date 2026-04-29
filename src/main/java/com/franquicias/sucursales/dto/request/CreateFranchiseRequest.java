package com.franquicias.sucursales.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateFranchiseRequest {
    @NotBlank
    private String name;
}
