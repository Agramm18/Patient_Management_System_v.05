# Patient Management System V5.01

Patient Management System V5.01 is a Java 21 console application that implements selected foundations of a hospital management system.

The project is being developed as a learning and portfolio project focused on software architecture, database design, authentication, security concepts, testing, and enterprise application development. It is not production-ready and must not be used with real patient data.

## Current Scope

- Controller-based bootstrap, authentication, menu, and service routing
- `.env` validation and MySQL connection setup
- Automatic local-admin and admin starter-account creation
- Registration with username, email, international phone-number, and password validation
- Login with BCrypt verification, attempt logging, and account-status updates
- Starter-account first-login password changes and recovery-key based reset
- Current-session handling for active users
- Pending-user department selection and access-request creation
- Initial role-based admin and local-admin menus
- Category-based Logback logging
- 53 passing JUnit 5 tests for password and registration validation

## Current Limitations

- Password creation and registration still contain critical end-to-end defects.
- Admin actions, access-request decisions, account activation, and logout are not connected.
- Local-admin and role-specific service implementations are placeholders.
- Authorization, recovery handling, and automated integration coverage are incomplete.
- Patient records, appointments, billing, reporting, JavaFX, REST APIs, CI, Docker, and deployment are not implemented.

See `docs/project_info/CURRENT_STATUS.md` and `docs/project_info/ToDo.md` for verified behavior, known defects, and current priorities.

## Technologies

- Java 21 and Maven
- MySQL and JDBC
- BCrypt and dotenv-java
- libphonenumber
- Logback
- JUnit 5

## Documentation

- `docs/project_info/CURRENT_STATUS.md` - Verified implementation status and test coverage
- `docs/project_info/ToDo.md` - Current priorities and backlog
- `docs/architecture/PROJECT_STRUCTURE.md` - Package and project structure
- `docs/architecture/TECHNICHAL.md` - Technical implementation details
- `docs/architecture/diagramms/patient-management-uml.md` - Mermaid class diagram
- `docs/setup/ENV_SETUP.md` - Required `.env` configuration
- `docs/setup/DB_SETUP.md` - Database schema, seed data, and verification
- `docs/project_info/RECRUITER.md` - Project goals and demonstrated skills
- `docs/project_info/FUTURE_PLANS.md` - Planned features and roadmap

## Run Commands

Complete the environment and database setup before starting. A terminal-backed console is required for hidden credential input.

```powershell
.\mvnw.cmd test
.\mvnw.cmd exec:java
```

If the Windows wrapper reports `Cannot start maven from wrapper`, use the global Maven equivalents `mvn test` and `mvn exec:java`.
