# Store Management API

A REST API for managing store products, built with Spring Boot and Maven.
> The application starts at `http://localhost:8080`.

## Features

- CRUD operations for products
- DTO – using Java `Record` — entity not directly exposed
- Input validation with `@NotBlank`, `@Min`
- Error handling and logging

## Demo Credentials

| Role  | Username | Password |
|-------|----------|----------|
| ADMIN | admin    | ##123    |
| USER  | user     | 1234#    |

> Credentials are defined in `application.properties` for easy testing.

## H2 Console

```
http://localhost:8080/h2-console/
JDBC URL: jdbc:h2:mem:store_db
Username: sa
Password: (empty)
```

> Note: H2 console is disabled by default. To enable it, set `spring.h2.console.enabled=true` in
`application.properties`.

## API Endpoints

| Method | Endpoint               | Role        | Description          |
|--------|------------------------|-------------|----------------------|
| GET    | `/products`            | USER, ADMIN | Get all products     |
| GET    | `/products/{id}`       | USER, ADMIN | Get product by ID    | 
| POST   | `/products`            | ADMIN       | Create new product   |
| PATCH  | `/products/{id}/price` | ADMIN       | Update product price |
| DELETE | `/products/{id}`       | ADMIN       | Delete product       |

### POST /products — Request Body

```json
{
  "name": "Laptop",
  "description": "something",
  "price": 5000,
  "category": "Electronics"
}
```

## Security

- Basic Authentication with two roles: `ADMIN` and `USER`
- `GET` requests — accessible by both roles
- `POST`, `PATCH`, `DELETE` — ADMIN only
- Passwords hashed with `BCryptPasswordEncoder`

When testing with Postman, set credentials in the Authorization tab – Basic Auth

## Testing

- Unit tests for `ProductService` with Mockito
- Integration tests for `ProductController` including authentication scenarios

## Technologies

- Java 21, Maven
- Spring Boot, Spring Security, Spring Data JPA, Lombok
- H2 in-memory database
- JUnit 5 & Mockito