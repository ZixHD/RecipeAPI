# 🍳 Mobile App Backend – Recipe API

### 📌 Overview

This project is a Spring Boot-based open-source API designed to 

It provides:
 - User authentication and authorization
 - Recipe creation and management
 - Role-based access control (RBAC)
 - RESTful API endpoints
 - MongoDB persistence
 - Dockerized deployment

The backend is designed to be:
  - Production-ready
  - Serves as a modular backend foundation for extension
  - Follows secure authentication practices using asymmetric cryptography

---------------------------------------

### 🏗 Architecture

Tech stack
- Java 17
- Spring Boot
- Sprint Security
- MongoDB (Atlas)
- Docker
- JWT authentication

----------

### 📂 Project Structure

```
Project/
│
├── src/main/java/
│   ├── config/          # Security, JWT, and configuration classes
│   ├── controller/      # REST controllers
│   ├── service/         # Business logic layer
│   ├── repository/      # MongoDB repositories
│   ├── model/           # Domain models
│   └── dto/             # Data transfer objects
│
├── src/main/resources/
│   └── application.properties
│
├── Dockerfile
├── docker-compose.yml
├── build_and_run.sh
└── .env
```

Layer Design
  - Controller layer -> Handles HTTP requests
  - Service layer -> Contains business logic
  - Repository layer -> Data persistence
  - Security layer -> Authentication and authorization
  - DTO layer -> Separates API contract from domain name

------

### 🔐 Authorization & Security Architecture

The system uses JWT-based authentication with role-basec access control.

##### User roles
  1. Developer (Recipe API)
     - Can access recipe endpoints
     - Intended for clients building various applications
  2. General BACKEND USER
     - Authenticated users using the backend as a base system
     - Can extend functionality beyond recipes

Instead of signing JWTs with a shared secret, this project uses RSA public/private key pairs.
 - Private key signs tokens
 - Public key verifies tokens
 - Public key can be shared easily
 - Private key never leaves the server

-----

### 📦 Environment Variables

Create a .env file in the root directory:

```
MONGO_USERNAME=username
MONGO_PASSWORD=password
MONGO_URI=cluster_name.cluster_id.mongodb.net
```
Also see .env.example.

-----

### 🐳 Running the Project

#### Requirements
 - Docker
 - Docker compose


 ##### 🚀 Start the Backend
 
 ```
./build_and_run.sh
 ```

The script performs:
  1. Stops existing `recipe-api` container
  2. Removes the container
  3. Creates a Docker network (if missing)
  4. Build the Docker image
  5. Starts container with:
      - Static IP (for MondoDB Atlas whitelisting)
      - Injected environment variables
      - Exposed port 8080
    
-----

### 🧪 Running Tests (Gradle)

To run tests:

```
./gradlew test
```

  

