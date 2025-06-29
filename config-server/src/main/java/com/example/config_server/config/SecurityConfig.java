package com.example.config_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Value("${admin.security.user.password}")
    private String password;

    @Value("${admin.security.user.name}")
    private String username;

    @Value("${my_spring.security.user.password}")
    private String clientPassword;

    @Value("${my_spring.security.user.name}")
    private String clientUsername;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, "/actuator/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/**").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/encrypt").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/decrypt").hasRole("ADMIN")

                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/actuator/**"))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails adminUser = User.withUsername(username).password(passwordEncoder.encode(password)).roles("ADMIN").build();

        UserDetails client = User.withUsername(clientUsername).password(passwordEncoder.encode(clientPassword)).roles("CLIENT").build();

        return new InMemoryUserDetailsManager(adminUser, client);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
