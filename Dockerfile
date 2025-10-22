# ========================
# Etapa 1: Build con Maven
# ========================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos archivos de Maven Wrapper
COPY backend/mvnw .
COPY backend/mvnw.cmd .
COPY backend/.mvn .mvn

# Copiamos el pom.xml y descargamos dependencias
COPY backend/pom.xml .

# Damos permisos de ejecución (solo necesario en Linux/Mac)
RUN chmod +x ./mvnw || true

# Descargamos dependencias
RUN ./mvnw dependency:go-offline -B

# Copiamos el código fuente
COPY backend/src ./src

# Construimos el JAR (sin tests)
RUN ./mvnw clean package -DskipTests

# =========================
# Etapa 2: Ejecutar la app
# =========================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copiamos el JAR generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto del servicio
EXPOSE 8080

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
