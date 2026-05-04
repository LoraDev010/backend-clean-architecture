package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.exception.FranchiseNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateFranchiseNameUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, UpdateNameRequest request) {
        return repository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException(franchiseId)))
                .flatMap(franchise -> {
                    franchise.setName(request.getName());
                    return repository.save(franchise);
                });
    }
}
