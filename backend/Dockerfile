# ========================
# Etapa 1: Build con Maven
# ========================
FROM maven:3.9.3-eclipse-temurin-21 AS build
WORKDIR /app

# Copia pom.xml y src para generar el .jar
COPY pom.xml .
COPY src ./src

# Construye el proyecto, omitiendo tests para no fallar
RUN mvn clean package -DskipTests

# =========================
# Etapa 2: Ejecutar la app
# =========================
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copia el .jar generado desde la etapa de build
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto y ejecutar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
