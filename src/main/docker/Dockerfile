# Stage 1: Build
FROM gradle:jdk17 as builder
WORKDIR /app
COPY . .
RUN gradle build -x test

# Stage 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
