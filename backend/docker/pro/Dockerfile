# ============================
# BUILD STAGE
# ============================
FROM eclipse-temurin:23-jdk AS build

WORKDIR /build

COPY build.gradle settings.gradle gradlew .
COPY gradle gradle

RUN chmod +x gradlew 

COPY ./src ./src

RUN ./gradlew clean bootJar --no-daemon


# ============================
# APP STAGE
# ============================
FROM eclipse-temurin:23-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

COPY --from=build /build/build/libs/*.jar app.jar

USER appuser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

