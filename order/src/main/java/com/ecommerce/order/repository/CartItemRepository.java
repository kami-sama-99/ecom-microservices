package com.ecommerce.order.repository;

import com.ecommerce.order.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserIdAndProductId(String userId, String productId);

    void deleteByUserIdAndProductId(String userId, String productId);

    Optional<List<CartItem>> findByUserId(String userId);

    void deleteByUserId(String userId);
}
