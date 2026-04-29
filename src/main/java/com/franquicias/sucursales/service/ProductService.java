package com.franquicias.sucursales.service;

import com.franquicias.sucursales.dto.request.UpdateNameRequest;
import com.franquicias.sucursales.dto.request.UpdateStockRequest;
import com.franquicias.sucursales.exception.ProductNotFoundException;
import com.franquicias.sucursales.model.Franchise;
import com.franquicias.sucursales.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final FranchiseRepository repository;

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
