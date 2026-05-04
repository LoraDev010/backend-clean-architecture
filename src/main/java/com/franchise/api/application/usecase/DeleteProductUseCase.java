package com.franchise.api.application.usecase;

import com.franchise.api.application.exception.BranchNotFoundException;
import com.franchise.api.application.exception.ProductNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Void> execute(String branchId, String productId) {
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
}
