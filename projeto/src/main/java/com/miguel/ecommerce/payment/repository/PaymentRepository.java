package com.miguel.ecommerce.payment.repository;

import com.miguel.ecommerce.payment.entity.Payment;
import com.miguel.ecommerce.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository <Payment, Long>{
    Optional<Payment> findByOrderId(Long orderId);
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);

}
