# Simple meet application example with Spring MVC

This project includes authentication (login, register, logout), groups (list, create, update, delete, subscribe), and events (list, create, update, detail, delete, participate) pages. It uses a PostgreSQL connection and a Tymeleaf template engine.
Also simple dockerizing example.

## Installation

### With docker
- First [install docker](https://docs.docker.com/engine/install/)
- (Optional) Change Postgresql auth properties on docker-compose.yml and application.properties.docker
- Run docker-compose to buil docker containers
```bash
docker-compose up -d --build
```
or
```bash
docker compose up -d --build
```
- It takes a few minutes application to get ready. If your containers up check logs to see backend is ready
```bash
docker logs backend -f 
```
- Go to [http://127.0.0.1:8080](http://127.0.0.1:8080) on your browser

### Without docker
- First you need Postgresql database
- Add your postgresql auth properties to src/main/recources/application.properties
- Use your IDE to run application or use maven build and run

```bash
mvn install
java -jar target/spring-meet-app.jar
```