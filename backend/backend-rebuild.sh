docker container stop godeck-spring-boot
docker container rm godeck-spring-boot
docker image rm backend_spring-boot
mvn clean install
docker-compose up -d --build