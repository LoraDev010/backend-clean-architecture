package com.franchise.api.application.dto.response;

import com.franchise.api.domain.model.Branch;

import java.util.List;

public record BranchResponse(String id, String name, List<ProductResponse> products) {

    public static BranchResponse from(Branch branch) {
        return new BranchResponse(
                branch.getId(),
                branch.getName(),
                branch.getProducts().stream().map(ProductResponse::from).toList()
        );
    }
}
