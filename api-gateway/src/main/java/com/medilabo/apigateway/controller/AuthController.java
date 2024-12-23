package com.medilabo.apigateway.controller;

import com.medilabo.apigateway.component.JwtTokenProvider;
import com.medilabo.apigateway.component.dto.LoginRequest;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequest loginRequest, ServerWebExchange exchange) {
        if ("user".equals(loginRequest.getUsername()) && "password123".equals(loginRequest.getPassword())) {
            // Génération du token JWT
            String token = jwtTokenProvider.generateToken(loginRequest.getUsername());

            // Création du cookie JWT
            ResponseCookie cookie = ResponseCookie.from("JSESSIONID", token)
                    .httpOnly(true)
                    .secure(true) // Assurez-vous d'utiliser HTTPS en production
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(60 * 60) // 1 heure
                    .build();

            // Ajouter le cookie à la réponse
            exchange.getResponse().addCookie(cookie);

            return Mono.just(ResponseEntity.ok("Login successful, token: " + token));
        }

        return Mono.just(new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerWebExchange exchange) {
        // Supprimer le cookie en créant un cookie vide avec une durée de vie de 0
        ResponseCookie cookie = ResponseCookie.from("JSESSIONID", null)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        exchange.getResponse().addCookie(cookie);

        return Mono.just(ResponseEntity.status(HttpStatus.OK).build());
    }

    @GetMapping("/check")
    public Mono<ResponseEntity<String>> checkAuthentication(ServerWebExchange exchange) {
        // Extraction du cookie "JSESSIONID"
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("JSESSIONID"))
                .map(HttpCookie::getValue)
                .flatMap(token -> {
                    boolean isValid = jwtTokenProvider.validateToken(token); // Validation du JWT
                    if (isValid) {
                        return Mono.just(ResponseEntity.ok("Authenticated: Token is valid"));
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Unauthorized: Token is invalid or expired"));
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Unauthorized: Token is missing")));
    }
}
