# Godeck

This is the repository of Godeck.

Godeck is a mobile turn-based card game themed with Gods and Mythical Creatures.

## Run project

To run the whole game, we need to setup the Backend Evironment (Game Server) and then the Godot Engine (Game Client).

### Running Game Server

The game server is being developed using Java Spring Boot and MySQL.

Luckly, we have a setted up Docker environment, so there is no need to install all the backend technologies, just Docker, it'll do everything for you.

-   IMPORTANT: If you are using **Linux**, you need to install [Maven](https://maven.apache.org/). Careful, it might have some prerequisites.

Download Docker Desktop [here](https://www.docker.com/products/docker-desktop/).

After installing and signing up, run:

-   [start-docker-windows.bat](backend/start-docker-windows.bat) (Windows)
-   [rebuild-all.sh](backend/rebuild-all.sh) (Linux)

Now the game server is running.

### Running Game Client

The game is being developed in Godot version 4.2.1.

You can find the Godot page [here](https://godotengine.org/).

After downloading Godot, just open the [project.godot](project.godot) file in the Game Engine. Now just click on the play button (▶) in the superior right corner.
