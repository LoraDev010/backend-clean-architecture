package com.franchise.api.application.dto.response;

import com.franchise.api.domain.model.Product;
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
