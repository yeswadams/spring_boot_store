package com.masoko.store.features.payment;

import com.masoko.store.features.order.Order;
import com.masoko.store.features.payment.dto.PaymentRequestDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentGatewayContext {

    private final List<PaymentProcessor> processors;

//  auto-inject all beans implementing the PaymentProcessor interface
    public PaymentGatewayContext(List<PaymentProcessor> processors) {
        this.processors = processors;
    }

//    Resolve the correct payment rail dynamically and initiate transaction logic
    public PaymentResponse processOrderPayment(Order order, PaymentRequestDto request) {
        return processors.stream()
                .filter(processor -> processors.supports(request.getClass().getSimpleName()))
                .findFirst()
                .map(processor -> processor.processPayment(order, request))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No matching payment channel found for the request token: " + request.getClass().getSimpleName()
                ));
    }
}
