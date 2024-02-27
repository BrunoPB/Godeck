FROM openjdk:21
WORKDIR /godeck
COPY ../target/*.jar app.jar
COPY ../src/data/* /data/

# HTTP Port
EXPOSE 8080
# TCP Port
EXPOSE ${MIN_TCP_PORT}-${MAX_TCP_PORT}

ENTRYPOINT ["java","-jar","./app.jar"]