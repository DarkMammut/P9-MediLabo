package com.medilabo.apigateway.controller;

import com.medilabo.apigateway.component.dto.AuthResponse;
import com.medilabo.apigateway.component.dto.LoginRequest;
import com.medilabo.apigateway.component.JwtTokenProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    // Injecter le JwtTokenProvider
    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Vérifie les identifiants (ici avec un simple exemple, tu peux les valider via une base de données)
        if ("user".equals(loginRequest.getUsername()) && "password123".equals(loginRequest.getPassword())) {
            // Générer le token
            String token = jwtTokenProvider.generateToken(loginRequest.getUsername());
            // Renvoyer le token
            return ResponseEntity.ok(new AuthResponse(token));
        }
        // Si les identifiants sont incorrects
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
