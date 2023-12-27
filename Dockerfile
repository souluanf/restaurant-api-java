FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
VOLUME /root/.m2
RUN mvn dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests
FROM openjdk:17-slim
WORKDIR /app
EXPOSE $PORT
COPY --from=build /app/target/restaurant-api.jar .
CMD ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} -Dspring.profiles.active=prd /app/restaurant-api.jar"]
