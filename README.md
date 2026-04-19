Store Management API

Features:

CRUD operations
Basic Authentication with 2 roles: - ADMIN - all the CRUD operations & USER (can only see the products)
                                   — add "local" in edit configuration → active profiles
Using Record for DTO – to not directly expose the entity
Validation input: @NotBlank, @Min
H2 database:
— http://localhost:8080/h2-console/

Security:

Postman test: check in the Auth tab that you have a username and pass before you try to add/get etc.
application-local.properties file contains passwords — added in .gitignore
Explicit rules per HTTP method: GET → authenticated, POST/PATCH/DELETE - ADMIN only

Testing:

Unit test for ProductService with Mockito
Integration test for ProductController + security tests

Endpoints:

GET: /products & /products/{id} – user and admin
POST /products – admin
{
"name": "Laptop",
"description": "something",
"price": 5000,
"category": "Electronics"
}
PATCH: /products/{id}/price – admin
DELETE: /products/{id} – admin

Technologies:
Java 21, Maven
Spring Boot, Security, Data & Lombok
H2 – database
JUnit5 & Mockito