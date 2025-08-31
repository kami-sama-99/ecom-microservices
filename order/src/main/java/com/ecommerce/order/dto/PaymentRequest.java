package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentRequest {
    private String userId;
    private String orderId;
    private BigDecimal amount;
    private Currency currency;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
