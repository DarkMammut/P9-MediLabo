package com.medilabo.apigateway.component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // Longueur de clé recommandée pour HMAC SHA-512 (512 bits = 64 bytes)
        int keyLengthInBytes = 64;

        // Générer une clé sécurisée avec SecureRandom
        byte[] keyBytes = new byte[keyLengthInBytes];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);

        // Convertir les octets en SecretKey
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA512");

        // Encoder en Base64 pour utilisation dans la configuration
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Key: " + base64Key);
    }
}
