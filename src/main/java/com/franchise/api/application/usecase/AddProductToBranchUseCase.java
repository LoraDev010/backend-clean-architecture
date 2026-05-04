package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.AddProductRequest;
import com.franchise.api.application.exception.BranchNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import com.franchise.api.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddProductToBranchUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String branchId, AddProductRequest request) {
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
}
