# Mancala
Mancala (Kalah) game for bol.com

**Prerequisites:** [Java 11, Docker]

* [Getting Started](#getting-started)
* [Usage](#usage)
* [Links](#links)

## Getting Started

To build this application, run the following command:

```bash
./mvnw clean package
```

This application uses PostgreSQL database, so you need to run it in Docker with the following command:
```bash
docker-compose up
```

To run this application, run the following command:

```bash
./mvnw spring-boot:run
```

To run unit test for this application, run the following command:

```bash
./mvnw test
```

### Usage

Run the Mancala game application and go to home page:
[http://localhost:8080/](http://localhost:8080/)

You need to click to `Create game` button and send the link of your game to your opponent.

Enjoy!

## Links

This application uses the following open source libraries:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring Data JDBC](https://spring.io/projects/spring-data-jdbc)
* [Spring Websocket](https://docs.spring.io/spring-framework/reference/web/websocket.html)
* [FlyWay](https://flywaydb.org/)
* [Project Lombok](https://projectlombok.org/)

