# Microservices Spring Boot
Welcome to MS-Spring repository! This project is a demo of a microservices architecture using Spring Boot.

## Overview
This project was developed with the objective of exploring and demonstrating microservices architecture applying the best practices of the Spring ecosystem. It includes several services that communicate with each other to offer a complete solution for an e-commerce system.

![image](https://github.com/user-attachments/assets/d774e94d-5776-4e7d-a5d9-6747909a5249)


Important! This is a work-in-progress project and will be soon having more functionalities! :)

### Tech Stack
- Java 21
- Spring Boot 3
- Spring Security
- Spring Cloud Gateway
- Spring Data JPA
- PostgreSQL
- Kafka
- Docker
- Swagger
- Hibernate
- JUnit 5
- Mockito
- H2 Database (for tests)
- Maven

## Project Structure
Currently, the project has the following structure:

- user-service: Service responsible for users management.
- notifications-service: Service responsible for sending notifications to users.
- api-gateway: Service responsible for managing routing of the app.
- kafka-service: Service responsible for handling async communication between services through messaging.
- zookeeper-service: Service responsible for managing Kafka clusters.

## How to run 
### Pre-requisites
- Java 21
- Maven
- PostgreSQL
- Docker
  
Steps:
1 - Clone repository:

```bash
git clone https://github.com/joaodslourenco/ms-spring.git
```

2 - Inside project's folder, run:
```bash
docker compose up -d
```

3 - Access the app through 'http://localhost:8080'

4 - Use 'http://localhost:8080/swagger-ui' or the Postman collection to check the available endpoints.

## Tests
This project has unit and integration tests. To run them, run:

```bash
mvn test
```



