FROM openjdk:21
WORKDIR /godeck
COPY ../target/*.jar app.jar
COPY ../src/data/* /data/
EXPOSE 8080
ENTRYPOINT ["java","-jar","./app.jar"]