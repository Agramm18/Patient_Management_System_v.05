# Project Archive

Last synchronized: 2026-06-16.

This document records completed milestones and superseded project directions. It is historical context, not the current task list.

For active information, use:

- `../project_info/CURRENT_STATUS.md`
- `../project_info/ToDo.md`
- `../architecture/PROJECT_STRUCTURE.md`

## Project Evolution

### Original Python Version

The first Patient Management System version was created in Python as a programming and OOP learning project. It focused on basic input handling and application logic without the current Java architecture, MySQL repositories, or authentication model.

### Java Rebuild

The project was rebuilt in Java to practice:

- Larger application structure
- Maven
- MySQL and JDBC
- Authentication and password security
- Controller, service, and repository separation
- Runtime session modeling
- Menu-routing and RBAC concepts
- Access-management concepts
- Technical documentation

## Completed Milestones

### Project and Runtime Foundation

- Java 21 Maven project created
- `app.Main` entry point created
- Bootstrap flow moved into `BootConfigService`
- `FrontController` and request types introduced
- Configuration and authentication controller routes connected
- `MENU` route connected for active menu-enabled users
- Placeholder service and UI controllers reserved

### Configuration and Database

- `.env` file validation implemented
- MySQL JDBC URL creation and connection validation implemented
- Shared runtime database connection values stored in `DBManager`
- Startup failure prevents authentication when configuration fails
- Database setup documentation moved into `docs/setup/DB_SETUP.md`
- Environment setup documentation moved into `docs/setup/ENV_SETUP.md`

### Starter Accounts

- Missing local-admin and admin accounts are detected by role
- Missing starter accounts are created automatically
- Starter passwords are hashed with BCrypt
- Starter accounts are marked as system accounts
- Starter accounts begin in `waiting_for_password_change`
- First password change activates the account and enables menu access

### Registration

- Username, email, phone number, and password collection implemented
- Basic registration validation implemented
- BCrypt password hashing implemented
- Pending-account insert implemented
- Registration logging migration started

### Login, Session, and Security Policy

- Username lookup implemented
- BCrypt password verification implemented
- Account-status routing implemented
- Login-attempt database persistence implemented
- 24-hour invalid-password counting implemented
- Persisted transitions to locked, suspicious, and quarantine states implemented
- `CurrentUser` object introduced for active-user runtime data
- `CurrentSession` introduced as the current runtime session holder
- Login values loaded through `CollectLoginValues`

### Recovery

- Recovery key added to required environment configuration
- Recovery key hashed during startup
- Recovery key stored at `recovery_keys.id = 1`
- Recovery menu route added
- Hidden recovery-key input implemented
- BCrypt recovery-key verification implemented
- Four-attempt recovery-key limit implemented
- System-account list display implemented
- Selected account password-hash update implemented

### Pending Access Requests

- Department menu implemented
- Department selection validation implemented
- Department-specific job menu classes created
- System department menu access guard added
- Pending access-request insert implemented with selected department and default job/role values

### Menu Routing

- `MenuController` introduced for role-based menu routing
- `MenuFlow` introduced for menu option validation groundwork
- Local-admin menu display class introduced
- Admin menu display class introduced
- Admin menu now lists the first planned administrative actions

### Logging

- Logback dependency added
- `LogManager` facade introduced
- Category loggers introduced for boot, configuration, authentication, security, SQL, database, and credentials
- Boot, configuration, recovery, and parts of authentication flows partially migrated from direct console diagnostics

### Documentation

- Documentation organized under `docs/project_info`, `docs/setup`, `docs/architecture`, and `docs/archive`
- Current status, roadmap, setup, architecture, and Mermaid UML documentation created
- Documentation synchronized with the session and menu-routing baseline on 2026-06-16

## Superseded Documentation Notes

The following older statements are no longer current:

- Failed-login policy methods are not empty placeholders; they now update account status.
- The project does use a logging framework; Logback is configured as a Maven dependency.
- Recovery-key failure now retries and stops after four invalid attempts.
- Recovery account display is now filtered to system accounts.
- Repository classes and authentication services were reorganized into more specific subpackages.
- `Query.sql` is not the complete database setup and is ignored by Git.
- `FrontController` no longer routes only `CONFIG` and `AUTH`; the `MENU` route is now connected.
- There is now a runtime current-user session baseline for active accounts.

## Historical Limitations Still Present

Several early-stage design limitations remain and are tracked in `ToDo.md`:

- Menu actions are displayed but not implemented
- Incomplete access-request details and no approval workflow
- Partial logging migration
- Inconsistent naming
- Inconsistent repository return values
- No automated tests
- No patient-management product features
