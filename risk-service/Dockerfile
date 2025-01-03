# Utiliser une image de base JDK pour exécuter Spring Boot
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR généré par Maven/Gradle dans l'image
COPY target/risk-service-0.0.1-SNAPSHOT.jar risk-service.jar

# Exposer le port de l'application
EXPOSE 8083

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "risk-service.jar"]
