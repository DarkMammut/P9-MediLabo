package com.medilabo.apigateway.controller;

import com.medilabo.apigateway.component.dto.LoginRequest;
import com.medilabo.apigateway.component.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    // Injecter le JwtTokenProvider
    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Vérifie les identifiants (ici avec un simple exemple, tu peux les valider via une base de données)
        if ("user".equals(loginRequest.getUsername()) && "password123".equals(loginRequest.getPassword())) {

            // Générer le token
            String token = jwtTokenProvider.generateToken(loginRequest.getUsername());

            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .secure(true) // Nécessite HTTPS
                    .sameSite("Strict") // Protection contre CSRF
                    .path("/")
                    .maxAge(60 * 60) // 1 heure
                    .build();

            // Ajouter le cookie à la réponse
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie", cookie.toString()) // Ajout du cookie à l'en-tête HTTP
                    .body("Login successful");
        }

        // Si les identifiants sont incorrects
        return new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);
    }
}
