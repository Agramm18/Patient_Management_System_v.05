# Patient Management System V5

This is the current Java rewrite of my long-running Patient Management System project.

I originally developed this project in Python to strengthen my software architecture skills and learn the language from the ground up. This version is being rewritten in Java, primarily because Java is part of my apprenticeship curriculum, and the way it is taught there does not meet my learning standards. I want to understand OOP and software design in Java on my own terms, in a way that actually sticks.

The original direction of the project is still the same: build a larger patient management application over time, improve the architecture step by step, and use each iteration to learn software design more deeply.

---

## Current Project Status

The repository is currently a console-based Java application with a working account and authentication foundation.

The active runtime flow is:

1. Start the application
2. Validate the `.env` file
3. Establish the MySQL connection
4. Ask the user to log in or register
5. Validate the account status
6. Validate the user role
7. Show the local admin menu when the account is allowed to enter it

---

## What Is Implemented Right Now

- CLI entry point and loader
- `.env` validation for the database configuration
- MySQL connection through JDBC
- Registration with BCrypt password hashing
- Login with password verification against the stored hash
- Account status checks via the `accounts` table
- Role-based routing for the `local_admin` path
- A first local admin control panel shell
- A legacy patient intake prototype that can collect and display patient data in memory

---

## What Is Present But Not Finished Yet

- Patient data is not yet stored in the database
- The legacy patient flow is currently not connected to the live login and role flow
- Most local admin menu actions are still placeholders
- The menu currently shows `Logout`, but that option is not wired yet
- Non-admin roles do not yet have their own UI flow
- JavaFX is not part of the current runtime, even though it was part of the earlier direction for the project
- There is no Maven or Gradle build yet; the project is compiled manually from the checked-in JAR files

---

## Requirements

- Java 21 or newer
- MySQL server
- A real terminal session

> **Important:** The password flow uses `System.console()`. Running the program from an IDE output pane usually will not work correctly. Use Command Prompt, PowerShell, or another real terminal.

---

## Database Setup

### 1. Create the Database

Start by creating the database. The application currently expects this database name:

```sql
CREATE DATABASE patient_management_v5;
```

Verify it was created:

```sql
SHOW DATABASES;
```

Switch into the database:

```sql
USE patient_management_v5;
```

---

### 2. Create the `accounts` Table

This table is required for registration, login, account status checks, and role-based routing:

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

Verify the setup:

```sql
USE patient_management_v5;
SHOW TABLES;
SHOW COLUMNS FROM accounts;
```

---

### 3. Create the `.env` File

Create the `.env` file in the project root:

```text
/lib
/src
/docs
.env.example
.gitignore
.env
README.md
```

The application expects the following database connection entries inside this file:

```env
DB_HOST=localhost
DB_PORT=3306
DB_USER=YOUR_MYSQL_USER
DB_NAME=patient_management_v5
DB_PWSD=YOUR_MYSQL_PASSWORD
```

The easiest setup is:

1. Copy `.env.example` to `.env`
2. Replace the placeholder values with your real MySQL login data
3. Keep the key names exactly as written, because the current loader checks for those exact names

What each value means:

- `DB_HOST` defines where the MySQL server is running. `localhost` means the database runs on the same machine as the application.
- `DB_PORT` defines which MySQL port should be used. `3306` is the standard MySQL default.
- `DB_USER` is the MySQL username the application uses to log in to the database.
- `DB_NAME` is the database name the program should connect to. In this project it should match `patient_management_v5`.
- `DB_PWSD` is the password for the MySQL user from `DB_USER`. Do not commit real passwords to Git.

What you need to set up before the program starts:

1. MySQL must be installed and running.
2. The database `patient_management_v5` must already exist.
3. The MySQL user from `DB_USER` must exist and have access to that database.
4. The password in `DB_PWSD` must match that MySQL user exactly.
5. The `.env` file must sit in the project root, not inside `src/` or another folder.

Why this file is required:

- On startup, the program checks whether `.env` exists.
- It then reads the five values above.
- If one of them is missing or empty, the loader stops before the SQL connection step.
- If the values exist but are wrong, the JDBC connection to MySQL fails and the application cannot continue into login or registration.

In short: without a valid `.env` file, the current application cannot connect to MySQL and the account system will not work.

### `.gitignore` and Safe Local Configuration

The repository now also includes a `.gitignore` file.

Its purpose is to keep local or sensitive files out of Git, especially:

- `.env`, so real database credentials are not committed by accident
- `/.VSCodeCounter`, because it is local tooling output
- `/docs`, if you want to keep local documentation artifacts out of commits in the current setup

This means the intended workflow is:

- keep example values in `.env.example`
- keep real local values only in `.env`
- do not commit passwords or machine-specific configuration

---

### 4. Prepare an Account for the Current Runtime Flow

A newly registered account is inserted into the `accounts` table, but with the defaults shown above it will remain disabled and will not enter the currently implemented menu flow.

To test the active runtime path, enable the account and assign the local admin role after registration:

```sql
UPDATE accounts
SET account_status = 'enabled',
    user_role = 'local_admin'
WHERE account_name = 'YOUR_ACCOUNT_NAME';
```

Without that change, the current code will stop at the account status or role step.

---

## Build and Run on Windows

The repository currently uses the checked-in JAR files from `lib/` and can be compiled manually.

### Compile

```bat
cmd /c "if not exist out mkdir out && dir /s /b src\*.java > sources.txt && javac -cp lib\mysql-connector-j-9.6.0.jar;lib\jbcrypt-0.4.jar;lib\dotenv-java-3.0.0.jar -d out @sources.txt && del sources.txt"
```

### Run

```bat
java -cp "out;lib/mysql-connector-j-9.6.0.jar;lib/jbcrypt-0.4.jar;lib/dotenv-java-3.0.0.jar" app.Main
```

---

## Relevant Project Structure

- `src/app/Main.java` - application entry point
- `src/app/controller/SystemLoader.java` - bootstraps configuration and database loading
- `src/app/controller/auth/` - registration, login, password handling, account flow
- `src/app/db/auth/` - environment checks, JDBC connection, account status and role checks
- `src/app/menu/` - role-based menu controller and admin menu UI
- `src/app/service/AddPatient.java` - legacy patient intake prototype
- `.env.example` - template for local database configuration
- `.gitignore` - prevents local config and ignored folders from being committed
- `docs/uml/current_program_flow.puml` - current flow documentation
- `lib/` - project dependencies kept locally in the repository

---

## Roadmap

The original roadmap is still relevant, but based on the current codebase the next realistic steps are:

- Connect the patient flow to the authenticated menu flow
- Persist patient data in the database
- Finish the local admin actions beyond the current placeholder state
- Add UI fallbacks for more roles than `local_admin`
- Improve validation, error handling, and query structure
- Introduce a proper build tool and tests
- Revisit GUI work after the CLI flow is stable
- Later: Docker packaging and possibly Kubernetes experiments

The original planned improvement areas remain valid:

- Database logic and query optimization
- GUI design and user experience
- Input verification and authentication mechanisms

The original future feature ideas also remain part of the long-term direction:

- Docker image for containerized deployment
- Potential Kubernetes support

---

> **Note:** This is a personal learning project aimed at deepening the skills described above. It is not intended to be a production-ready software product.
>
> All rights reserved by the developer **Agramm18**.
