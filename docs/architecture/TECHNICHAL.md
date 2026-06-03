# Technical Overview

Last synchronized: 2026-06-03.

## Development Tools

The following tools and technologies are currently used by this project:

- **Java 21** as the application language and runtime target
- **Maven** for build configuration and dependency management
- **exec-maven-plugin** 3.1.0 to run `app.Main` through `mvn exec:java`
- **MySQL** as the relational database system
- **JDBC** for database access from Java
- **`.env` configuration** for local database, starter account, bootstrap, and recovery-key values
- **SQL setup files and docs** for database schema, seed data, migrations, and verification queries

## Libraries and Dependencies

The project currently uses these Maven dependencies:

- **dotenv-java** 3.0.0 (`io.github.cdimascio`) for loading environment variables from `.env` files
- **mysql-connector-j** 9.6.0 (`com.mysql`) for MySQL database connectivity
- **jbcrypt** 0.4 (`org.mindrot`) for password hashing, password verification, and recovery-key verification

The application also uses Java standard library and JDBC components such as:

- `Scanner`
- `Console`
- `Connection`
- `DriverManager`
- `PreparedStatement`
- `ResultSet`
- `SQLException`

These components are used for SQL operations including:

- `SELECT`
- `INSERT`
- `UPDATE`

## Runtime Architecture

The active runtime is a console application with a controller-driven startup and authentication flow:

```text
Main -> BootConfigService -> FrontController -> ConfigController -> AuthController
```

Currently active controller phases:

- `CONFIG`
- `AUTH`

Reserved controller phases:

- `MENU`
- `SERVICE`
- `UI`
- `EXIT`

`MENU`, `SERVICE`, and `UI` are placeholders for later application phases.

## Current Technical Behavior

Implemented technical foundations:

- Startup validation for `.env` values.
- Startup fail-fast behavior when environment validation, database connection validation, or DB runtime initialization fails.
- JDBC connection setup through `SQLValidationService` and `DBManager`.
- Starter account creation for missing `local_admin` and `admin` accounts.
- BCrypt password hashing for starter accounts, registration, password change, and recovery.
- Terminal-only hidden password input through `System.console()` for login, password creation, and recovery-key entry.
- Login attempt persistence in `login_attempts`.
- 24-hour failed login counting groundwork through `CountFailedLoginAttempts`.
- Recovery-key startup storage in `recovery_keys.id = 1`.
- Recovery-key validation and password-hash reset for a selected existing account.
- Pending-user access request storage with selected department and default job/role values.

## Security Model Status

Implemented:

- Passwords are stored as BCrypt hashes.
- Recovery keys are stored as BCrypt hashes.
- Login password checks use BCrypt verification.
- Recovery-key checks use BCrypt verification.
- Starter accounts must change their generated/default password before becoming active.
- Password input fails fast when no terminal-backed `System.console()` is available.

Partially implemented:

- Failed-login thresholds are detected based on recent login attempts.
- `ExecutePWSDPolicy` contains placeholder methods for locking, suspicious activity, and quarantine.
- Pending access requests are persisted but not approved or rejected through an admin workflow yet.

Not implemented yet:

- Persistent failed-login status updates.
- Admin approval or rejection workflow.
- Role/job-based runtime menu routing.
- Automated security tests.

## Development Environments

- IntelliJ IDEA as the primary development environment
- DataGrip for database development and management
- Visual Studio Code used during earlier project versions

## Version Control And Collaboration

- Git for version control
- GitHub for repository hosting and source code management

## Documentation Tools

- Markdown (`.md`) files for project documentation
- Mermaid (`.mmd`) files for UML and architecture diagrams
- `docs/architecture/diagramms/patient-management-uml.md` for the GitHub-renderable Mermaid UML diagram
- `docs/architecture/diagramms/patient-management-uml.mmd` for the raw Mermaid UML source
- `docs/setup/ENV_SETUP.md` for environment setup
- `docs/setup/DB_SETUP.md` for database setup, schema, seed data, migrations, and verification queries
- `docs/architecture/PROJECT_STRUCTURE.md` for package and runtime structure
- `docs/project_info/CURRENT_STATUS.md` for current implementation state
- `docs/project_info/ToDo.md` for current implementation priorities

## Currently Not Configured

The following tools are not configured in the current project yet:

- Maven Wrapper (`mvnw`)
- Automated test framework such as JUnit
- GitHub Actions or another CI pipeline
- Docker or Docker Compose
- Java logging framework such as SLF4J or Logback
- Code formatting or linting tool

## Software Engineering Concepts

### Coding Principles

- Object-Oriented Programming (OOP)
- Separation of logic into dedicated classes and methods
- Controller, service, and repository separation
- Runtime configuration validation
- Database-backed persistence through repositories
- Password hashing and credential verification
- Input validation and error handling
- Modular package structure

### Project Structure

- Clean and organized project architecture
- Clear separation of responsibilities between packages and classes
- Maintainable folder and package structure
- Technical documentation using Markdown (`.md`) files
- UML and architecture diagrams using Mermaid (`.mmd`)
- Setup documentation split between environment and database concerns
