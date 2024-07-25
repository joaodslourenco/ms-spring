FROM amazoncorretto:21
# ARG JAR_FILE=target/*.jar
# COPY ${JAR_FILE} app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]


RUN mkdir /app
WORKDIR /app

# RUN mvn clean package

COPY ./target/*.jar /app/app.jar

# # Crie um usuário e grupo para rodar a aplicação
# RUN groupadd -g 1000 appgroup && \
#     useradd -r -u 1000 -g appgroup appuser && \
#     chown -R appuser:appgroup /app

# Mude para o usuário criado
# USER appuser

# Comando para iniciar a aplicação
CMD ["java", "-jar", "/app/app.jar"]