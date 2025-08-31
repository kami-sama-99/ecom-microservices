package com.ecom.payment.service;

import com.ecom.payment.dto.PaymentRequest;
import com.ecom.payment.dto.PaymentResponse;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public PaymentResponse createPayment(PaymentRequest paymentRequest) throws RazorpayException {
        if (paymentRequest.getAmount() == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        System.out.println(paymentRequest.getAmount());

        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject paymentLinkRequest = new JSONObject();
        long amountInPaise = paymentRequest.getAmount()
                .multiply(BigDecimal.valueOf(100))  // Convert rupees to paise
                .setScale(0, RoundingMode.HALF_UP) // Round correctly
                .longValueExact(); // Will throw if out of int range

        paymentLinkRequest.put("amount", amountInPaise); // amount in paise
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name", paymentRequest.getFirstName() + " " + paymentRequest.getLastName());
        customer.put("contact", paymentRequest.getPhone());
        customer.put("email", paymentRequest.getEmail());
        paymentLinkRequest.put("customer", customer);

        PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

        return mapToPaymentResponse(payment, paymentRequest);
    }

    private PaymentResponse mapToPaymentResponse(PaymentLink payment, PaymentRequest request) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.get("id"));                // Razorpay PaymentLink ID
        response.setOrderId(request.getOrderId());               // Your internal order ID
        response.setUserId(request.getUserId());                 // Your internal user ID
        response.setStatus(payment.get("status"));               // created / paid / cancelled
        response.setAmount(request.getAmount());         // Convert paise → INR
        response.setCurrency(payment.get("currency"));
        response.setPaymentMethod(null);            // Not known yet (user chooses)
        response.setTransactionId(null);            // Only available after success
        response.setPaymentLink(payment.get("short_url"));
        response.setTimestamp(LocalDateTime.now());

        return response;
    }
}
