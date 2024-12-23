package com.medilabo.apigateway.component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    /**
     * Génère un token JWT pour l'utilisateur spécifié.
     * @param username Nom de l'utilisateur
     * @return Le token JWT généré
     */
    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        Date expirationDate = new Date(now + 3600000); // 1 heure d'expiration

        return Jwts.builder()
                .claim("sub", username)
                .claim("iat", now / 1000L) // Émet l'heure en secondes
                .claim("exp", expirationDate.getTime() / 1000L) // Date d'expiration en secondes
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // Utilise HMAC-SHA-256
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())// Utilise la clé secrète pour valider le token
                    .build()
                    .parseSignedClaims(token); // Si le token est valide, il est parsé correctement
            return true;
        } catch (Exception e) {
            // Le token est invalide ou expiré et Log l'erreur pour débogage
            System.err.println("Erreur de validation du token: " + e.getMessage());
            return false;
        }
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey.trim());
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("Invalid secret key: must be at least 32 bytes for HS256");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
