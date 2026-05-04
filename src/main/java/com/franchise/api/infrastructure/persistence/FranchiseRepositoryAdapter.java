package com.franchise.api.infrastructure.persistence;

import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepositoryPort {

    private final FranchiseRepository repository;

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(franchise);
    }

    @Override
    public Mono<Franchise> findByBranchId(String branchId) {
        return repository.findByBranchId(branchId);
    }

    @Override
    public Mono<Franchise> findByProductId(String productId) {
        return repository.findByProductId(productId);
    }
}
