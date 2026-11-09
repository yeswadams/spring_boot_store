package com.masoko.store.features.payment.processor;

import com.masoko.store.features.payment.dto.PaymentResponseDto;
import com.masoko.store.features.payment.entity.Payment;
import com.masoko.store.features.payment.entity.PaymentMethod;

public interface PaymentProcessor {

    PaymentMethod getPaymentMethod();

    PaymentResponseDto initiate(Payment payment);
}
