package com.ecommerce.order.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class PaymentServiceClientConfig {

    @Bean
    public PaymentServiceClient restClientInterfacePayment(RestClient.Builder restClientBuilder) {
        RestClient restClient = restClientBuilder.baseUrl("http://payment-service").build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return factory.createClient(PaymentServiceClient.class);
    }
}
