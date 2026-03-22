package com.miguel.ecommerce.address.mapper;

import com.miguel.ecommerce.address.dto.AddressRequest;
import com.miguel.ecommerce.address.dto.AddressResponse;
import com.miguel.ecommerce.address.entity.Address;
import com.miguel.ecommerce.user.dto.UserResponse;
import com.miguel.ecommerce.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "userId", source = "user.id")
    AddressResponse toAddressResponse(Address addressed);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Address toEntity(AddressRequest addressRequest);

    List<AddressResponse> toAddressResponseList(List<Address> addresses);
}
