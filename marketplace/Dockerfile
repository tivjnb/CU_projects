FROM gradle:8.12.1-jdk17 AS build

WORKDIR /app
COPY week8 .

RUN gradle bootJar --no-daemon
RUN ls /app/build/libs/

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/build/libs/week8-0.0.1-SNAPSHOT.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
