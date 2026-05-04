package com.franchise.api.application.usecase;

import com.franchise.api.application.dto.request.CreateFranchiseRequest;
import com.franchise.api.application.port.out.FranchiseRepositoryPort;
import com.franchise.api.domain.model.Franchise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepositoryPort repository;

    @InjectMocks
    private CreateFranchiseUseCase useCase;

    @Test
    void execute_savesAndReturnsFranchise() {
        CreateFranchiseRequest request = new CreateFranchiseRequest();
        request.setName("McDonald's");

        Franchise saved = Franchise.builder().id("1").name("McDonald's").build();
        when(repository.save(any(Franchise.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(useCase.execute(request))
                .assertNext(f -> {
                    assertThat(f.getId()).isEqualTo("1");
                    assertThat(f.getName()).isEqualTo("McDonald's");
                })
                .verifyComplete();
    }
}
