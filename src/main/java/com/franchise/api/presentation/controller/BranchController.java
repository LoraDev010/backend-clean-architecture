package com.franchise.api.presentation.controller;

import com.franchise.api.application.dto.request.AddProductRequest;
import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.service.BranchService;
import com.franchise.api.domain.model.Franchise;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Franchise> addProduct(@PathVariable String branchId,
                                      @Valid @RequestBody AddProductRequest request) {
        return branchService.addProduct(branchId, request);
    }

    @DeleteMapping("/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProduct(@PathVariable String branchId,
                                    @PathVariable String productId) {
        return branchService.deleteProduct(branchId, productId);
    }

    @PatchMapping("/{id}/name")
    public Mono<Franchise> updateName(@PathVariable String id,
                                      @Valid @RequestBody UpdateNameRequest request) {
        return branchService.updateName(id, request);
    }
}
