package com.medilabo.patientservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@RefreshScope
public class PatientServiceApplication {

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

        SpringApplication.run(PatientServiceApplication.class, args);
    }
}