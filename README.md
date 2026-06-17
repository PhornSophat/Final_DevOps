# ID Card Management Spring Boot Project

This project manages ID cards for students, employees, and general users.

## Features

- CRUD APIs for profiles and templates
- MySQL persistence with Spring Data JPA
- JPEG/PNG photo upload with 2MB validation
- Thymeleaf live ID-card preview
- Registration number generation in `YEAR-DEPT-###` format
- PDF export with iText
- Batch profile creation endpoint
- QR code and barcode generation with ZXing

## Run locally

Create a MySQL database, or let the JDBC URL create it automatically:

```bash
MYSQL_URL='jdbc:mysql://localhost:3306/id_card_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC' \
MYSQL_USER=root \
MYSQL_PASSWORD='Hello@123' \
./mvnw spring-boot:run
```

Open `http://localhost:8080`.

## Useful endpoints

- `GET /api/profiles`
- `POST /api/profiles` multipart form data
- `PUT /api/profiles/{id}` multipart form data
- `DELETE /api/profiles/{id}`
- `POST /api/profiles/batch`
- `GET /profiles/{id}/preview`
- `GET /api/profiles/{id}/pdf`
- `GET /api/profiles/{id}/qr`
- `GET /api/profiles/{id}/barcode`
- `GET /api/templates`

## Tests

```bash
./mvnw test
```
