# Recruiter Overview

Last synchronized: 2026-06-07.

Patient Management System V5.01 is a Java 21 console-based learning and portfolio project. It demonstrates the incremental development of a larger database-backed application with authentication, account security, access-management foundations, and technical documentation.

## Demonstrated Skills

- Java and Object-Oriented Programming
- Maven project organization
- MySQL schema design and JDBC repositories
- Controller, service, and repository separation
- Runtime configuration through `.env`
- BCrypt password and recovery-key hashing
- Authentication and account-status handling
- Login attempt auditing and persisted security-policy updates
- SLF4J and Logback integration through a custom logging facade
- Markdown and Mermaid documentation
- Git-based incremental development

## Current Technical Highlights

- Fail-fast configuration and database validation before authentication
- Automatic creation of missing local-admin and admin starter accounts
- Registration of pending user accounts
- BCrypt login verification
- First-login password replacement for starter accounts
- Recovery-key validation with retry limits and system-account listing
- Database-backed login attempt history
- Persisted `locked`, `suspicious`, and `on_quarantine` status changes based on recent failed passwords
- Pending-user department requests
- Central `LogManager` introduced for categorized application logging

## Current Development Stage

The project is not presented as a finished hospital product. The current stage is focused on stabilizing:

- Registration integrity
- Authentication and security policies
- Recovery boundaries
- Logging consistency
- Access requests and approval
- Account activation
- Main-menu routing
- Automated tests

Patient records, appointments, treatment, billing, reporting, JavaFX, REST APIs, and deployment tooling are future work.

## Current Engineering Challenges

The repository intentionally exposes active development work rather than hiding unfinished areas. Current priorities include resolving environment-key inconsistencies, completing the logging migration, improving return-based error handling, enforcing recovery scope, and adding tests.

## Repository Reading Order

1. `README.md`
2. `docs/project_info/CURRENT_STATUS.md`
3. `docs/project_info/ToDo.md`
4. `docs/architecture/PROJECT_STRUCTURE.md`
5. `docs/architecture/TECHNICHAL.md`
6. `docs/setup/ENV_SETUP.md`
7. `docs/setup/DB_SETUP.md`
8. `docs/architecture/diagramms/patient-management-uml.md`
