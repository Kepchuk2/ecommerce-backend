# 🛒 E-commerce Backend (Spring Boot)

## 📖 About the Project

This project was built from scratch as part of my journey to become a Java Backend Developer.  
It focuses on real-world backend challenges such as entity relationships, transactions, lazy loading, and API design.

This is a backend application for an e-commerce system built with Java and Spring Boot.  
The project implements core business logic such as users, products, carts, and orders.

---

## 🚀 Tech Stack

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- PostgreSQL
- Maven
- Lombok
- Jakarta Validation

---

## 📦 Features

### 👤 Users
- Create user
- Get user by ID
- Get user by email
- Change password
- Delete user

### 🛒 Cart
- Cart for authenticated user
- Cart for guest (via sessionId)
- Add items to cart
- Get cart

### 📦 Orders
- Create order from user cart
- Create order from guest cart
- Get order by ID
- Get all orders for user
- Update order status

### 🧥 Products
- Create product
- Get product by ID
- Get products by category
- Get all products

### 🎯 Product Variants
- Add variant (size, color, price)
- Change variant price
- Find by SKU

### 🖼 Product Images
- Add images to product

---

## 🏗 Architecture

The project follows a layered architecture:

Controller → Service → Repository → Entity  
                    ↓  
                   DTO  
                    ↓  
                  Mapper  

- Controller – handles HTTP requests  
- Service – contains business logic  
- Repository – interacts with database  
- Entity – database models  
- DTO + Mapper – data transformation  

---

## ⚙️ Getting Started

### 1. Clone the repository

git clone https://github.com/Kepchuk2/ecommerce-backend.git  
cd ecommerce-backend

---

### 2. Setup PostgreSQL

Create a database:

shop_db

---

### 3. Configure application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/shop_db  
spring.datasource.username=YOUR_USERNAME  
spring.datasource.password=YOUR_PASSWORD  

spring.jpa.hibernate.ddl-auto=update  
spring.jpa.show-sql=true  

---

### 4. Run the application

./mvnw spring-boot:run  

Or run via your IDE.

---

## 📬 API Examples

### ➕ Create Product

POST /api/products

Request:
``json
{
  "name": "Hoodie",
  "description": "Cool hoodie",
  "category": "CLOTHING"
}

Response:
{
  "id": 1,
  "name": "Hoodie",
  "description": "Cool hoodie",
  "category": "CLOTHING"
}

---

### 🛒 Get Cart by sessionId

GET /api/carts/guest/{sessionId}

---

### 📦 Create Order from User Cart

POST /api/orders/from-user-cart/{userId}

---

## ❗ Error Handling

The project uses a global exception handler.  
All errors are returned in JSON format:

{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "User not found"
}

---

## 🔒 Security

- Passwords are hashed using BCrypt via Spring Security
- The current version does not implement authentication yet
- API endpoints are currently open for development and testing purposes

---

## 🧪 Tests

Unit tests are implemented for the core service layer to verify business logic and key service scenarios.

Current coverage includes:
- successful service operations
- validation and exception handling
- selected business edge cases

The tests validate business rules such as cart management, order creation, and product operations.

Covered services:
- CartService
- OrderService
- ProductService

Tests are written using:
- JUnit 5
- Mockito

The service layer is tested in isolation with mocked repositories, ensuring fast and focused verification of business logic without using the database.

---

## 📌 Future Improvements

- Authentication (login / JWT)
- Pagination & filtering
- Docker support
- Improved test coverage

---

## 👨‍💻 Author

Rostyslav  
Junior Java Developer
