package com.masoko.store.features.payment.repository;

import com.masoko.store.features.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByMpesaCheckoutRequestId(String mpesaCheckoutRequestId);
}
