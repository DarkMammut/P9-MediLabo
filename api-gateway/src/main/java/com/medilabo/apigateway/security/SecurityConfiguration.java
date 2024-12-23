package com.medilabo.apigateway.security;

import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Gestion CORS
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Désactiver CSRF
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/public/**", "/auth/login", "/auth/logout", "/auth/check").permitAll()
                        .pathMatchers("/api/patients/**", "/api/notes/**").authenticated()
                        .anyExchange().authenticated()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // Désactiver l'authentification basique
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // Désactiver l'authentification par formulaire
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        User.UserBuilder userBuilder = User.builder();
        return new MapReactiveUserDetailsService(
                userBuilder.username("user").password("password123").roles("USER").build()
        );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000")); // Frontend autorisé
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Applique la configuration à toutes les routes
        return source;
    }

    @Bean
    public Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        return new Converter<>() {
            private final ReactiveJwtAuthenticationConverterAdapter adapter =
                    new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);

            @Override
            public Mono<? extends AbstractAuthenticationToken> convert(@NonNull Jwt jwt) {
                return adapter.convert(jwt);
            }
        };
    }

    @Bean
    public NimbusReactiveJwtDecoder jwtDecoder(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey.trim());
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA256");

        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }
}
