package com.franchise.api;

import com.franchise.api.application.dto.response.FranchiseResponse;
import com.franchise.api.application.dto.response.TopStockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class FranchiseApiIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @BeforeEach
    void cleanDb() {
        mongoTemplate.dropCollection("franchises").block();
    }

    @Test
    void fullFlow_createFranchise_addBranch_addProduct_getTopStock() {
        FranchiseResponse franchise = webTestClient.post().uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"McDonald's\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FranchiseResponse.class)
                .returnResult().getResponseBody();

        assertThat(franchise).isNotNull();
        assertThat(franchise.name()).isEqualTo("McDonald's");

        FranchiseResponse withBranch = webTestClient.post()
                .uri("/api/franchises/{id}/branches", franchise.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Sucursal Norte\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(FranchiseResponse.class)
                .returnResult().getResponseBody();

        assertThat(withBranch).isNotNull();
        String branchId = withBranch.branches().get(0).id();

        webTestClient.post()
                .uri("/api/branches/{id}/products", branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"Big Mac\",\"stock\":100}")
                .exchange()
                .expectStatus().isCreated();

        webTestClient.post()
                .uri("/api/branches/{id}/products", branchId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"name\":\"McFlurry\",\"stock\":200}")
                .exchange()
                .expectStatus().isCreated();

        webTestClient.get()
                .uri("/api/franchises/{id}/top-stock", franchise.id())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TopStockResponse.class)
                .value(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).branchName()).isEqualTo("Sucursal Norte");
                    assertThat(list.get(0).product().stock()).isEqualTo(200);
                });
    }

    @Test
    void getFranchise_notFound_returns404() {
        webTestClient.get()
                .uri("/api/franchises/{id}/top-stock", "nonexistent-id")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.error").isEqualTo("Not Found");
    }
}
