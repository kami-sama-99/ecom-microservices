package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        Optional<ProductResponse> optionalProductResponse = productService.getProduct(id);
        return optionalProductResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.addProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestBody ProductRequest updatedProduct,
            @PathVariable Long id
    ) {
        Optional<ProductResponse> optionalProductResponse = productService.updateProduct(id, updatedProduct);
        return optionalProductResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public  ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.ok("Product successfully deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
