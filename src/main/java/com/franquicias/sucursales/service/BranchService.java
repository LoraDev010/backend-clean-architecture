package com.franquicias.sucursales.service;

import com.franquicias.sucursales.dto.request.AddProductRequest;
import com.franquicias.sucursales.dto.request.UpdateNameRequest;
import com.franquicias.sucursales.exception.BranchNotFoundException;
import com.franquicias.sucursales.exception.ProductNotFoundException;
import com.franquicias.sucursales.model.Franchise;
import com.franquicias.sucursales.model.Product;
import com.franquicias.sucursales.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final FranchiseRepository repository;

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
