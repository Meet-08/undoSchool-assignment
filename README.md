# UndoSchool - Course Search Service

A Spring Boot application that provides search functionality for courses using Elasticsearch. This service allows users to search for educational courses based on various criteria like category, type, age range, price, and session dates, with support for autocomplete suggestions and fuzzy matching.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Sample Data](#sample-data)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)

## ✨ Features

- **Basic Course Search**: Full-text search on title and description
- **Multi-filter Support**: Filter by category, type, age range, price range, and upcoming session date
- **Sorting & Pagination**: Sort by next session date or price, with pagination controls
- **Elasticsearch Integration**: Fast and efficient search engine
- **Dockerized Elasticsearch**: One-command setup with Docker Compose
- **RESTful API**: Clean endpoints

## 🛠 Technology Stack

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data Elasticsearch** (compatible with ES 7.x and 8.x)
- **Elasticsearch 8.x**
- **Lombok**
- **Maven**
- **Docker & Docker Compose**

## 📋 Prerequisites

- Java 21 or later
- Maven 3.6+
- Docker & Docker Compose
- Git

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/Meet-08/undoSchool-assignment.git
cd undoSchool-assignment
```

### 2. Start Elasticsearch

Ensure Docker is running, then:

```bash
docker-compose up -d
```

This spins up a single-node Elasticsearch at `http://localhost:9200`.

### 3. Verify Elasticsearch

```bash
curl http://localhost:9200
```

Expect a JSON response with cluster health.

### 4. Build the Service

```bash
mvn clean compile
```

## ⚙️ Configuration

Configure Elasticsearch connection in `src/main/resources/application.properties`:

```properties
spring.elasticsearch.uris=http://localhost:9200
```

## 🏃‍♂️ Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using Executable Jar

```bash
mvn package
java -jar target/undoSchool-0.0.1-SNAPSHOT.jar
```

The service listens at `http://localhost:8080`.

## 📚 Sample Data

Place your sample data under `src/main/resources/sample-courses.json`. Rename or create:

```text
src/main/resources/sample-courses.json
```

Each object should include:

- **id**: unique identifier
- **title**: string
- **description**: string
- **category**: e.g. "Math", "Science", "Art"
- **type**: ONE_TIME, COURSE, or CLUB
- **gradeRange**: e.g. "1st–3rd"
- **minAge** / **maxAge**: numeric
- **price**: decimal
- **nextSessionDate**: ISO-8601 datetime, e.g. `2025-06-10T15:00:00Z`

On startup, the application bulk-indexes this file into the configured index.

## 📚 API Documentation

### 1. Search Courses

- **Endpoint**: `GET /api/search`
- **Query Parameters**:

| Parameter         | Type    | Required | Description                                             |
| ----------------- | ------- | -------- | ------------------------------------------------------- |
| `q`               | String  | No       | Keyword for title/description                           |
| `category`        | String  | No       | Exact match category                                    |
| `type`            | String  | No       | ONE_TIME, COURSE, or CLUB                               |
| `minAge`          | Integer | No       | Minimum age                                             |
| `maxAge`          | Integer | No       | Maximum age                                             |
| `minPrice`        | Double  | No       | Minimum price                                           |
| `maxPrice`        | Double  | No       | Maximum price                                           |
| `nextSessionFrom` | String  | No       | ISO date filter for upcoming sessions (`YYYY-MM-DD`...) |
| `sort`            | String  | No       | `upcoming` (default), `priceAsc`, or `priceDesc`        |
| `page`            | Integer | No       | Page number (default: 0)                                |
| `size`            | Integer | No       | Page size (default: 10)                                 |

- **Response**:

```json
{
  "totalHits": 3,
  "courses": [
    {
      "id": 11,
      "title": "Rocket Science Basics",
      "description": "Build and launch small model rockets while learning fundamental physics principles.",
      "category": "Science",
      "type": "ONE_TIME",
      "gradeRange": "5th–7th",
      "minAge": 10,
      "maxAge": 12,
      "price": 49.99,
      "nextSession": "2025-07-18T16:00:00Z"
    },
    {
      "id": 2,
      "title": "Science Explorers",
      "description": "Hands-on experiments in biology, chemistry, and physics designed to spark curiosity and critical thinking.",
      "category": "Science",
      "type": "COURSE",
      "gradeRange": "5th–7th",
      "minAge": 10,
      "maxAge": 12,
      "price": 59.99,
      "nextSession": "2025-07-25T14:00:00Z"
    },
    {
      "id": 24,
      "title": "Physics Olympiad Prep",
      "description": "Challenge yourself with advanced physics problems and exam strategies.",
      "category": "Science",
      "type": "COURSE",
      "gradeRange": "10th–12th",
      "minAge": 15,
      "maxAge": 18,
      "price": 119.99,
      "nextSession": "2025-08-08T18:00:00Z"
    }
  ]
}
```

- **Examples**:

```bash
# All courses
curl -X GET "http://localhost:8080/api/search"

# Filter by category
curl -X GET "http://localhost:8080/api/search?category=Math"

# Age range
curl -X GET "http://localhost:8080/api/search?minAge=8&maxAge=12"

# Price range
curl -X GET "http://localhost:8080/api/search?minPrice=20&maxPrice=100"

# Keyword search
curl -X GET "http://localhost:8080/api/search?q=physics"

# Date filter
curl -X GET "http://localhost:8080/api/search?nextSessionFrom=2025-07-20T10:00:00Z"

# Pagination
curl -X GET "http://localhost:8080/api/search?page=1&size=5"

# Sort by price descending
curl -X GET "http://localhost:8080/api/search?sort=priceDesc"
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/meet/undoschool/
│   │   ├── UndoSchoolApplication.java      # Entry point
│   │   ├── DataLoader.java               # Loads sample data
│   │   ├── config/                       # Elasticsearch config
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── enums/
│   │   ├── model/                        # CourseDocument.java
│   │   ├── repository/                   # ES repository interface
│   │   └── service/                      # SearchService.java
│   └── resources/
│       ├── application.properties
│       └── sample-courses.json
├── test/                                 # Unit & integration tests
└── docker-compose.yml                    # ES setup
pom.xml                                   # Maven config
```
