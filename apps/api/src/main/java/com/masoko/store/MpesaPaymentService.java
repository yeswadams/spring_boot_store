package com.masoko.store;

import org.springframework.stereotype.Service;

@Service
public class MpesaPaymentService implements PaymentService {
    @Override
    public void processPayment(double amount) {
        System.out.println("MPESA PAYMENT SERVICE");
        System.out.println("Amount: " + amount);
    }
}
