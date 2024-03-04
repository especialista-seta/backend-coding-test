# Spring Boot to-do app challenge

With this challenge we want to see your skills and the quality of the code, we will take into account the use of SOLID
principles. You can use all the tools and libraries you want!

## Required tools

1. [Java 11](https://adoptopenjdk.net/)
2. [MySQL](https://dev.mysql.com/downloads/mysql/)

## Objectives

##### Principal

Develop the necessary functionalities for the application to be able to perform the following requests:

- **GET** http request that returns a list of all tasks stored in the database.
- **GET** http request that returns a specific task by their ID.
- **POST** http request that stores a new task in the database.
- **PUT** http request that updates a specific task by their ID.
- **DELETE** http request that deletes a task in the database.

##### Optional

1. Improve the request that returns a list of tasks by adding the possibility to order and filter the results, for
   example:

    - Order results by priority or creation date.
    - Filter results by priority and/or completion.

2. Create a new entity called *SubtaskEntity* that allows one task to have multiple subtasks.

# Result

## Instructions

In order to run the API, first we need to create a database in MySQL. You can do this by running the following command
in the mysql folder:

```docker-compose up```

The first time the command is executed, it will create the mysql instance and run the scripts under database folder
After that API can be started normally. The data will be persisted in the database, so you don't need to recreate the
database every time you start the application.

If there's a need to recreate the database, you can run the following command:

```docker-compose rm -v -f db && docker-compose up```

## Credentials

The request are authenticated, so you need to use the following credentials:

```AUTH_TOKEN_HEADER_NAME = "X-API-KEY"```

```AUTH_TOKEN = "Guillermo"```

These values are hardcoded in the application, but in a real application, they would be stored in a secure way, such as
environment variables.

## API Documentation

After starting the application, you can access the API documentation at the following URL:

```http://localhost:8443/swagger-ui/index.html```

The documentation was generated using Swagger (OpenAPI 3.0) and it takes into account the login and the token
requirements for the requests.

## Running the tests

The test cases run on the H2 database, so you don't need to have a MySQL database running to run the tests.

## Decisions

### Spring Boot

I decided to update the Spring Boot version to 2.7.17, since its one of latest stable version that works with Java 11.
The rest of the dependencies were also updated since Spring Boot uses BOM to maintain the dependencies versions.

If I were to update the Java version (to 17 or 21), I would also update the Spring Boot version to the latest stable
relase.(^3.X.X)

### Spring Security

I chose to use Spring Security to secure the API, creating a simple service that checks a certain header contains an
specific value. This behaviour can be enhanced to use JWT tokens, for example, to make the API more secure or to make a
request to an external service to validate the token. I also disabled the default behaviour of Spring Security that
request an user/password to access the API.

### Docker-compose

In order to facilitate the execution of the application, I created a docker-compose file that starts a MySQL database.
As of now, it is the only service in the docker-compose file, but it can be expanded to include other services such as
the application itself. In this case, we would have a docker file for the application and the docker compose file would
contain both the database and the application.

With this approach, the same values used in the application.properties file
could be used in the docker-compose file, making it easier to manage the application's configuration.

### Architecture

I chose to use a layered architecture, with the following layers: controller, service, repository and model. This
architecture is widely used in Spring Boot applications and is very easy to understand and maintain. It separates the
responsibilities of each layer, making the code more organized and easier to maintain. Also, it's easy to test, as each
layer can be tested separately and fits well with the SOLID principles. For a small application or a mvp,
this architecture is more than enough.

In case of a larger application, I would consider using a hexagonal architecture, which is a more modern approach and
fits well with microservices. It is a more complex architecture, but it is more scalable and easier to maintain in the
long run. Another approach could be to use clean architecture, which is also a modern approach and is very similar to
hexagonal architecture.

### OpenAPI

I chose to use OpenAPI to document the API. It is a widely used tool and is very easy to use. It generates a very
complete documentation, which is very useful for the development team and for the users of the API. It also generates
the client code, which can be used to consume the API.

The documentation generated can be found at the following URL:

```http://localhost:8443/api-docs```

### Profiles

In order to maintain simplicity, I chose to use only one profile. In a real application, we would have
at least two profiles, the dev and prod profiles. The dev profile would be used for development, while the prod profile
would be used for production. The prod profile would have different configurations, such as the database connection.
Ideally, a profile for each environment would be created.

The testing profile would also be used, which would have different configurations for testing, such as the database.

### Testing

Although it wasn't part of the requirements, I decided to write some tests for the application, using
JUnit and Mockito to write the tests. I also used the H2 database to run the tests, so that I don't need to have a MySQL
database running to run the tests.

For end-to-end tests, I would use [Robot Framework](https://robotframework.org/), which is a very complete tool for
testing APIs and is very easy to use. It is also very easy to maintain and has a very complete documentation. 