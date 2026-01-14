# iRent Backend

Backend for the iRent MVP application.

---

## 🚀 Built With

- Language: Java 25 (LTS)
- Framework: Spring Boot 4.0.1
- Security: Spring Security (Stateless JWT)
- Database: Microsoft SQL Server
- Migration: Flyway
- Build Tool: Maven
- ORM: Hibernate / JPA
- Utilities: Lombok

---

## 🛠️ Prerequisites

- JDK 25 installed
- Maven 3.9+ installed
- SQL Server running
- Database created: `mvp`

---

## ⚙️ Configuration

Set these as environment variables or inside  
`src/main/resources/application.yml`

| Variable        | Description                     | Default 
|-----------------|---------------------------------|---------
| DB_URL          | SQL Server connection string    | jdbc:sqlserver://localhost:1433;databaseName=mvp
| DB_USERNAME     | Database username               | aymen
| DB_PASSWORD     | Database password               | root
| SERVER_PORT     | Application port                | 8080

---

## 🏃 How to Run

### Clone the repository

```bash
git clone <repository-url>
cd mvp
```

### Build the project

```bash
mvn clean install
```

### Run the application

```bash
mvn spring-boot:run
```

Server starts at:

```
http://localhost:8080
```

Flyway automatically runs migrations from:

```
src/main/resources/db/migration
```

---

## 📖 API Documentation

Base path: `/api/auth`  
Content-Type: `application/json`

---

### 1. User Registration

**Endpoint**

```
POST /api/auth/register
```

**Access:** Public

**Request Body**

```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "fullName": "John Doe"
}
```

**Responses**

- 201 Created – User registered successfully
- 400 Bad Request – Email already exists or invalid data

---

### 2. User Login

**Endpoint**

```
POST /api/auth/login
```

**Access:** Public

**Request Body**

```json
{
  "email": "user@example.com",
  "password": "securePassword123"
}
```

**Responses**

- 200 OK – Returns JWT token
- 401 Unauthorized – Invalid credentials

---

### 3. Get Current Profile

**Endpoint**

```
GET /api/auth/me
```

**Access:** Private (Bearer Token required)

**Headers**

```
Authorization: Bearer <JWT_TOKEN>
```

**Responses**

- 200 OK – Returns UserDto
- 401 Unauthorized – Missing or invalid token

---

