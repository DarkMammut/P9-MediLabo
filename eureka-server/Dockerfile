# Utiliser une image de base JDK pour exécuter Spring Boot
FROM openjdk:21-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le fichier JAR généré par Maven/Gradle dans l'image
COPY target/eurekaserver-1.0.0.jar eureka-server.jar

# Exposer le port de l'application
EXPOSE 8061

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
