# Utiliser une image de base JDK pour exécuter Spring Boot
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR généré par Maven/Gradle dans l'image
COPY target/config-server-0.0.1-SNAPSHOT.jar config-server.jar

# Installer curl si nécessaire pour les healthchecks
RUN apt-get update && apt-get install -y curl

# Exposer le port de l'application
EXPOSE 8088

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "config-server.jar"]