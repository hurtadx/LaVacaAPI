# Dockerfile para proyecto Spring Boot (lavaca)
FROM eclipse-temurin:21-jdk-alpine as build

WORKDIR /app

# Copiar archivos de build y dependencias
COPY build.gradle.kts settings.gradle.kts gradlew gradlew.bat ./
COPY gradle ./gradle

# Copiar el código fuente
COPY src ./src

# Dar permisos de ejecución a gradlew
RUN chmod +x gradlew

# Descargar dependencias y compilar el proyecto
RUN ./gradlew build --no-daemon

# Segunda etapa: imagen liviana para ejecución
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el jar generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Puerto expuesto por Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
