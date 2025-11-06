FROM maven:3.9.6-eclipse-temurin-17-focal AS build

WORKDIR /build

COPY demo/mvnw .
COPY demo/pom.xml .
COPY demo/.mvn ./.mvn

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

COPY demo/src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=build /build/target/demo-*.jar app.jar

EXPOSE 8080

ENV SPRING_DATASOURCE_USERNAME=""
ENV SPRING_DATASOURCE_PASSWORD=""

ENTRYPOINT ["java", "-jar", "app.jar"]