package com.miguel.ecommerce.auth.controller;

import com.miguel.ecommerce.auth.dto.AuthRequest;
import com.miguel.ecommerce.auth.dto.AuthResponse;
import com.miguel.ecommerce.config.security.JwtService;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import com.miguel.ecommerce.user.entity.User;
import com.miguel.ecommerce.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse>login(@Valid @RequestBody AuthRequest authRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.email(),
                        authRequest.password()
                )
        );
        User user = userRepository.findByEmail(authRequest.email()).
                orElseThrow(() -> new ResourceNotFoundException("User not found", 0L));

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
