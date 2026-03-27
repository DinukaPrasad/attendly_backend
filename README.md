# Attendly Backend

A RESTful API backend for the **Attendly** attendance management system, built with Spring Boot. Designed to manage academic programmes, sessions, and users with JWT-based authentication.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running in Development](#running-in-development)
  - [Running in Production](#running-in-production)
- [Configuration](#configuration)
- [API Reference](#api-reference)
  - [Authentication](#authentication)
  - [Users](#users)
  - [Programmes](#programmes)
  - [Sessions](#sessions)
  - [Health Check](#health-check)
- [Data Models](#data-models)
- [Response Format](#response-format)
- [Profiles](#profiles)

---

## Overview

Attendly Backend provides the server-side logic for an attendance tracking platform used in educational institutions. It exposes a set of REST endpoints to manage:

- **Users** — registration, retrieval, updates, and deletion
- **Authentication** — JWT-based login flow with role claims
- **Programmes** — academic programme records
- **Sessions** — lecture/session scheduling and status tracking

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.3 |
| Build Tool | Apache Maven |
| ORM | Spring Data JPA / Hibernate |
| Database (dev) | H2 (in-memory) |
| Database (prod) | PostgreSQL |
| Authentication | JWT (JJWT 0.12.6) |
| Validation | Jakarta Bean Validation |
| Utilities | Lombok |

---

## Project Structure

```
src/main/java/com/attendly/attendly_backend/
├── AttendlyBackendApplication.java
├── exception/
│   └── GlobalExceptionHandler.java
├── modules/
│   ├── healthCheck/
│   │   └── HealthController.java
│   ├── user/
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   └── UserController.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   └── UserService.java
│   │   ├── model/
│   │   │   └── User.java
│   │   ├── dto/
│   │   │   ├── AuthResponse.java
│   │   │   ├── CreateUserRequest.java
│   │   │   ├── LoginRequest.java
│   │   │   ├── UpdateUserRequest.java
│   │   │   └── UserResponse.java
│   │   └── repo/
│   │       └── UserRepository.java
│   ├── programme/
│   │   ├── controller/
│   │   │   └── ProgrammeController.java
│   │   ├── service/
│   │   │   └── ProgrammeService.java
│   │   ├── model/
│   │   │   └── Programme.java
│   │   ├── dto/
│   │   │   ├── CreateProgrammeRequest.java
│   │   │   └── ProgrammeResponse.java
│   │   └── repo/
│   │       └── ProgrammeRepository.java
│   └── session/
│       ├── controller/
│       │   └── SessionController.java
│       ├── service/
│       │   └── SessionService.java
│       ├── model/
│       │   └── Session.java
│       ├── dto/
│       │   ├── SessionRequest.java
│       │   └── SessionResponse.java
│       └── repo/
│           └── SessionRepository.java
├── security/
│   └── JwtTokenProvider.java
└── utility/
    └── ApiResponse.java
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+ (or use the included `mvnw` wrapper)
- PostgreSQL (for production profile only)

### Running in Development

The dev profile uses an H2 in-memory database — no external database setup required.

```bash
# Clone the repository
git clone <repository-url>
cd attendly_backend

# Run using the Maven wrapper
./mvnw spring-boot:run
```

The server starts on **port 8080** by default.

Access the H2 console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:attendlydb`
- Username: `sa`
- Password: *(leave empty)*

### Running in Production

1. Ensure PostgreSQL is running on `localhost:5432` with a database named `attendlydb`.

2. Set the active profile to `prod`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

Or set the environment variable:

```bash
export SPRING_PROFILES_ACTIVE=prod
./mvnw spring-boot:run
```

The production server starts on **port 1999**.

---

## Configuration

All configuration is in `src/main/resources/application.yaml`.

| Property | Dev Value | Prod Value |
|---|---|---|
| `server.port` | `8080` | `1999` |
| `spring.datasource.url` | `jdbc:h2:mem:attendlydb` | `jdbc:postgresql://localhost:5432/attendlydb` |
| `spring.datasource.username` | `sa` | `postgres` |
| `spring.jpa.hibernate.ddl-auto` | `create-drop` | `validate` |
| `jwt.expiration` | `3600000` (1 hour) | `3600000` (1 hour) |

> **Important:** Replace `jwt.secret` with a strong, unique secret before deploying to production.

---

## API Reference

All endpoints are prefixed with `/api`. All responses follow the [standard response format](#response-format).

### Authentication

#### Login

```
POST /api/auth/login
```

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "<JWT>",
    "role": "ADMIN",
    "message": "Login successful"
  },
  "timestamp": "2026-03-27T10:00:00"
}
```

---

### Users

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users` | Create a new user |
| `GET` | `/api/users` | Get all users |
| `GET` | `/api/users/{id}` | Get a user by ID |
| `PUT` | `/api/users/{id}` | Update a user |
| `DELETE` | `/api/users/{id}` | Delete a user |

**Create User — Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securepassword",
  "role": "STUDENT"
}
```

**Update User — Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "role": "LECTURER"
}
```

---

### Programmes

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/programmes` | Get all programmes |
| `POST` | `/api/programmes` | Create a programme |
| `PUT` | `/api/programmes/{id}` | Update a programme |
| `DELETE` | `/api/programmes/{id}` | Delete a programme |

**Create / Update Programme — Request Body:**
```json
{
  "code": "CS101",
  "name": "Computer Science",
  "description": "Bachelor of Science in Computer Science"
}
```

---

### Sessions

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/sessions` | Get all sessions |
| `POST` | `/api/sessions` | Create a session |
| `PUT` | `/api/sessions/{id}` | Update a session |
| `DELETE` | `/api/sessions/{id}` | Delete a session |

**Create / Update Session — Request Body:**
```json
{
  "module": "Introduction to Algorithms",
  "lecturer": "Dr. Smith",
  "title": "Lecture 01 — Sorting Algorithms",
  "description": "Overview of sorting algorithms and their time complexity",
  "venue": "Room A101",
  "startTime": "09:00",
  "endTime": "11:00",
  "sessionStatus": "SCHEDULED",
  "code": "CS101-L01"
}
```

---

### Health Check

```
GET /api/health
```

Returns the current health status of the application.

---

## Data Models

### User

| Field | Type | Notes |
|---|---|---|
| `id` | Long | Auto-generated primary key |
| `name` | String | Full name |
| `email` | String | Unique identifier for login |
| `password` | String | User password |
| `role` | String | e.g., `ADMIN`, `LECTURER`, `STUDENT` |

### Programme

| Field | Type | Notes |
|---|---|---|
| `id` | Long | Auto-generated primary key |
| `code` | String | Unique programme code |
| `name` | String | Programme name |
| `description` | String | Programme description |
| `createdAt` | LocalDateTime | Set on creation, not updatable |
| `updatedAt` | LocalDateTime | Updated on every save |

### Session

| Field | Type | Notes |
|---|---|---|
| `id` | Long | Auto-generated primary key |
| `module` | String | Module name |
| `lecturer` | String | Lecturer name |
| `title` | String | Session title |
| `description` | String | Session description |
| `venue` | String | Physical or virtual location |
| `startTime` | String | Session start time |
| `endTime` | String | Session end time |
| `sessionStatus` | String | e.g., `SCHEDULED`, `ONGOING`, `COMPLETED` |
| `code` | String | Session code |
| `attendanceStatus` | Boolean | Whether attendance has been taken |
| `createdAt` | LocalDateTime | Set on creation |
| `updatedAt` | LocalDateTime | Updated on every save |

---

## Response Format

All endpoints return a consistent response envelope:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {},
  "timestamp": "2026-03-27T10:00:00"
}
```

| Field | Type | Description |
|---|---|---|
| `success` | Boolean | `true` for successful operations, `false` on errors |
| `message` | String | Human-readable status message |
| `data` | Object / Array / null | Returned payload |
| `timestamp` | String | ISO-8601 datetime of the response |

### Error Response Example

```json
{
  "success": false,
  "message": "Resource not found",
  "data": null,
  "timestamp": "2026-03-27T10:00:00"
}
```

---

## Profiles

| Profile | Database | Port | DDL Mode | Use Case |
|---|---|---|---|---|
| `dev` *(default)* | H2 in-memory | 8080 | `create-drop` | Local development |
| `prod` | PostgreSQL | 1999 | `validate` | Production deployment |

To switch profiles, set the environment variable:

```bash
export SPRING_PROFILES_ACTIVE=prod
```

Or pass it as a JVM argument:

```bash
java -Dspring.profiles.active=prod -jar attendly_backend.jar
```
