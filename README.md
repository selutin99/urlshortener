# Netology tesk task

URL-shortener application via Spring Boot & Redis.

## Build JAR

- Package the application

`$ ./mvnw clean package`

> To skip the tests use: `-DskipTests=true` 

## Run

`$ docker-compose build && docker-compose up -d`

## Verify the application is running

> Application listens on port 8080.

Check swagger-ui
```
GET http://localhost:8080/swagger-ui/index.html
```

Or use curl:
```
Create short-URL:
GET http://localhost:8080/urlshortener?url=http://google.com

Get full URL by short-link:
GET http://localhost:8080/urlshortener/4170157c
```