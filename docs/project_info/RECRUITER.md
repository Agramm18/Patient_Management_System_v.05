# Recruiter Overview

Last synchronized: 2026-07-31.

Patient Management System V5.01 is a Java 21 console-based learning and portfolio project. It demonstrates incremental development of a database-backed application with configuration validation, authentication, account security, runtime sessions, access-management foundations, typed routing, logging, unit testing, and technical documentation.

It is an in-progress engineering project, not a finished hospital product and not suitable for real patient data.

## Demonstrated Skills

- Java and Object-Oriented Programming
- Maven project organization and dependency management
- MySQL schema design and JDBC repositories
- Controller, flow, service, and repository separation
- Runtime configuration through `.env` and the `EnvSetup` record
- BCrypt password and recovery-key hashing
- Authentication and account-status handling
- Enum-based login outcomes and immutable record types
- Multi-window failed-login policy modeling
- Static session lifecycle through `CurrentSession`
- Typed menu actions and role-aware routing
- SLF4J and Logback configuration
- JUnit 5 unit testing
- Markdown and Mermaid documentation
- Git-based incremental refactoring

## Current Technical Baseline

- Fail-fast environment and initial database validation
- Automatic local-admin and admin starter-account creation
- Pending-account registration with profile and password validation
- BCrypt login verification and attempt persistence
- Explicit `LoginOutcome` values carried by `StoreLogs`
- Successful-attempt persistence only for `PERMITTED`
- Consistent `INVALID_PASSWORD` persistence and counting
- Failed-attempt evaluation across six time windows with scaled thresholds
- First-login password replacement for starter accounts
- Recovery-key verification with a four-attempt limit
- Active-user session records through `SessionAccount`
- Pending department access requests
- Five admin options mapped to typed `ServiceAction` values
- One connected request-listing service
- Console and category file logging

The 2026-07-31 source snapshot contains 98 production Java files and two test classes. `.\mvnw.cmd test` completed 55 tests successfully:

- 15 `PasswordServiceTest` tests
- 40 `RegistrationServiceTest` tests
- 0 failures, 0 errors, and 0 skipped tests

## Recent Engineering Work

The login path now separates credential input, input checking, session setup, status handling, outcomes, and persisted attempt data. The failed-password policy uses named threshold and time-period enums plus a record containing counts for day, week, month, year, five years, and ten years. The current attempt is included before evaluation, and only a permitted login is stored as successful.

The menu layer uses immutable `MenuOption` records, `ServiceAction`, and `MenuContextStructure`. The Requests option is connected to `ShowCurrentRequests`; the remaining actions still need implementation.

## Current Engineering Limitations

- Login policy queries and threshold transitions have no automated coverage and are not transactional with attempt persistence.
- Active accounts can receive a permitted session even when menu access is false.
- Policy windows mix calendar and rolling semantics.
- Missing terminal input can still terminate through generic bootstrap handling.
- Registration and access-request repositories do not return structured outcomes.
- Recovery target selection is not limited to displayed system accounts.
- Recovery leaves password-change, menu-access, and session fields inconsistent.
- Access-request job and role selection, approval, rejection, and activation are incomplete.
- Four admin actions, local-admin services, logout, and a menu loop are not implemented.
- Service-layer role/action authorization is incomplete.
- Repository error handling, logging migration, and integration tests remain incomplete.

## Development Stage

Patient records, appointments, treatment, billing, reporting, complete administrator workflows, JavaFX, REST APIs, continuous integration, and deployment tooling remain future work.

The current focus is to test and harden login policies, enforce session and service authorization, finish account recovery and access approval, implement remaining menu actions, and add integration-level coverage.

## Repository Reading Order

1. [README](../../README.md)
2. [Current project status](CURRENT_STATUS.md)
3. [Project backlog](ToDo.md)
4. [Project structure](../architecture/PROJECT_STRUCTURE.md)
5. [Technical overview](../architecture/TECHNICHAL.md)
6. [Environment setup](../setup/ENV_SETUP.md)
7. [Database setup](../setup/DB_SETUP.md)
8. [Program-flow diagram](../architecture/diagramms/patient-management-uml.md)
