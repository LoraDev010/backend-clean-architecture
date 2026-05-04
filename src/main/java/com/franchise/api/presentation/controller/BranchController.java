package com.franchise.api.presentation.controller;

import com.franchise.api.application.dto.request.AddProductRequest;
import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.dto.response.FranchiseResponse;
import com.franchise.api.application.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@Tag(name = "Branches")
public class BranchController {

    private final BranchService branchService;

    @PostMapping("/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a product to a branch")
    public Mono<FranchiseResponse> addProduct(@PathVariable String branchId,
                                              @Valid @RequestBody AddProductRequest request) {
        return branchService.addProduct(branchId, request).map(FranchiseResponse::from);
    }

    @DeleteMapping("/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a product from a branch")
    public Mono<Void> deleteProduct(@PathVariable String branchId,
                                    @PathVariable String productId) {
        return branchService.deleteProduct(branchId, productId);
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Update branch name")
    public Mono<FranchiseResponse> updateName(@PathVariable String id,
                                              @Valid @RequestBody UpdateNameRequest request) {
        return branchService.updateName(id, request).map(FranchiseResponse::from);
    }
}
