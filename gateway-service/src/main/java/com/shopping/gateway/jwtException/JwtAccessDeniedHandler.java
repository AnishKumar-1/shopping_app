package com.shopping.gateway.jwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAccessDeniedHandler
        implements ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange,
                             AccessDeniedException ex) {

        var response = exchange.getResponse();

        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("status", 403);
        body.put("message", "Access Denied");
        body.put("timestamp", LocalDateTime.now());

        try {

            byte[] bytes = objectMapper.writeValueAsBytes(body);

            return response.writeWith(
                    Mono.just(response.bufferFactory().wrap(bytes))
            );

        } catch (Exception e) {

            return Mono.error(e);

        }
    }
}