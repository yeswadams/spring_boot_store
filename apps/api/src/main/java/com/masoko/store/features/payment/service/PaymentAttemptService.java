package com.masoko.store.features.payment.service;

import com.masoko.store.common.exception.ResourceNotFoundException;
import com.masoko.store.features.order.entity.Order;
import com.masoko.store.features.payment.dto.PaymentResponseDto;
import com.masoko.store.features.payment.entity.Payment;
import com.masoko.store.features.payment.entity.PaymentMethod;
import com.masoko.store.features.payment.entity.PaymentStatus;
import com.masoko.store.features.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

@Service
public class PaymentAttemptService {

    private final PaymentRepository paymentRepository;

    public PaymentAttemptService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment createPendingPayment(
            Order order,
            PaymentMethod paymentMethod,
            String phoneNumber
    ) {
        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        if(paymentMethod == PaymentMethod.MPESA) {
            payment.setPhoneNumber(phoneNumber);
        }

        return  paymentRepository.save(payment);
    }

    @Transactional
    public void updateInitiationResult(
            Long paymentId,
            PaymentResponseDto response
    ) {
        Payment payment = getPayment(paymentId);
        payment.setPaymentStatus(response.getStatus());

        paymentRepository.save(payment);
    }

    @Transactional
    public void markInitiationFailed(Long paymentId) {
        Payment payment = getPayment(paymentId);

        if(payment.getPaymentStatus() == PaymentStatus.PENDING) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
        }
    }

    @Transactional
    public void processMpesaCallback(
            String checkoutRequestId,
            String receiptNumber,
            boolean isSuccess
    ) {
        Payment payment = paymentRepository
                .findByMpesaCheckoutRequestId(checkoutRequestId)
                .orElseThrow(() -> new ResourceAccessException(
                        "No transaction records match checkout id: " + checkoutRequestId
                ));

        if(payment.getPaymentStatus() != PaymentStatus.PENDING) {
            return;
        }

        if(isSuccess) {
            if(receiptNumber == null || receiptNumber.isBlank()) {
                throw new IllegalArgumentException(
                        "Successful M-PESA callback must contain a receipt number"
                );
            }

            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setMpesaReceiptNumber(receiptNumber);

            payment.getOrder().setStatus("PAID");
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }

        paymentRepository.save(payment);
    }

    private Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with ID: " + paymentId
                ));
    }
}
