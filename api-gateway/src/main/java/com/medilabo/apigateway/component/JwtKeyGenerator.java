package com.medilabo.apigateway.component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

public class JwtKeyGenerator {

    public static void main(String[] args) {
        // Longueur de clé recommandée pour HMAC SHA-512 (512 bits = 64 bytes)
        int keyLengthInBytes = 32;

        // Générer une clé sécurisée avec SecureRandom
        byte[] keyBytes = new byte[keyLengthInBytes];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);

        // Convertir les octets en SecretKey
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        // Encoder en Base64 pour utilisation dans la configuration
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // Afficher ou sauvegarder la clé
        System.out.println("Votre clé secrète pour HMAC SHA-256 a été générée avec succès.");
        System.out.println("Veuillez l'ajouter dans votre fichier de configuration sous le nom `jwt.secret`.");
        System.out.println("Generated Key (Base64): " + base64Key);

        // Sauvegarder dans un fichier
        saveKeyToFile(base64Key);
    }

    private static void saveKeyToFile(String base64Key) {
        try (FileWriter writer = new FileWriter(new File("jwt_secret.key"))) {
            writer.write(base64Key);
            System.out.println("Clé sauvegardée dans le fichier : " + "jwt_secret.key");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de la clé : " + e.getMessage());
        }
    }
}
