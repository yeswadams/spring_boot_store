package com.masoko.store.features.payment.processor;

import com.masoko.store.features.payment.dto.PaymentResponseDto;
import com.masoko.store.features.payment.entity.Payment;
import com.masoko.store.features.payment.entity.PaymentMethod;

import org.springframework.stereotype.Component;

@Component
public class MpesaProcessor implements PaymentProcessor{

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.MPESA;
    }

    @Override
    public PaymentResponseDto initiate(Payment payment) {
        throw new UnsupportedOperationException(
                "MPESA integration has not been implemented yet."
        );
    }
}
