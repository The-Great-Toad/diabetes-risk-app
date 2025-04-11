FROM openjdk:17-jdk-alpine
LABEL authors="ningo"
COPY target/*.jar app.jar
COPY diabetes_trigger_terms.txt diabetes_trigger_terms.txt
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app.jar"]