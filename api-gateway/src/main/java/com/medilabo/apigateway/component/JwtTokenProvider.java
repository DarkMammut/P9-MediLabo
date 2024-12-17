package com.medilabo.apigateway.component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        Date expirationDate = new Date(now + 3600000); // 1 heure d'expiration

        // Décoder la clé secrète encodée en Base64
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .claim("sub", username) // Ajoute le sujet
                .claim("iat", now / 1000L) // Émettre l'heure en secondes
                .claim("exp", expirationDate.getTime() / 1000L) // Date d'expiration en secondes
                .signWith(key) // Utilise uniquement la clé secrète
                .compact();
    }
}
