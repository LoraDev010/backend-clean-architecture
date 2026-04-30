package com.franchise.api.application.port.out;

import com.franchise.api.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepositoryPort {
    Mono<Franchise> findById(String id);
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findByBranchId(String branchId);
    Mono<Franchise> findByProductId(String productId);
}
