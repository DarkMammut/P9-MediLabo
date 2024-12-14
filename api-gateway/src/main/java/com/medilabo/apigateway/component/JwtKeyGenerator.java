package com.medilabo.apigateway.component;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // Générer une clé sécurisée pour HS512
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Key: " + base64Key);
    }
}
