package com.masoko.store.features.payment.dto;

import com.masoko.store.features.payment.PaymentStatus;

public class PaymentResponseDto {

    private PaymentStatus status;
    private String message;

    public PaymentResponseDto() {}

    public PaymentResponseDto(PaymentStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
