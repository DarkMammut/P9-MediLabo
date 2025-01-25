package com.medilabo.notesservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@RefreshScope
public class NotesServiceApplication {

    public static void main(String[] args) {
        // Charger le fichier .env uniquement en local
        if (System.getenv("DOCKER_ENV") == null) {
            Dotenv dotenv = Dotenv.configure()
                    .directory("../../../../../..") // Chemin relatif vers MediLabo
                    .load();

            // Définir le profil actif pour Spring
            String activeProfile = dotenv.get("SPRING_PROFILES_ACTIVE");
            if (activeProfile != null) {
                System.setProperty("spring.profiles.active", activeProfile);
            }
        }

        SpringApplication.run(NotesServiceApplication.class, args);
    }
}