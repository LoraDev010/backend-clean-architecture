package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.exception.ProductNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateProductNameUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String productId, UpdateNameRequest request) {
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
