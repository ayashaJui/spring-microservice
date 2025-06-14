package com.photoApp.ApiGateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Value("${secret_key}")
    private String token_secret;

    public AuthenticationFilter() {
        super(Config.class);
    }

    public static class Config{

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No Authorization Header");
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            assert authHeader != null;
            String jwt = authHeader.replace("Bearer ", "");

            if(!isJwtValid(jwt)){
                return onError(exchange, "Invalid JWT");
            }

            return chain.filter(exchange);
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String noAuthorizationHeader) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        byte[] secretKeyBytes = Base64.getEncoder().encode(token_secret.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        JwtParser parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {

            Claims claims = parser.parseSignedClaims(jwt).getPayload();
            subject = (String) claims.get("sub");

        } catch (Exception ex) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }


}
