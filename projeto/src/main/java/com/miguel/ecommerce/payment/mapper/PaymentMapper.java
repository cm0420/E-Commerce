package com.miguel.ecommerce.payment.mapper;

import com.miguel.ecommerce.payment.dto.PaymentCardResponse;
import com.miguel.ecommerce.payment.dto.PaymentResponse;
import com.miguel.ecommerce.payment.entity.Payment;
import com.miguel.ecommerce.payment.entity.PaymentCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toPaymentResponse(Payment payment);

    PaymentCardResponse toCardResponse(PaymentCard card);

    List<PaymentCardResponse> toCardResponseList(List<PaymentCard> cards);
}
