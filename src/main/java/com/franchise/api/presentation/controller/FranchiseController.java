package com.franchise.api.presentation.controller;

import com.franchise.api.application.dto.request.AddBranchRequest;
import com.franchise.api.application.dto.request.CreateFranchiseRequest;
import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.dto.response.TopStockResponse;
import com.franchise.api.application.service.FranchiseService;
import com.franchise.api.domain.model.Franchise;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> create(@Valid @RequestBody CreateFranchiseRequest request) {
        return franchiseService.create(request);
    }

    @PostMapping("/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> addBranch(@PathVariable String franchiseId,
                                     @Valid @RequestBody AddBranchRequest request) {
        return franchiseService.addBranch(franchiseId, request);
    }

    @PatchMapping("/{id}/name")
    public Mono<Franchise> updateName(@PathVariable String id,
                                      @Valid @RequestBody UpdateNameRequest request) {
        return franchiseService.updateName(id, request);
    }

    @GetMapping("/{franchiseId}/top-stock")
    public Flux<TopStockResponse> getTopStock(@PathVariable String franchiseId) {
        return franchiseService.getTopStockPerBranch(franchiseId);
    }
}
