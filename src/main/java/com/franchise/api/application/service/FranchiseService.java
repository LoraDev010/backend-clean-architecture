package com.franchise.api.application.service;

import com.franchise.api.application.dto.request.AddBranchRequest;
import com.franchise.api.application.dto.request.CreateFranchiseRequest;
import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.dto.response.TopStockResponse;
import com.franchise.api.application.exception.FranchiseNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Branch;
import com.franchise.api.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FranchiseService {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> create(CreateFranchiseRequest request) {
        return repository.save(Franchise.builder()
                .name(request.getName())
                .branches(new ArrayList<>())
                .build());
    }

    public Mono<Franchise> addBranch(String franchiseId, AddBranchRequest request) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    franchise.getBranches().add(Branch.builder()
                            .id(UUID.randomUUID().toString())
                            .name(request.getName())
                            .products(new ArrayList<>())
                            .build());
                    return repository.save(franchise);
                });
    }

    public Mono<Franchise> updateName(String franchiseId, UpdateNameRequest request) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    franchise.setName(request.getName());
                    return repository.save(franchise);
                });
    }

    public Flux<TopStockResponse> getTopStockPerBranch(String franchiseId) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch -> Flux.fromIterable(branch.getProducts())
                        .reduce((a, b) -> a.getStock() >= b.getStock() ? a : b)
                        .map(product -> new TopStockResponse(branch.getName(), product)));
    }
}
