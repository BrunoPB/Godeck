FROM openjdk:21
WORKDIR /godeck
COPY ../target/*.jar app.jar
COPY ../src/data/* /data/

ENTRYPOINT ["java","-jar","./app.jar"]