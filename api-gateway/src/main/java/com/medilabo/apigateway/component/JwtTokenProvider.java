package com.medilabo.apigateway.component;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expirationDate = new Date(now + 3600000); // 1 heure d'expiration

        // Décoder la clé secrète encodée en Base64
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                //.setIssuer("Stormpath")
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
