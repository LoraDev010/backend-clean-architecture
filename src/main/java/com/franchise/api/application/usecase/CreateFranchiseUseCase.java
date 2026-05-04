package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.CreateFranchiseRequest;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseRepositoryPort repository;

    public Mono<Franchise> execute(CreateFranchiseRequest request) {
        return repository.save(Franchise.builder()
                .name(request.getName())
                .branches(new ArrayList<>())
                .build());
    }
}
