package com.miguel.ecommerce.user.service;

import com.miguel.ecommerce.user.dto.UserRequest;
import com.miguel.ecommerce.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse findByID(Long id);
    List<UserResponse> findAll();
    UserResponse save(UserRequest userRequest);
    void deactivate(Long id);
    UserResponse update(Long id, UserRequest userRequest);



}
