#
# Build stage
#
ARG MVN_HOME=/mvn/home
FROM eclipse-temurin:17-jdk-alpine AS build
ARG MVN_HOME
ENV HOME=${MVN_HOME}
WORKDIR "$HOME"
ADD pom.xml "$HOME"
ADD src "$HOME"
ADD . "$HOME"
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f "$HOME/pom.xml" clean package

FROM payara/micro:6.2023.11-jdk17 as deploy
ARG MVN_HOME
ENV WAR_FILE=${MVN_HOME}/target/*.war
COPY --from=build "$WAR_FILE" $DEPLOY_DIR/ROOT.war
