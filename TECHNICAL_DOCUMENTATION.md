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
12. [Changelog](#changelog)

---

## Project Overview

**Attendly Backend** is a RESTful API server for the Attendly attendance management system, designed for educational institutions. The application provides endpoints to manage academic programs, sessions, users, and authentication. Built with Spring Boot and secured with JWT tokens and Spring Security, it supports both development (H2 in-memory) and production (PostgreSQL) database configurations.

### Purpose

Attendly Backend provides server-side logic for an attendance tracking platform that enables:
- User registration and management with role-based access control (RBAC)
- Academic program/course management
- Session (lecture/class) scheduling and tracking with typed time fields
- Attendance recording and querying per user and programme
- Notification sending and inbox management between users
- Secure BCrypt-hashed password storage
- JWT-enforced authentication on all protected endpoints
- Health monitoring endpoints

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| **Language** | Java 21 | Core programming language |
| **Framework** | Spring Boot 4.0.3 | Application framework & auto-configuration |
| **Build Tool** | Apache Maven | Project management and build automation |
| **ORM** | Spring Data JPA / Hibernate | Object-relational mapping and persistence |
| **Security** | Spring Security | Authentication filter chain, RBAC, BCrypt |
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
│   │   │   ├── AttendlyBackendApplication.java          # Main entry point; @EnableMethodSecurity
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java                  # Spring Security filter chain & BCrypt bean
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java          # Centralized exception handling
│   │   │   ├── modules/                                 # Feature modules
│   │   │   │   ├── attendance/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── AttendanceController.java    # Attendance CRUD; role-protected
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── CreateAttendanceRequest.java # Request DTO; userId, programmeId, time, status
│   │   │   │   │   │   └── AttendanceResponse.java      # Response DTO; @Builder; @JsonFormat times
│   │   │   │   │   ├── model/
│   │   │   │   │   │   └── Attendance.java              # JPA Entity; @ManyToOne User & Programme
│   │   │   │   │   ├── repo/
│   │   │   │   │   │   └── AttendanceRepository.java    # findByUserId, findByProgrammeId
│   │   │   │   │   └── service/
│   │   │   │   │       └── AttendanceService.java       # Business logic; FK validation; status update
│   │   │   │   ├── healthCheck/
│   │   │   │   │   └── controller/
│   │   │   │   │       └── HealthController.java        # Health status endpoint (public)
│   │   │   │   ├── notification/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── NotificationController.java  # Notification CRUD; read/unread; role-protected
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── CreateNotificationRequest.java # Request DTO; senderId, recipientId, content
│   │   │   │   │   │   └── NotificationResponse.java    # Response DTO; @Builder; @JsonFormat datetime
│   │   │   │   │   ├── model/
│   │   │   │   │   │   └── Notification.java            # JPA Entity; @ManyToOne sender & recipient (User)
│   │   │   │   │   ├── repo/
│   │   │   │   │   │   └── NotificationRepository.java  # findByRecipientId, findBySenderId, findByRecipientIdAndStatus
│   │   │   │   │   └── service/
│   │   │   │   │       └── NotificationService.java     # Business logic; status UNREAD/READ; FK validation
│   │   │   │   ├── programme/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── ProgrammeController.java     # Programme CRUD; role-protected
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── CreateProgrammeRequest.java  # Request DTO with validation
│   │   │   │   │   │   └── ProgrammeResponse.java       # Response DTO
│   │   │   │   │   ├── model/
│   │   │   │   │   │   └── Programme.java               # JPA Entity; @OneToMany sessions & attendance
│   │   │   │   │   ├── repo/
│   │   │   │   │   │   └── ProgrammeRepository.java     # Data access layer
│   │   │   │   │   └── service/
│   │   │   │   │       └── ProgrammeService.java        # Business logic
│   │   │   │   ├── session/
│   │   │   │   │   ├── controller/
│   │   │   │   │   │   └── SessionController.java       # Session CRUD; role-protected
│   │   │   │   │   ├── dto/
│   │   │   │   │   │   ├── SessionRequest.java          # Request DTO; programmeId + LocalDateTime times
│   │   │   │   │   │   └── SessionResponse.java         # Response DTO; LocalDateTime times
│   │   │   │   │   ├── model/
│   │   │   │   │   │   └── Session.java                 # JPA Entity; @ManyToOne Programme
│   │   │   │   │   ├── repo/
│   │   │   │   │   │   └── SessionRepository.java       # Data access layer
│   │   │   │   │   └── service/
│   │   │   │   │       └── SessionService.java          # Business logic; programme FK validation
│   │   │   │   └── user/
│   │   │   │       ├── controller/
│   │   │   │       │   ├── AuthController.java          # Login endpoint (public)
│   │   │   │       │   └── UserController.java          # User CRUD; LECTURER-only
│   │   │   │       ├── dto/
│   │   │   │       │   ├── AuthResponse.java            # JWT token response DTO
│   │   │   │       │   ├── CreateUserRequest.java       # Create user validation DTO
│   │   │   │       │   ├── LoginRequest.java            # Login credentials DTO
│   │   │   │       │   ├── UpdateUserRequest.java       # Update user validation DTO
│   │   │   │       │   └── UserResponse.java            # Response DTO (no password)
│   │   │   │       ├── model/
│   │   │   │       │   └── User.java                    # JPA Entity; email UNIQUE NOT NULL
│   │   │   │       ├── repo/
│   │   │   │       │   └── UserRepository.java          # findByEmail (Optional), existsByEmail
│   │   │   │       └── service/
│   │   │   │           ├── AuthService.java             # Login; BCrypt password verification
│   │   │   │           └── UserService.java             # CRUD; email duplicate check; BCrypt hash
│   │   │   ├── security/
│   │   │   │   ├── JwtAuthFilter.java                   # OncePerRequestFilter; validates JWT per request
│   │   │   │   └── JwtTokenProvider.java                # JWT generation, validation, claim extraction
│   │   │   └── utility/
│   │   │       ├── ApiResponse.java                     # Standardized API response wrapper
│   │   │       └── LocationUtils.java                   # Haversine distance & range utilities
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
- **`security/`** — JWT token provider and per-request authentication filter
- **`utility/`** — Shared utility classes: `ApiResponse` wrapper and `LocationUtils` (Haversine formula)
- **`config/`** — Spring Security configuration: filter chain, BCrypt bean, authorization rules

---

## Dependencies & Tech Stack Details

### Core Spring Boot Starters

| Dependency | Version | Purpose |
|---|---|---|
| **spring-boot-starter-parent** | 4.0.3 | Parent POM providing version management & common configs |
| **spring-boot-starter-data-jpa** | [inherited] | JPA support with Hibernate ORM for database persistence |
| **spring-boot-starter-webmvc** | [inherited] | Spring MVC framework for building REST endpoints |
| **spring-boot-starter-validation** | [inherited] | Jakarta Bean Validation for request/DTO validation |
| **spring-boot-starter-logging** | [inherited] | SLF4J + Logback for structured logging with @Slf4j |

### Security & Authentication

| Dependency | Version | Purpose |
|---|---|---|
| **spring-boot-starter-security** | [inherited] | Spring Security filter chain, BCryptPasswordEncoder, method security |
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
  application:
    name: attendly_backend
    version: 0.0.1
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
| `email` | VARCHAR(255) | **UNIQUE, NOT NULL** | User's email address — uniqueness enforced at DB and service level |
| `password` | VARCHAR(255) | NOT NULL | **BCrypt-hashed** password (never stored as plaintext) |
| `role` | VARCHAR(255) | NOT NULL | User role (`STUDENT`, `LECTURER`) |

**Primary Key:** `id`
**Unique Constraints:** `email`
**Foreign Keys:** None

**JPA Entity:** [User.java](src/main/java/com/attendly/attendly_backend/modules/user/model/User.java)

#### 2. `programmes` Table

| Column | Data Type | Constraints | Purpose |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique programme identifier |
| `code` | VARCHAR(255) | | Programme code (e.g., `CS101`) |
| `name` | VARCHAR(255) | NOT NULL | Programme name (e.g., `Computer Science`) |
| `description` | TEXT | | Detailed description of the programme |
| `created_at` | TIMESTAMP | NOT NULL, non-updatable | Auto-set on creation via Hibernate `@CreationTimestamp` |
| `updated_at` | TIMESTAMP | NOT NULL | Auto-updated on modification via Hibernate `@UpdateTimestamp` |

**Primary Key:** `id`
**Foreign Keys:** None
**Relationships:** One programme has many sessions (`@OneToMany` — enforced via FK on `sessions.programme_id`)

**JPA Entity:** [Programme.java](src/main/java/com/attendly/attendly_backend/modules/programme/model/Programme.java)

#### 3. `sessions` Table

| Column | Data Type | Constraints | Purpose |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique session identifier |
| `programme_id` | BIGINT | **FOREIGN KEY → programmes.id, NOT NULL** | Programme this session belongs to |
| `module` | VARCHAR(255) | | Module/course code |
| `lecturer` | VARCHAR(255) | | Lecturer's name |
| `title` | VARCHAR(255) | NOT NULL | Session title |
| `description` | TEXT | | Session description/agenda |
| `venue` | VARCHAR(255) | | Physical location (e.g., `Room 101`) |
| `start_time` | **TIMESTAMP** | **NOT NULL** | Session start — stored as `LocalDateTime` |
| `end_time` | **TIMESTAMP** | **NOT NULL** | Session end — stored as `LocalDateTime` |
| `session_status` | VARCHAR(255) | | Status (`SCHEDULED`, `ONGOING`, `COMPLETED`) |
| `attendance_status` | BOOLEAN | DEFAULT=false | Whether attendance was recorded |
| `code` | VARCHAR(255) | | Session code (e.g., `CS101-L01`) |
| `created_at` | TIMESTAMP | NOT NULL, non-updatable | Auto-set on creation via JPA `@PrePersist` |
| `updated_at` | TIMESTAMP | NOT NULL | Auto-updated on modification via JPA `@PreUpdate` |

**Primary Key:** `id`
**Foreign Keys:** `programme_id` → `programmes(id)` — enforced; sessions cannot exist without a valid programme

**JPA Entity:** [Session.java](src/main/java/com/attendly/attendly_backend/modules/session/model/Session.java)

#### 4. `attendance` Table

| Column | Data Type | Constraints | Purpose |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique attendance record identifier |
| `user_id` | BIGINT | **FOREIGN KEY → users.id, NOT NULL** | The student whose attendance is recorded |
| `programme_id` | BIGINT | **FOREIGN KEY → programmes.id, NOT NULL** | The programme the attendance belongs to |
| `time` | TIMESTAMP | NOT NULL | The recorded attendance timestamp (`LocalDateTime`) |
| `status` | VARCHAR(255) | | Attendance status (e.g., `PRESENT`, `ABSENT`, `LATE`) |
| `created_at` | TIMESTAMP | non-updatable | Auto-set on creation via Hibernate `@CreationTimestamp` |

**Primary Key:** `id`
**Foreign Keys:** `user_id` → `users(id)`, `programme_id` → `programmes(id)`

**JPA Entity:** [Attendance.java](src/main/java/com/attendly/attendly_backend/modules/attendance/model/Attendance.java)

#### 5. `notifications` Table

| Column | Data Type | Constraints | Purpose |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique notification identifier |
| `sender_id` | BIGINT | **FOREIGN KEY → users.id, NOT NULL** | User who sent the notification |
| `recipient_id` | BIGINT | **FOREIGN KEY → users.id, NOT NULL** | User who receives the notification |
| `content` | VARCHAR(255) | NOT NULL | Notification message body |
| `datetime` | TIMESTAMP | non-updatable | Auto-set on creation via Hibernate `@CreationTimestamp` |
| `status` | VARCHAR(255) | NOT NULL | Read status — defaults to `UNREAD`; set to `READ` via mark-as-read endpoint |

**Primary Key:** `id`
**Foreign Keys:** `sender_id` → `users(id)`, `recipient_id` → `users(id)`

**JPA Entity:** [Notification.java](src/main/java/com/attendly/attendly_backend/modules/notification/model/Notification.java)

### Entity-Relationship (ER) Summary

```
Programme (1) ──────────────── * Session
              programme_id FK
              (cascade ALL, fetch LAZY)

Programme (1) ──────────────── * Attendance
              programme_id FK
              (cascade ALL, fetch LAZY)

User (1) ──────────────────── * Attendance
              user_id FK

User (1) ──────────────────── * Notification (as sender)
              sender_id FK

User (1) ──────────────────── * Notification (as recipient)
              recipient_id FK
```

**Note:** All FK relationships are enforced at the database level via `@JoinColumn(nullable = false)`. The `Programme` entity carries two `@OneToMany` collections: `sessions` and `attendance`, both with `CascadeType.ALL` and `@ToString.Exclude` to prevent Lombok recursion.

---

## API Endpoints

### Base URL

- **Development**: `http://localhost:8080/api`
- **Production**: `http://localhost:1999/api`

### Authentication Header

All protected endpoints require a valid JWT token in the `Authorization` header:

```
Authorization: Bearer <jwt-token>
```

### Request/Response Format

All endpoints return responses wrapped in a standardized `ApiResponse` format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

**Error response format (e.g., duplicate email, programme not found):**
```json
{
  "success": false,
  "message": "Email already registered",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

---

### Authentication Endpoints (Public)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/auth/login` | Authenticate user, receive JWT | No |

#### POST `/api/auth/login` — Login

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "securePass123"
}
```

**Success Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "message": "Login successful!",
  "role": "LECTURER"
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "An error occurred during login: Invalid email or password",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

---

### Health Check (Public)

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| GET | `/api/health` | Verify server is running | No |

---

### User Management Endpoints

> Most endpoints require a valid JWT token with role `ADMIN` or `LECTURER`. **DELETE operations restricted to ADMIN only.** **User registration (POST) is public.**

| Method | Endpoint | Description | Required Role |
|---|---|---|---|
| POST | `/api/users` | Create a new user (public registration) | **Public (no auth)** |
| GET | `/api/users` | Retrieve all users | ADMIN or LECTURER |
| GET | `/api/users/{id}` | Retrieve user by ID | ADMIN or LECTURER |
| PUT | `/api/users/{id}` | Update user by ID | ADMIN or LECTURER |
| DELETE | `/api/users/{id}` | Delete user by ID | **ADMIN only** |

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
- `email`: Required, must be valid email format; **must be unique**
- `password`: Required, minimum 8 characters; **BCrypt-hashed before storage**
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

**Duplicate Email Response (200 OK with success: false):**
```json
{
  "success": false,
  "message": "Email already registered",
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
    { "id": 1, "name": "John Doe", "email": "john@example.com", "role": "STUDENT" },
    { "id": 2, "name": "Jane Smith", "email": "jane@example.com", "role": "LECTURER" }
  ],
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### PUT `/api/users/{id}` — Update User by ID

**Request Body (all fields optional):**
```json
{
  "name": "John Updated",
  "email": "john.updated@example.com",
  "role": "LECTURER"
}
```

**Validation Rules:**
- At least one field must be provided
- Fields can be omitted (only provided fields are updated)

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "User updated successfully",
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

---

### Programme Management Endpoints

| Method | Endpoint | Description | Required Role |
|---|---|---|---|
| GET | `/api/programmes` | Retrieve all programmes | ADMIN, STUDENT, or LECTURER |
| POST | `/api/programmes` | Create a new programme | ADMIN or LECTURER |
| POST | `/api/programmes/{id}` | Update programme (alias) | ADMIN or LECTURER |
| PUT | `/api/programmes/{id}` | Update programme by ID | ADMIN or LECTURER |
| DELETE | `/api/programmes/{id}` | Delete programme by ID | **ADMIN only** |

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

| Method | Endpoint | Description | Required Role |
|---|---|---|---|
| GET | `/api/sessions` | Retrieve all sessions | ADMIN, STUDENT, or LECTURER |
| POST | `/api/sessions` | Create a new session | ADMIN or LECTURER |
| POST | `/api/sessions/{id}` | Update session (alias) | ADMIN or LECTURER |
| PUT | `/api/sessions/{id}` | Update session by ID | ADMIN or LECTURER |
| DELETE | `/api/sessions/{id}` | Delete session by ID | **ADMIN only** |

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
      "startTime": "2026-04-29T09:00:00",
      "endTime": "2026-04-29T10:30:00",
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
  "programmeId": 1,
  "module": "CS101",
  "lecturer": "Prof. Alice",
  "title": "Lecture 1: Introduction",
  "description": "Overview of the course",
  "startTime": "2026-04-29T09:00:00",
  "endTime": "2026-04-29T10:30:00",
  "sessionStatus": "SCHEDULED",
  "code": "CS101-L01",
  "venue": "Room 101"
}
```

**Validation Rules:**
- `programmeId`: Required (`@NotNull`); referenced programme must exist in the database
- `startTime` / `endTime`: Required; ISO format `yyyy-MM-dd'T'HH:mm:ss`
- All other string fields: Required, non-blank

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
    "startTime": "2026-04-29T09:00:00",
    "endTime": "2026-04-29T10:30:00",
    "status": "SCHEDULED",
    "code": "CS101-L01",
    "venue": "Room 101"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

**Programme Not Found Response (200 OK with success: false):**
```json
{
  "success": false,
  "message": "Programme not found with id: 99",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### PUT `/api/sessions/{id}` — Update Session by ID

**Request Body (all fields optional):**
```json
{
  "sessionStatus": "ONGOING",
  "lecturer": "Prof. Bob",
  "startTime": "2026-04-29T09:15:00"
}
```

**Note:** `programmeId` is not updatable via this endpoint. Only session content fields can be changed after creation.

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

### Attendance Management Endpoints

| Method | Endpoint | Description | Required Role |
|---|---|---|---|
| POST | `/api/attendance` | Record a new attendance entry | ADMIN or STUDENT |
| GET | `/api/attendance` | Retrieve all attendance records | ADMIN or LECTURER |
| GET | `/api/attendance/{id}` | Retrieve attendance record by ID | ADMIN, STUDENT, or LECTURER |
| GET | `/api/attendance/user/{userId}` | Retrieve all attendance for a user | ADMIN, STUDENT, or LECTURER |
| GET | `/api/attendance/programme/{programmeId}` | Retrieve all attendance for a programme | ADMIN or LECTURER |
| PUT | `/api/attendance/{id}` | Update attendance status | ADMIN or LECTURER |
| DELETE | `/api/attendance/{id}` | Delete attendance record | **ADMIN only** |

#### POST `/api/attendance` — Record Attendance

**Request Body:**
```json
{
  "userId": 1,
  "programmeId": 2,
  "time": "2026-04-29T09:05:00",
  "status": "PRESENT"
}
```

**Validation Rules:**
- `userId`: Required (`@NotNull`); referenced user must exist
- `programmeId`: Required (`@NotNull`); referenced programme must exist
- `time`: Required (`@NotNull`); ISO format `yyyy-MM-dd'T'HH:mm:ss`
- `status`: Required, non-blank

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Attendance marked successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "userName": "John Doe",
    "programmeId": 2,
    "programmeName": "Introduction to Computer Science",
    "time": "2026-04-29T09:05:00",
    "status": "PRESENT",
    "createdAt": "2026-04-29T09:05:01"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### PUT `/api/attendance/{id}` — Update Attendance Status

**Request Body:**
```json
{
  "userId": 1,
  "programmeId": 2,
  "time": "2026-04-29T09:05:00",
  "status": "LATE"
}
```

**Note:** Only `status` is applied in the update; other fields in the body are currently ignored by the service.

#### DELETE `/api/attendance/{id}` — Delete Attendance Record

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Attendance deleted successfully",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

---

### Notification Management Endpoints

| Method | Endpoint | Description | Required Role |
|---|---|---|---|
| POST | `/api/notifications` | Send a new notification | ADMIN or LECTURER |
| GET | `/api/notifications` | Retrieve all notifications | ADMIN or LECTURER |
| GET | `/api/notifications/{id}` | Retrieve notification by ID | ADMIN, STUDENT, or LECTURER |
| GET | `/api/notifications/recipient/{recipientId}` | All notifications for a recipient | ADMIN, STUDENT, or LECTURER |
| GET | `/api/notifications/sender/{senderId}` | All notifications sent by a sender | ADMIN or LECTURER |
| GET | `/api/notifications/unread/{recipientId}` | Unread notifications for a recipient | ADMIN, STUDENT, or LECTURER |
| PUT | `/api/notifications/{id}/read` | Mark a notification as read | ADMIN, STUDENT, or LECTURER |
| DELETE | `/api/notifications/{id}` | Delete a notification | **ADMIN only** |

#### POST `/api/notifications` — Send Notification

**Request Body:**
```json
{
  "senderId": 2,
  "recipientId": 1,
  "content": "Your attendance for CS101 has been recorded."
}
```

**Validation Rules:**
- `senderId`: Required (`@NotNull`); referenced user must exist
- `recipientId`: Required (`@NotNull`); referenced user must exist
- `content`: Required, non-blank

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Notification sent successfully",
  "data": {
    "id": 1,
    "senderId": 2,
    "senderName": "Prof. Alice",
    "recipientId": 1,
    "recipientName": "John Doe",
    "content": "Your attendance for CS101 has been recorded.",
    "datetime": "2026-04-29T09:06:00",
    "status": "UNREAD"
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

**Note:** `status` is automatically set to `UNREAD` on creation.

#### PUT `/api/notifications/{id}/read` — Mark as Read

**No request body required.**

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Notification marked as read successfully",
  "data": {
    "id": 1,
    "status": "READ",
    ...
  },
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

#### DELETE `/api/notifications/{id}` — Delete Notification

**Success Response (200 OK):**
```json
{
  "success": true,
  "message": "Notification deleted successfully",
  "timestamp": "2026-04-29T15:30:45.123456"
}
```

---

## Authentication & Security

### Overview

The application uses a layered security model:

1. **BCrypt password hashing** — passwords never stored in plaintext
2. **JWT token issuance** — stateless, signed tokens issued on login
3. **`JwtAuthFilter`** — validates JWT on every protected request
4. **Spring Security filter chain** — enforces public vs protected routes
5. **`@PreAuthorize` RBAC** — method-level role enforcement per endpoint

### Password Hashing

**Class:** [SecurityConfig.java](src/main/java/com/attendly/attendly_backend/config/SecurityConfig.java)

Passwords are hashed with `BCryptPasswordEncoder` before storage and verified using `passwordEncoder.matches()` at login — the plaintext password never touches the database.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**Storage:** `UserService.createUser()` calls `passwordEncoder.encode(request.getPassword())` before saving.
**Verification:** `AuthService.login()` calls `passwordEncoder.matches(rawPassword, storedHash)`.

### JWT Token Generation

**Provider Class:** [JwtTokenProvider.java](src/main/java/com/attendly/attendly_backend/security/JwtTokenProvider.java)

**Available Methods:**

| Method | Description |
|---|---|
| `generateToken(String subject, Map<String,Object> claims)` | Creates a signed JWT with email as subject; embeds `userId`, `role`, `name` as claims |
| `validateToken(String token)` | Returns `true` if signature is valid and token is not expired |
| `getSubject(String token)` | Returns the email (subject) from the token |
| `getClaim(String token, String claimName)` | Extracts a specific custom claim (e.g., `"role"`) |

**Token Claims:**

| Claim | Value | Purpose |
|---|---|---|
| `sub` | User email | Token subject / identifier |
| `userId` | User DB id | For downstream lookups |
| `role` | e.g., `LECTURER` | Used by `JwtAuthFilter` for RBAC |
| `name` | User full name | Informational |
| `iat` | Issued-at timestamp | Token age tracking |
| `exp` | Expiry timestamp | Token expiration (default: 1 hour) |

### JWT Authentication Filter

**Class:** [JwtAuthFilter.java](src/main/java/com/attendly/attendly_backend/security/JwtAuthFilter.java)

Extends `OncePerRequestFilter`. Runs before `UsernamePasswordAuthenticationFilter` on every request.

**Filter Flow:**
```
Request received
    │
    ▼
Read "Authorization" header
    │
    ├── No header / not "Bearer " prefix ──→ skip filter, continue chain
    │
    ▼
Extract token (substring after "Bearer ")
    │
    ▼
jwtTokenProvider.validateToken(token)
    │
    ├── Invalid / expired ──→ skip filter, continue chain (Spring Security rejects)
    │
    ▼
Extract email (getSubject) + role (getClaim "role")
    │
    ▼
Prefix role with "ROLE_" if not already prefixed
    │
    ▼
Set UsernamePasswordAuthenticationToken in SecurityContextHolder
    │
    ▼
Continue filter chain
```

**Note:** `JwtAuthFilter` is not annotated with `@Component`. It is instantiated inline in `SecurityConfig` to prevent Spring Boot from auto-registering it in the servlet filter chain, which would cause it to execute twice per request.

### Custom Authentication & Authorization Error Handlers

**Class:** [SecurityConfig.java](src/main/java/com/attendly/attendly_backend/config/SecurityConfig.java)

The security filter chain includes custom error handlers for enhanced error responses:

**Unauthorized Entry Point** — Handles authentication failures (missing/invalid JWT):
- **HTTP Status:** 401 Unauthorized
- **Response Format:** JSON with error message
- **Example Response:**
  ```json
  {"status":"error","message":"Authentication required","data":null}
  ```

**Access Denied Handler** — Handles authorization failures (valid JWT but insufficient role):
- **HTTP Status:** 403 Forbidden
- **Response Format:** JSON with error message
- **Example Response:**
  ```json
  {"status":"error","message":"Access denied","data":null}
  ```

**Configuration:**
```java
.exceptionHandling(exceptionHandling -> exceptionHandling
        .authenticationEntryPoint(unauthorizedEntryPoint())
        .accessDeniedHandler(accessDeniedHandler()))
```

### Security Filter Chain

**Class:** [SecurityConfig.java](src/main/java/com/attendly/attendly_backend/config/SecurityConfig.java)

```
Public routes (no token required):
  /api/auth/**    → login endpoints
  /api/health/**  → health check

Protected routes (valid JWT required):
  /api/users/**            → ADMIN or LECTURER (POST/GET/PUT); ADMIN only (DELETE)
  /api/programmes/**       → GET: ADMIN, STUDENT, or LECTURER | POST/PUT: ADMIN or LECTURER | DELETE: ADMIN only
  /api/sessions/**         → GET: ADMIN, STUDENT, or LECTURER | POST/PUT: ADMIN or LECTURER | DELETE: ADMIN only
  /api/attendance/**       → POST: ADMIN or STUDENT | GET all/programme/PUT: ADMIN or LECTURER | GET id/user: ADMIN, LECTURER, or STUDENT | DELETE: ADMIN only
  /api/notifications/**    → POST/GET-all/sender: ADMIN or LECTURER | GET id/recipient/unread/read: ADMIN, LECTURER, or STUDENT | DELETE: ADMIN only
```

**Configuration:**
- CSRF disabled (stateless REST API)
- Session management: `STATELESS` (no server-side session)
- `JwtAuthFilter` inserted before `UsernamePasswordAuthenticationFilter`

### Role-Based Access Control (RBAC)

**Activated by:** `@EnableMethodSecurity` on `AttendlyBackendApplication`

Roles are stored in the `users.role` column and embedded in the JWT `role` claim. The filter prefixes the role with `ROLE_` before placing it into the `SecurityContext`, enabling Spring Security's standard `hasRole()` checks.

| Role | Prefix in SecurityContext | Access Level |
|---|---|---|
| `STUDENT` | `ROLE_STUDENT` | Read-only: sessions, programmes; attendance submission; notification inbox |
| `LECTURER` | `ROLE_LECTURER` | Full CRUD: sessions, programmes; all user management; attendance admin; notification sending |
| `ADMIN` | `ROLE_ADMIN` | **Full access to all endpoints** — super-user role for system administration |

**Controller-level annotations:**

| Controller | Method | Annotation |
|---|---|---|
| `UserController` | All non-DELETE | `@PreAuthorize("hasAnyRole('ADMIN','LECTURER')")` (class-level) |
| `UserController` | DELETE | `@PreAuthorize("hasRole('ADMIN')")` (method-level override) |
| `ProgrammeController` | GET | `@PreAuthorize("hasAnyRole('ADMIN','STUDENT','LECTURER')")` |
| `ProgrammeController` | POST, PUT | `@PreAuthorize("hasAnyRole('ADMIN','LECTURER')")` |
| `ProgrammeController` | DELETE | `@PreAuthorize("hasRole('ADMIN')")` |
| `SessionController` | GET | `@PreAuthorize("hasAnyRole('ADMIN','STUDENT','LECTURER')")` |
| `SessionController` | POST, PUT | `@PreAuthorize("hasAnyRole('ADMIN','LECTURER')")` |
| `SessionController` | DELETE | `@PreAuthorize("hasRole('ADMIN')")` |
| `AttendanceController` | POST | `@PreAuthorize("hasAnyRole('ADMIN','STUDENT')")` |
| `AttendanceController` | GET all, GET by programme, PUT | `@PreAuthorize("hasAnyRole('ADMIN','LECTURER')")` |
| `AttendanceController` | GET by id, GET by user | `@PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")` |
| `AttendanceController` | DELETE | `@PreAuthorize("hasRole('ADMIN')")` |
| `NotificationController` | POST, GET all, GET by sender | `@PreAuthorize("hasAnyRole('ADMIN','LECTURER')")` |
| `NotificationController` | GET by id, GET by recipient, GET unread, PUT read | `@PreAuthorize("hasAnyRole('ADMIN','LECTURER','STUDENT')")` |
| `NotificationController` | DELETE | `@PreAuthorize("hasRole('ADMIN')")` |

### JWT Configuration

| Property | Value (Dev) | Value (Prod) | Purpose |
|---|---|---|---|
| `jwt.secret` | `dev-attendly-super-secret-key-change-in-production` | **MUST BE CHANGED** | HMAC-SHA256 signing key (256-bit minimum recommended) |
| `jwt.expiration-ms` | 3600000 | 3600000 (recommended) | Token lifetime in milliseconds (1 hour) |

**⚠️ SECURITY WARNING:**
- Production must use a strong, randomly generated secret key
- Never commit real secrets to version control
- Use environment variables or a secret management system (AWS Secrets Manager, HashiCorp Vault)

---

## Business Logic Summary

### User Service

**Class:** [UserService.java](src/main/java/com/attendly/attendly_backend/modules/user/service/UserService.java)

**Key Operations:**
- **`createUser(CreateUserRequest)`** — Returns `ApiResponse<UserResponse>`; checks `existsByEmail()` first and returns `ApiResponse.error("Email already registered")` if duplicate; hashes password with BCrypt before saving
- **`getAllUsers()`** — Fetch all users, convert to response DTOs (password excluded)
- **`getCurrentUser(Long id)`** — Fetch user by ID; throws `NoSuchElementException` if not found
- **`updateUserById(Long id, UpdateUserRequest)`** — Partial update; at least one field required
- **`deleteUserById(Long id)`** — Delete user; throws exception if not found
- **`convertUserToUserResponse(User)`** — DTO mapping; password field never included in response

**Business Rules:**
- Password is **never** returned in responses
- Email uniqueness enforced at both service level (`existsByEmail`) and database level (`UNIQUE` constraint)
- Passwords are BCrypt-hashed before persistence; compared with `passwordEncoder.matches()` at login

### Auth Service

**Class:** [AuthService.java](src/main/java/com/attendly/attendly_backend/modules/user/service/AuthService.java)

**Key Operations:**
- **`login(LoginRequest)`** — Looks up user by email (`Optional<User>`); verifies password with `passwordEncoder.matches()`; generates JWT with `userId`, `role`, `name` claims; returns `AuthResponse` with token and role

**Business Rules:**
- Returns a generic "Invalid email or password" message (does not distinguish between wrong email vs wrong password — intentional for security)
- JWT token embeds user role for downstream RBAC without additional DB lookups

### Programme Service

**Class:** [ProgrammeService.java](src/main/java/com/attendly/attendly_backend/modules/programme/service/ProgrammeService.java)

**Key Operations:**
- **`getAllProgrammes()`** — Fetch all programmes with logging
- **`createProgramme(CreateProgrammeRequest)`** — Create new programme; validates request is not null
- **`updateProgrammeById(Long id, CreateProgrammeRequest)`** — Partial update; at least one field required
- **`deleteProgrammeById(Long id)`** — Delete programme; throws exception if not found

**Business Rules:**
- Timestamps (`createdAt`, `updatedAt`) auto-managed by Hibernate `@CreationTimestamp`/`@UpdateTimestamp`
- Deleting a programme cascades to its sessions (`CascadeType.ALL` on the `@OneToMany` relationship)

### Session Service

**Class:** [SessionService.java](src/main/java/com/attendly/attendly_backend/modules/session/service/SessionService.java)

**Key Operations:**
- **`createSession(SessionRequest)`** — Returns `ApiResponse<SessionResponse>`; validates programme exists via `ProgrammeRepository.findById()`; returns `ApiResponse.error(...)` if not found; sets `attendanceStatus = false` by default; links session to programme entity
- **`getAllSessions()`** — Fetch all sessions with logging
- **`updateSessionById(Long id, SessionRequest)`** — Partial update; checks `null` for `LocalDateTime` fields (`startTime`, `endTime`); checks non-blank for String fields; at least one field required
- **`deleteSessionById(Long id)`** — Delete session; throws exception if not found

**Business Rules:**
- `attendanceStatus` defaults to `false` on creation
- Timestamps managed by JPA `@PrePersist` / `@PreUpdate` lifecycle callbacks (not Hibernate annotations)
- `startTime` and `endTime` stored as `LocalDateTime` (typed, not string) — serialized as `yyyy-MM-dd'T'HH:mm:ss`
- Programme FK validated at service level before DB write; error returned as `ApiResponse` rather than exception
- `programmeId` is immutable after creation (update endpoint ignores it)

### Attendance Service

**Class:** [AttendanceService.java](src/main/java/com/attendly/attendly_backend/modules/attendance/service/AttendanceService.java)

**Key Operations:**
- **`createAttendance(CreateAttendanceRequest)`** — Validates user exists via `UserRepository.findById()` and programme exists via `ProgrammeRepository.findById()`; throws `NoSuchElementException` if either is not found; saves record and returns `AttendanceResponse`
- **`getAllAttendance()`** — Fetch all attendance records with logging
- **`getAttendanceById(Long id)`** — Fetch single record; throws `NoSuchElementException` if not found
- **`getAttendanceByUserId(Long userId)`** — Validates user exists (`existsById`); returns all records for that user via `AttendanceRepository.findByUserId()`
- **`getAttendanceByProgrammeId(Long programmeId)`** — Validates programme exists; returns all records via `AttendanceRepository.findByProgrammeId()`
- **`updateAttendanceStatus(Long id, String status)`** — Fetches record, updates `status` field, saves and returns updated response
- **`deleteAttendance(Long id)`** — Validates existence; deletes record

**Business Rules:**
- `createdAt` is auto-set by Hibernate `@CreationTimestamp`; the field is non-updatable
- Both `user_id` and `programme_id` FKs are validated at service level before DB write; throws `NoSuchElementException` (caught by `GlobalExceptionHandler`) on failure
- `time` stored as `LocalDateTime` — serialized as `yyyy-MM-dd'T'HH:mm:ss` via `@JsonFormat`

### Notification Service

**Class:** [NotificationService.java](src/main/java/com/attendly/attendly_backend/modules/notification/service/NotificationService.java)

**Key Operations:**
- **`createNotification(CreateNotificationRequest)`** — Validates sender and recipient via `UserRepository.findById()`; throws `NoSuchElementException` if either not found; sets `status = "UNREAD"` on creation; saves and returns `NotificationResponse`
- **`getAllNotifications()`** — Fetch all notifications with logging
- **`getNotificationById(Long id)`** — Fetch single notification; throws `NoSuchElementException` if not found
- **`getNotificationsByRecipientId(Long recipientId)`** — Validates recipient exists; returns all notifications via `findByRecipientId()`
- **`getNotificationsBySenderId(Long senderId)`** — Validates sender exists; returns all notifications via `findBySenderId()`
- **`getUnreadByRecipientId(Long recipientId)`** — Validates recipient exists; returns `UNREAD` notifications via `findByRecipientIdAndStatus(recipientId, "UNREAD")`
- **`markAsRead(Long id)`** — Fetches notification, sets `status = "READ"`, saves and returns updated response
- **`deleteNotification(Long id)`** — Validates existence; deletes record

**Business Rules:**
- `status` is always initialized to `"UNREAD"` on creation; transitions to `"READ"` only via the mark-as-read endpoint
- `datetime` auto-set by Hibernate `@CreationTimestamp`; non-updatable
- Both sender and recipient FKs validated before DB write; throws `NoSuchElementException` on failure

---

## Key Design Decisions

### 1. REST API Architecture
- **Why:** Industry standard for scalable web services; easy for frontend integration; stateless design
- **How:** Spring MVC with `@RestController`, `@RequestMapping`, standard HTTP methods

### 2. JWT-Based Authentication (Stateless)
- **Why:** Reduces server memory overhead; supports horizontal scaling; no server-side session needed
- **How:** `JwtTokenProvider` generates HMAC-SHA256 signed tokens; `JwtAuthFilter` validates token on every request; role embedded in token for zero-DB-lookup authorization

### 3. BCrypt Password Hashing
- **Why:** Industry standard; adaptive cost factor resists brute-force attacks; one-way hash — cannot be reversed
- **How:** `BCryptPasswordEncoder` bean in `SecurityConfig`; `encode()` on create, `matches()` on login
- **Important:** Raw passwords never stored, never logged

### 4. Spring Security Filter Chain
- **Why:** Centralized, declarative security configuration; separates cross-cutting auth concerns from business logic
- **How:** `SecurityConfig` defines public routes (`/api/auth/**`, `/api/health/**`), registers `JwtAuthFilter`, enforces STATELESS sessions; CSRF disabled for REST
- **Filter registration note:** `JwtAuthFilter` constructed inline (not `@Component`) to avoid double-execution from Spring Boot's auto-registration

### 5. Method-Level RBAC with `@PreAuthorize`
- **Why:** Fine-grained, declarative; authorization expressed alongside the endpoint it protects; easier to audit
- **How:** `@EnableMethodSecurity` on application class; `@PreAuthorize("hasRole('LECTURER')")` / `hasAnyRole(...)` on controller methods; class-level annotation used on `UserController` (all methods share same rule)

### 6. Email Uniqueness — Dual-Layer Enforcement
- **Why:** Database constraint alone gives poor UX (generic DB error); service check gives clean API error response
- **How:** `@Column(unique = true)` on `User.email` (DB constraint) + `UserRepository.existsByEmail()` check in `UserService.createUser()` returns `ApiResponse.error("Email already registered")`

### 7. Feature-Driven Module Structure
- **Why:** Separation of concerns; easy to understand business domains; facilitates parallel development
- **How:** Each module (user, programme, session) has its own controller, service, model, DTO, repository
- **Benefit:** Can extract modules into separate microservices later

### 8. DTOs for Request/Response Separation
- **Why:** Validation logic separate from domain model; expose only necessary fields
- **How:** `CreateUserRequest`, `UserResponse`, etc.; mapped in service layer
- **Benefit:** Password excluded from responses; `LocalDateTime` serialized cleanly with `@JsonFormat`

### 9. Structured Logging with SLF4J & Lombok @Slf4j
- **Why:** Enhanced observability for debugging, monitoring, and audit trails; consistent logging patterns
- **How:** `@Slf4j` annotation on service classes (e.g., `ProgrammeService`, `SessionService`); `log.info()`, `log.error()` throughout CRUD operations
- **Benefit:** Captures business events (create, update, delete) with contextual data; easily integrated with external monitoring (ELK, Datadog, etc.)
- **Logs Include:** Operation start/completion, entity IDs, error messages, validation failures

### 10. Custom Security Error Handlers
- **Why:** Consistent, user-friendly error responses for authentication/authorization failures
- **How:** Custom `authenticationEntryPoint()` and `accessDeniedHandler()` beans in `SecurityConfig`
- **Benefit:** Clients receive clear JSON error messages instead of generic HTML pages; improves frontend error handling

### 11. Typed Time Fields (`LocalDateTime` not `String`)
- **Why:** Enables time-based queries; format enforced by the type system; avoids silent data corruption
- **How:** `Session.startTime` / `endTime` are `LocalDateTime @Column(nullable = false)`; `@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")` on both request and response DTOs

### 12. FK-Enforced Programme → Session Relationship
- **Why:** Referential integrity; prevents orphaned sessions; enables cascade delete
- **How:** `@ManyToOne @JoinColumn(name = "programme_id", nullable = false)` on `Session`; `@OneToMany(mappedBy = "programme", cascade = ALL, fetch = LAZY) @ToString.Exclude` on `Programme`
- **`@ToString.Exclude` note:** Required to prevent Lombok's `toString()` from recursing infinitely between `Programme → sessions → Session.programme → Programme → ...`

### 13. Standardized `ApiResponse<T>` Wrapper
- **Why:** Consistent response format for all endpoints; `success` flag lets clients distinguish errors without relying solely on HTTP status codes
- **How:** Generic class with `success`, `message`, `data`, `timestamp`; factory methods `success(...)` / `error(...)`; null fields excluded from JSON via `@JsonInclude(NON_NULL)`
- **Service-level returns:** `UserService.createUser()` and `SessionService.createSession()` return `ApiResponse<T>` directly so they can signal errors without throwing exceptions; controllers pass the result straight to `ResponseEntity.ok()`

### 14. Global Exception Handling
- **Why:** Centralized error responses; consistent error format; cleaner controller code
- **How:** `@RestControllerAdvice` with `@ExceptionHandler` for validation, not-found, illegal argument, and general exceptions

### 15. Profile-Based Configuration (Dev vs Prod)
- **Why:** Single codebase for multiple environments; secrets not in code
- **How:** Spring profiles (dev/prod) with different database, ports, JWT secrets
- **Current issue:** JWT secret still hardcoded in dev profile; production must inject via environment variable

---

## Limitations & Known Issues

### 1. ✅ RESOLVED — Authentication Filter
- **Was:** JWT tokens generated but not validated on requests
- **Resolution:** `JwtAuthFilter` (`OncePerRequestFilter`) validates Bearer token on every non-public request; wired into `SecurityConfig` before `UsernamePasswordAuthenticationFilter`

### 2. 🔴 Hardcoded JWT Secret in Dev Config
- **Issue:** `dev-attendly-super-secret-key-change-in-production` is visible in `application.yaml`
- **Impact:** Not a risk in dev, but production secret must be injected via environment variable
- **Fix Needed:** Use environment variables or a secret manager for production deployment
- **Severity:** 🔴 CRITICAL — must not ship to production without override

### 3. ✅ RESOLVED — Email Uniqueness
- **Was:** Duplicate emails allowed in database
- **Resolution:** `@Column(unique = true, nullable = false)` on `User.email`; `existsByEmail()` check in `UserService` returns `ApiResponse.error("Email already registered")` before attempting insert

### 4. ✅ RESOLVED — Time Fields Stored as Strings
- **Was:** `start_time` / `end_time` stored as `VARCHAR` with no format enforcement
- **Resolution:** Changed to `LocalDateTime @Column(nullable = false)`; `@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")` on both `SessionRequest` and `SessionResponse`

### 5. ✅ RESOLVED — No Role-Based Access Control
- **Was:** All endpoints publicly accessible; roles stored but not enforced
- **Resolution:** `@EnableMethodSecurity` + `@PreAuthorize` on all controller methods; `/api/auth/**` and `/api/health/**` public; all other routes require a valid JWT with appropriate role

### 6. ✅ RESOLVED — No Foreign Key Constraints
- **Was:** Sessions and Programmes were independent entities; no referential integrity
- **Resolution:** `Session.programme_id` FK enforced at DB level (`@JoinColumn(nullable = false)`); service validates programme exists before create and returns an error response if not

### 7. ✅ RESOLVED — No Password Hashing
- **Was:** Passwords stored and compared as plaintext
- **Resolution:** `BCryptPasswordEncoder` bean in `SecurityConfig`; `encode()` on create; `matches()` on login

### 8. 🟡 No Input Sanitization
- **Issue:** String inputs validated for format but not sanitized against injection attacks
- **Impact:** Low risk with JPA (parameterized queries); XSS risk if data served to a web UI
- **Severity:** 🟡 MEDIUM — depends on client usage

### 9. 🟡 H2 Console Enabled in All Profiles
- **Issue:** `spring-boot-h2console` includes a web console endpoint
- **Current State:** Dev uses H2; prod uses PostgreSQL — mitigated in practice
- **Fix Needed:** Conditionally restrict H2 console access in security config
- **Severity:** 🟡 LOW — mitigated by prod using PostgreSQL

### 10. 🟡 No API Documentation (Swagger/OpenAPI)
- **Issue:** No Swagger/OpenAPI documentation; no API versioning
- **Impact:** Harder for frontend developers to integrate
- **Fix Needed:** Add Springdoc OpenAPI; implement API versioning strategy
- **Severity:** 🟡 MEDIUM — developer experience issue

### 11. 🟢 Redundant POST-as-Update Endpoints
- **Issue:** `POST /api/programmes/{id}` and `POST /api/sessions/{id}` duplicate `PUT /{id}`
- **Impact:** RESTful convention violated; redundant routes
- **Fix Needed:** Remove POST update aliases; standardize on PUT
- **Severity:** 🟢 LOW — cosmetic; endpoints work correctly

### 12. 🟡 No Pagination or Sorting
- **Issue:** `getAllUsers()`, `getAllProgrammes()`, `getAllSessions()` return all records
- **Impact:** Performance degradation with large datasets
- **Fix Needed:** Add `Pageable` parameter; return `Page<T>` with limit, offset, sorting
- **Severity:** 🟡 HIGH — scalability concern

### 13. 🟡 No CORS Configuration
- **Issue:** CORS headers not explicitly configured
- **Impact:** Frontend apps on different domains may be blocked
- **Fix Needed:** Configure `WebMvcConfigurer` CORS mappings for the frontend domain
- **Severity:** 🟡 MEDIUM — frontend integration issue

### 14. 🟡 No Database Indexes Beyond Primary Key
- **Issue:** No indexes on frequently queried fields
- **Impact:** Slow queries on `email` (login), `code` (programme/session lookup)
- **Fix Needed:** Add indexes on `users.email`, `programmes.code`, `sessions.code`
- **Severity:** 🟡 MEDIUM — performance concern at scale

### 15. 🟡 `programmeId` Not Updatable on Sessions
- **Issue:** Once a session is created under a programme, its programme cannot be changed via the update endpoint
- **Current State:** By design for now; `programmeId` in update request is silently ignored
- **Fix Needed:** If reassignment is a real use case, add explicit programme update logic with validation
- **Severity:** 🟡 LOW — depends on product requirements

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

## Changelog

### v3.1 — May 1, 2026 (Current)

#### Infrastructure Improvements

**Structured Logging Implementation**
- Added SLF4J + Logback logging across all service classes (`ProgrammeService`, `SessionService`, `AttendanceService`, `NotificationService`)
- Annotated services with `@Slf4j` (Lombok) for clean log statement syntax
- Logs capture: operation start/completion, entity IDs, validation failures, error messages
- Benefits: Enhanced observability for debugging, monitoring integration (ELK, Datadog), audit trail capabilities
- Logs in each service layer track:
  - CRUD operation lifecycle (create, read, update, delete)
  - Entity creation/deletion with ID tracking
  - FK validation failures with specific resource IDs
  - Execution flow for troubleshooting

**Custom Authentication & Authorization Error Handlers**
- Implemented `unauthorizedEntryPoint()` bean in `SecurityConfig` for 401 Unauthorized responses
- Implemented `accessDeniedHandler()` bean in `SecurityConfig` for 403 Forbidden responses
- Both handlers return JSON error responses instead of default HTML pages
- Improves frontend error handling with machine-readable error messages
- Consistent error format: `{"status":"error","message":"...","data":null}`

**Configuration Updates**
- Added application version info to `application.yaml`: `spring.application.version: 0.0.1`
- Clarified JWT expiration time comment in dev config: `# 1 hour`

**Security Enhancements**
- Public user registration endpoint: `POST /api/users` now permissible without authentication
- Enables self-registration flow for new users
- All other endpoints remain protected by JWT requirement
- H2 console explicitly permitted in security filter chain (dev only)

---

### v3.0 — April 29, 2026 (`Final_modification` branch)

#### New Modules

**Attendance Module** (`modules/attendance/`)
- New `Attendance` JPA entity — FK to `users.id` (`user_id`) and `programmes.id` (`programme_id`); `time` stored as `LocalDateTime`; `status` as `VARCHAR`; `createdAt` via `@CreationTimestamp`
- `AttendanceController` at `/api/attendance` — 7 endpoints; STUDENT can record attendance; LECTURER has full access including programme-level and all-records queries
- `AttendanceService` — validates both `user_id` and `programme_id` FKs before save; throws `NoSuchElementException` on missing references
- `AttendanceRepository` — `findByUserId()`, `findByProgrammeId()` derived query methods
- `CreateAttendanceRequest` DTO — `@NotNull` on `userId`, `programmeId`, `time`; `@NotBlank` on `status`
- `AttendanceResponse` DTO — `@Builder`; `@JsonFormat` on `time` and `createdAt`

**Notification Module** (`modules/notification/`)
- New `Notification` JPA entity — dual FK to `users.id` (`sender_id`, `recipient_id`); `content` NOT NULL; `status` NOT NULL; `datetime` via `@CreationTimestamp`
- `NotificationController` at `/api/notifications` — 8 endpoints; LECTURER sends and manages; STUDENT can read their inbox, mark as read
- `NotificationService` — initializes `status = "UNREAD"` on create; `markAsRead()` transitions status to `"READ"`; validates both sender and recipient FKs
- `NotificationRepository` — `findByRecipientId()`, `findBySenderId()`, `findByRecipientIdAndStatus()` derived query methods
- `CreateNotificationRequest` DTO — `@NotNull` on `senderId`, `recipientId`; `@NotBlank` on `content`
- `NotificationResponse` DTO — `@Builder`; `@JsonFormat` on `datetime`

#### Model Updates

- `Programme.java` — added second `@OneToMany(mappedBy = "programme", cascade = ALL, fetch = LAZY) @ToString.Exclude` collection for `List<Attendance> attendance`

#### Security & Infrastructure

- `JwtAuthFilter.java` — committed to version control (previously untracked); no logic changes
- `config/SecurityConfig.java` — committed to version control (previously untracked); no logic changes
- RBAC table updated to include `AttendanceController` and `NotificationController` access rules

---

**Document Version:** 3.1
**Last Updated:** May 1, 2026
**Generated for:** Attendly Backend v0.0.1-SNAPSHOT
