package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductResponse> getProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse addProduct(ProductRequest product) {
        Product newProduct = new Product();
        updateProductFromProductRequest(newProduct, product);
        productRepository.save(newProduct);
        return mapProductToProductResponse(newProduct);
    }

    public Optional<ProductResponse> getProduct(Long id) {
        return productRepository.findByIdAndActiveTrue(id).map(this::mapProductToProductResponse);
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest updatedProduct) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            updateProductFromProductRequest(existingProduct, updatedProduct);
            productRepository.save(existingProduct); // Save updated product
        }
        return productRepository.findById(id).map(this::mapProductToProductResponse);
    }

    public boolean deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setActive(false);
            productRepository.save(product);
            return true;
        } else {
            return false;
        }
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapProductToProductResponse)
                .collect(Collectors.toList());
    }

    private void updateProductFromProductRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUri(request.getImageUri());
    }

    private ProductResponse mapProductToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setCategory(product.getCategory());
        response.setStock(product.getStock());
        response.setImageUri(product.getImageUri());
        return response;
    }
}
