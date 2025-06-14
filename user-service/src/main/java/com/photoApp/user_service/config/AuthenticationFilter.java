package com.photoApp.user_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photoApp.user_service.dtos.UserDto;
import com.photoApp.user_service.dtos.UserLoginRequestDto;
import com.photoApp.user_service.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private UserService usersService;
//    private Environment environment;

    @Value("${token_expiration_time}")
    private String token_expiration_time;

    @Value("${secret_key}")
    private String secret_key;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService usersService, String token_expiration_time,
                                String secret_key) {
        super(authenticationManager);
        this.usersService = usersService;
        this.token_expiration_time = token_expiration_time;
        this.secret_key = secret_key;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            UserLoginRequestDto creds = new ObjectMapper().readValue(req.getInputStream(), UserLoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String userName = ((User) auth.getPrincipal()).getUsername();
        UserDto userDetails = usersService.getUserByEmail(userName);
//        String tokenSecret = environment.getProperty("token.secret");
        byte[] secretKeyBytes = Base64.getEncoder().encode(secret_key.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        String token = Jwts.builder()
                .subject(userDetails.getUserId())
                .expiration(Date.from(now.plusMillis(Long.parseLong(token_expiration_time))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        res.addHeader("token", token);
        res.addHeader("userId", userDetails.getUserId());
    }
}
