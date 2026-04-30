package com.franchise.api.application.service;

import com.franchise.api.application.dto.request.AddBranchRequest;
import com.franchise.api.application.dto.request.CreateFranchiseRequest;
import com.franchise.api.application.dto.request.UpdateNameRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceTest {

    @Mock
    private FranchiseRepositoryPort repository;

    @InjectMocks
    private FranchiseService service;

    @Test
    void create_savesAndReturnsFranchise() {
        CreateFranchiseRequest request = new CreateFranchiseRequest();
        request.setName("McDonald's");

        Franchise saved = Franchise.builder().id("1").name("McDonald's").build();
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(service.create(request))
                .assertNext(f -> {
                    assertThat(f.getId()).isEqualTo("1");
                    assertThat(f.getName()).isEqualTo("McDonald's");
                })
                .verifyComplete();
    }

    @Test
    void addBranch_appendsBranchAndSaves() {
        AddBranchRequest request = new AddBranchRequest();
        request.setName("Sucursal Norte");

        Franchise franchise = Franchise.builder().id("1").name("McDonald's").branches(new ArrayList<>()).build();
        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.addBranch("1", request))
                .assertNext(f -> {
                    assertThat(f.getBranches()).hasSize(1);
                    assertThat(f.getBranches().get(0).getName()).isEqualTo("Sucursal Norte");
                })
                .verifyComplete();
    }

    @Test
    void addBranch_franchiseNotFound_emitsError() {
        AddBranchRequest request = new AddBranchRequest();
        request.setName("Sucursal Norte");
        when(repository.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(service.addBranch("missing", request))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }

    @Test
    void updateName_changesNameAndSaves() {
        UpdateNameRequest request = new UpdateNameRequest();
        request.setName("Burger King");

        Franchise franchise = Franchise.builder().id("1").name("McDonald's").branches(new ArrayList<>()).build();
        when(repository.findById("1")).thenReturn(Mono.just(franchise));
        when(repository.save(any(Franchise.class))).thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(service.updateName("1", request))
                .assertNext(f -> assertThat(f.getName()).isEqualTo("Burger King"))
                .verifyComplete();
    }

    @Test
    void getTopStockPerBranch_returnsProductWithMaxStockPerBranch() {
        Product p1 = Product.builder().id("p1").name("Big Mac").stock(10).build();
        Product p2 = Product.builder().id("p2").name("McFlurry").stock(50).build();
        Branch branch = Branch.builder().id("b1").name("Norte").products(new ArrayList<>(List.of(p1, p2))).build();
        Franchise franchise = Franchise.builder().id("1").name("McDonald's").branches(new ArrayList<>(List.of(branch))).build();

        when(repository.findById("1")).thenReturn(Mono.just(franchise));

        StepVerifier.create(service.getTopStockPerBranch("1"))
                .assertNext(r -> {
                    assertThat(r.getBranchName()).isEqualTo("Norte");
                    assertThat(r.getProduct().getStock()).isEqualTo(50);
                })
                .verifyComplete();
    }

    @Test
    void getTopStockPerBranch_franchiseNotFound_emitsError() {
        when(repository.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(service.getTopStockPerBranch("missing"))
                .expectError(FranchiseNotFoundException.class)
                .verify();
    }
}
