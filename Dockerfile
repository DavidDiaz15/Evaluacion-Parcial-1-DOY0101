# ETAPA 1: Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
# Copiamos el pom y el código fuente
COPY pom.xml .
COPY src ./src
# Compilamos el proyecto dentro de Docker
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (Run)
FROM eclipse-temurin:25-jdk
WORKDIR /app
# Extraemos SOLO el .jar generado en la etapa anterior
COPY --from=builder /app/target/libreria-0.0.1-SNAPSHOT.jar app.jar
# Exponemos el puerto de tu API
EXPOSE 8080
# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
