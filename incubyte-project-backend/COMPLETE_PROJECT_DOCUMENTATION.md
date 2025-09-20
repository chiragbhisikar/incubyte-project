# Incubyte Project Backend - Complete Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Project Structure](#project-structure)
4. [Database Schema](#database-schema)
5. [API Documentation](#api-documentation)
6. [Security Implementation](#security-implementation)
7. [Service Layer](#service-layer)
8. [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
9. [Exception Handling](#exception-handling)
10. [Configuration](#configuration)
11. [Testing](#testing)
12. [Deployment](#deployment)
13. [Development Guidelines](#development-guidelines)

---

## Project Overview

**Incubyte Project Backend** is a Spring Boot-based REST API application for managing a sweet shop inventory system. The application provides comprehensive functionality for user authentication, sweet management, inventory tracking, and sales operations.

### Key Features:
- **User Authentication & Authorization**: JWT-based authentication with role-based access control
- **Sweet Management**: CRUD operations for sweet inventory
- **Inventory Management**: Stock tracking, purchasing, and restocking
- **Search & Catalog**: Advanced search and filtering capabilities
- **Security**: Comprehensive security implementation with Spring Security
- **Validation**: Input validation using Bean Validation (JSR-303)
- **Exception Handling**: Global exception handling with custom exceptions

### Business Domain:
The application manages a sweet shop with the following core entities:
- **Users**: Customers and administrators
- **Sweets**: Products with categories, prices, and inventory
- **Transactions**: Purchase and restock operations

---

## Technology Stack

### Core Framework
- **Spring Boot 3.5.5**: Main application framework
- **Java 21**: Programming language
- **Maven**: Build and dependency management

### Database & Persistence
- **MySQL 8.0.33**: Primary database
- **Spring Data JPA**: Data access layer
- **Hibernate**: ORM implementation

### Security
- **Spring Security**: Authentication and authorization
- **JWT (JSON Web Tokens)**: Token-based authentication
- **BCrypt**: Password encoding

### Validation & Mapping
- **Bean Validation (JSR-303)**: Input validation
- **ModelMapper 3.2.4**: Object mapping
- **Lombok**: Code generation

### Testing
- **JUnit 5**: Unit testing framework
- **Mockito**: Mocking framework
- **Spring Boot Test**: Integration testing
- **MockMvc**: Web layer testing

### Development Tools
- **Spring Boot DevTools**: Hot reloading
- **Maven Wrapper**: Consistent build environment

---

## Project Structure

```
src/
├── main/
│   ├── java/com/incubyte/incubyte_project_backend/
│   │   ├── configuration/          # Configuration classes
│   │   │   ├── AuthenticationManagerConfig.java
│   │   │   ├── MapperConfig.java
│   │   │   └── SecurityEncoderConfig.java
│   │   ├── controller/             # REST controllers
│   │   │   ├── AuthController.java
│   │   │   └── SweetController.java
│   │   ├── dto/                    # Data Transfer Objects
│   │   │   ├── request/            # Request DTOs
│   │   │   │   ├── auth/
│   │   │   │   │   ├── LoginRequestDto.java
│   │   │   │   │   └── SignUpRequestDto.java
│   │   │   │   └── sweet/
│   │   │   │       ├── AddSweetRequest.java
│   │   │   │       ├── UpdateSweetRequest.java
│   │   │   │       ├── PurchaseRequest.java
│   │   │   │       ├── RestockRequest.java
│   │   │   │       └── SearchSweetRequest.java
│   │   │   └── response/           # Response DTOs
│   │   │       ├── ApiResponse.java
│   │   │       ├── ApiError.java
│   │   │       ├── auth/
│   │   │       │   ├── LoginResponseDto.java
│   │   │       │   └── SignupResponseDto.java
│   │   │       └── sweet/
│   │   │           └── SweetSoldResponse.java
│   │   ├── entity/                 # JPA entities
│   │   │   ├── User.java
│   │   │   ├── Sweet.java
│   │   │   └── type/
│   │   │       ├── RoleType.java
│   │   │       └── PermissionType.java
│   │   ├── exception/              # Custom exceptions
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── UserAlreadyExistException.java
│   │   │   ├── UserNotFoundException.java
│   │   │   ├── SweetNotFoundException.java
│   │   │   └── NotEnoughStockException.java
│   │   ├── repository/             # Data access layer
│   │   │   ├── user/
│   │   │   │   └── UserRepository.java
│   │   │   └── sweet/
│   │   │       ├── SweetRepository.java
│   │   │       ├── SweetAvailabilityRepository.java
│   │   │       └── SweetSearchRepository.java
│   │   ├── security/               # Security implementation
│   │   │   ├── access/
│   │   │   ├── adapter/
│   │   │   ├── auth/
│   │   │   ├── contract/
│   │   │   ├── filter/
│   │   │   ├── jwt/
│   │   │   ├── permission/
│   │   │   ├── role/
│   │   │   ├── service/
│   │   │   ├── token/
│   │   │   ├── user/
│   │   │   ├── SecurityUser.java
│   │   │   └── WebSecurityConfig.java
│   │   ├── service/                # Business logic layer
│   │   │   └── sweet/
│   │   │       ├── SweetCatalogService.java
│   │   │       ├── SweetCatalogServiceImpl.java
│   │   │       ├── SweetInventoryService.java
│   │   │       ├── SweetInventoryServiceImpl.java
│   │   │       ├── SweetManagementService.java
│   │   │       └── SweetManagementServiceimpl.java
│   │   └── IncubyteProjectBackendApplication.java
│   └── resources/
│       ├── application.properties
│       └── mysql-connector-j-9.0.0.jar
└── test/                           # Test classes
    └── java/com/incubyte/incubyte_project_backend/
        ├── dto/request/
        ├── security/service/
        └── IncubyteProjectBackendApplicationTests.java
```

---

## Database Schema

### Users Table
```sql
CREATE TABLE user (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id VARCHAR(36),
    roles VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### Sweets Table
```sql
CREATE TABLE sweets (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Entity Relationships
- **User** → **Roles**: One-to-Many (User can have multiple roles)
- **Sweet**: Standalone entity with no foreign key relationships

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### 1. User Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "user@example.com",
    "password": "Password@123"
}
```

**Response (200 OK):**
```json
{
    "message": "Login successful",
    "data": {
        "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        "userId": "123e4567-e89b-12d3-a456-426614174000"
    }
}
```

#### 2. User Registration
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "newuser@example.com",
    "password": "NewPassword@123"
}
```

**Response (201 Created):**
```json
{
    "message": "User Registered Successfully !",
    "data": {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "username": "newuser@example.com"
    }
}
```

### Sweet Management Endpoints

#### 1. Get All Sweets
```http
GET /api/sweets
Authorization: Bearer <jwt-token>
```

#### 2. Get Sweet by ID
```http
GET /api/sweets/{sweetId}
Authorization: Bearer <jwt-token>
```

#### 3. Get Available Sweets
```http
GET /api/sweets/available
Authorization: Bearer <jwt-token>
```

#### 4. Get Out-of-Stock Sweets
```http
GET /api/sweets/not-available
Authorization: Bearer <jwt-token>
```

#### 5. Search Sweets
```http
GET /api/sweets/search?name=chocolate&category=candy&minPrice=1.0&maxPrice=10.0
Authorization: Bearer <jwt-token>
```

#### 6. Add New Sweet (Admin Only)
```http
POST /api/sweets
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
    "name": "Chocolate Bar",
    "category": "Chocolate",
    "price": 2.50,
    "quantity": 100
}
```

#### 7. Update Sweet (Admin Only)
```http
PUT /api/sweets/{sweetId}
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
    "name": "Premium Chocolate Bar",
    "price": 3.00,
    "quantity": 150
}
```

#### 8. Delete Sweet (Admin Only)
```http
DELETE /api/sweets/{sweetId}
Authorization: Bearer <jwt-token>
```

#### 9. Purchase Sweet
```http
POST /api/sweets/{sweetId}/purchase
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
    "quantity": 5
}
```

#### 10. Restock Sweet (Admin Only)
```http
POST /api/sweets/{sweetId}/restock
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
    "quantity": 50
}
```

### HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Validation error |
| 401 | Unauthorized - Authentication required |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Business logic violation |
| 415 | Unsupported Media Type - Wrong content type |
| 500 | Internal Server Error - Server error |

---

## Security Implementation

### Authentication Flow
1. **User Registration**: User provides email and password
2. **Password Encoding**: Password is encoded using BCrypt
3. **User Login**: Credentials are validated against database
4. **JWT Generation**: Upon successful authentication, JWT token is generated
5. **Token Validation**: Subsequent requests include JWT token for authorization

### Authorization
- **Role-Based Access Control (RBAC)**: Users have roles (USER, ADMIN)
- **Method-Level Security**: `@PreAuthorize` annotations on service methods
- **Endpoint Protection**: Different access levels for different endpoints

### Security Components

#### 1. JWT Token Provider
- **Token Generation**: Creates JWT tokens with user information
- **Token Validation**: Validates incoming JWT tokens
- **Token Expiration**: Configurable token expiration time

#### 2. Authentication Manager
- **Credential Validation**: Validates user credentials
- **Authentication Provider**: Custom JWT authentication provider

#### 3. Security Filter Chain
- **JWT Filter**: Intercepts requests and validates JWT tokens
- **CORS Configuration**: Cross-origin resource sharing setup
- **CSRF Protection**: Disabled for stateless API

#### 4. User Details Service
- **User Loading**: Loads user details from database
- **Authority Mapping**: Maps user roles to Spring Security authorities

### Security Configuration
```java
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
    // Security filter chain configuration
    // JWT filter integration
    // Access rule configuration
}
```

---

## Service Layer

### Authentication Services

#### IAuthenticationService
- **login()**: Authenticates user and returns JWT token
- **Dependencies**: AuthenticationManager, ITokenProvider

#### IRegistrationService
- **register()**: Registers new user with encoded password
- **Dependencies**: UserRepository, PasswordEncoder

### Sweet Services

#### SweetCatalogService
- **getAllSweetOfStore()**: Retrieves all sweets
- **getSweetById()**: Retrieves sweet by ID
- **getAllAvailableSweet()**: Retrieves available sweets
- **getNotAvailableSweet()**: Retrieves out-of-stock sweets
- **searchSweet()**: Advanced search with filters

#### SweetInventoryService
- **purchaseSweet()**: Handles sweet purchase with stock validation
- **restockSweet()**: Adds inventory to existing sweet

#### SweetManagementService
- **addSweet()**: Adds new sweet (Admin only)
- **updateSweet()**: Updates existing sweet (Admin only)
- **deleteSweet()**: Deletes sweet (Admin only)

### Service Implementation Patterns
- **Interface Segregation**: Each service has specific responsibilities
- **Dependency Injection**: Services depend on abstractions
- **Transaction Management**: `@Transactional` for data consistency
- **Security Integration**: `@PreAuthorize` for method-level security

---

## Data Transfer Objects (DTOs)

### Request DTOs

#### Authentication DTOs
```java
// LoginRequestDto
{
    "username": "user@example.com",    // @NotBlank, @Email
    "password": "Password@123"         // @NotBlank
}

// SignUpRequestDto
{
    "username": "user@example.com",    // @NotBlank, @Email
    "password": "Password@123"         // @NotBlank, @Pattern (complexity rules)
}
```

#### Sweet DTOs
```java
// AddSweetRequest
{
    "name": "Chocolate Bar",           // @NotBlank, @Pattern, @Length(3-50)
    "category": "Chocolate",           // @NotBlank, @Pattern, @Length(2-50)
    "price": 2.50,                     // @NotNull, @Positive
    "quantity": 100                    // @NotNull, @Min(1)
}

// UpdateSweetRequest
{
    "name": "Premium Chocolate",       // @Pattern, @Size(2-50)
    "category": "Premium",             // @Pattern, @Size(2-30)
    "price": 3.00,                     // @DecimalMin(0.01)
    "quantity": 150                    // @Min(0)
}

// PurchaseRequest
{
    "quantity": 5                      // @NotNull, @Min(1)
}

// RestockRequest
{
    "quantity": 50                     // @NotNull, @Min(1)
}

// SearchSweetRequest
{
    "name": "chocolate",               // Optional
    "category": "candy",               // Optional
    "minPrice": 1.0,                   // @PositiveOrZero
    "maxPrice": 10.0                   // @Positive
}
```

### Response DTOs

#### Authentication Responses
```java
// LoginResponseDto
{
    "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": "123e4567-e89b-12d3-a456-426614174000"
}

// SignupResponseDto
{
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "username": "user@example.com"
}
```

#### Sweet Responses
```java
// SweetSoldResponse
{
    "sweet": { /* Sweet object */ },
    "quantity": 95,                    // Remaining quantity
    "totalAmount": 12.50               // Total purchase amount
}
```

#### Generic Response
```java
// ApiResponse
{
    "message": "Operation successful",
    "data": { /* Response data */ }
}
```

### Validation Rules

#### Email Validation
- **Required**: Cannot be null, blank, or whitespace
- **Format**: Valid email format with @ and domain
- **Pattern**: Standard email regex validation

#### Password Validation (Registration)
- **Required**: Cannot be null or empty
- **Length**: Minimum 8 characters
- **Complexity**: Must contain:
  - At least one uppercase letter (A-Z)
  - At least one lowercase letter (a-z)
  - At least one digit (0-9)
  - At least one special character (#@$!%*?&)

#### Sweet Name/Category Validation
- **Required**: Cannot be null or blank
- **Length**: 2-50 characters
- **Pattern**: Only letters, spaces, hyphens, apostrophes, and ampersands
- **No Numbers**: Cannot contain numeric characters

#### Price Validation
- **Required**: Cannot be null
- **Positive**: Must be greater than 0
- **Decimal**: Supports decimal values

#### Quantity Validation
- **Required**: Cannot be null
- **Minimum**: Must be at least 1 (for operations)
- **Non-negative**: Cannot be negative (for updates)

---

## Exception Handling

### Global Exception Handler
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Centralized exception handling
    // Consistent error response format
    // HTTP status code mapping
}
```

### Custom Exceptions

#### UserAlreadyExistException
- **Trigger**: Duplicate email registration
- **HTTP Status**: 208 Already Reported
- **Message**: "User Already Exist !"

#### UserNotFoundException
- **Trigger**: User not found in database
- **HTTP Status**: 404 Not Found
- **Message**: "User not found"

#### SweetNotFoundException
- **Trigger**: Sweet not found by ID
- **HTTP Status**: 404 Not Found
- **Message**: "Sweet not found with id: {id}"

#### NotEnoughStockException
- **Trigger**: Insufficient stock for purchase
- **HTTP Status**: 409 Conflict
- **Message**: "Sweet Has Not Enough Quantity We Can Provide Only {quantity} Quantities !"

### Validation Exceptions
- **MethodArgumentNotValidException**: Bean validation failures
- **HttpMessageNotReadableException**: JSON parsing errors
- **ConstraintViolationException**: Database constraint violations

### Error Response Format
```json
{
    "message": "Error description",
    "data": null,
    "timestamp": "2024-01-01T12:00:00Z",
    "path": "/api/endpoint"
}
```

---

## Configuration

### Application Properties
```properties
# Application
spring.application.name=SweetShopManagementSystem

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost/sweet-shop-management-system
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=Chirag#2208

# JPA Configuration
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=update

# JWT Configuration
jwt.secretKey=asdfhads9f67as98dfyaisudhfa98s67dfy89aishudfuays89dfyasi8df7asdf87987g98a7sg986a89sdf7y
```

### Configuration Classes

#### AuthenticationManagerConfig
- **AuthenticationManager Bean**: Configures authentication manager
- **Password Encoder**: BCrypt password encoder configuration

#### MapperConfig
- **ModelMapper Bean**: Object mapping configuration
- **Mapping Rules**: Entity to DTO mapping rules

#### SecurityEncoderConfig
- **Password Encoder**: BCrypt configuration
- **Encoding Strength**: BCrypt strength configuration

---

## Testing

### Test Structure
```
src/test/java/
├── dto/request/                     # DTO validation tests
├── security/service/                # Security service tests
├── service/sweet/                   # Sweet service tests
├── controller/                      # Controller tests
└── IncubyteProjectBackendApplicationTests.java
```

### Test Types

#### 1. Unit Tests
- **Service Layer Tests**: Business logic testing with mocked dependencies
- **DTO Validation Tests**: Bean validation testing
- **Repository Tests**: Data access layer testing

#### 2. Integration Tests
- **Controller Tests**: End-to-end API testing with MockMvc
- **Security Tests**: Authentication and authorization testing
- **Database Tests**: Repository integration testing

#### 3. Test Coverage
- **Authentication APIs**: Login and registration testing
- **Sweet Management APIs**: CRUD operations testing
- **Validation Testing**: Input validation testing
- **Exception Handling**: Error scenario testing
- **Security Testing**: Role-based access testing

### Test Execution
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=AuthControllerTest

# Run tests with coverage
./mvnw test jacoco:report
```

### Test Data
- **Valid Test Data**: Properly formatted test data
- **Invalid Test Data**: Edge cases and validation failures
- **Mock Data**: Test doubles for external dependencies

---

## Deployment

### Prerequisites
- **Java 21**: Runtime environment
- **MySQL 8.0+**: Database server
- **Maven 3.6+**: Build tool

### Build Process
```bash
# Clean and compile
./mvnw clean compile

# Run tests
./mvnw test

# Package application
./mvnw package

# Run application
java -jar target/incubyte-project-backend-0.0.1-SNAPSHOT.jar
```

### Environment Setup
1. **Database Setup**: Create MySQL database
2. **Configuration**: Update application.properties
3. **Dependencies**: Install required dependencies
4. **Build**: Compile and package application
5. **Deploy**: Run application with proper configuration

### Production Considerations
- **Database Security**: Secure database credentials
- **JWT Secret**: Use strong, unique JWT secret key
- **CORS Configuration**: Configure for production domains
- **Logging**: Implement proper logging configuration
- **Monitoring**: Add health checks and monitoring

---

## Development Guidelines

### Code Standards
- **Java 21 Features**: Use modern Java features
- **Spring Boot Best Practices**: Follow Spring Boot conventions
- **Clean Code**: Write readable and maintainable code
- **Documentation**: Document public APIs and complex logic

### Architecture Patterns
- **Layered Architecture**: Controller → Service → Repository
- **Dependency Injection**: Use constructor injection
- **Interface Segregation**: Separate interfaces for different concerns
- **Single Responsibility**: Each class has one responsibility

### Security Best Practices
- **Input Validation**: Validate all inputs
- **Password Security**: Use strong password policies
- **JWT Security**: Secure JWT token handling
- **Access Control**: Implement proper authorization

### Database Best Practices
- **Entity Design**: Proper entity relationships
- **Indexing**: Add indexes for performance
- **Transactions**: Use transactions for data consistency
- **Migration**: Use database migrations for schema changes

### Testing Best Practices
- **Test Coverage**: Maintain high test coverage
- **Test Isolation**: Tests should be independent
- **Mock Usage**: Use mocks for external dependencies
- **Test Data**: Use consistent test data

### API Design
- **RESTful Design**: Follow REST principles
- **Consistent Responses**: Use consistent response format
- **Error Handling**: Provide meaningful error messages
- **Documentation**: Document all API endpoints

---

## Future Enhancements

### Planned Features
1. **User Profile Management**: User profile CRUD operations
2. **Order Management**: Order creation and tracking
3. **Payment Integration**: Payment gateway integration
4. **Reporting**: Sales and inventory reports
5. **Audit Logging**: Comprehensive audit trail
6. **Caching**: Redis caching for performance
7. **File Upload**: Image upload for sweets
8. **Email Notifications**: Email service integration

### Technical Improvements
1. **API Versioning**: API version management
2. **Rate Limiting**: API rate limiting
3. **Swagger Documentation**: API documentation
4. **Docker Support**: Containerization
5. **CI/CD Pipeline**: Automated deployment
6. **Monitoring**: Application monitoring
7. **Performance Optimization**: Query optimization
8. **Security Hardening**: Enhanced security measures

### Scalability Considerations
1. **Database Optimization**: Query optimization and indexing
2. **Caching Strategy**: Multi-level caching
3. **Load Balancing**: Horizontal scaling support
4. **Microservices**: Service decomposition
5. **Event-Driven Architecture**: Asynchronous processing
6. **Database Sharding**: Data partitioning strategies

---

## Conclusion

The Incubyte Project Backend is a comprehensive Spring Boot application that demonstrates modern Java development practices, security implementation, and API design. The application provides a solid foundation for a sweet shop management system with room for future enhancements and scalability improvements.

### Key Strengths:
- **Modern Technology Stack**: Latest Spring Boot and Java features
- **Comprehensive Security**: JWT-based authentication with role-based access
- **Clean Architecture**: Well-structured layered architecture
- **Extensive Testing**: Comprehensive test coverage
- **Input Validation**: Robust validation and error handling
- **Documentation**: Detailed documentation and API specifications

### Learning Outcomes:
- Spring Boot application development
- JWT-based authentication implementation
- RESTful API design and development
- Database design and JPA implementation
- Security best practices
- Testing strategies and implementation
- Exception handling and error management
- Project documentation and maintenance

This project serves as an excellent example of enterprise-level Spring Boot application development with modern security practices and comprehensive testing strategies.
