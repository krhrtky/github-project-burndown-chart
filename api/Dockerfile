FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine-slim

RUN mkdir ./app

COPY . ./app

WORKDIR ./app

RUN ./gradlew build -x test --build-cache

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/github-project-burndown-chart-api-0.0.1-SNAPSHOT.jar"]
