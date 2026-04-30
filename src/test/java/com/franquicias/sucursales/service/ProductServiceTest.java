package com.franquicias.sucursales.service;

import com.franquicias.sucursales.dto.request.UpdateNameRequest;
import com.franquicias.sucursales.dto.request.UpdateStockRequest;
import com.franquicias.sucursales.exception.ProductNotFoundException;
import com.franquicias.sucursales.model.Branch;
import com.franquicias.sucursales.model.Franchise;
import com.franquicias.sucursales.model.Product;
import com.franquicias.sucursales.repository.FranchiseRepository;
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
class ProductServiceTest {

    @Mock
    private FranchiseRepository repository;

    @InjectMocks
    private ProductService service;

    private Franchise franchiseWithProduct(String productId, int stock) {
        Product product = Product.builder().id(productId).name("Big Mac").stock(stock).build();
        Branch branch = Branch.builder().id("b1").name("Norte").products(new ArrayList<>(List.of(product))).build();
        return Franchise.builder().id("f1").name("McDonald's").branches(new ArrayList<>(List.of(branch))).build();
    }

    @Test
    void updateStock_changesProductStock() {
        UpdateStockRequest request = new UpdateStockRequest();
        request.setStock(99);

        Franchise franchise = franchiseWithProduct("p1", 10);
        when(repository.findByProductId("p1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.updateStock("p1", request))
                .assertNext(f -> {
                    int updatedStock = f.getBranches().get(0).getProducts().get(0).getStock();
                    assertThat(updatedStock).isEqualTo(99);
                })
                .verifyComplete();
    }

    @Test
    void updateStock_productNotFound_emitsError() {
        UpdateStockRequest request = new UpdateStockRequest();
        request.setStock(10);
        when(repository.findByProductId("missing")).thenReturn(Mono.empty());

        StepVerifier.create(service.updateStock("missing", request))
                .expectError(ProductNotFoundException.class)
                .verify();
    }

    @Test
    void updateName_changesProductName() {
        UpdateNameRequest request = new UpdateNameRequest();
        request.setName("McFlurry");

        Franchise franchise = franchiseWithProduct("p1", 10);
        when(repository.findByProductId("p1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.updateName("p1", request))
                .assertNext(f -> {
                    String updatedName = f.getBranches().get(0).getProducts().get(0).getName();
                    assertThat(updatedName).isEqualTo("McFlurry");
                })
                .verifyComplete();
    }

    @Test
    void updateName_productNotFound_emitsError() {
        UpdateNameRequest request = new UpdateNameRequest();
        request.setName("McFlurry");
        when(repository.findByProductId("missing")).thenReturn(Mono.empty());

        StepVerifier.create(service.updateName("missing", request))
                .expectError(ProductNotFoundException.class)
                .verify();
    }
}
