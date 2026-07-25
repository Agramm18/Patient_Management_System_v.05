# About This Project

Last synchronized: 2026-07-23.

## Purpose

Patient Management System V5.01 is a long-term Java learning and portfolio project. It develops the foundations of a larger database-backed business application while providing practical experience with architecture, persistence, authentication, security, testing, and technical documentation.

The repository currently focuses on platform foundations rather than patient-management product features:

- Application bootstrap and controller routing
- `.env` validation and MySQL connectivity
- Database-backed registration and authentication
- BCrypt password and recovery-key hashing
- Login-attempt persistence and account-status policies
- Runtime session state through `SessionAccount` and `CurrentSession`
- Pending-user access requests
- Typed role-aware menu and service-action routing
- SLF4J and Logback logging
- JUnit 5 tests for registration and password validation

This is not a production-ready hospital system and must not be used with real patient data.

## Background

The first version was written in Python while learning programming fundamentals and Object-Oriented Programming. The Java rebuild began during vocational training in application development and is intended to improve the architecture, persistence model, security behavior, testability, and maintainability.

The original Python project is available in the [Patient Management System repository](https://github.com/Agramm18/Patient-Management-System).

## Current Implementation

The current implementation is a Java 21 Maven console application. It can:

- Validate all 13 required environment values and a MySQL connection
- Hash and persist the configured recovery key
- Create missing local-admin and admin starter accounts
- Register pending users with validated profile and password input
- Authenticate accounts with BCrypt and persist login attempts
- Route account-status-specific login behavior
- Reset a selected account password after recovery-key verification
- Create an active runtime session
- Collect a pending user's requested department and create an access request
- Display local-admin or admin parent menus
- Route the admin Requests option to the current access-request listing query

The password creation sequence now converts the validated password before hashing and clears both character arrays only after the hash input is no longer needed. Registration now stores the hash returned by `PasswordFlow` after the final confirmation step, including after profile corrections. These fixes remove the two critical hash-handling defects documented in the previous baseline.

The 2026-07-23 login refactor separates credential and status work across `SetupCurrentSession`, `HandleAccountStatus`, `StoreLogs`, and `PasswordPolicies`. Active account data is stored in the immutable `SessionAccount` record, and `CurrentSession` provides `setCurrentAccount`, `getCurrentAccount`, `isLoggedIn`, and `clear`.

The menu refactor uses immutable `MenuOption` entries, typed `ServiceAction` values, and `MenuContextStructure(userRole, action)`. `MenuControllerParent` maps all five displayed admin options to actions. Only `ADMIN_USER_REQUESTS` currently invokes a service; the other four admin actions and `LOCAL_ADMIN_DASHBOARD` reach the unsupported-action exception. The application also completes only one menu and service pass after authentication.

## Verified Quality Baseline

On 2026-07-23, `.\mvnw.cmd test` completed successfully with:

- 93 production Java files under `src/main/java`
- 2 test source files under `src/test/java`
- 15 passing `PasswordServiceTest` tests
- 40 passing `RegistrationServiceTest` tests
- 55 tests in total, with 0 failures, 0 errors, and 0 skipped tests

The tests cover individual password rules, password retype and array clearing, missing-console detection at the input-method level, registration fields, confirmation and correction input, and the registration password-hash guard. They do not cover complete interactive, database-backed, session, recovery, or menu-routing workflows.

## Current Limitations

- Failed-password policy counting is not currently coherent: `PasswordPolicies` returns the stored reason `to many false attempts`, while `CountFailedLoginAttempts` counts only `INVALID_PASSWORD`. Policy evaluation also occurs before the current attempt is persisted.
- Without a terminal console, `PasswordService.userPWSD` propagates an exception to the generic fatal bootstrap handler instead of returning a controlled authentication result; complete password/hash behavior is also not covered end to end.
- `CreateAccount` has no repository-level null or blank hash guard and does not return an explicit result.
- Recovery displays system accounts but the final target lookup still accepts any existing account.
- Pending access requests store job `unassigned` and role ID 9; selection, duplicate handling, approval, rejection, and activation are incomplete.
- `ServiceController` does not yet enforce role/action authorization independently of the supplied menu context.
- Four admin actions, the local-admin dashboard, logout, a controlled menu loop, and non-admin role menus are not implemented.
- Repository failures are often printed and swallowed rather than returned as structured results.
- Logging migration and automated integration coverage remain incomplete.

Patient records, appointments, treatments, billing, reporting, JavaFX, REST APIs, continuous integration, and deployment automation are not implemented.

## Learning Goals

- Java and Object-Oriented Programming
- Controller, flow, service, and repository separation
- MySQL schema design and JDBC access
- Authentication and authorization foundations
- BCrypt password and recovery-key hashing
- Role-Based Access Control concepts
- Immutable runtime and menu context records
- Menu and service-action routing
- Security policy implementation
- SLF4J and Logback configuration
- JUnit 5 unit testing
- Maven dependency and build management
- Markdown and Mermaid documentation
- Incremental refactoring and defect tracking

## Documentation

- [Current project status](CURRENT_STATUS.md) describes verified behavior and current limitations.
- [Project backlog](ToDo.md) is the active implementation backlog.
- [Future plans](FUTURE_PLANS.md) describes the planned product and engineering direction.
- [Recruiter overview](RECRUITER.md) provides a concise portfolio-oriented summary.
- [Project structure](../architecture/PROJECT_STRUCTURE.md) maps packages and responsibilities.
- [Technical overview](../architecture/TECHNICHAL.md) explains the technical design.
- [Environment setup](../setup/ENV_SETUP.md) and [database setup](../setup/DB_SETUP.md) describe local setup.
