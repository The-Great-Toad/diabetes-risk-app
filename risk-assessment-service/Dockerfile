FROM openjdk:17-jdk-alpine
LABEL authors="ningo"
COPY target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar"]