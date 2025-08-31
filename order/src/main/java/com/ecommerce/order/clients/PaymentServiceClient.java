package com.ecommerce.order.clients;

import com.ecommerce.order.dto.PaymentRequest;
import com.ecommerce.order.dto.PaymentResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface PaymentServiceClient {

    @PostExchange("/api/payments/create")
    PaymentResponse createPayment(@RequestBody PaymentRequest paymentRequest);
}
