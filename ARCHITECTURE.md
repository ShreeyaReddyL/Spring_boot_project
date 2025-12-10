RideShare Backend – Architecture & System Design

A complete explanation of how the backend is structured, how different layers interact, and what design patterns make the system stable, secure, and scalable.

🏗️ Overall System Architecture

The application follows a Layered Architecture, where each layer handles one clear responsibility.

CLIENT (Mobile / Web / Postman)
        │
        ▼
CONTROLLER LAYER (REST APIs)
        │
        ▼
SECURITY LAYER (JWT Auth)
        │
        ▼
SERVICE LAYER (Business Logic)
        │
        ▼
REPOSITORY LAYER (MongoDB Access)
        │
        ▼
DATABASE (MongoDB)

🎯 Layer-by-Layer Description
1️⃣ Client Layer

Sends HTTP requests

Includes the JWT token in the Authorization header

Receives JSON responses

2️⃣ Controller Layer

All endpoints are defined here

Maps HTTP requests → Java methods

Delegates logic to services

Returns proper status codes

Controllers:

AuthController → /api/auth/*

RideController → /api/v1/*

3️⃣ Security Layer

Handles everything related to authentication:

Components:

JwtAuthenticationFilter → Pulls token from request

JwtTokenProvider → Creates & validates tokens

Responsibilities:

Validates JWT

Extracts user identity & role

Blocks unauthorized access

4️⃣ Service Layer

This layer contains all important business logic.
Nothing related to database or HTTP is done here.

Services:

AuthService → Register, login, verify password

RideService → Request ride, accept ride, complete ride

Responsibilities:

Enforce rules (only drivers can accept rides, status must be valid, etc.)

Validate business conditions

Use repositories for database access

5️⃣ Repository Layer

Interacts with MongoDB

Uses MongoRepository

Contains custom query methods:

UserRepository:

findByUsername()

RideRepository:

findByStatus()

findByUserId()

6️⃣ Database Layer

Collections:

users

rides

Each ride references:

userId (who requested)

driverId (who accepted)

🔄 Request Handling Flow
A successful request goes through:

Client sends request with token

JWT Filter validates it

Spring Security checks access rights

Controller receives validated request

Service processes business logic

Repository interacts with MongoDB

Response goes back as JSON

❌ Error Flow

If any layer fails, GlobalExceptionHandler catches it.

Handled errors include:

NotFoundException → 404

BadRequestException → 400

Validation errors → 400

Any runtime exception → 500

All errors return a consistent JSON format.

🔐 JWT Authentication Workflow
Registration

Validate input

Hash password using BCrypt

Save user

Login

Compare stored hash with password

Create JWT containing:

username

role

issuedAt

expiry (24 hours)

Send token back

Using the Token

Client includes:

Authorization: Bearer <token>


Server then:

verifies signature

checks expiry

loads user role

grants or denies access

🗂️ Data Model
User
_id          ObjectId
username     String (unique)
password     String (BCrypt hash)
role         String (ROLE_USER / ROLE_DRIVER)

Ride
_id              ObjectId
userId           String
driverId         String
pickupLocation   String
dropLocation     String
status           REQUESTED | ACCEPTED | COMPLETED
createdAt        LocalDateTime

🚗 Ride Status Flow
REQUESTED → ACCEPTED → COMPLETED


User creates ride → Status = REQUESTED

Driver accepts ride → Status = ACCEPTED

Ride finished → Status = COMPLETED

🧪 Validation Flow
Validation occurs in 3 stages:
1. DTO Validation

@NotBlank

@Email

@Pattern

If failed → 400 Validation Error

2. Service Validation

Check if username exists

Verify ride status transitions

Role-based rules

If failed → 400 Bad Request

3. Database Validation

Unique index constraints

Data consistency checks

If failed → 500 Internal Error

🛡️ Exception Handling System

A global handler catches all exceptions and returns clean JSON errors.

Examples:

User not found → 404

Wrong password → 400

Invalid JWT → 403

Server crash → 500

📦 Design Patterns Used
Pattern	Why it's used
Layered Architecture	Clear separation of responsibilities
Repository Pattern	Hides MongoDB logic
Service Pattern	Centralized business rules
DTO Pattern	Clean API input/output
Global Exception Handler	Consistent error responses
Dependency Injection	Loose coupling, easier testing
JWT Authentication	Stateless & scalable security
📈 Performance Considerations
Indexes

username is indexed

status is indexed for fast driver queries

userId is indexed for ride history

MongoDB Connection Pool

Reuses connections

Reduces latency

Stateless API

No session stored on server

Easy horizontal scaling