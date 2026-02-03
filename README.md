# Smart Digital Banking System

A production-ready Banking System built with **Spring Boot**, **MySQL**, and **Vanilla JavaScript**. This project simulates a real-world digital banking environment, perfect for learning and **Automation Testing**.

## 🚀 Features

### Core Banking
*   **User Authentication**: Secure Login/Registration with JWT & Spring Security.
*   **Role-Based Access**:
    *   **Admin**: Manage users, view all accounts, block/unblock users.
    *   **User**: View dashboard, transfer funds, pay bills.
*   **Fund Transfers**: Real-time internal transfers between accounts.
*   **Profile Management**: Update user details and profile pictures.
*   **Transaction History**: View detailed logs of all deposits, withdrawals, and transfers.

### Tech Stack
*   **Backend**: Java 17, Spring Boot 3.2.0, Spring Security, Spring Data JPA.
*   **Frontend**: HTML5, CSS3, JavaScript (Vanilla).
*   **Database**: MySQL 8.0.
*   **Testing**: Selenium WebDriver, TestNG.

---

## 🛠️ Setup Instructions

### Prerequisites
*   Java 17+ installed.
*   Maven installed.
*   MySQL Server running.

### Step 1: Database Setup
1.  Open your MySQL Client (Workbench/Command Line).
2.  Create a database:
    ```sql
    CREATE DATABASE banking_system;
    ```
3.  Open `src/main/resources/application.properties` and update your credentials:
    ```properties
    spring.datasource.username=root
    spring.datasource.password=YOUR_PASSWORD
    ```

### Step 2: Build & Run
1.  Open a terminal in the project root.
2.  Run the application:
    ```bash
    mvn spring-boot:run
    ```
3.  The app will start on `http://localhost:8080`.

### Step 3: Default Credentials
*   **Admin**: `admin` / `admin123`
*   **User**: `user1` / `user123`

---

## 🤖 Automation Testing Guide (Selenium + Java)

This project is designed with automation in mind. All key elements have stable `id` attributes.

### Prerequisites for Testing
Ensure you have the following in your `pom.xml` (already included):
*   `selenium-java`
*   `testng`

### how to Run Tests
1.  **Ensure the Backend is Running**: The app must be live on `http://localhost:8080`.
2.  **Open Project in IDE** (IntelliJ/Eclipse).
3.  **Navigate to Test Folder**: `src/test/java/com/bank/system/tests`.
4.  **Run `LoginTest.java`**:
    *   Right-click `LoginTest.java` -> **Run 'LoginTest'**.

### Sample Test Code
Here is an example of how to write a test using the Page Object Model (POM):

**`src/test/java/com/bank/system/tests/LoginTest.java`**:
```java
package com.bank.system.tests;

import com.bank.system.base.BaseTest;
import com.bank.system.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test
    public void testValidLogin() {
        // Initialize Page Object
        LoginPage loginPage = new LoginPage(driver);

        // Perform Action
        loginPage.login("admin", "admin123");

        // Verify Result
        String expectedUrl = "http://localhost:8080/admin.html"; // or dashboard.html
        Assert.assertTrue(driver.getCurrentUrl().contains("admin") || driver.getCurrentUrl().contains("dashboard"), 
            "Login failed: URL did not redirect to dashboard/admin");
    }
}
```

### Automation-Friendly Attributes
| Page | Element | ID / Selector |
|------|---------|---------------|
| Login | Username Field | `#username` |
| Login | Password Field | `#password` |
| Login | Login Button | `#login-form button[type="submit"]` |
| Dashboard | Balance Display | `#balance-display` |
| Transfer | Recipient Input | `#recipient-account` |
| Transfer | Amount Input | `#transfer-amount` |

---

## ☁️ Deployment (Render/Cloud)
See the `cloud_deployment.md` file in the project root for detailed instructions on how to deploy this app for free using Render and Aiven.
