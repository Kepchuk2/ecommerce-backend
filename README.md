# 🛒 E-commerce Backend (Spring Boot & Docker)

## 📖 About the Project
My name is Rostyslav (Rost), and I am an aspiring Java Developer.
This project is a backend solution for an e-commerce platform, built to address real-world engineering challenges such as complex entity relationships, transaction management, and containerized deployment.

The main focus of the project is Clean Architecture, Data Security, and Database Performance.

---

## 🚀 Tech Stack
- Java 21 (latest LTS features)
- Spring Boot 3.x (Web, Data JPA, Security)
- PostgreSQL (relational database)
- Hibernate (ORM with query optimization)
- Docker & Docker Compose (containerization)
- JUnit 5 & Mockito (unit testing)
- Maven Wrapper (environment-independent builds)

---

## 📦 Features

### 👤 Users
- User creation with email normalization
- Secure password hashing using BCrypt
- Safe deletion logic with order existence validation

### 🛒 Cart
- Cart management for authenticated users
- Guest cart support via sessionId
- Automatic quantity updates and item removal

### 📦 Orders
- Order creation from user or guest cart
- Dynamic price recalculation
- Optimized fetching using join fetch (findByIdWithDetails)

### 🧥 Products & Variants
- Support for multiple variants (size, color, price)
- SKU-based search with unique constraint validation
- Optimized variant removal using Hibernate Dirty Checking

---

## 🛠 Key Engineering Solutions

### 🔹 Database Optimization (Dirty Checking)
The service layer leverages Hibernate’s Dirty Checking mechanism.
This eliminates redundant .save() calls, reduces database round-trips, and keeps the codebase clean.

### 🔹 Infrastructure as Code (Docker)
The application is fully containerized using Docker.
A single docker-compose.yml file orchestrates both the Spring Boot app and PostgreSQL database.
Persistent storage is handled via Docker volumes.

### 🔹 Secure Secret Management
Sensitive data (like database credentials) is stored in environment variables and managed through a .env file, which is excluded from version control.

### 🔹 Global Error Handling
A centralized GlobalExceptionHandler handles all application exceptions and returns consistent JSON responses with meaningful error messages.

---

## 🏗 Architecture

The project follows a classic layered architecture:

Controller → Service → Repository → Entity

- Controller — handles HTTP requests and responses
- Service — contains business logic and transaction management
- Repository — interacts with the database via Spring Data JPA
- Entity — represents database models

---

## ⚙️ Getting Started

### 1. Clone the repository
git clone https://github.com/Kepchuk2/ecommerce-backend.git
cd ecommerce-backend

### 2. Setup environment variables
cp .env.example .env

Fill in your values (or use defaults for local testing).

### 3. Build and run
./mvnw clean package
docker-compose up --build

The API will be available at:
http://localhost:8080

---

## 🧪 Testing

Core business logic is covered with unit tests in key modules:

- CartService — guest and authenticated cart logic
- OrderService — order creation and price recalculation
- ProductService — variant management and deletion flows
- UserService — validation and secure user operations

---

## 📬 API Endpoints (Examples)

| Method | Endpoint | Description |
|--------|----------|------------|
| GET | /api/products | Retrieve all products |
| POST | /api/orders/from-user-cart/{id} | Create order from user cart |
| DELETE | /api/products/{id}/variants/{vId} | Remove product variant |

---

## 👨‍💻 Author

Rostyslav
Junior Java Developer 🚀