🛒 E-commerce Backend (Spring Boot & Docker)
📖 About the Project
My name is Rostyslav, and I am a Java Backend Developer. This project is a scalable backend system for an e-commerce platform, built with a focus on Clean Architecture, data security, and database performance.

The system solves key e-commerce challenges: managing complex product relationships (variants, images), flexible cart handling, and reliable order lifecycles.

---

## 🚀 Tech Stack
- Java 21: Leveraging modern language features and syntax.
- Spring Boot 3.2.5: Core framework (Web, Data JPA, Security, Validation).
- PostgreSQL 15: Relational database for storing business entities.
- Hibernate 6: ORM with deep query optimization.
- Docker & Docker Compose: Full infrastructure containerization.
- JUnit 5 & Mockito: Unit testing of core business logic.
- Lombok: Boilerplate code reduction.

---

## 🏗 Architecture & Engineering Solutions

🔹 Database Optimization & Solving the N+1 Problem
- Fetch Joins: Critical repository queries utilize JOIN FETCH (JPQL) to load associated collections (cart items, order items) in a single DB trip, entirely avoiding the N+1 problem.

- Dirty Checking: The service layer relies on Hibernate's automatic dirty checking mechanism. This keeps the codebase free of redundant .save() calls and makes transactions more transparent.

- Indexing & Constraints: Unique constraints (e.g., on cart_id + variant_id and sku) are enforced at the DB level, guaranteeing data integrity even during concurrent requests.

🔹 Security & Validation
- Multi-Layer Validation: Jakarta Bean Validation at the DTO level ensures incoming data correctness (email format, password strength, positive prices) before it ever reaches the service layer.

- Security: User passwords are securely hashed using BCrypt. User deletion logic includes rigorous checks against existing orders to preserve financial history.

- Secret Management: All sensitive credentials (DB accounts) are externalized into environment variables and managed via a .env file, which is excluded from version control.
  
🔹 API Design & UX
- Flexible Carts: Supports both anonymous shoppers (via X-Session-Id headers) and authenticated users, with seamless cart tracking.

- RESTful Principles: Strict adherence to HTTP method semantics (GET, POST, PUT, DELETE, PATCH) and correct status code handling.

- Global Error Handling: Centralized exception handling via @RestControllerAdvice returns standardized ApiError JSON responses with detailed validation feedback.

---

## ⚙️ Getting Started

### 1. Environment Setup
Clone the repository and create your environment variables file:

git clone https://github.com/Kepchuk2/ecommerce-backend.git

cd ecommerce-backend

cp .env.example .env

### 2. Build & Run via Docker
The project is fully containerized and ready to launch:

./mvnw clean package -DskipTests

docker-compose up --build

Once running, the API will be available at: http://localhost:8080

---

## 🧪 Testing

Core application logic is covered by isolated unit tests using mocks:

- CartService: Adding, updating, and clearing carts.
- OrderService: Order creation and total price calculation.
- ProductService — variant management and deletion flows
- UserService: Registration, validation, and role management.

To run the tests locally: ./mvnw test

---

## 📬 API Endpoints (Examples)

| Method | Endpoint         | Description                                   |
|--------|------------------|-----------------------------------------------|
| GET    | /api/products    | Retrieve all products                         |
| POST   | /api/orders      | Create order from user cart (user or session) |
| GET    | /api/carts/guest | Retrieve guest cart by session ID             |
| DELETE | /api/products/{id}/variants/{vId} | Remove product variant                        |
---

## 👨‍💻 Author

Rostyslav

Junior Java Developer 🚀