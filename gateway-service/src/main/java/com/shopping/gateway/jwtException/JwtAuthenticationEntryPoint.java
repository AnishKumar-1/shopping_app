package com.shopping.gateway.jwtException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.gateway.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint
        implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange,
                               AuthenticationException ex) {
        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        String errorCode;
        String message;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            errorCode = "AUTH_TOKEN_MISSING";
            message = "Authentication required. Please login.";

        } else {

            errorCode = "AUTH_TOKEN_INVALID";
            message = "Invalid or expired authentication token.";
        }

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorCode(errorCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        ServerHttpResponse httpResponse = exchange.getResponse();

        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        httpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {

            byte[] body =
                    objectMapper.writeValueAsBytes(response);

            return httpResponse.writeWith(
                    Mono.just(httpResponse.bufferFactory().wrap(body))
            );

        } catch (Exception e) {

            return Mono.error(e);

        }
    }
}