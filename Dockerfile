# Usa una imagen oficial de Java 17 o 21
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el archivo JAR generado por Maven
COPY target/*.jar app.jar

# Expone el puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar el JAR
CMD ["java", "-jar", "app.jar"]