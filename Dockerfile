# Usar a imagem oficial Eclipse Temurin (Java 17)
FROM eclipse-temurin:17-jre-jammy

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR da aplicação para o container
COPY target/*.jar app.jar

# Expor a porta que a aplicação irá rodar
EXPOSE 9001

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
