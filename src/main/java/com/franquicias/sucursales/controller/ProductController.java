package com.franquicias.sucursales.controller;

import com.franquicias.sucursales.dto.request.UpdateNameRequest;
import com.franquicias.sucursales.dto.request.UpdateStockRequest;
import com.franquicias.sucursales.model.Franchise;
import com.franquicias.sucursales.service.ProductService;
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
