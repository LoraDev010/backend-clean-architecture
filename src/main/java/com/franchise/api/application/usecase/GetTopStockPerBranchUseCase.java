package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.response.ProductResponse;
import com.franchise.api.application.dto.response.TopStockResponse;
import com.franchise.api.application.exception.FranchiseNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GetTopStockPerBranchUseCase {

    private final FranchiseRepositoryPort repository;

    public Flux<TopStockResponse> execute(String franchiseId) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch -> Flux.fromIterable(branch.getProducts())
                        .reduce((a, b) -> a.getStock() >= b.getStock() ? a : b)
                        .map(product -> new TopStockResponse(branch.getName(), ProductResponse.from(product))));
    }
}
