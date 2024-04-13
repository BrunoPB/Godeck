# Stop container
docker container stop godeck-spring-boot

# Remove container
docker container rm godeck-spring-boot

# Remove image
docker image rm server_spring-boot

# Recompile application
mvn clean install

# Build image and run container
docker-compose up -d --build