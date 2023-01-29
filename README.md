# Spring JWT Authentication With MySQL

This is a simple Spring Boot application that uses JWT for authentication using OAuth2 Resource Server. The application
uses MySQL as the database.

## Run Locally

1. Clone the project

```bash
git clone https://github.com/sandaruanjana/springboot-oauth2-jwt.git
```

2. Go to the project directory

```bash
cd springboot-oauth2-jwt
```

3. Install dependencies

```bash
mvn install
```

4. Run the application

```bash
mvn spring-boot:run
```

## Database Schema

The database schema is in the `schema.sql` file in the `resources` folder.

## API Reference

#### Sign up

```http
  POST /api/auth/v1/signup
```

JSON Body

```json
{
  "username": "user",
  "password": "1234"
}
```

#### Sign in

```http
  POST /api/auth/v1/login
```

JSON Body

```json
{
  "username": "user",
  "password": "1234"
}
```

#### Refresh Token

```http
  POST /api/auth/v1/refresh
```

#### Header

```http
  Authorization: Bearer <access_token>
```

### Form Data

```http
  refreshToken: <refresh_token>
```

#### Tech Stack

- Spring Boot
- Spring Security
- Spring Data JDBC
- OAuth2 Resource Server
- MySQL
- JWT
- Maven
- Lombok



