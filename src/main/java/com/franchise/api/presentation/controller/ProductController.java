package com.franchise.api.presentation.controller;

import com.franchise.api.application.dto.request.UpdateNameRequest;
import com.franchise.api.application.dto.request.UpdateStockRequest;
import com.franchise.api.application.service.ProductService;
import com.franchise.api.domain.model.Franchise;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PatchMapping("/{id}/stock")
    public Mono<Franchise> updateStock(@PathVariable String id,
                                       @Valid @RequestBody UpdateStockRequest request) {
        return productService.updateStock(id, request);
    }

    @PatchMapping("/{id}/name")
    public Mono<Franchise> updateName(@PathVariable String id,
                                      @Valid @RequestBody UpdateNameRequest request) {
        return productService.updateName(id, request);
    }
}
