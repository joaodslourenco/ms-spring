FROM maven:3.9.8 AS build

COPY src /app/src
COPY pom.xml /app

WORKDIR /app

RUN mvn clean install


FROM amazoncorretto:21

COPY --from=build /app/target/*.jar /app/app.jar

WORKDIR /app

CMD ["java", "-jar", "/app/app.jar"]