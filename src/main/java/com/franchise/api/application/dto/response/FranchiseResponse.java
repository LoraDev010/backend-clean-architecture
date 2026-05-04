package com.franchise.api.application.dto.response;

import com.franchise.api.domain.model.Franchise;

import java.util.List;

public record FranchiseResponse(String id, String name, List<BranchResponse> branches) {

    public static FranchiseResponse from(Franchise franchise) {
        return new FranchiseResponse(
                franchise.getId(),
                franchise.getName(),
                franchise.getBranches().stream().map(BranchResponse::from).toList()
        );
    }
}
