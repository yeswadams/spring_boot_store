package com.masoko.store.features.payment;

import com.masoko.store.features.payment.entity.Payment;
import com.masoko.store.features.payment.entity.PaymentMethod;
import com.masoko.store.features.payment.processor.PaymentProcessor;
import com.masoko.store.features.payment.dto.PaymentResponseDto;
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
    public PaymentResponseDto processPayment(Payment payment) {
        PaymentMethod paymentMethod = payment.getPaymentMethod();

        return processors.stream()
                .filter(processor ->
                        processor.getPaymentMethod() == paymentMethod
                )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported payment method: " + paymentMethod
                ))
                .initiate(payment);
    }
}
