# BUILD STAGE
FROM eclipse-temurin:23-jdk AS build

WORKDIR /build

COPY build.gradle settings.gradle gradlew .
COPY gradle gradle

RUN chmod +x gradlew && ./gradlew build --no-daemon

COPY ./src ./src

RUN ./gradlew clean bootJar --no-daemon

# APP STAGE
FROM eclipse-temurin:23-jre-alpine

WORKDIR /app 

COPY --from=build --chown=node:node /build/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
