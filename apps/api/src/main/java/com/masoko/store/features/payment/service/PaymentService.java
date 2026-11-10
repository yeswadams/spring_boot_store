package com.masoko.store.features.payment.service;

import com.masoko.store.common.exception.ResourceNotFoundException;
import com.masoko.store.features.order.entity.Order;
import com.masoko.store.features.order.repository.OrderRepository;
import com.masoko.store.features.payment.PaymentGatewayContext;
import com.masoko.store.features.payment.dto.PaymentRequestDto;
import com.masoko.store.features.payment.dto.PaymentResponseDto;
import com.masoko.store.features.payment.entity.Payment;
import com.masoko.store.features.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayContext paymentGatewayContext;
    private final PaymentAttemptService paymentAttemptService;

    public PaymentService(
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            PaymentGatewayContext paymentGatewayContext,
            PaymentAttemptService paymentAttemptService
    ) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGatewayContext = paymentGatewayContext;
        this.paymentAttemptService = paymentAttemptService;
    }

    public PaymentResponseDto processCheckout(PaymentRequestDto request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with ID: " + request.getOrderId()
                ));

        Payment payment = paymentAttemptService.createPendingPayment(
                order,
                request.getPaymentMethod(),
                request.getPhoneNumber()
        );

        try {
            PaymentResponseDto response =
                    paymentGatewayContext.processPayment(payment);

            paymentAttemptService.updateInitiationResult(
                    payment.getId(),
                    response
            );

            return response;

        } catch (RuntimeException exception) {
            paymentAttemptService.markInitiationFailed(payment.getId());
            throw exception;
        }
    }

    public void processMpesaCallback(
            String checkoutRequestId,
            String receiptNumber,
            boolean isSuccess
    ) {
        paymentAttemptService.processMpesaCallback(
                checkoutRequestId,
                receiptNumber,
                isSuccess
        );
    }
}