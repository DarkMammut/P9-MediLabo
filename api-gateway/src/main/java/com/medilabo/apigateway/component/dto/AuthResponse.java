package com.medilabo.apigateway.component.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String message;

    // Si vous voulez gérer spécifiquement les erreurs avec un seul message, vous pouvez ajouter un constructeur alternatif ici
    public AuthResponse(String message) {
        this.token = null;  // Pas de token en cas d'erreur
        this.message = message;
    }
}
