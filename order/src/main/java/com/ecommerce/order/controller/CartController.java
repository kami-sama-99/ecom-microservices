package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CartItemRequest;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/cart")
@RestController
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CartItemRequest request
            ) {
        if (cartService.addToCart(userId, request))
            return ResponseEntity.status(HttpStatus.CREATED).build();
        else
            return ResponseEntity.badRequest().body("Product unavailable or user not found");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> deleteFromCart(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long productId
    ) {
        if (cartService.deleteFromCart(userId, productId)) {
            return ResponseEntity.ok().body("Item deleted from cart");
        } else {
            return ResponseEntity.badRequest().body("Item or user is not present");
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-Id") String userId
    ) {
        Optional<List<CartItem>> optionalCartItems = cartService.getCart(userId);
        return optionalCartItems.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
