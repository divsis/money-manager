# Money Manager Application

## Project Overview
Money Manager is a Spring Boot-based REST API application designed to manage user profiles and financial data. The application is built with modern Java technologies and provides user registration and profile management functionality.

**Application Version:** 0.0.1-SNAPSHOT  
**Java Version:** 21  
**Build Tool:** Maven

---

## Project Structure & Directory Explanation

### Root Directory Structure
```
money-manager/
├── src/                      # Source code directory
├── target/                   # Compiled build artifacts
├── pom.xml                  # Maven configuration file
├── mvnw / mvnw.cmd         # Maven wrapper scripts
├── HELP.md                  # Spring Boot help documentation
└── README.md                # This file
```

---

## Directory Structure & Purpose

### 1. **src/main/java/com/divsis/money_manager/**
Main source code directory containing the application logic. This is organized following Spring Boot best practices with domain-driven design.

#### **config/** 
- **Purpose:** Contains Spring Boot configuration classes
- **Why Created:** To centralize and manage application configurations (database, security, JWT, email settings)
- **Future Use:** Will contain configurations for:
  - Database connection pools
  - JWT token generation and validation
  - Email service configuration
  - CORS settings

#### **controller/**
- **Purpose:** REST API endpoint handlers
- **Why Created:** Following MVC pattern, controllers handle HTTP requests and route them to appropriate services
- **Files:**
  - `HomeController.java` - Health check endpoints (`/status`, `/health`)
  - `ProfileController.java` - User profile management endpoints (`/register`)
- **Pattern:** Controllers are thin layers that delegate business logic to services

#### **dto/** (Data Transfer Object)
- **Purpose:** Plain Java objects for data transfer between layers
- **Why Created:** 
  - Encapsulates data being sent over HTTP
  - Prevents exposing internal database entities directly
  - Allows for data transformation and validation
- **Files:**
  - `ProfileDTO.java` - Data structure for user profile information during API communication

#### **entity/**
- **Purpose:** JPA Entity classes representing database tables
- **Why Created:** 
  - ORM mapping between Java objects and database records
  - Defines database schema structure
- **Files:**
  - `ProfileEntity.java` - Represents user profile in database with fields:
    - `id` - Primary key (auto-generated)
    - `name` - User's full name
    - `email` - Unique email address
    - `password` - Hashed password
    - `profileImageUrl` - User's profile picture URL
    - `createdAt` - Timestamp when profile was created
    - `updatedAt` - Timestamp when profile was last updated
    - `isActive` - Account activation status
    - `activationToken` - Email verification token

#### **repository/**
- **Purpose:** Data access layer using Spring Data JPA
- **Why Created:** 
  - Abstracts database operations
  - Provides CRUD (Create, Read, Update, Delete) operations
  - Custom query methods
- **Files:**
  - `ProfileRepository.java` - Interface extending JpaRepository
    - Built-in methods: `save()`, `findById()`, `findAll()`, `delete()`
    - Custom method: `findByEmail(String email)` - for unique email lookups

#### **service/**
- **Purpose:** Business logic and service layer
- **Why Created:** 
  - Separates business logic from HTTP handling
  - Promotes code reusability and testability
  - Manages transactions and data transformations
- **Files:**
  - `ProfileService.java` - Contains:
    - `registerProfile()` - Handles user registration logic
    - `toEntity()` - Converts DTO to Entity
    - `toDTO()` - Converts Entity to DTO
    - Generates unique activation tokens using UUID

#### **security/**
- **Purpose:** Security configurations and JWT token handling
- **Why Created:** Placeholder for:
  - JWT token generation and validation
  - Spring Security configurations
  - Authentication filters
  - Password encoding strategies
- **Status:** Ready for implementation

#### **util/**
- **Purpose:** Utility classes and helper methods
- **Why Created:** Reusable utility functions such as:
  - String formatting utilities
  - Date/time helpers
  - Validation helpers
  - Email validators
- **Status:** Ready for implementation

---

### 2. **src/main/resources/**
Configuration and static assets directory.

#### **application.properties**
- **Purpose:** Spring Boot application configuration
- **Configuration Details:**
  - Application Name: `money-manager`
  - Database: MySQL (localhost:3306/moneymanager)
  - Context Path: `/api/v1.0` (Base URL for all endpoints)
  - JPA/Hibernate Settings: Auto-update schema, SQL logging enabled
  - Username: `root`
  - Password: `Ankit@2002`

#### **static/**
- **Purpose:** Static web resources (CSS, JavaScript, images)
- **When Used:** For frontend assets if served from this backend

#### **templates/**
- **Purpose:** Thymeleaf HTML templates (if needed)
- **When Used:** For server-side rendered pages (currently using REST API)

---

### 3. **src/test/**
Unit and integration test files directory.

#### **Purpose:**
- Test cases for service layer logic
- Integration tests for controllers
- Repository tests with embedded database

---

### 4. **target/**
Build output directory (generated automatically).

- **money-manager-0.0.1-SNAPSHOT.jar** - Executable JAR file
- **classes/** - Compiled .class files
- **generated-sources/** - Code generated by annotation processors (Lombok)
- **maven-status/** - Build status information

---

## Key Dependencies

### Spring Boot Framework
- `spring-boot-starter-data-jpa` - ORM and database operations
- `spring-boot-starter-webmvc` - REST API development
- `spring-boot-starter-webflux` - Reactive web support

### Database Support
- `mysql-connector-j` - MySQL driver
- `postgresql` - PostgreSQL driver (for multi-DB support)

### Security & Authentication
- `jjwt-api@0.12.6` - JWT token creation
- `jjwt-impl@0.12.6` - JWT implementation
- `jjwt-jackson@0.12.6` - JWT JSON processing

### Utilities
- `lombok` - Reduce boilerplate code (@Data, @Builder, etc.)
- `spring-boot-starter-mail` - Email functionality
- `poi-ooxml@5.5.1` - Excel file handling

---

## Work Completed Today (April 12, 2026)

### 1. **Project Setup & Configuration**
   - Created Spring Boot 4.0.5 project structure
   - Configured MySQL database connection
   - Set up Maven as build tool
   - Configured API context path: `/api/v1.0`

### 2. **Core Architecture Implementation**
   - Implemented layered architecture:
     - Controller Layer (HTTP endpoints)
     - Service Layer (Business logic)
     - Repository Layer (Data access)
     - Entity Layer (Database mapping)

### 3. **Entity & Database Model**
   - Created `ProfileEntity` with all necessary fields
   - Implemented JPA annotations for:
     - Auto-generated ID
     - Unique email constraint
     - Automatic timestamp tracking
     - Account activation mechanism

### 4. **Data Transfer Layer**
   - Created `ProfileDTO` for API communication
   - Implemented data mapping between Entity and DTO

### 5. **API Endpoints Development**
   - **Health Check Endpoints:**
     - `GET /api/v1.0/status` - Application status
     - `GET /api/v1.0/health` - Health check
   - **User Management Endpoint:**
     - `POST /api/v1.0/register` - User registration

### 6. **Service Layer Implementation**
   - `ProfileService` with registration logic
   - UUID-based activation token generation
   - DTO-Entity conversion methods

### 7. **Repository Pattern**
   - Created `ProfileRepository` with JpaRepository extension
   - Implemented custom method: `findByEmail()`
   - Enables type-safe database queries

### 8. **Security & Scalability Preparation**
   - Added JWT dependencies for future authentication
   - Added mail service for email notifications
   - Added Apache POI for report generation
   - Database support for both MySQL and PostgreSQL

---

## API Endpoints

### Health Check
```
GET /api/v1.0/status
GET /api/v1.0/health
Response: "Application is running"
```

### User Registration
```
POST /api/v1.0/register
Content-Type: application/json

Request Body:
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "profileImageUrl": "https://example.com/image.jpg"
}

Response (201 Created):
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "profileImageUrl": "https://example.com/image.jpg",
    "createdAt": "2026-04-12T10:30:00",
    "updatedAt": "2026-04-12T10:30:00"
}
```

---

## How to Build & Run

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- MySQL 5.7+ (with database: `moneymanager`)

### Build
```bash
cd money-manager
mvn clean package
```

### Run
```bash
java -jar target/money-manager-0.0.1-SNAPSHOT.jar
```

Or using Maven:
```bash
mvn spring-boot:run
```

### Application will be available at:
```
http://localhost:8080/api/v1.0
```

---

## Naming Conventions Used

### Package Structure
- `com.divsis.money_manager` - Base package (Reverse domain)
- `com.divsis.money_manager.controller` - Web layer
- `com.divsis.money_manager.service` - Business logic layer
- `com.divsis.money_manager.repository` - Data access layer
- `com.divsis.money_manager.entity` - JPA entities
- `com.divsis.money_manager.dto` - Data transfer objects

### Naming Patterns
- **Entity Classes:** Suffix with `Entity` (e.g., `ProfileEntity`)
- **DTOs:** Suffix with `DTO` (e.g., `ProfileDTO`)
- **Service Classes:** Suffix with `Service` (e.g., `ProfileService`)
- **Repository Interfaces:** Suffix with `Repository` (e.g., `ProfileRepository`)
- **Controller Classes:** Suffix with `Controller` (e.g., `ProfileController`)
- **Database Tables:** Prefix with `tbl_` (e.g., `tbl_profile`)

---

## Future Enhancements

1. **Authentication & Authorization**
   - JWT token-based authentication
   - Role-based access control (RBAC)
   - OAuth2 integration

2. **User Features**
   - Profile update endpoint
   - Profile deletion
   - Profile retrieval

3. **Transaction Management**
   - Income/Expense tracking
   - Budget management
   - Financial reports (Excel export)

4. **Email Notifications**
   - Account activation emails
   - Password reset emails
   - Transaction alerts

5. **Validation & Error Handling**
   - Request validation
   - Global exception handling
   - Comprehensive error responses

6. **Security Enhancements**
   - Password hashing (BCrypt)
   - Input sanitization
   - Rate limiting

---

## Developer Information

- **Project Type:** Spring Boot REST API
- **Architecture:** Layered Architecture (3-tier)
- **Database:** MySQL (Primary) + PostgreSQL (Alternative)
- **Build System:** Maven
- **Java Version:** 21
- **Start Date:** April 12, 2026

---

## Database Schema

### Table: tbl_profile
```sql
CREATE TABLE tbl_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255),
    profile_image_url VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE,
    activation_token VARCHAR(255)
);
```

---

## Notes

- All timestamps use automatic generation and update tracking
- Email field is unique to prevent duplicate accounts
- Accounts default to inactive status with activation token
- Application uses Lombok for reducing boilerplate code
- All endpoints follow RESTful conventions
- Project is ready for containerization (Docker)

---

**Last Updated:** April 12, 2026  
**Status:** Development in Progress ✅

