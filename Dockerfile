FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S payments && adduser -S payments -G payments

WORKDIR /app

COPY target/fraud-service-0.0.1-SNAPSHOT.jar app.jar

USER payments

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]