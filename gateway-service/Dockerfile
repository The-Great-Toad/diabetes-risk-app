# Utilisation de l'image Maven pour compiler et tester le projet
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

# Copier les fichiers nécessaires pour le build Maven
COPY pom.xml .
COPY src ./src

# Étape de compilation
RUN mvn clean package -DskipTests=true

# Utilisation de l'image OpenJDK pour exécuter l'application
FROM openjdk:17-jdk-alpine
LABEL authors="ningo"

# Copier le JAR généré depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Exposer le port de l'application
EXPOSE 8080

# Commande d'exécution
ENTRYPOINT ["java", "-jar", "/app.jar"]