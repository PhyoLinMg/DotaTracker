FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts ./
COPY src src
RUN gradle build -x test --no-daemon

FROM openjdk:17-jdk-slim
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]