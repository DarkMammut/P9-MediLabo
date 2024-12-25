package com.medilabo.apigateway.controller;

import com.medilabo.apigateway.component.JwtTokenProvider;
import com.medilabo.apigateway.component.dto.AuthResponse;
import com.medilabo.apigateway.component.dto.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    // Injection du JwtTokenProvider et du ReactiveAuthenticationManager
    public AuthController(JwtTokenProvider jwtTokenProvider, ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest loginRequest, ServerWebExchange exchange) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        return reactiveAuthenticationManager.authenticate(authenticationToken) // Directly authenticate reactively
                .map(authentication -> {
                    // Si l'authentification réussie, générer le token JWT
                    String token = jwtTokenProvider.generateToken(authentication.getName());

                    System.out.println(token);

                    // Création et ajout du cookie
                    ResponseCookie cookie = ResponseCookie.from("JWT", token)
                            .httpOnly(true)
                            .secure(false) // Mettre à true en production
                            .sameSite("Lax")
                            .path("/")
                            .maxAge(60 * 60) // 1 heure
                            .build();

                    exchange.getResponse().addCookie(cookie);

                    // Retourner le token dans le corps de la réponse
                    return ResponseEntity.ok(new AuthResponse(token));
                })
                .onErrorResume(BadCredentialsException.class, e -> {
                    log.error("Authentication error: {}", e.getMessage());

                    // Retourner un ResponseEntity avec un AuthResponse contenant un message d'erreur
                    AuthResponse errorResponse = new AuthResponse("Invalid username or password");
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
                });

    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(ServerWebExchange exchange) {
        // Création du cookie JWT pour le logout
        ResponseCookie cookie = ResponseCookie.from("JWT", null)
                .httpOnly(true)
                .secure(false) // Passer à true en production
                .sameSite("Lax")
                .path("/")
                .maxAge(0)  // Expire immédiatement
                .build();

        exchange.getResponse().addCookie(cookie);
        return Mono.just(ResponseEntity.status(HttpStatus.OK).build());
    }

    @GetMapping("/check")
    public Mono<ResponseEntity<String>> checkAuthentication(ServerWebExchange exchange) {
        // Extraction du token JWT depuis le cookie "JWT"
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("JWT"))
                .map(HttpCookie::getValue)  // On obtient la valeur du cookie
                .flatMap(token -> {
                    if (jwtTokenProvider.validateToken(token)) {
                        return Mono.just(ResponseEntity.ok("Authenticated"));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token missing")));
    }
}