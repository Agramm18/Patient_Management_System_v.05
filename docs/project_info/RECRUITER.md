# Recruiter Overview

Last synchronized: 2026-06-18.

Patient Management System V5.01 is a Java 21 console-based learning and portfolio project. It demonstrates the incremental development of a larger database-backed application with authentication, account security, runtime-session modeling, access-management foundations, first role-aware menu and service routing, and technical documentation.

## Demonstrated Skills

- Java and Object-Oriented Programming
- Maven project organization
- MySQL schema design and JDBC repositories
- Controller, service, and repository separation
- Runtime configuration through `.env`
- BCrypt password and recovery-key hashing
- Authentication and account-status handling
- Login attempt auditing and persisted security-policy updates
- Runtime user-session modeling through `CurrentUser` and `CurrentSession`
- Early role-aware menu routing for local admin and admin accounts
- Menu-choice transfer into service routing through `MenuValues`
- First admin service repository for listing access-management requests
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
- Active-user session creation with account ID, status, role, system-account flag, and menu-access flag
- Basic `MENU` routing through `FrontController` and `MenuController`
- Basic `SERVICE` routing through `FrontController` and `ServiceController`
- Admin option `1` lists current access-management requests from the database
- Pending-user department requests
- Central `LogManager` introduced for categorized application logging

## Current Development Stage

The project is not presented as a finished hospital product. The current stage is focused on stabilizing:

- Registration integrity
- Authentication and security policies
- Recovery boundaries
- Logging consistency
- Runtime session behavior
- Role-aware menu routing
- Service routing
- Access requests and approval
- Account activation
- Automated tests

Patient records, appointments, treatment, billing, reporting, complete admin workflows, JavaFX, REST APIs, and deployment tooling are future work.

## Current Engineering Challenges

The repository intentionally exposes active development work rather than hiding unfinished areas. Current priorities include completing the logging migration, improving return-based error handling, enforcing recovery scope, expanding service actions beyond the first access-request listing, completing access-request approval, and adding tests.

## Repository Reading Order

1. `README.md`
2. `docs/project_info/CURRENT_STATUS.md`
3. `docs/project_info/ToDo.md`
4. `docs/architecture/PROJECT_STRUCTURE.md`
5. `docs/architecture/TECHNICHAL.md`
6. `docs/setup/ENV_SETUP.md`
7. `docs/setup/DB_SETUP.md`
8. `docs/architecture/diagramms/patient-management-uml.md`
