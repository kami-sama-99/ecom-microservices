package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-Id") String userId
    ) {
        Optional<OrderResponse> orderResponseOptional = orderService.createOrder(userId);
        if (orderResponseOptional.isEmpty())
            return ResponseEntity.badRequest().build();
        OrderResponse orderResponse = orderResponseOptional.get();
        return new ResponseEntity<>(orderResponse, HttpStatusCode.valueOf(201));
    }
}
