package com.franchise.api.application.usecase;

import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class GetAllFranchisesUseCase {

    private final FranchiseRepositoryPort repository;

    public Flux<Franchise> execute() {
        return repository.findAll();
    }
}
