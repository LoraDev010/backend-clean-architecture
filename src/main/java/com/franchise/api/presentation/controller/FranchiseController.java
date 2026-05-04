package com.franchise.api.presentation.controller;

import com.franchise.api.application.dto.request.AddBranchRequest;
import com.franchise.api.application.dto.request.CreateFranchiseRequest;
import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.dto.response.FranchiseResponse;
import com.franchise.api.application.dto.response.TopStockResponse;
import com.franchise.api.application.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
@Tag(name = "Franchises")
public class FranchiseController {

    private final FranchiseService franchiseService;

    @GetMapping
    @Operation(summary = "List all franchises")
    public Flux<FranchiseResponse> getAll() {
        return franchiseService.getAll().map(FranchiseResponse::from);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new franchise")
    public Mono<FranchiseResponse> create(@Valid @RequestBody CreateFranchiseRequest request) {
        return franchiseService.create(request).map(FranchiseResponse::from);
    }

    @PostMapping("/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a branch to a franchise")
    public Mono<FranchiseResponse> addBranch(@PathVariable String franchiseId,
                                             @Valid @RequestBody AddBranchRequest request) {
        return franchiseService.addBranch(franchiseId, request).map(FranchiseResponse::from);
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Update franchise name")
    public Mono<FranchiseResponse> updateName(@PathVariable String id,
                                              @Valid @RequestBody UpdateNameRequest request) {
        return franchiseService.updateName(id, request).map(FranchiseResponse::from);
    }

    @GetMapping("/{franchiseId}/top-stock")
    @Operation(summary = "Get product with highest stock per branch for a franchise")
    public Flux<TopStockResponse> getTopStock(@PathVariable String franchiseId) {
        return franchiseService.getTopStockPerBranch(franchiseId);
    }
}
