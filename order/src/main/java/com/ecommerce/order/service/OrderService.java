package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderItemDTO;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.model.CartItem;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;

    public Optional<OrderResponse> createOrder(String userId) {
//        Optional<User> optionalUser = userRepository.findById(Long.valueOf(userId));
//        if (optionalUser.isEmpty())
//            return Optional.empty();
//        User user = optionalUser.get();
        Optional<List<CartItem>> optionalCartItems = cartService.getCart(userId);
        if (optionalCartItems.isEmpty())
            return Optional.empty();
        List<CartItem> cartItems = optionalCartItems.get();
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                        null,
                        cartItem.getProductId(),
                        cartItem.getQuantity(),
                        cartItem.getPrice(),
                        order
                )).toList();
        order.setOrderItemList(orderItems);
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order savedOrder) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(savedOrder.getId());
        orderResponse.setTotalAmount(savedOrder.getTotalAmount());
        orderResponse.setStatus(savedOrder.getOrderStatus());
        List<OrderItemDTO> orderItemDTOList = savedOrder.getOrderItemList().stream()
                .map(orderItem -> new OrderItemDTO(
                        null,
                        orderItem.getProductId(),
                        orderItem.getQuantity(),
                        orderItem.getPrice()
                        )
                ).toList();
        orderResponse.setOrderItemList(orderItemDTOList);
        orderResponse.setCreatedAt(savedOrder.getCreatedAt());
        return orderResponse;
    }
}
