package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Branch;
import com.franchise.api.domain.model.Franchise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateBranchNameUseCaseTest {

    @Mock
    private FranchiseRepositoryPort repository;

    @InjectMocks
    private UpdateBranchNameUseCase useCase;

    @Test
    void execute_changesBranchName() {
        UpdateNameRequest request = new UpdateNameRequest();
        request.setName("Sucursal Sur");

        Branch branch = Branch.builder().id("b1").name("Norte").products(new ArrayList<>()).build();
        Franchise franchise = Franchise.builder().id("f1").name("McDonald's").branches(new ArrayList<>(List.of(branch))).build();

        when(repository.findByBranchId("b1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("b1", request))
                .assertNext(f -> assertThat(f.getBranches().get(0).getName()).isEqualTo("Sucursal Sur"))
                .verifyComplete();
    }
}
