package com.medilabo.apigateway.security.filter;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtCookieFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Récupérer le cookie JWT
        HttpCookie jwtCookie = exchange.getRequest().getCookies().getFirst("JWT");

        if (jwtCookie != null) {
            String token = jwtCookie.getValue(); // Récupère la valeur du cookie
            System.out.println("Token récupéré depuis le cookie: " + token);

            // Créer une nouvelle requête avec l'en-tête Authorization
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("Authorization", "Bearer " + token) // Ajouter l'en-tête Authorization
                    .build();

            // Debug : Vérifier l'en-tête Authorization
            System.out.println("En-tête Authorization ajouté : " + modifiedRequest.getHeaders().getFirst("Authorization"));

            // Créer un nouvel échange avec la requête modifiée
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)
                    .build();

            // Continuer avec la chaîne de filtres en utilisant l'échange modifié
            return chain.filter(modifiedExchange);
        }

        // Si le cookie n'est pas présent, on continue sans modification
        return chain.filter(exchange);
    }
}
