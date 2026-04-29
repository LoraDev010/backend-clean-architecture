package com.franquicias.sucursales.repository;

import com.franquicias.sucursales.model.Franchise;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FranchiseRepository extends ReactiveMongoRepository<Franchise, String> {

    @Query("{ 'branches.id': ?0 }")
    Mono<Franchise> findByBranchId(String branchId);

    @Query("{ 'branches.products.id': ?0 }")
    Mono<Franchise> findByProductId(String productId);
}
