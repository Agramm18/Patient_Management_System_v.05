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

Create the database:

```sql
CREATE DATABASE patient_management_v5;
```

The current code only verifies whether a connection to the configured MySQL database can be established. It does not yet create tables automatically.

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
