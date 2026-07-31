# About This Project

Last synchronized: 2026-07-31.

## Purpose

Patient Management System V5.01 is a Java learning and portfolio project for building the foundations of a database-backed business application. The current repository focuses on configuration, authentication, account security, access requests, menu routing, logging, testing, and technical documentation. Patient-management product features have not been implemented yet.

This is not a production-ready hospital system and must not be used with real patient data.

## Background

The first version was written in Python while learning programming and Object-Oriented Programming. The Java rebuild began during vocational training in application development and is intended to improve architecture, persistence, security behavior, testability, and maintainability.

The original Python project is available in the [Patient Management System repository](https://github.com/Agramm18/Patient-Management-System).

## Current Implementation

The project is a Java 21 Maven console application backed by MySQL. It currently provides:

- Validation of 13 required `.env` values and the initial database connection
- BCrypt persistence of a recovery key and automatic creation of missing starter accounts
- Registration with username, email, international phone, and password validation
- BCrypt login verification and login-attempt persistence
- Typed login results through `LoginOutcome` and `StoreLogs`
- Account-status-specific handling for active, disabled, pending, locked, quarantined, password-change, and suspicious accounts
- A multi-window failed-password policy using day, week, month, year, five-year, and ten-year counts
- Runtime session state through the immutable `SessionAccount` record and static `CurrentSession`
- Pending department access requests
- Typed menu actions through `MenuOption`, `ServiceAction`, and `MenuContextStructure`
- One connected admin service for listing access requests
- SLF4J and Logback logging
- JUnit 5 tests for password and registration helpers

The current login implementation stores `INVALID_PASSWORD` consistently and includes the current failed attempt in policy evaluation before the attempt row is written. Only the `PERMITTED` outcome is persisted as a successful login. Pending requests and starter-account password changes return to authentication without creating an authenticated session.

## Verified Baseline

On 2026-07-31, `\.\mvnw.cmd test` completed successfully with:

- 98 production Java files under `src/main/java`
- 2 test source files under `src/test/java`
- 15 passing `PasswordServiceTest` tests
- 40 passing `RegistrationServiceTest` tests
- 55 tests in total, with 0 failures, 0 errors, and 0 skipped tests

The tests are unit-level helper tests. They do not connect to MySQL or cover complete registration, login, recovery, session, menu, or service workflows.

## Current Limitations

- An active account receives a session and `PERMITTED` even when `has_access_to_menu` is false.
- Failed-login counting, status updates, and attempt persistence are separate operations and are not transactional.
- Policy windows mix calendar and rolling SQL semantics, and the multi-window thresholds have no automated coverage.
- The displayed per-session password retry count is recreated for each invalid attempt.
- Missing terminal-backed hidden input can still terminate the runtime through the generic bootstrap error path.
- Registration repositories do not independently reject blank hashes or return structured outcomes.
- Recovery displays system accounts but accepts any existing account as the final target.
- Recovery now sets the password and status ID 1, but it does not reconcile password-change flags, menu access, or session state.
- Access requests still store job `unassigned` and role ID 9 and have no approval or activation workflow.
- Four admin actions, the local-admin dashboard, logout, and a repeated menu loop are not implemented.
- `ServiceController` does not enforce role/action authorization independently.
- Repository failures are often printed and swallowed.
- Logging migration and automated integration coverage remain incomplete.

Patient records, appointments, treatments, billing, reporting, JavaFX, REST APIs, continuous integration, and deployment automation are not implemented.

## Learning Goals

- Java and Object-Oriented Programming
- Controller, flow, service, and repository separation
- MySQL schema design and JDBC access
- Authentication, authorization, and account-status policies
- BCrypt password and recovery-key hashing
- Immutable records and enum-based outcomes
- Role-aware menu and service routing
- SLF4J and Logback configuration
- JUnit 5 unit testing
- Maven dependency and build management
- Markdown and Mermaid documentation
- Incremental refactoring and defect tracking

## Documentation

- [Current project status](CURRENT_STATUS.md) describes verified behavior and limitations.
- [Project backlog](ToDo.md) contains unfinished work only.
- [Future plans](FUTURE_PLANS.md) describes the planned direction.
- [Recruiter overview](RECRUITER.md) provides a concise portfolio summary.
- [Project structure](../architecture/PROJECT_STRUCTURE.md) maps packages and responsibilities.
- [Technical overview](../architecture/TECHNICHAL.md) explains the current design.
- [Environment setup](../setup/ENV_SETUP.md) and [database setup](../setup/DB_SETUP.md) describe local setup.
