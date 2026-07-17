# About This Project

Last synchronized: 2026-07-18.

## Purpose

Patient Management System V5.01 is a long-term Java learning and portfolio project. Its purpose is to develop the foundations of a larger database-backed business application while practicing architecture, persistence, authentication, security, testing, and technical documentation.

The repository currently focuses on platform foundations rather than patient-management product features:

- Application bootstrap and controller routing
- `.env` validation and MySQL connectivity
- Database-backed registration and authentication
- BCrypt password and recovery-key hashing
- Login-attempt persistence and account-status policies
- Runtime user-session state
- Pending-user access requests
- Early role-aware menu routing
- SLF4J and Logback logging
- JUnit 5 unit tests for registration and password validation

## Background

The first version was written in Python while learning programming fundamentals and Object-Oriented Programming. The Java rebuild began during vocational training in application development and is intended to improve the architecture, persistence model, security behavior, testability, and maintainability.

The original Python project is available at:

<https://github.com/Agramm18/Patient-Management-System>

## Current Stage

The current implementation is a Java 21 Maven console application. It can validate configuration, initialize database access, create missing starter accounts, register pending users, authenticate accounts, persist login attempts, reset passwords through a recovery key, create an active runtime session, and display the first local-admin or admin menu.

The menu and service layer is being restructured. The admin menu currently exposes five parent options, but `SubMenuController`, `RequestMenu`, and both role-specific handlers in `ServiceController` do not execute business actions yet. `ShowCurrentRequests` contains an access-request query, but it is not connected to the current runtime path.

Patient records, appointments, treatments, billing, reporting, complete access approval, JavaFX, REST APIs, and deployment automation are not implemented.

## Verified Quality Baseline

The project contains 89 production Java files and two test classes. On 2026-07-18, `mvn test` completed successfully with 53 tests:

- 11 `PasswordServiceTest` tests
- 42 `RegistrationServiceTest` tests
- 0 failures, 0 errors, and 0 skipped tests

These tests cover individual validation methods. They do not yet cover complete registration, login, recovery, session, menu, or repository workflows.

## Learning Goals

- Java and Object-Oriented Programming
- Controller, flow, service, and repository separation
- MySQL schema design and JDBC access
- Authentication and authorization foundations
- BCrypt password and recovery-key hashing
- Role-Based Access Control concepts
- Runtime session modeling
- Menu and service routing
- Security policy implementation
- SLF4J and Logback configuration
- JUnit 5 unit testing
- Maven dependency and build management
- Markdown and Mermaid documentation
- Incremental refactoring and defect tracking

## Documentation

- `CURRENT_STATUS.md` describes verified behavior and current limitations.
- `ToDo.md` is the active implementation backlog.
- `FUTURE_PLANS.md` describes the planned product and engineering direction.
- `RECRUITER.md` provides a concise portfolio-oriented overview.
- `../architecture/PROJECT_STRUCTURE.md` maps packages and responsibilities.
- `../architecture/TECHNICHAL.md` explains the technical design.
- `../setup/ENV_SETUP.md` and `../setup/DB_SETUP.md` describe local setup.
