# Utiliser une image de base JDK pour exécuter Spring Boot
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR généré par Maven/Gradle dans l'image
COPY target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar

# Exposer le port de l'application
EXPOSE 8080

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
