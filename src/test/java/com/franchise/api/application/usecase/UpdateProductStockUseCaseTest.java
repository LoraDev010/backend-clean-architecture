package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.UpdateStockRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductStockUseCaseTest {

    @Mock
    private FranchiseRepositoryPort repository;

    @InjectMocks
    private UpdateProductStockUseCase useCase;

    @Test
    void execute_updatesStockCorrectly() {
        UpdateStockRequest request = new UpdateStockRequest();
        request.setStock(500);

        Product product = Product.builder().id("p1").name("Big Mac").stock(100).build();
        Branch branch = Branch.builder().id("b1").name("Norte").products(new ArrayList<>(List.of(product))).build();
        Franchise franchise = Franchise.builder().id("f1").name("McDonald's").branches(new ArrayList<>(List.of(branch))).build();

        when(repository.findByProductId("p1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.execute("p1", request))
                .assertNext(f -> assertThat(f.getBranches().get(0).getProducts().get(0).getStock()).isEqualTo(500))
                .verifyComplete();
    }

    @Test
    void execute_productNotFound_emitsError() {
        when(repository.findByProductId("missing")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("missing", new UpdateStockRequest()))
                .expectError(ProductNotFoundException.class)
                .verify();
    }
}
