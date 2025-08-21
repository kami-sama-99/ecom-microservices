package com.ecommerce.order.service;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.UserResponse;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.repository.CartItemRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "cartService")
    public boolean addToCart(String userId, CartItemRequest request) {
        ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
        if (productResponse == null) return false;
        if (productResponse.getStock() < request.getQuantity()) return false;

        UserResponse userResponse = userServiceClient.getUserDetails(userId);
        if (userResponse == null) return false;

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
        if (existingCartItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(request.getProductId());
            cartItem.setUserId(userId);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(cartItem);
        } else {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(existingCartItem);
        }
        return true;
    }

    @Transactional
    public boolean deleteFromCart(String userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, Long.toString(productId));
        if (cartItem != null) {
            cartItemRepository.deleteByUserIdAndProductId(userId, Long.toString(productId));
            return true;
        }
        return false;
    }

    public Optional<List<CartItem>> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
