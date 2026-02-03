# Smart Digital Banking System

A production-ready Banking System built with Spring Boot and Vanilla JS.

## Features
- **User & Security**: JWT Authentication, Role-based access (Admin/User).
- **Accounts**: create accounts, view balance.
- **Transactions**: Deposit, Withdraw, Transfer with history.
- **Admin**: Manage users and accounts.
- **UI**: Responsive dashboard.

## Prerequisites
- Java 17+
- Maven
- MySQL Database

## Setup Instructions

1. **Database Setup**
   - Create a MySQL database named `banking_system`.
   - Update `src/main/resources/application.properties` with your MySQL username and password.
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

2. **Build & Run**
   ```bash
   mvn spring-boot:run
   ```
   The application will start on `http://localhost:8080`.

3. **Access the Application**
   - Open Browser: `http://localhost:8080/index.html`
   - **Default Admin Credentials**:
     - Username: `admin`
     - Password: `admin123`

## API Documentation
Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

## Automation Testing
- All buttons and inputs have stable IDs (e.g., `#login-form`, `#username`, `#btn-login`).
- Alert boxes use class `.alert` for easy detection.

## Project Structure
- `src/main/java`: Backend Logic
- `src/main/resources/static`: Frontend HTML/CSS/JS
