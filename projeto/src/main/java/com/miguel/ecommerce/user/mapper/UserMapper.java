package com.miguel.ecommerce.user.mapper;

import com.miguel.ecommerce.user.dto.UserRequest;
import com.miguel.ecommerce.user.dto.UserResponse;
import com.miguel.ecommerce.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);

    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequest userRequest);

    List<UserResponse> toUserResponseList(List<User> users);
}
