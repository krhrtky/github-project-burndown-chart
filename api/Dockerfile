FROM adoptopenjdk/openjdk11:alpine

RUN mkdir ./app

COPY . ./app

WORKDIR ./app

RUN ./gradlew build -x test --build-cache

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/github-project-burndown-chart-api-0.0.1-SNAPSHOT.jar"]
