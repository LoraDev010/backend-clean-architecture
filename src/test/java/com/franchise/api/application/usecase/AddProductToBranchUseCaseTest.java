package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.AddProductRequest;
import com.franchise.api.application.exception.BranchNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddProductToBranchUseCaseTest {

    @Mock
    private FranchiseRepositoryPort repository;

    @InjectMocks
    private AddProductToBranchUseCase useCase;

    private Franchise franchiseWithBranch(String branchId) {
        Branch branch = Branch.builder().id(branchId).name("Norte").products(new ArrayList<>()).build();
        return Franchise.builder().id("f1").name("McDonald's").branches(new ArrayList<>(List.of(branch))).build();
    }

    @Test
    void execute_appendsProductToBranch() {
        AddProductRequest request = new AddProductRequest();
        request.setName("Big Mac");
        request.setStock(30);

        Franchise franchise = franchiseWithBranch("b1");
        when(repository.findByBranchId("b1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("b1", request))
                .assertNext(f -> {
                    List<Product> products = f.getBranches().get(0).getProducts();
                    assertThat(products).hasSize(1);
                    assertThat(products.get(0).getName()).isEqualTo("Big Mac");
                    assertThat(products.get(0).getStock()).isEqualTo(30);
                })
                .verifyComplete();
    }

    @Test
    void execute_branchNotFound_emitsError() {
        AddProductRequest request = new AddProductRequest();
        request.setName("Big Mac");
        when(repository.findByBranchId("missing")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("missing", request))
                .expectError(BranchNotFoundException.class)
                .verify();
    }
}
