package com.franchise.api.application.service;

import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.dto.request.UpdateStockRequest;
import com.franchise.api.application.exception.ProductNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> updateStock(String productId, UpdateStockRequest request) {
        return repository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .flatMap(franchise -> {
                    franchise.getBranches().forEach(branch ->
                            branch.getProducts().stream()
                                    .filter(p -> p.getId().equals(productId))
                                    .findFirst()
                                    .ifPresent(p -> p.setStock(request.getStock())));
                    return repository.save(franchise);
                });
    }

    public Mono<Franchise> updateName(String productId, UpdateNameRequest request) {
        return repository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productId)))
                .flatMap(franchise -> {
                    franchise.getBranches().forEach(branch ->
                            branch.getProducts().stream()
                                    .filter(p -> p.getId().equals(productId))
                                    .findFirst()
                                    .ifPresent(p -> p.setName(request.getName())));
                    return repository.save(franchise);
                });
    }
}
