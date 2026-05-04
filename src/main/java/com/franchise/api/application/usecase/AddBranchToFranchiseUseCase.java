package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.AddBranchRequest;
import com.franchise.api.application.exception.FranchiseNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Branch;
import com.franchise.api.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddBranchToFranchiseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(String franchiseId, AddBranchRequest request) {
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
}
