package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.AddBranchRequest;
import com.franchise.api.application.exception.FranchiseNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddBranchToFranchiseUseCaseTest {

    @Mock
    private FranchiseRepositoryPort repository;

    @InjectMocks
    private AddBranchToFranchiseUseCase useCase;

    @Test
    void execute_appendsBranchAndSaves() {
        AddBranchRequest request = new AddBranchRequest();
        request.setName("Sucursal Norte");

        Franchise franchise = Franchise.builder().id("1").name("McDonald's").branches(new ArrayList<>()).build();
        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("1", request))
                .assertNext(f -> {
                    assertThat(f.getBranches()).hasSize(1);
                    assertThat(f.getBranches().get(0).getName()).isEqualTo("Sucursal Norte");
                })
                .verifyComplete();
    }

    @Test
    void execute_franchiseNotFound_emitsError() {
        AddBranchRequest request = new AddBranchRequest();
        request.setName("Sucursal Norte");
        when(repository.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("missing", request))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}
