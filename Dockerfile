FROM maven:3.0.0-jdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
ADD /target/user-tracking.jar user-tracking.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/user-tracking.jar"]