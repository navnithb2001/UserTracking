FROM openjdk:17
ADD /target/user-tracking.jar user-tracking.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/user-tracking.jar"]