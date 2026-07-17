# Recruiter Overview

Last synchronized: 2026-07-18.

Patient Management System V5.01 is a Java 21 console-based learning and portfolio project. It demonstrates the incremental development of a database-backed application with configuration validation, authentication, account security, runtime sessions, access-management foundations, logging, unit testing, and technical documentation.

## Demonstrated Skills

- Java and Object-Oriented Programming
- Maven project organization and dependency management
- MySQL schema design and JDBC repositories
- Controller, flow, service, and repository separation
- Runtime configuration through `.env` and a validated `EnvSetup` record
- BCrypt password and recovery-key hashing
- Authentication and account-status handling
- Login-attempt auditing and persisted status changes
- Runtime session modeling through `CurrentUser` and `CurrentSession`
- Early role-aware parent-menu routing
- Typed logging state through SLF4J and Logback
- JUnit 5 unit testing
- Markdown and Mermaid documentation
- Git-based incremental development

## Current Technical Baseline

- Fail-fast environment and database validation before authentication
- Automatic creation of missing local-admin and admin starter accounts
- Registration of pending accounts with username, email, and international phone validation
- BCrypt login verification
- First-login password replacement for starter accounts
- Recovery-key verification with a four-attempt limit
- Database-backed login-attempt history
- Persisted locked, suspicious, and quarantine states
- Active-user session creation with account ID, status, role, system-account flag, and menu access
- Pending department access requests
- Parent-menu routing for local-admin and admin roles
- Console and per-category file logging through `logback.xml`
- 53 passing unit tests: 11 password-service tests and 42 registration-service tests

## Current Architecture Work

The project is in an active menu and service-routing refactor. `MenuValues` now carries parent, role, and child context, and a `SubMenuController` plus `RequestMenu` have been introduced as placeholders. The admin menu has five parent options. Role-based service dispatch exists, but its handlers do not yet invoke business services.

An access-request listing repository already exists, but it is currently disconnected from the runtime. This is tracked explicitly rather than presented as a finished workflow.

## Engineering Risks Being Addressed

- The full password creation path clears the original character array before constructing the value to hash.
- Registration correction does not retain the password hash returned by `PasswordFlow`.
- Failed-login policy evaluation excludes the current failed attempt.
- Recovery displays system accounts but accepts any existing account in its final lookup.
- Repositories often print and swallow failures instead of returning structured results.
- Automated tests cover validators but not database-backed or end-to-end authentication flows.
- Logging migration and category alignment remain incomplete.

## Development Stage

This repository is not presented as a finished hospital system. Patient records, appointments, treatment, billing, reporting, complete administrator workflows, JavaFX, REST APIs, and deployment tooling remain future work.

The current engineering focus is to stabilize password handling, registration integrity, recovery scope, status policies, session lifecycle, submenu and service routing, access approval, and integration-test coverage.

## Repository Reading Order

1. `../../README.md`
2. `CURRENT_STATUS.md`
3. `ToDo.md`
4. `../architecture/PROJECT_STRUCTURE.md`
5. `../architecture/TECHNICHAL.md`
6. `../setup/ENV_SETUP.md`
7. `../setup/DB_SETUP.md`
8. `../architecture/diagramms/patient-management-uml.md`
