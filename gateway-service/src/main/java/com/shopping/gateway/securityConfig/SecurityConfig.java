package com.shopping.gateway.securityConfig;
import com.shopping.gateway.jwtException.JwtAccessDeniedHandler;
import com.shopping.gateway.jwtException.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JwtAccessDeniedHandler handler;
    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Value("${jwt.secret.key}")
    private String secret;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange->
                        exchange.pathMatchers(
                                "/api/auth/**").permitAll()// or .hasAnyRole("USER", "ADMIN")
                                // Categories - Admin Only
                                .pathMatchers(HttpMethod.POST, "/api/v1/categories/**")
                                .hasRole("ADMIN")

                                .pathMatchers(HttpMethod.PUT, "/api/v1/categories/**")
                                .hasRole("ADMIN")

                                .pathMatchers(HttpMethod.DELETE, "/api/v1/categories/**")
                                .hasRole("ADMIN")
                                // Product - Admin Only
                                .pathMatchers(HttpMethod.POST, "/api/v1/products/**")
                                .hasRole("ADMIN")

                                .pathMatchers(HttpMethod.PUT, "/api/v1/products/**")
                                .hasRole("ADMIN")

                                .pathMatchers(HttpMethod.DELETE, "/api/v1/products/**")
                                .hasRole("ADMIN")

                                .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(handler)
                        .jwt(jwt -> jwt
                                .jwtDecoder(reactiveJwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )

                .build();
    }

    @Bean
    public ReactiveJwtDecoder  reactiveJwtDecoder(){

        SecretKey key = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return NimbusReactiveJwtDecoder
                .withSecretKey(key)
                .build();
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
