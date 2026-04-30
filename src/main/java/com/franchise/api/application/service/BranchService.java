package com.franchise.api.application.service;

import com.franchise.api.application.dto.request.AddProductRequest;
import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.exception.BranchNotFoundException;
import com.franchise.api.application.exception.ProductNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import com.franchise.api.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> addProduct(String branchId, AddProductRequest request) {
        return repository.findByBranchId(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(franchise -> {
                    franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId))
                            .getProducts().add(Product.builder()
                                    .id(UUID.randomUUID().toString())
                                    .name(request.getName())
                                    .stock(request.getStock())
                                    .build());
                    return repository.save(franchise);
                });
    }

    public Mono<Void> deleteProduct(String branchId, String productId) {
        return repository.findByBranchId(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(franchise -> {
                    var branch = franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId));
                    if (!branch.getProducts().removeIf(p -> p.getId().equals(productId))) {
                        return Mono.error(new ProductNotFoundException(productId));
                    }
                    return repository.save(franchise).then();
                });
    }

    public Mono<Franchise> updateName(String branchId, UpdateNameRequest request) {
        return repository.findByBranchId(branchId)
                .switchIfEmpty(Mono.error(new BranchNotFoundException(branchId)))
                .flatMap(franchise -> {
                    franchise.getBranches().stream()
                            .filter(b -> b.getId().equals(branchId))
                            .findFirst()
                            .orElseThrow(() -> new BranchNotFoundException(branchId))
                            .setName(request.getName());
                    return repository.save(franchise);
                });
    }
}
