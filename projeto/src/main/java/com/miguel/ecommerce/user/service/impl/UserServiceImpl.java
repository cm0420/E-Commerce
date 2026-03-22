package com.miguel.ecommerce.user.service.impl;

import com.miguel.ecommerce.excpetion.BusinessException;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import com.miguel.ecommerce.user.dto.UserRequest;
import com.miguel.ecommerce.user.dto.UserResponse;
import com.miguel.ecommerce.user.entity.User;
import com.miguel.ecommerce.user.mapper.UserMapper;
import com.miguel.ecommerce.user.repository.UserRepository;
import com.miguel.ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse findByID(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException("User not found"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        return userMapper.toUserResponseList(userRepository.findAll());

    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        if(userRepository.existsByCpf(userRequest.cpf())){
            throw new BusinessException("Cpf already exits");
        }
        if(userRepository.existsByEmail(userRequest.email())){
            throw new BusinessException("Email already exits");
        }

        User user = userMapper.toEntity(userRequest);
            user.setPassword(passwordEncoder.encode(userRequest.password()));
            user.setIsActive(true);
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);


    }

    @Override
    public void deactivate(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found", id));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public UserResponse update(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setEmail(userRequest.email());
        user.setPhoneNumber(userRequest.phoneNumber());
        user.setCpf(userRequest.cpf());
        user.setRole(userRequest.role());

        if (userRequest.password() != null && !userRequest.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.password()));
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }


}
