# Usa la imagen oficial de Java 21
FROM openjdk:21-jdk-slim

# Crea un directorio dentro del contenedor
WORKDIR /app

# Copia el archivo .jar generado por Maven
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto del backend
EXPOSE 8080

# Ejecuta la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
