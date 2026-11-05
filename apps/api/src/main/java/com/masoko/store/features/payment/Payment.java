// File: apps/api/src/main/java/com/masoko/store/features/payment/Payment.java
package com.masoko.store.features.payment;

import com.masoko.store.features.order.Order;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 50)
    private PaymentStatus paymentStatus;

    // Rail Specific Fields (Kept clear for Hibernate mappings)
    @Column(name = "mpesa_checkout_request_id", length = 100)
    private String mpesaCheckoutRequestId;

    @Column(name = "mpesa_receipt_number", length = 100)
    private String mpesaReceiptNumber;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "stripe_payment_intent_id", length = 100)
    private String stripePaymentIntentId;

    @Column(name = "transaction_hash", length = 66)
    private String transactionHash;

    @Column(name = "sender_wallet", length = 42)
    private String senderWallet;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Payment() {}

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Standard Getters and Setters ...
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String method) { this.paymentMethod = method; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus status) { this.paymentStatus = status; }
    public String getTransactionHash() { return transactionHash; }
    public void setTransactionHash(String hash) { this.transactionHash = hash; }
    public void setMpesaCheckoutRequestId(String id) { this.mpesaCheckoutRequestId = id; }
    public void setMpesaReceiptNumber(String receiptNumber) { this.mpesaReceiptNumber = receiptNumber; }

}
