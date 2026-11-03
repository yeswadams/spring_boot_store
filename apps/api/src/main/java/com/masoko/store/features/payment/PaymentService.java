package com.masoko.store.features.payment;

import com.masoko.store.features.order.Order;
import com.masoko.store.features.order.OrderRepository;
import com.masoko.store.features.payment.dto.PaymentRequest;
import com.masoko.store.exception.ResourceNotFound;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public PaymentResponse processCheckout(PaymentRequest request) {
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
    public void processMpesaCallback(String checkouRequestId, String receiptNumber, boolean isSuccess) {
        Payment payment = paymentRepository.findByCheckoutRequestId(checkouRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("No transaction records match checkout id: " + checkouRequestId));

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
