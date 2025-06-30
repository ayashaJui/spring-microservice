package com.photoApp.user_service.config;

import com.photoApp.user_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${token_expiration_time}")
    private String tokenExpiration;

    @Value("${secret_key}")
    private String secretKey;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        return auth.build();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        AuthenticationFilter authFilter = new AuthenticationFilter(authenticationManager, userService, tokenExpiration, secretKey);
        authFilter.setFilterProcessesUrl("/login");

        http.csrf(csrf -> csrf.disable());

        http
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/users/**", "/h2-console/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll())
                .addFilter(authFilter)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.headers(headers ->
                headers.frameOptions((frame) -> frame.sameOrigin())
                );

        return http.build();
    }
}
