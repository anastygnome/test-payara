FROM maven:3.9.2-eclipse-temurin-17-alpine
COPY pom.xml app/pom.xml
COPY src app/src
WORKDIR app
RUN mvn compile
CMD mvn exec:java


