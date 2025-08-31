package com.ecom.payment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private String paymentId;
    private String orderId;
    private String userId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String transactionId;
    private String paymentLink;
    private LocalDateTime timestamp;
}
