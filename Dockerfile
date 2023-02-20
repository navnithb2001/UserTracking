FROM maven:3.9.0-eclipse-temurin-17-focal AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
COPY --from=build /target/user-tracking.jar user-tracking.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/user-tracking.jar"]