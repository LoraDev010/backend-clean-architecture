package com.franchise.api.application.usecase;

import com.franchise.api.application.exception.BranchNotFoundException;
import com.franchise.api.application.exception.ProductNotFoundException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteProductUseCaseTest {

    @Mock
    private FranchiseRepositoryPort repository;

    @InjectMocks
    private DeleteProductUseCase useCase;

    private Franchise franchiseWithBranch(String branchId) {
        Branch branch = Branch.builder().id(branchId).name("Norte").products(new ArrayList<>()).build();
        return Franchise.builder().id("f1").name("McDonald's").branches(new ArrayList<>(List.of(branch))).build();
    }

    @Test
    void execute_removesProductFromBranch() {
        Product product = Product.builder().id("p1").name("Big Mac").stock(10).build();
        Branch branch = Branch.builder().id("b1").name("Norte").products(new ArrayList<>(List.of(product))).build();
        Franchise franchise = Franchise.builder().id("f1").name("McDonald's").branches(new ArrayList<>(List.of(branch))).build();

        when(repository.findByBranchId("b1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("b1", "p1"))
                .verifyComplete();
    }

    @Test
    void execute_productNotFound_emitsError() {
        Franchise franchise = franchiseWithBranch("b1");
        when(repository.findByBranchId("b1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(useCase.execute("b1", "nonexistent"))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    @Test
    void execute_branchNotFound_emitsError() {
        when(repository.findByBranchId("missing")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("missing", "p1"))
                .expectError(BranchNotFoundException.class)
                .verify();
    }
}
