# ✈️ Flight Booking API

A RESTful backend API for managing flights, users, and bookings — built with Java 17, Spring Boot 3, PostgreSQL, and secured with JWT authentication.

---

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Run with Docker](#run-with-docker)
  - [Run Locally](#run-locally)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Authentication](#authentication)
- [Endpoints](#endpoints)

---

## Features

- JWT-based authentication and authorization
- Role-based access control (USER / ADMIN)
- Full CRUD for flights, users, and bookings
- Filtered search endpoints for flights and users
- Pagination on all list endpoints
- Input validation with detailed error responses
- Swagger UI for interactive API exploration
- Dockerized setup with PostgreSQL

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.4 |
| Database | PostgreSQL 16 |
| Security | Spring Security + JWT (JJWT) |
| ORM | Spring Data JPA / Hibernate |
| Validation | Jakarta Validation |
| Documentation | SpringDoc OpenAPI (Swagger) |
| Containerization | Docker + Docker Compose |

---

## Getting Started

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- Java 17+ (only needed for running locally without Docker)
- Maven 3.9+ (only needed for running locally without Docker)

### Environment Variables

Copy the example env file and fill in your values:

```bash
cp .env.example .env
```

`.env.example`:

```env
DB_NAME=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=
```

> ⚠️ Never commit your `.env` file — it is already in `.gitignore`.

### Run with Docker

This is the recommended way to run the application. It starts both the database and the API in containers.

```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

To stop the application:

```bash
docker-compose down
```

To stop and remove all data (including the database volume):

```bash
docker-compose down -v
```

### Run Locally

If you want to run the Spring Boot app outside of Docker (e.g. for development), start only the database via Docker and run the app through IntelliJ or Maven.

**1. Start the database:**

```bash
docker-compose up db -d
```

**2. Run the application:**

```bash
./mvnw spring-boot:run
```

---

## API Documentation

Once the application is running, the interactive Swagger UI is available at:

```
http://localhost:8080/swagger-ui/index.html
```

Here you can explore all endpoints, view request/response schemas, and test the API directly in the browser using a JWT token.

---

## Project Structure

```
src/main/java/com/melle/flightbooking/
│
├── config/          # Security, JWT, Swagger, validation constants
├── controller/      # REST controllers (Auth, Flight, Booking, User)
├── dto/             # Request and response DTOs per domain
├── exception/       # Custom exceptions and global exception handler
├── interfaces/      # Service interfaces
├── model/           # JPA entities (Flight, User, Booking)
├── repository/      # Spring Data JPA repositories
├── seeders/         # Development data seeders
└── service/         # Business logic implementations
```

---

## Authentication

The API uses **JWT Bearer token** authentication.

**1. Register an account:**

```
POST /api/v1/auth/register
```

**2. Login to receive a token:**

```
POST /api/v1/auth/login
```

**3. Use the token in subsequent requests:**

```
Authorization: Bearer <your_token>
```

In Swagger UI, click the **Authorize** button (top right) and paste your token there.

### Roles

| Role | Access |
|---|---|
| `USER` | Read flights, manage own bookings, update own profile |
| `ADMIN` | Full access including creating/deleting flights and managing all users |

---

## Endpoints

### Auth

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Public | Register a new user |
| POST | `/api/v1/auth/login` | Public | Login and receive JWT token |

### Flights

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/v1/flights?page=0&size=10` | Authenticated | Get all flights (paginated) |
| GET | `/api/v1/flights/{id}` | Authenticated | Get flight by ID |
| GET | `/api/v1/flights/search?origin=&destination=&date=&seats=` | Authenticated | Search flights by filters |
| POST | `/api/v1/flights` | Admin | Create a new flight |
| PUT | `/api/v1/flights/origin` | Admin | Update flight origin |
| PUT | `/api/v1/flights/destination` | Admin | Update flight destination |
| PUT | `/api/v1/flights/date` | Admin | Update flight date |
| PUT | `/api/v1/flights/seats` | Admin | Update flight seats |
| DELETE | `/api/v1/flights/{id}` | Admin | Delete a flight |

### Bookings

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/v1/booking?page=0&size=10` | Admin | Get all bookings (paginated) |
| GET | `/api/v1/booking/{userId}?page=0&size=10` | Admin | Get bookings by user ID (paginated) |
| GET | `/api/v1/booking/me?page=0&size=10` | User | Get own bookings (paginated) |
| GET | `/api/v1/booking/{bookingId}` | User | Get booking by ID |
| POST | `/api/v1/booking` | User | Create a booking |
| PUT | `/api/v1/booking/flight` | Admin | Update booking flight |
| PUT | `/api/v1/booking/user` | Admin | Update booking user |
| DELETE | `/api/v1/booking` | User | Delete own booking |
| DELETE | `/api/v1/booking/{bookingId}` | Admin | Delete any booking |

### Users

| Method | Endpoint | Access | Description |
|---|---|---|---|
| GET | `/api/v1/users?page=0&size=10` | Admin | Get all users (paginated) |
| GET | `/api/v1/users/search?username=&email=&role=` | Admin | Search users by filters |
| PUT | `/api/v1/users/me/username` | Authenticated | Update own username |
| PUT | `/api/v1/users/me/email` | Authenticated | Update own email |
| PUT | `/api/v1/users/me/password` | Authenticated | Update own password |
| PUT | `/api/v1/users/admin/username` | Admin | Update any user's username |
| PUT | `/api/v1/users/admin/email` | Admin | Update any user's email |
| PUT | `/api/v1/users/admin/password` | Admin | Update any user's password |
| DELETE | `/api/v1/users/{id}` | Admin | Delete a user |