package com.masoko.store.features.payment.service;

import com.masoko.store.features.order.Order;
import com.masoko.store.features.order.OrderRepository;
import com.masoko.store.features.payment.PaymentGatewayContext;
import com.masoko.store.features.payment.dto.PaymentRequestDto;
import com.masoko.store.common.exception.ResourceNotFoundException;
import com.masoko.store.features.payment.entity.Payment;
import com.masoko.store.features.payment.entity.PaymentStatus;
import com.masoko.store.features.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentGatewayContext paymentGatewayContext;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          PaymentGatewayContext paymentGatewayContext) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentGatewayContext = paymentGatewayContext;
    }

    @Transactional
    public PaymentResponse processCheckout(PaymentRequestDto request) {
//      fetch order using hibernate lifecycle
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.getOrderId()));

//      initialize a blank structural payment record block
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(request.getAmount());
        payment.setPaymentStatus(PaymentStatus.PENDING);

//        delegation to the gateway router
        PaymentResponse response = paymentGatewayContext.processOrderPayment(order, request);

//        Enrich the entity with processing properties before flushing to the db
        payment.setPaymentStatus((response.getStatus()));

//        Save using Spring Data JPA
        paymentRepository.save(payment);

        return response;
    }

    @Transactional
    public void processMpesaCallback(String checkoutRequestId, String receiptNumber, boolean isSuccess) {
//        Query db for the matching pending payment instance
        Payment payment = paymentRepository.findByCheckoutRequestId(checkoutRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("No transaction records match checkout id: " + checkoutRequestId));

        if(isSuccess) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setMpesaReceiptNumber(receiptNumber);
            payment.getOrder().setStatus("PAID");
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.getOrder().setStatus("PAYMENT_FAILED");
        }

        paymentRepository.save(payment);
    }
}
