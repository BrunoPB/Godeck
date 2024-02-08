@ECHO OFF

:: Stop and remove all containers
FOR /f "tokens=*" %%i IN ('docker ps -q') DO docker stop %%i

:: Remove the rest
docker system prune -a -f

:: Build the docker image and start the container
docker-compose up -d --build

pause