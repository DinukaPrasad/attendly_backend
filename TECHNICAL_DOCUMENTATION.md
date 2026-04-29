# Attendly Backend - Technical Documentation

## Table of Contents

1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Dependencies & Tech Stack Details](#dependencies--tech-stack-details)
5. [Configuration](#configuration)
6. [Database](#database)
7. [API Endpoints](#api-endpoints)
8. [Authentication & Security](#authentication--security)
9. [Business Logic Summary](#business-logic-summary)
10. [Key Design Decisions](#key-design-decisions)
11. [Limitations & Known Issues](#limitations--known-issues)

---

## Project Overview

**Attendly Backend** is a RESTful API server for the Attendly attendance management system, designed for educational institutions. The application provides endpoints to manage academic programs, sessions, users, and authentication. Built with Spring Boot and secured with JWT tokens, it supports both development (H2 in-memory) and production (PostgreSQL) database configurations.

### Purpose

Attendly Backend provides server-side logic for an attendance tracking platform that enables:
- User registration and management with role-based access
- Academic program/course management
- Session (lecture/class) scheduling and tracking
- Health monitoring endpoints

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| **Language** | Java 21 | Core programming language |
| **Framework** | Spring Boot | Application framework & auto-configuration |
| **Build Tool** | Apache Maven | Project management and build automation |
| **ORM** | Spring Data JPA / Hibernate | Object-relational mapping and persistence |
| **Database (Dev)** | H2 | In-memory database for rapid local development |
| **Database (Prod)** | PostgreSQL | Production-grade relational database |
| **Authentication** | JWT (JJWT 0.12.6) | Token-based security & stateless auth |
| **Validation** | Jakarta Validation | Bean validation annotations |
| **Lombok** | Latest | Reduce Java boilerplate code |
| **Spring DevTools** | Latest | Development productivity (hot reload) |
| **H2 Console** | Latest | Web UI for inspecting in-memory database |

---

## Project Structure

```
attendly_backend/
├── src/
│   ├── main/
│   │   ├── java/com/attendly/attendly_backend/
│   │   │   ├── AttendlyBackendApplication.java          # Main Spring Boot entry point
│   │   │   ├── config/                                  # Empty - ready for app configuration
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java          # Centralized exception handling
│   │   │   ├── modules/                                 # Feature modules
│   │   │   │   ├── healthCheck/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── HealthController.java        # Health status endpoint
│   │   │   │   │   └── service/
│   │   │   │   ├── programme/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── ProgrammeController.java     # Programme CRUD endpoints
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── CreateProgrammeRequest.java  # Request DTO with validation
│   │   │   │   │   │   └── ProgrammeResponse.java       # Response DTO
│   │   │   │   │   ├── model/
│   │   │   │   │   │   └── Programme.java               # JPA Entity
│   │   │   │   │   ├── repo/
│   │   │   │   │   │   └── ProgrammeRepository.java     # Data access layer
│   │   │   │   │   └── service/
│   │   │   │   │       └── ProgrammeService.java        # Business logic
│   │   │   │   ├── session/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── SessionController.java       # Session CRUD endpoints
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── SessionRequest.java          # Request DTO with validation
│   │   │   │   │   │   └── SessionResponse.java         # Response DTO
│   │   │   │   │   ├── model/
│   │   │   │   │   │   └── Session.java                 # JPA Entity
│   │   │   │   │   ├── repo/
│   │   │   │   │   │   └── SessionRepository.java       # Data access layer
│   │   │   │   │   └── service/
│   │   │   │   │       └── SessionService.java          # Business logic
│   │   │   │   └── user/
│   │   │   │       ├── controller/
│   │   │   │       │   └── UserController.java          # User CRUD endpoints
│   │   │   │       ├── dto/
│   │   │   │       │   ├── CreateUserRequest.java       # Create user validation DTO
│   │   │   │       │   ├── UpdateUserRequest.java       # Update user validation DTO
│   │   │   │       │   └── UserResponse.java            # Response DTO
│   │   │   │       ├── model/
│   │   │   │       │   └── User.java                    # JPA Entity
│   │   │   │       ├── repo/
│   │   │   │       │   └── UserRepository.java          # Data access layer
│   │   │   │       └── service/
│   │   │   │           └── UserService.java             # Business logic
│   │   │   ├── security/
│   │   │   │   └── JwtTokenProvider.java                # JWT token generation & validation
│   │   │   └── utility/
│   │   │       └── ApiResponse.java                     # Standardized API response wrapper
│   │   └── resources/
│   │       ├── application.yaml                         # Application configuration (dev & prod profiles)
│   │       ├── static/                                  # Static web resources (empty)
│   │       └── templates/                               # HTML templates (empty)
│   └── test/
│       └── java/com/attendly/attendly_backend/
│           └── AttendlyBackendApplicationTests.java     # Basic Spring Boot test
├── pom.xml                                               # Maven project configuration & dependencies
├── mvnw / mvnw.cmd                                       # Maven wrapper scripts
└── README.md                                             # Basic project readme
```

### Key Directories Explained

- **`modules/`** — Feature-driven structure; each subdirectory is an independent feature with controller, service, repository, model, and DTO layers
- **`exception/`** — Global exception handling centralized in one `RestControllerAdvice` class
- **`security/`** — JWT token provider for stateless authentication
- **`utility/`** — Shared utility classes like the standardized `ApiResponse` wrapper
- **`config/`** — Empty folder reserved for future Spring configuration classes (e.g., CORS, security filters)

---

## Dependencies & Tech Stack Details

### Core Spring Boot Starters

| Dependency | Version | Purpose |
|---|---|---|
| **spring-boot-starter-parent** | 4.0.3 | Parent POM providing version management & common configs |
| **spring-boot-starter-data-jpa** | [inherited] | JPA support with Hibernate ORM for database persistence |
| **spring-boot-starter-webmvc** | [inherited] | Spring MVC framework for building REST endpoints |
| **spring-boot-starter-validation** | [inherited] | Jakarta Bean Validation for request/DTO validation |

### Security & Authentication

| Dependency | Version | Purpose |
|---|---|---|
| **jjwt-api** | 0.12.6 | JWT public API for token creation, signing, and parsing |
| **jjwt-impl** | 0.12.6 (runtime) | Runtime implementation required by jjwt-api |
| **jjwt-jackson** | 0.12.6 (runtime) | Jackson JSON serialization for JWT claims |

### Database & ORM

| Dependency | Version | Purpose |
|---|---|---|
| **spring-boot-h2console** | [inherited] | H2 web console for browsing in-memory DB (dev only) |
| **h2** | [inherited] (runtime) | Embedded in-memory database for development & rapid testing |
| **postgresql** | [inherited] (runtime) | PostgreSQL JDBC driver for production database |

### Development & Utilities

| Dependency | Version | Purpose |
|---|---|---|
| **lombok** | [inherited] | Reduces boilerplate: auto-generates getters, setters, constructors, toString, etc. |
| **spring-boot-devtools** | [inherited] (runtime, optional) | Auto-restart, LiveReload support for fast iteration during development |

### Testing

| Dependency | Version | Scope | Purpose |
|---|---|---|---|
| **spring-boot-starter-data-jpa-test** | [inherited] | test | JPA test utilities for repository testing |
| **spring-boot-starter-validation-test** | [inherited] | test | Validation-specific test helpers |
| **spring-boot-starter-webmvc-test** | [inherited] | test | MVC test support for controller testing |

---

## Configuration

### Profile Management

The application supports two Spring profiles defined in `application.yaml`:

#### Development Profile (`dev`)

**Active by default.** Optimized for rapid local development.

```yaml
spring:
  profiles:
    active: dev

---
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:h2:mem:attendlydb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop  # Recreates schema on every restart
  devtools:
    restart:
      enabled: true

server:
  port: 8080

jwt:
  secret: dev-attendly-super-secret-key-change-in-production
  expiration-ms: 3600000  # 1 hour
```

**Key Points:**
- **Database**: H2 in-memory (resets on app restart)
- **Schema**: Auto-created and dropped on startup (`create-drop`)
- **Port**: 8080
- **DevTools**: Enabled for auto-restart on file changes
- **JWT Secret**: Insecure development key (MUST be changed in production)
- **JWT Expiration**: 3,600,000 ms = 1 hour

#### Production Profile (`prod`)

For deployment in a production environment with persistent PostgreSQL database.

```yaml
---
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:postgresql://localhost:5432/attendlydb
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: 
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate  # Only validates schema; does NOT create/drop

server:
  port: 1999
```

**Key Points:**
- **Database**: PostgreSQL at `localhost:5432`
- **Schema**: Validation only (`validate`) — manual migration required
- **Port**: 1999 (non-standard to avoid conflicts)
- **DevTools**: Disabled for production

### Environment Variables (Required for Production)

| Variable | Purpose | Default |
|---|---|---|
| `spring.datasource.password` | PostgreSQL password | None (must be set) |
| `jwt.secret` | JWT signing key | Must be overridden |

---

## Database

### Database Type & Version

| Environment | Type | Version | Connection |
|---|---|---|---|
| **Development** | H2 | In-memory | `jdbc:h2:mem:attendlydb` |
| **Production** | PostgreSQL | 9.6+ recommended | `jdbc:postgresql://localhost:5432/attendlydb` |

### Tables & Schema

#### 1. `users` Table

| Column | Data Type | Constraints | Purpose |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| `name` | VARCHAR(255) | NOT NULL | User's full name |
| `email` | VARCHAR(255) | NOT NULL | User's email address |
| `password` | VARCHAR(255) | NOT NULL | Hashed/plaintext password (stored) |
| `role` | VARCHAR(255) | NOT NULL | User role (e.g., "ADMIN", "USER", "LECTURER") |

**Primary Key:** `id`
**Foreign Keys:** None
**Indexes:** Consider adding on `email` for login queries

**JPA Entity:** [User.java](src/main/java/com/attendly/attendly_backend/modules/user/model/User.java)

#### 2. `programmes` Table

| Column | Data Type | Constraints | Purpose |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique programme identifier |
| `code` | VARCHAR(255) | | Programme code (e.g., "CS101") |
| `name` | VARCHAR(255) | NOT NULL | Programme name (e.g., "Computer Science") |
| `description` | TEXT | | Detailed description of the programme |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT=CURRENT_TIMESTAMP | Auto-set on creation |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT=CURRENT_TIMESTAMP | Auto-updated on modification |

**Primary Key:** `id`
**Foreign Keys:** None
**Relationships:** One programme contains many sessions (logical, not enforced in DB)

**JPA Entity:** [Programme.java](src/main/java/com/attendly/attendly_backend/modules/programme/model/Programme.java)

#### 3. `sessions` Table

| Column | Data Type | Constraints | Purpose |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique session identifier |
| `module` | VARCHAR(255) | | Module/course code |
| `lecturer` | VARCHAR(255) | | Lecturer's name |
| `title` | VARCHAR(255) | NOT NULL | Session title (e.g., "Lecture 1: Intro to Databases") |
| `description` | TEXT | | Session description/agenda |
| `venue` | VARCHAR(255) | | Physical location (e.g., "Room 101") |
| `start_time` | VARCHAR(255) | | Session start time (stored as string; format not enforced) |
| `end_time` | VARCHAR(255) | | Session end time (stored as string; format not enforced) |
| `session_status` | VARCHAR(255) | | Status (e.g., "SCHEDULED", "ONGOING", "COMPLETED") |
| `attendance_status` | BOOLEAN | DEFAULT=false | Whether attendance was recorded |
| `code` | VARCHAR(255) | | Session code/ID (e.g., "CS101-L01") |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT=CURRENT_TIMESTAMP | Auto-set on creation |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT=CURRENT_TIMESTAMP | Auto-updated on modification |

**Primary Key:** `id`
**Foreign Keys:** None (not modeled, but logically relates to Programmes)
**Relationships:** Many sessions belong to one programme

**JPA Entity:** [Session.java](src/main/java/com/attendly/attendly_backend/modules/session/model/Session.java)

### Entity-Relationship (ER) Summary

```
User (1) ─── * Session
            (logical: sessions are attended by users)

Programme (1) ─── * Session
                  (one programme has many sessions)
```

**Note:** Foreign key relationships are NOT enforced in the current database schema. They exist only as logical relationships in the domain model.

---

## API Endpoints

### Base URL

- **Development**: `http://localhost:8080/api`
- **Production**: `http://localhost:1999/api`

### Request/Response Format

All endpoints return responses wrapped in a standardized `ApiResponse` format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* entity or list */ },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

### Health Check

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/health` | Verify server is running | No |

**Response:**
```json
{
  "status": "healthy",
  "message": "Attendly Backend is running",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

---

### User Management Endpoints

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/users` | Create a new user | No |
| GET | `/api/users` | Retrieve all users | No |
| GET | `/api/users/{id}` | Retrieve user by ID | No |
| PUT | `/api/users/{id}` | Update user by ID | No |
| DELETE | `/api/users/{id}` | Delete user by ID | No |

#### POST `/api/users` — Create User

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePass123",
  "role": "STUDENT"
}
```

**Validation Rules:**
- `name`: Required, non-blank
- `email`: Required, must be valid email format
- `password`: Required, minimum 8 characters
- `role`: Required, non-blank

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "STUDENT"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

**Error Response (400 Bad Request — Validation Failed):**
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": "Email should be valid",
    "password": "Password must be at least 8 characters"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### GET `/api/users` — Retrieve All Users

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "role": "STUDENT"
    },
    {
      "id": 2,
      "name": "Jane Smith",
      "email": "jane@example.com",
      "role": "LECTURER"
    }
  ],
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### GET `/api/users/{id}` — Retrieve User by ID

**Response (200 OK):**
```json
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "STUDENT"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

**Error Response (404 Not Found):**
```json
{
  "success": false,
  "message": "User not found with id: 99",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### PUT `/api/users/{id}` — Update User by ID

**Request Body (all fields optional):**
```json
{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "role": "ADMIN"
}
```

**Validation Rules:**
- At least one field must be provided
- `email`: If provided, must be valid email format
- Fields can be omitted (only provided fields are updated)

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "User updated successfully",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "At least one field must be provided for update",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### DELETE `/api/users/{id}` — Delete User by ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "User deleted successfully",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

**Error Response (404 Not Found):**
```json
{
  "success": false,
  "message": "User not found with id: 99",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

---

### Programme Management Endpoints

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/programmes` | Retrieve all programmes | No |
| POST | `/api/programmes` | Create a new programme | No |
| POST | `/api/programmes/{id}` | Update programme (alias) | No |
| PUT | `/api/programmes/{id}` | Update programme by ID | No |
| DELETE | `/api/programmes/{id}` | Delete programme by ID | No |

#### GET `/api/programmes` — Retrieve All Programmes

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Programmes retrieved successfully",
  "data": [
    {
      "code": "CS101",
      "name": "Introduction to Computer Science",
      "description": "Fundamentals of programming and CS concepts"
    },
    {
      "code": "MATH201",
      "name": "Calculus II",
      "description": "Advanced calculus topics"
    }
  ],
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### POST `/api/programmes` — Create Programme

**Request Body:**
```json
{
  "code": "CS101",
  "name": "Introduction to Computer Science",
  "description": "Fundamentals of programming and CS concepts"
}
```

**Validation Rules:**
- `code`: Required, non-blank
- `name`: Required, non-blank
- `description`: Required, non-blank

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Programme created successfully",
  "data": {
    "code": "CS101",
    "name": "Introduction to Computer Science",
    "description": "Fundamentals of programming and CS concepts"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### PUT `/api/programmes/{id}` — Update Programme by ID

**Request Body (all fields optional):**
```json
{
  "code": "CS101-UPDATED",
  "name": "Advanced CS",
  "description": "Updated description"
}
```

**Validation Rules:**
- At least one field must be provided
- Fields can be omitted (only provided fields are updated)

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Programme updated successfully",
  "data": {
    "code": "CS101-UPDATED",
    "name": "Advanced CS",
    "description": "Updated description"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### DELETE `/api/programmes/{id}` — Delete Programme by ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Programme deleted successfully",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

---

### Session Management Endpoints

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/sessions` | Retrieve all sessions | No |
| POST | `/api/sessions` | Create a new session | No |
| POST | `/api/sessions/{id}` | Update session (alias) | No |
| PUT | `/api/sessions/{id}` | Update session by ID | No |
| DELETE | `/api/sessions/{id}` | Delete session by ID | No |

#### GET `/api/sessions` — Retrieve All Sessions

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Sessions retrieved successfully",
  "data": [
    {
      "module": "CS101",
      "lecturer": "Prof. Alice",
      "title": "Lecture 1: Introduction",
      "description": "Overview of the course",
      "startTime": "09:00",
      "endTime": "10:30",
      "status": "COMPLETED",
      "code": "CS101-L01",
      "venue": "Room 101"
    }
  ],
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### POST `/api/sessions` — Create Session

**Request Body:**
```json
{
  "module": "CS101",
  "lecturer": "Prof. Alice",
  "title": "Lecture 1: Introduction",
  "description": "Overview of the course",
  "startTime": "09:00",
  "endTime": "10:30",
  "sessionStatus": "SCHEDULED",
  "code": "CS101-L01",
  "venue": "Room 101"
}
```

**Validation Rules:**
- All fields are required and non-blank
- No format validation on time fields (stored as strings)

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Session created successfully",
  "data": {
    "module": "CS101",
    "lecturer": "Prof. Alice",
    "title": "Lecture 1: Introduction",
    "description": "Overview of the course",
    "startTime": "09:00",
    "endTime": "10:30",
    "status": "SCHEDULED",
    "code": "CS101-L01",
    "venue": "Room 101"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### PUT `/api/sessions/{id}` — Update Session by ID

**Request Body (all fields optional):**
```json
{
  "sessionStatus": "ONGOING",
  "lecturer": "Prof. Bob"
}
```

**Validation Rules:**
- At least one field must be provided
- Fields can be omitted (only provided fields are updated)

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Session updated successfully",
  "data": {
    "module": "CS101",
    "lecturer": "Prof. Bob",
    "title": "Lecture 1: Introduction",
    "description": "Overview of the course",
    "startTime": "09:00",
    "endTime": "10:30",
    "status": "ONGOING",
    "code": "CS101-L01",
    "venue": "Room 101"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### DELETE `/api/sessions/{id}` — Delete Session by ID

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Session deleted successfully",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

---

## Authentication & Security

### Authentication Mechanism

**Type:** JWT (JSON Web Token) - Stateless, token-based authentication

**Architecture:**
1. Client sends credentials → Receives a signed JWT token
2. Token is included in subsequent requests (typically in `Authorization` header)
3. Server validates token signature and expiration on each request
4. No server-side session storage required (stateless)

### JWT Token Generation

**Provider Class:** [JwtTokenProvider.java](src/main/java/com/attendly/attendly_backend/security/JwtTokenProvider.java)

**Token Structure:**
```
Header.Payload.Signature
```

**Token Creation Process:**
```java
public String generateToken(String subject, Map<String, Object> claims) {
    Instant now = Instant.now();
    Instant expiry = now.plusMillis(expirationMs);  // Default: 3600000 ms = 1 hour

    return Jwts.builder()
            .subject(subject)                        // Usually user ID or email
            .claims(claims)                          // Custom claims (e.g., roles, permissions)
            .issuedAt(Date.from(now))               // Token issue time
            .expiration(Date.from(expiry))          // Token expiration time
            .signWith(signingKey)                   // Sign with HMAC-SHA256
            .compact();                             // Serialize to string
}
```

### JWT Token Validation

**Validation Process:**
```java
public boolean validateToken(String token) {
    try {
        parseClaims(token);  // Parses and verifies signature
        return true;
    } catch (JwtException | IllegalArgumentException ex) {
        return false;  // Signature invalid, expired, or malformed
    }
}
```

**Validation Checks:**
1. Signature verification (using secret key)
2. Expiration time check
3. Format/structure validation

### JWT Configuration

| Property | Value (Dev) | Value (Prod) | Purpose |
|---|---|---|---|
| `jwt.secret` | `dev-attendly-super-secret-key-change-in-production` | **MUST BE CHANGED** | HMAC-SHA256 signing key (256-bit minimum recommended) |
| `jwt.expiration-ms` | 3600000 | 3600000 (recommended) | Token lifetime in milliseconds (1 hour) |

**⚠️ SECURITY WARNING:**
- Production must use a strong, randomly generated secret key
- Never commit real secrets to version control
- Use environment variables or secret management systems (e.g., AWS Secrets Manager, HashiCorp Vault)
- Consider rotating keys periodically

### Role Definitions

Currently, roles are stored as string values in the `users.role` column. Common roles (not enforced by system):

| Role | Purpose |
|---|---|
| `ADMIN` | Full system access, user/programme management |
| `LECTURER` | Can create and manage sessions, view attendance |
| `STUDENT` | Can view assigned sessions and own attendance |

**Implementation Note:** Role-based access control (RBAC) is not yet enforced in endpoints. All endpoints are currently publicly accessible. Authorization logic should be added in a security filter or via `@PreAuthorize` annotations.

### Security Filter Chain

Currently **NOT IMPLEMENTED**. The system does NOT:
- Require JWT tokens in requests
- Validate roles for endpoint access
- Enforce token expiration checks in controllers

**Recommended Implementation:**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Add JWT authentication filter
    // Add authorization rules per endpoint
    // Add CORS configuration
}
```

---

## Business Logic Summary

### User Service

**Class:** [UserService.java](src/main/java/com/attendly/attendly_backend/modules/user/service/UserService.java)

**Key Operations:**
- **`getAllUsers()`** — Fetch all users, convert to response DTOs
- **`getCurrentUser(Long id)`** — Fetch user by ID; throws `NoSuchElementException` if not found
- **`createUser(CreateUserRequest)`** — Create new user with validation; checks for duplicates are NOT implemented
- **`updateUserById(Long id, UpdateUserRequest)`** — Partial update; at least one field required; email validation performed
- **`deleteUserById(Long id)`** — Delete user; throws exception if not found
- **`convertUserToUserResponse(User)`** — DTO mapping; excludes password field from response

**Business Rules:**
- Password is never returned in responses
- Email uniqueness is NOT enforced at database or service level
- Update requires at least one non-blank field

### Programme Service

**Class:** [ProgrammeService.java](src/main/java/com/attendly/attendly_backend/modules/programme/service/ProgrammeService.java)

**Key Operations:**
- **`getAllProgrammes()`** — Fetch all programmes with logging
- **`createProgramme(CreateProgrammeRequest)`** — Create new programme; validates request is not null
- **`updateProgrammeById(Long id, CreateProgrammeRequest)`** — Partial update; at least one field required; logs all operations
- **`deleteProgrammeById(Long id)`** — Delete programme; throws exception if not found; logs operation
- **`convertProgrammeToProgrammeResponse(Programme)`** — DTO mapping

**Business Rules:**
- Timestamps (`createdAt`, `updatedAt`) auto-managed by Hibernate
- Update requires at least one non-blank field
- Logging present for audit trail
- Helper method `isValid(String)` checks if string is not null and not blank

### Session Service

**Class:** [SessionService.java](src/main/java/com/attendly/attendly_backend/modules/session/service/SessionService.java)

**Key Operations:**
- **`getAllSessions()`** — Fetch all sessions with logging
- **`createSession(SessionRequest)`** — Create new session; `attendanceStatus` auto-set to `false`; validates request is not null
- **`updateSessionById(Long id, SessionRequest)`** — Partial update; at least one field required; supports all session fields; logs operations
- **`deleteSessionById(Long id)`** — Delete session; throws exception if not found; logs operation
- **`convertSessionToSessionResponse(Session)`** — DTO mapping

**Business Rules:**
- `attendanceStatus` defaults to `false` on session creation
- Timestamps (`createdAt`, `updatedAt`) auto-managed by Hibernate
- Update requires at least one non-blank field
- Time fields (`startTime`, `endTime`) stored as strings (no time format validation)
- Comprehensive logging for debugging and auditing

---

## Key Design Decisions

### 1. **REST API Architecture**
- **Why:** Industry standard for scalable web services; easy for frontend integration; stateless design
- **How:** Spring MVC with `@RestController`, `@RequestMapping`, standard HTTP methods

### 2. **JWT-Based Authentication (Stateless)**
- **Why:** Reduces server memory overhead; supports horizontal scaling; suitable for microservices
- **How:** `JwtTokenProvider` generates HMAC-SHA256 signed tokens with expiration; clients include in requests
- **Current Limitation:** Not enforced in endpoints yet; filter/security config needed

### 3. **Feature-Driven Module Structure**
- **Why:** Separation of concerns; easy to understand business domains; facilitates parallel development
- **How:** Each module (user, programme, session) has own controller, service, model, DTO, repository
- **Benefit:** Can extract modules into separate microservices later

### 4. **DTOs for Request/Response Separation**
- **Why:** Validation logic separate from domain model; expose only necessary fields; prevent over-fetching
- **How:** `CreateUserRequest`, `UserResponse`, etc.; mapped in service layer
- **Benefit:** Password excluded from responses; flexibility to change entity without affecting API

### 5. **Global Exception Handling**
- **Why:** Centralized error responses; consistent error format across endpoints; cleaner controller code
- **How:** `@RestControllerAdvice` with `@ExceptionHandler` methods for validation, not found, illegal argument, and general exceptions

### 6. **Standardized API Response Wrapper**
- **Why:** Consistent response format for all endpoints; easier client-side parsing; includes metadata (success flag, timestamp)
- **How:** `ApiResponse<T>` generic class with factory methods for success/error responses
- **Benefit:** Flexible payload support via generics; JSON excludes null fields for cleaner output

### 7. **Lombok for Boilerplate Reduction**
- **Why:** Less code to maintain; fewer bugs from manual implementations; faster development
- **How:** `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` annotations
- **Trade-off:** Less explicit code visibility; requires IDE support

### 8. **Profile-Based Configuration (Dev vs Prod)**
- **Why:** Single codebase for multiple environments; easy switching; secrets not in code
- **How:** Spring profiles (dev/prod) with different database, ports, JWT secrets
- **Current Issue:** Secrets still hardcoded in dev profile; should use environment variables

### 9. **Spring Data JPA with Hibernate**
- **Why:** Reduces boilerplate SQL; automatic DDL management; query DSL support
- **How:** `JpaRepository` auto-provides CRUD; Hibernate maps entities to tables; HQL for custom queries
- **Limitation:** No custom queries yet; simple repositories only

---

## Limitations & Known Issues

### 1. **No Authentication Filter**
- **Issue:** JWT tokens are generated but NOT validated in endpoints
- **Impact:** All endpoints are publicly accessible; roles cannot be enforced
- **Fix Needed:** Implement `OncePerRequestFilter` to extract and validate JWT tokens
- **Severity:** 🔴 CRITICAL — Security risk

### 2. **Hardcoded JWT Secret in Development**
- **Issue:** `dev-attendly-super-secret-key-change-in-production` is visible in `application.yaml`
- **Impact:** Not a concern in dev, but production secret must be changed and injected via environment variables
- **Fix Needed:** Use environment variables or secret management system for production
- **Severity:** 🔴 CRITICAL — Security risk

### 3. **No Email Uniqueness Enforcement**
- **Issue:** User emails can be duplicated in the database
- **Impact:** Login by email may be ambiguous; business logic assumes unique emails
- **Fix Needed:** Add unique constraint on `users.email` column; service-layer validation
- **Severity:** 🟡 HIGH — Data integrity issue

### 4. **Time Fields Stored as Strings**
- **Issue:** Session `startTime` and `endTime` stored as VARCHAR; no format validation
- **Impact:** Cannot perform time-based queries (e.g., "sessions between 9am and 10am")
- **Fix Needed:** Change to `LocalTime` or `LocalDateTime` type; add format validation
- **Current Workaround:** Time stored as "HH:MM" format by convention
- **Severity:** 🟡 MEDIUM — Limits query flexibility

### 5. **No Role-Based Access Control (RBAC)**
- **Issue:** Roles are stored but not enforced; all endpoints accessible to all users
- **Impact:** Students can delete users, admins can be created by anyone
- **Fix Needed:** Implement `@PreAuthorize` annotations on endpoints; configure security filter chain
- **Severity:** 🔴 CRITICAL — Authorization bypass

### 6. **No Foreign Key Constraints**
- **Issue:** Programmes and Sessions are independent; no referential integrity enforced
- **Impact:** Sessions can reference non-existent programmes; orphaned records possible
- **Fix Needed:** Add foreign key constraints in database; JPA relationship annotations
- **Severity:** 🟡 MEDIUM — Data consistency issue

### 7. **No Password Hashing**
- **Issue:** User passwords stored in plaintext in requests; no hashing implemented
- **Impact:** Severe security vulnerability; passwords exposed in database
- **Fix Needed:** Use BCryptPasswordEncoder; hash passwords before storage
- **Severity:** 🔴 CRITICAL — Security vulnerability

### 8. **No Input Sanitization**
- **Issue:** String inputs are validated for format but not sanitized against injection attacks
- **Impact:** Potential SQL injection via custom queries (low risk with JPA); XSS if data served to web UI
- **Severity:** 🟡 MEDIUM — Depends on usage

### 9. **H2 Console Enabled in All Profiles**
- **Issue:** `spring-boot-h2console` dependency includes web console endpoint
- **Impact:** In production, the H2 console (if accidentally deployed with H2) would be publicly accessible
- **Current State:** Dev uses H2; prod uses PostgreSQL (so not exposed)
- **Fix Needed:** Conditionally disable H2 console in prod profile; add security rules
- **Severity:** 🟡 LOW — Mitigated by prod using PostgreSQL

### 10. **Limited API Documentation**
- **Issue:** No Swagger/OpenAPI documentation; no API versioning
- **Impact:** Harder for frontend developers to integrate; breaking changes not handled gracefully
- **Fix Needed:** Add Springdoc OpenAPI (Swagger); implement API versioning strategy
- **Severity:** 🟡 MEDIUM — DevOps/integration issue

### 11. **No Update Endpoint Uses POST Instead of PUT**
- **Issue:** `POST /api/programmes/{id}` and `POST /api/sessions/{id}` used for updates (should be PUT)
- **Impact:** RESTful convention violated; confusing API semantics
- **Current State:** PUT endpoints also exist, but POST is redundant
- **Fix Needed:** Remove POST update endpoints; standardize on PUT
- **Severity:** 🟢 LOW — Cosmetic issue; endpoints still work

### 12. **No Pagination or Sorting**
- **Issue:** `getAllUsers()`, `getAllProgrammes()`, `getAllSessions()` return all records
- **Impact:** Performance degradation with large datasets
- **Fix Needed:** Add `Pageable` parameter; return `Page<T>` with limit, offset, sorting
- **Severity:** 🟡 HIGH — Scalability issue

### 13. **No Logging in Controllers**
- **Issue:** Only services have logging; controller layer lacks request/response logging
- **Impact:** Harder to debug API issues; limited audit trail
- **Fix Needed:** Add AOP-based logging interceptor or controller-level logging
- **Severity:** 🟡 MEDIUM — Observability issue

### 14. **No CORS Configuration**
- **Issue:** CORS headers not explicitly configured
- **Impact:** Frontend apps on different domains cannot make API calls
- **Current State:** Default Spring CORS policy may block cross-origin requests
- **Fix Needed:** Configure `WebMvcConfigurer` with CORS mappings for frontend domain
- **Severity:** 🟡 MEDIUM — Frontend integration issue

### 15. **No Database Indexes**
- **Issue:** Only primary key indexed; no indexes on frequently queried fields
- **Impact:** Slow queries on `email` (user lookup), `code` (programme/session lookup)
- **Fix Needed:** Add indexes on `users.email`, `programmes.code`, `sessions.code`
- **Severity:** 🟡 MEDIUM — Performance issue with growth

---

## Appendix: Running the Application

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+** (or use included `mvnw`)
- **PostgreSQL 9.6+** (for production profile only)

### Development Environment

1. **Navigate to project root:**
   ```bash
   cd attendly_backend
   ```

2. **Run with Maven:**
   ```bash
   mvn spring-boot:run
   ```
   
   Or using Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access application:**
   - API: `http://localhost:8080/api`
   - Health check: `http://localhost:8080/api/health`
   - H2 Console: `http://localhost:8080/h2-console` (Username: `sa`, Password: blank)

4. **Build JAR:**
   ```bash
   mvn clean package
   ```

### Production Environment

1. **Set environment variables:**
   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   export SPRING_DATASOURCE_PASSWORD=<postgres-password>
   export JWT_SECRET=<strong-random-key>
   ```

2. **Run JAR:**
   ```bash
   java -jar target/attendly_backend-0.0.1-SNAPSHOT.jar
   ```

3. **Access application:**
   - API: `http://localhost:1999/api`

---

**Document Version:** 1.0  
**Last Updated:** April 29, 2026  
**Generated for:** Attendly Backend v0.0.1-SNAPSHOT
