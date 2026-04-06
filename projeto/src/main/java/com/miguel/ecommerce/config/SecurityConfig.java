package com.miguel.ecommerce.config;

import com.miguel.ecommerce.config.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ── PÚBLICO — sem autenticação ──────────────────────
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/stock/**").permitAll()


                        // ── ADMIN only ──────────────────────────────────────
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/financial/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
                     /*   .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasRole("ADMIN")*/

                        // ── ADMIN e WORKER ──────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasAnyRole("ADMIN", "WORKER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyRole("ADMIN", "WORKER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/products/**").hasAnyRole("ADMIN", "WORKER")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/stock/**").hasAnyRole("ADMIN", "WORKER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/stock/**").hasAnyRole("ADMIN", "WORKER")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasAnyRole("ADMIN", "WORKER")

                        //---ADMIN AND CLIENT
                        .requestMatchers("/api/v1/addresses/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers("/api/v1/cart/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/orders/**").hasAnyRole("CLIENT", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/orders/**").hasAnyRole("CLIENT", "ADMIN")

                        // ── AUTENTICADO — qualquer role ─────────────────────
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
