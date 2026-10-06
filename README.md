# File Upload & Management System

A backend REST API built using **Java, Spring Boot, Spring Data JPA, and MySQL** for uploading and managing files.

## CODTECH INTERNSHIP

**NAME** : Syed Asif
**Intern ID:** CITS9357

## Features

* Upload files
* Store files in local storage
* Store file metadata in MySQL
* View all files
* View file details
* Download files
* Delete files
* Rename files
* Search files by name
* Filter files by file type
* Global exception handling
* 10 MB file upload limit

## Technologies Used

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* REST API
* Postman

## How It Works

The application separates file storage from metadata storage.

```text
Client / Postman
       |
       v
FileController
       |
       v
FileService
       |
       +------------------+
       |                  |
       v                  v
    MySQL              uploads/
  File Metadata       Actual Files
```

The actual files are stored in the `uploads` folder, while information such as filename, file type, size, and upload time is stored in MySQL.

## API Endpoints

| Method | Endpoint                               | Description         |
| ------ | -------------------------------------- | ------------------- |
| POST   | `/api/files/upload`                    | Upload a file       |
| GET    | `/api/files`                           | Get all files       |
| GET    | `/api/files/{id}`                      | Get file details    |
| GET    | `/api/files/{id}/download`             | Download a file     |
| DELETE | `/api/files/{id}`                      | Delete a file       |
| PUT    | `/api/files/{id}/rename`               | Rename a file       |
| GET    | `/api/files/search?name=resume`        | Search files        |
| GET    | `/api/files/type?type=application/pdf` | Filter by file type |

## Database Setup

Create the MySQL database:

```sql
CREATE DATABASE file_management;
```

The application automatically creates the required table using Hibernate.

## Configuration

The MySQL password is provided through an environment variable.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/file_management
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
```

For Windows PowerShell:

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

## Project Structure

```text
file-management/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/codtech/filemanagement/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── exception/
│       │       ├── repository/
│       │       └── service/
│       └── resources/
│           └── application.properties
│
├── uploads/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

## Error Handling

The application handles missing files using global exception handling.

Example:

```text
GET /api/files/999
```

Response:

```text
File not found with id: 999
```

## Author

**Syed Asif**

**Intern ID:** CITS9357
