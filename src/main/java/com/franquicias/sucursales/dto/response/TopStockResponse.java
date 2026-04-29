package com.franquicias.sucursales.dto.response;

import com.franquicias.sucursales.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopStockResponse {
    private String branchName;
    private Product product;
}
