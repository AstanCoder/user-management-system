# syntax=docker/dockerfile:1

FROM gradle:8.11.1-jdk21-alpine AS build
WORKDIR /app

COPY settings.gradle.kts build.gradle.kts ./
COPY buildSrc buildSrc
COPY backend/build.gradle.kts backend/build.gradle.kts

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle :backend:dependencies --no-daemon -q

COPY backend/src backend/src

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle :backend:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN mkdir -p /app/data/avatars
COPY --from=build /app/backend/build/libs/backend.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
