#
# Build stage
#
ARG MVN_HOME=/mvn/home
FROM eclipse-temurin:17-jdk-alpine AS build
ARG MVN_HOME
ENV HOME=${MVN_HOME}
RUN mkdir -p "$HOME"
WORKDIR "$HOME"
ADD . "$HOME"
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f "$HOME/pom.xml" clean package

FROM payara/server-full:6.2023.11-jdk17 as deploy
ARG MVN_HOME
ENV WAR_FILE=${MVN_HOME}/target/*.war
COPY --from=build "$WAR_FILE" $DEPLOY_DIR
