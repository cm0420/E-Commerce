package com.miguel.ecommerce.cart.mapper;


import com.miguel.ecommerce.cart.dto.CartItemResponse;
import com.miguel.ecommerce.cart.dto.CartResponse;
import com.miguel.ecommerce.cart.entity.Cart;
import com.miguel.ecommerce.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "items", source = "cartItems")
    CartResponse toCartResponse(Cart cart);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "subtotal", ignore = true)
    CartItemResponse toCartItemResponse(CartItem cartItem);

}
