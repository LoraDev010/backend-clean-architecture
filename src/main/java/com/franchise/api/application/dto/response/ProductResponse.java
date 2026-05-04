package com.franchise.api.application.dto.response;

import com.franchise.api.domain.model.Product;

public record ProductResponse(String id, String name, int stock) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getStock());
    }
}
