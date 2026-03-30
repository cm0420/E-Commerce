package com.miguel.ecommerce.payment.repository;

import com.miguel.ecommerce.payment.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentCardRepository extends JpaRepository <PaymentCard, Long>{
    List<PaymentCard> findAllByUserId(Long userId);
    Optional<PaymentCard> findByIdAndUserId(Long id, Long userId);
    @Modifying // Necessário para operações de escrita (UPDATE/DELETE)
    @Query("UPDATE PaymentCard c SET c.isDefault = false WHERE c.user.id = :userId")
    void resetDefaultByUserId(@Param("userId") Long userId);
}
