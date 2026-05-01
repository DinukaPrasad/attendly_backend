# Attendly Backend

Professional, academic-level REST API backend for the Attendly attendance management system. The project implements secure user management, academic programme handling, session scheduling, attendance recording, and notification workflows using Spring Boot with JWT-based authentication and role-based access control.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Branch Status](#branch-status)
- [Key Features](#key-features)
- [Architecture Summary](#architecture-summary)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Development Setup](#development-setup)
  - [Production Setup](#production-setup)
- [Configuration](#configuration)
- [Security Model](#security-model)
- [API Summary](#api-summary)
- [Data Models (Core)](#data-models-core)
- [Response Format](#response-format)
- [Testing](#testing)
- [Profiles](#profiles)

---

## Project Overview

Attendly Backend provides the server-side logic for a university attendance platform. It exposes REST endpoints to manage users, academic programmes, sessions, attendance records, and notifications. The system emphasizes security, clean architecture, and consistent API responses suitable for integration with web or mobile clients.

---

## Branch Status

The `main` branch is intentionally behind the latest work. Use the `Final_modification` branch to see the final output for the assignment. I am still actively developing the project, so I have not merged `Final_modification` into `main`. This branch was created specifically for assignment submission and evaluation.

---

## Key Features

- JWT-based authentication with role claims and stateless authorization
- Role-based access control (ADMIN, LECTURER, STUDENT)
- Public user registration endpoint for onboarding
- Academic programme and session management
- Attendance recording with programme and user validation
- Notification system with unread/read status tracking
- Standardized API response envelope for predictable client parsing
- Structured logging in service layers using SLF4J

---

## Architecture Summary

- **Layered architecture**: Controller → Service → Repository → Entity/DTO
- **Feature-driven modules**: user, programme, session, attendance, notification, healthCheck
- **Global error handling**: centralized exception responses for validation and not-found cases
- **Security filter chain**: JWT auth filter + custom 401/403 JSON handlers
- **Profiles**: dev (H2) and prod (PostgreSQL)

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
| Logging | SLF4J + Logback |
| Utilities | Lombok |

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+ (or use the included `mvnw` wrapper)
- PostgreSQL (for production profile only)

### Development Setup

The dev profile uses an H2 in-memory database (no external DB required).

```bash
git clone <repository-url>
cd attendly_backend
./mvnw spring-boot:run
```

- Base URL: `http://localhost:8080/api`
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:attendlydb`
  - Username: `sa`
  - Password: (leave empty)

### Production Setup

1. Ensure PostgreSQL is running on `localhost:5432` with a database named `attendlydb`.
2. Set the active profile and secrets:

```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_PASSWORD=<postgres-password>
JWT_SECRET=<strong-random-secret>
```

3. Run:

```bash
./mvnw spring-boot:run
```

- Base URL: `http://localhost:1999/api`

---

## Configuration

Primary configuration lives in `src/main/resources/application.yaml` and defines dev/prod profiles.

| Property | Dev Value | Prod Value |
|---|---|---|
| `server.port` | `8080` | `1999` |
| `spring.datasource.url` | `jdbc:h2:mem:attendlydb` | `jdbc:postgresql://localhost:5432/attendlydb` |
| `spring.datasource.username` | `sa` | `postgres` |
| `spring.jpa.hibernate.ddl-auto` | `create-drop` | `validate` |
| `jwt.expiration-ms` | `3600000` (1 hour) | `3600000` (1 hour) |

Important: Always override `jwt.secret` in production via environment variables or a secret manager.

---

## Security Model

- **Authentication**: JWT issued on login; includes role and user claims
- **Authorization**: `@PreAuthorize` on controllers with role checks
- **Public endpoints**: `/api/auth/**`, `/api/health/**`, and `POST /api/users`
- **Custom error handlers**: JSON responses for 401 (unauthorized) and 403 (forbidden)

---

## API Summary

Base URL: `/api`

**Authentication**
- `POST /auth/login`

**Users**
- `POST /users` (public registration)
- `GET /users`
- `GET /users/{id}`
- `PUT /users/{id}`
- `DELETE /users/{id}`

**Programmes**
- `GET /programmes`
- `POST /programmes`
- `PUT /programmes/{id}`
- `DELETE /programmes/{id}`

**Sessions**
- `GET /sessions`
- `POST /sessions`
- `PUT /sessions/{id}`
- `DELETE /sessions/{id}`

**Attendance**
- `POST /attendance`
- `GET /attendance`
- `GET /attendance/{id}`
- `GET /attendance/user/{userId}`
- `GET /attendance/programme/{programmeId}`
- `PUT /attendance/{id}`
- `DELETE /attendance/{id}`

**Notifications**
- `POST /notifications`
- `GET /notifications`
- `GET /notifications/{id}`
- `GET /notifications/recipient/{recipientId}`
- `GET /notifications/sender/{senderId}`
- `GET /notifications/unread/{recipientId}`
- `PUT /notifications/{id}/read`
- `DELETE /notifications/{id}`

**Health Check**
- `GET /health`

---

## Data Models (Core)

**User**
- `id`, `name`, `email`, `password`, `role`

**Programme**
- `id`, `code`, `name`, `description`, `createdAt`, `updatedAt`

**Session**
- `id`, `programme`, `module`, `lecturer`, `title`, `description`, `venue`
- `startTime`, `endTime`, `sessionStatus`, `code`, `attendanceStatus`

**Attendance**
- `id`, `user`, `programme`, `time`, `status`, `createdAt`

**Notification**
- `id`, `sender`, `recipient`, `content`, `datetime`, `status`

---

## Response Format

All endpoints return a consistent response envelope:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "data": {},
  "timestamp": "2026-05-01T10:00:00"
}
```

Error response example:

```json
{
  "success": false,
  "message": "Resource not found",
  "timestamp": "2026-05-01T10:00:00"
}
```

---

## Testing

Run the test suite with:

```bash
./mvnw test
```

---

## Profiles

| Profile | Database | Port | DDL Mode | Use Case |
|---|---|---|---|---|
| `dev` (default) | H2 in-memory | 8080 | `create-drop` | Local development |
| `prod` | PostgreSQL | 1999 | `validate` | Production deployment |

To switch profiles:

```bash
SPRING_PROFILES_ACTIVE=prod
```
