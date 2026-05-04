package com.franchise.api.application.usecase;

import com.franchise.api.application.exception.FranchiseNotFoundException;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Branch;
import com.franchise.api.domain.model.Franchise;
import com.franchise.api.domain.model.Product;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetTopStockPerBranchUseCaseTest {

    @Mock
    private FranchiseRepositoryPort repository;

    @InjectMocks
    private GetTopStockPerBranchUseCase useCase;

    @Test
    void execute_returnsProductWithMaxStockPerBranch() {
        Product p1 = Product.builder().id("p1").name("Big Mac").stock(10).build();
        Product p2 = Product.builder().id("p2").name("McFlurry").stock(50).build();
        Branch branch = Branch.builder().id("b1").name("Norte").products(new ArrayList<>(List.of(p1, p2))).build();
        Franchise franchise = Franchise.builder().id("1").name("McDonald's").branches(new ArrayList<>(List.of(branch))).build();

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.execute("1"))
                .assertNext(r -> {
                    assertThat(r.branchName()).isEqualTo("Norte");
                    assertThat(r.product().stock()).isEqualTo(50);
                })
                .verifyComplete();
    }

    @Test
    void execute_franchiseNotFound_emitsError() {
        when(repository.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("missing"))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}
