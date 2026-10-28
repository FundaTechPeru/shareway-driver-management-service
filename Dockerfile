FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
RUN useradd --system --no-create-home appuser \
    && mkdir -p /app/storage \
    && chown appuser /app/storage
COPY --from=build /workspace/target/*.jar app.jar
USER appuser
ENV STORAGE_PATH=/app/storage
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
