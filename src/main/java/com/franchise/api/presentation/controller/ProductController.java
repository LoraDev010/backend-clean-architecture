package com.franchise.api.presentation.controller;

import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.dto.request.UpdateStockRequest;
import com.franchise.api.application.dto.response.FranchiseResponse;
import com.franchise.api.application.usecase.UpdateProductNameUseCase;
import com.franchise.api.application.usecase.UpdateProductStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products")
public class ProductController {

    private final UpdateProductStockUseCase updateProductStock;
    private final UpdateProductNameUseCase updateProductName;

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock")
    public Mono<FranchiseResponse> updateStock(@PathVariable String id,
                                               @Valid @RequestBody UpdateStockRequest request) {
        return updateProductStock.execute(id, request).map(FranchiseResponse::from);
    }

    @PatchMapping("/{id}/name")
    @Operation(summary = "Update product name")
    public Mono<FranchiseResponse> updateName(@PathVariable String id,
                                              @Valid @RequestBody UpdateNameRequest request) {
        return updateProductName.execute(id, request).map(FranchiseResponse::from);
    }
}
