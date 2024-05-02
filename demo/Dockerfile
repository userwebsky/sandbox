FROM openjdk:17-slim
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
CMD ["java", "-jar", "app.jar"]
#docker build -t familyapp:1 .
