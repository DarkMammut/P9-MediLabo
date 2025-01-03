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

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /**
     * Génère un token JWT pour l'utilisateur spécifié.
     * @param username Nom de l'utilisateur
     * @return Le token JWT généré
     */
    public String generateToken(String username) {

        long jwtExpirationInMs = 3600000;

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs)) // Date d'expiration
                .signWith(getSigningKey())
                .compact();
    }
}
