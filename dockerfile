# Use uma imagem base com JDK 17
FROM openjdk:17

# Copie o arquivo jar para a imagem
COPY target/project-0.0.1-SNAPSHOT.jar app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","/app.jar"]