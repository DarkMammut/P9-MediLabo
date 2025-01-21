# Utilisation d'une image Java officielle
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le jar du microservice
COPY target/notes-service-0.0.1-SNAPSHOT.jar notes-service.jar

# Exposer le port du service
EXPOSE 8082

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "notes-service.jar"]