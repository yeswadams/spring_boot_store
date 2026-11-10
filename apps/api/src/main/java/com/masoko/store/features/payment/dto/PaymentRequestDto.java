package com.masoko.store.features.payment.dto;

import com.masoko.store.features.payment.entity.PaymentMethod;

public class PaymentRequestDto {

    private Long orderId;
    private PaymentMethod paymentMethod;
    private String phoneNumber;

    public PaymentRequestDto() {}

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}