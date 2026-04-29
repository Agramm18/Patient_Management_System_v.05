# Patient Management System V5.01

Java console project for a patient management system rewrite. The current focus is the technical foundation: bootstrap flow, controller structure, `.env` validation, and MySQL connection setup.

## Current Status

The project currently starts through a simple bootstrap flow and routes requests into dedicated controllers.

Active flow right now:

1. Start `app.Main`
2. Show boot and loader messages
3. Run `BootConfigService`
4. Route to `ConfigController`
5. Validate the `.env` file
6. Read database values from `.env`
7. Try to establish the MySQL connection
8. Route to `AuthController`
9. Start the authentication area placeholder

## What Is Implemented

- Maven project setup via `pom.xml`
- Java 21 compiler target
- Console entry point in `app.Main`
- Bootstrap service in `app.Bootstrap.BootConfigService`
- Central routing through `FrontController`
- Configuration flow via `ConfigController`
- `.env` file validation with `dotenv-java`
- JDBC connection test for MySQL
- Installed dependencies for BCrypt password handling
- Base controller and service structure for future expansion

## What Changed Compared To The Old README

- Removed outdated descriptions about finished login, registration, role checks, and admin menu logic
- Removed old manual run instructions with `javac`, `lib/*`, and `run.sh`
- Removed references to files that are not present right now, such as `.env.example`
- Adjusted the documentation to the actual package structure under `src/main/java`
- Reduced the README to the current real project scope

## Current Project Structure

```text
src/main/java/app
|-- Main.java
|-- Auth
|   |-- LoginService.java
|   |-- PasswordService.java
|   `-- RegistrationService.java
|-- Bootstrap
|   `-- BootConfigService.java
|-- Config
|   |-- EnvValidationService.java
|   `-- SQLValidationService.java
|-- ConsoleView
|   `-- CLIText.java
`-- Controller
    |-- AuthController.java
    |-- ConfigController.java
    |-- FrontController.java
    |-- MenuController.java
    |-- ServiceController.java
    `-- uiController.java
```

## Requirements

- Java 21 or newer
- Maven 3.9+ recommended
- MySQL server
- `.env` file in the project root

## .env Setup

The application currently expects these keys:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=patient_management_v5
DB_USER=your_mysql_user
DB_PWSD=your_mysql_password
```

Important:

- The `.env` file must be located in the project root
- All values are required
- `DB_PORT` must be numeric

## Database Setup

The current configuration flow depends on a valid MySQL setup before the application starts. The program reads the database values from `.env` and then tries to build a JDBC connection with them.

Create the database:

```sql
CREATE DATABASE patient_management_v5;
```

Verify that the database exists:

```sql
SHOW DATABASES;
```

Switch into the database:

```sql
USE patient_management_v5;
```

Create the `accounts` table used by the current authentication foundation:

```sql
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    user_job VARCHAR(50) DEFAULT 'intern',
    user_role VARCHAR(50) DEFAULT 'user',
    account_status VARCHAR(50) DEFAULT 'disabled',
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

Optional checks:

```sql
SHOW TABLES;
SHOW COLUMNS FROM accounts;
```

Important for the current config flow:

- `DB_NAME` in `.env` must match the created database exactly
- The MySQL user from `DB_USER` must have access to that database
- `DB_PWSD` must match the password of that MySQL user
- The application currently checks the connection only; it does not create tables automatically

## Run The Project

Build:

```bash
mvn clean package
```

Start:

```bash
mvn exec:java
```

## Dependencies

- `dotenv-java`
- `mysql-connector-j`
- `jbcrypt`

## In Progress

- `Auth` services exist, but the login and registration logic is not implemented yet
- `MenuController`, `ServiceController`, and `uiController` are currently placeholders
- No patient management workflow is connected yet
- No persistent patient data logic exists yet

Developed by Agramm18 (c) 2026
