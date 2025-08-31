package com.ecom.payment.model;

import com.ecom.payment.dto.PaymentMethod;
import com.ecom.payment.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "payments_table")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Primary key in DB

    @Column(nullable = false)
    private String orderId;   // Link to order table

    @Column(nullable = false)
    private String userId;    // Link to user/customer

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status; // SUCCESS, FAILED, PENDING

    @Enumerated(EnumType.STRING)
    private PaymentMethod method; // CARD, UPI, NET_BANKING etc.

    private String transactionId; // Provided by payment gateway

    private String message;       // Optional, error/success message

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
