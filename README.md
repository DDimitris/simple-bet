# Simple Betting App – Fullstack with Docker Compose
![Build & Tests](https://github.com/DDimitris/simple-bet/actions/workflows/maven.yml/badge.svg)

This project contains:

* **Backend**: Spring Boot (Java 21 + Maven)
* **Frontend**: Angular
* **Database**: PostgreSQL with Flyway migrations

---

## 🚀 How to Run

### Prerequisites

Make sure you have **Docker** and **Docker Compose** installed on your machine.

---

### 1. Clone the repository

```bash
git clone git@github.com:DDimitris/simple-bet.git
cd simple-bet
```

---

### 2. Build and start all services locally (Docker Compose + local build)

```bash
docker-compose -f docker-compose-dev.yml up --build -d
```

This will build the backend and frontend images from the Dockerfiles and start all services

---

### 3. Start all services using pre-built images (Docker Compose + remote images)

```bash
docker-compose up -d
```

This uses the default `docker-compose.yml` which pulls the pre-built images from the GitHub Container Registry

---

## API Endpoints

### `GET /api/matches`

Fetches a paginated list of matches.

**Query Parameters:**

| Name      | Type   | Default | Description                                 |
|-----------|--------|---------|---------------------------------------------|
| page      | int    | 0       | The page number to retrieve                 |
| size      | int    | 10      | Number of items per page                    |
| sortBy    | string | id      | Field to sort by (e.g., `matchDate`)        |
| direction | string | desc    | Sort direction (`asc` or `desc`)            |

**Responses:**

| Status Code | Description                                            |
|-------------|--------------------------------------------------------|
| 200 OK      | Successfully fetched matches                           |
| 500 Error   | Server-side error                                      |

---

### `POST /api/matches`

Create a new match.

**Request Body Parameters:**

| Name        | Type   | Description                       |
|-------------|--------|-----------------------------------|
| teamA       | string | First team                        |
| teamB       | string | Second team                       |
| matchDate   | string | Date of match (YYYY-MM-DD)        |
| matchTime   | string | Time of match (HH:mm)             |
| sport       | string | Sport type (FOOTBALL, BASKETBALL) |
| description | string | Optional match description        |
| odds        | array  | List of 3 odds (1, X, 2)          |

**Responses:**

| Status Code     | Description                        |
|-----------------|------------------------------------|
| 201 Created     | Match created successfully         |
| 400 Bad Request | Invalid request body or parameters |
| 409 Conflict    | Conflict (e.g., duplicate match)   |
| 500 Error       | Server-side error                  |

---

### `PUT /api/matches/{id}`

Update an existing match by ID.

**Path Parameter:**

| Name | Type | Description        |
|------|------|--------------------|
| id   | int  | Match ID to update |

**Request Body Parameters:**

| Name        | Type   | Description                       |
|-------------|--------|-----------------------------------|
| teamA       | string | First team                        |
| teamB       | string | Second team                       |
| matchDate   | string | Date of match (YYYY-MM-DD)        |
| matchTime   | string | Time of match (HH:mm)             |
| sport       | string | Sport type (FOOTBALL, BASKETBALL) |
| description | string | Optional match description        |
| odds        | array  | List of 3 odds (1, X, 2)          |

**Responses:**

| Status Code     | Description                        |
|-----------------|------------------------------------|
| 200 OK          | Match updated successfully         |
| 400 Bad Request | Invalid request body or parameters |
| 404 Not Found   | Match ID does not exist            |
| 500 Error       | Server-side error                  |

---

### `DELETE /api/matches/{id}`

Delete a match by ID.

**Path Parameter:**

| Name | Type | Description        |
|------|------|--------------------|
| id   | int  | Match ID to delete |

**Responses:**

| Status Code    | Description                |
|----------------|----------------------------|
| 204 No Content | Match deleted successfully |
| 404 Not Found  | Match ID does not exist    |
| 500 Error      | Server-side error          |

---


## 🔗 URLs

* Frontend: [http://localhost:4200](http://localhost:4200)
* Backend API: [http://localhost:8081/api/matches](http://localhost:8081/api/matches)
* PostgreSQL: `localhost:5432` (DB: `postgres`, Schema: `matches_service`, user: `postgres`, password: `postgres`)
* Swagger UI: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

---

## 🛠 Development Notes

* The **backend** is built inside a multi-stage Dockerfile:

    * Stage 1: Uses Maven to build the JAR
    * Stage 2: Runs the built JAR in a slim JDK container
* The **frontend** is built using Node.js and served by Nginx.
* **Flyway migrations** run automatically on backend startup to prepare the database schema.

---

## 🧹 Cleanup

To stop and remove containers, networks, and volumes:

```bash
docker-compose down -v
```
---

## 🧪 Testing

The project includes both **unit tests** and **integration tests**.

---

### ▶️ Running Tests

Run all tests:

```bash
mvn test
```

Run only **unit tests**:

```bash
mvn test -Dgroups=unit
```

Run only **integration tests**:

```bash
mvn test -Dgroups=integration
```

---

### 🔧 Notes

* Integration tests require Docker, since they use Testcontainers to start PostgreSQL.
* Tests rely on JUnit 5 tags (`@Tag("unit")`, `@Tag("integration")`) for filtering.


Tools used:

  * **Mockito** for mocking
  * **Spring MockMvc** for controller tests
  * **RestAssured** for integration tests
  * **Testcontainers** for PostgreSQL

---

## ⚙️ Continuous Integration (CI)

This project uses **GitHub Actions** to automatically run tests on every push and pull request.

* Ensures that both **unit tests** and **integration tests** pass before merging.
* Runs in a clean environment with **Java + Maven + Docker** support (required for Testcontainers).

### 🚀 Workflow

The workflow file is located in:

```
.github/workflows/maven.yml

```
