#Lost & Found Application Backend (Spring boot)

A Spring Boot 3 backend for a campus Lost & Found system. It provides REST APIs for user registration and management, reporting lost and found items, and fuzzy matching/search to help users find likely matches.

## Tech Stack

- Java 17
- Spring Boot 3.3.x (Web, Security, Data JPA)
- MySQL (via `mysql-connector-j`)
- Maven (wrapper included)

## Features

- User registration and basic user management (students, admin endpoints)
- CRUD for Lost Items and Found Items
- Fuzzy matching between lost and found items
- Fuzzy search with adjustable thresholds
- CORS configured for local frontends (`http://localhost:5173`, `http://localhost:3939`)

## Project Layout

```
CampusManagement/
  lostAndFoundApplication/
    pom.xml
    mvnw, mvnw.cmd
    src/main/java/edu/infosys/lostAndFoundApplication/
      LostAndFoundApplication.java
      controller/
      service/
      dao/
      bean/
    src/main/resources/
      application.properties
```

## Prerequisites

- JDK 17+
- MySQL 8+
- Maven 3.9+ (optional, Maven Wrapper is included)

## Configuration

Default application configuration is in `lostAndFoundApplication/src/main/resources/application.properties`:

```
spring.application.name=lostAndFoundApplication
server.port=9999

spring.datasource.url=jdbc:mysql://localhost:3306/campusdb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=*****
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

Adjust the MySQL credentials and database name as needed before running.

## Getting Started

### 1) Start MySQL

- Ensure MySQL is running and a user exists that matches your configured username/password.
- The database `campusdb` will be created automatically if it does not exist (due to `createDatabaseIfNotExist=true`).

### 2) Build and Run

From the `lostAndFoundApplication` directory:

Using Maven Wrapper (recommended):

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS/Linux
./mvnw spring-boot:run
```

Or using a globally installed Maven:

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:9999` by default.

### 3) Packaging a JAR

```bash
# From lostAndFoundApplication
./mvnw clean package
java -jar target/lostAndFoundApplication-0.0.1-SNAPSHOT.jar
```

## API Reference

Base URL: `http://localhost:9999`

### Auth and Users

- POST `/lost-found/register`
  - Registers a user. If `role` is not provided, defaults to `Student`.
  - Body example:
    ```json
    {
      "username": "alice",
      "password": "secret",
      "role": "Student"
    }
    ```
- GET `/lost-found/user/details`
  - Returns details of the currently authenticated user.
- GET `/lost-found/admin/students`
  - Returns all students (admin only).
- DELETE `/lost-found/admin/student/{username}`
  - Deletes a student by username (admin only).

### Lost Items

- POST `/lost-found/lost-items`
  - Create a lost item.
- GET `/lost-found/lost-items`
  - List all lost items.
- GET `/lost-found/lost-items/{id}`
  - Get a lost item by id.
- DELETE `/lost-found/lost-items/{id}`
  - Delete a lost item by id.
- GET `/lost-found/lost-items/user`
  - List lost items created by the current user.

### Found Items

- POST `/lost-found/found-items`
  - Create a found item.
- GET `/lost-found/found-items`
  - List all found items.
- GET `/lost-found/found-items/{id}`
  - Get a found item by id.
- DELETE `/lost-found/found-items/{id}`
  - Delete a found item by id.
- GET `/lost-found/found-items/user`
  - List found items created by the current user.

### Fuzzy Matching and Search

- GET `/lost-found/fuzzy/match/found/{lostItemId}?threshold=0.7`
  - Returns found items matching a given lost item id; `threshold` optional (default `0.7`).
- GET `/lost-found/fuzzy/match/lost/{foundItemId}?threshold=0.7`
  - Returns lost items matching a given found item id; `threshold` optional (default `0.7`).
- GET `/lost-found/fuzzy/search/found?query=...&username=...&threshold=0.3`
  - Fuzzy search across found items; `threshold` optional (default `0.3`).
- GET `/lost-found/fuzzy/search/lost?query=...&username=...&threshold=0.3`
  - Fuzzy search across lost items; `threshold` optional (default `0.3`).

## CORS

CORS is enabled for local frontend development at:

- `http://localhost:5173`
- `http://localhost:3939`

If your frontend runs elsewhere, update the `@CrossOrigin` annotations in the controllers.

## Notes on Security

The project includes Spring Security. Ensure you configure authentication as appropriate for your deployment (e.g., stateless JWT, sessions, or basic auth). The provided endpoints assume an authenticated context for user-specific data. Review `CampusUserService` and any security configuration before production use.
