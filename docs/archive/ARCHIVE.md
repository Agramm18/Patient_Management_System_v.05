# Project Archive

Last synchronized: 2026-07-18.

This document records completed milestones, later refactors, and superseded statements. It is historical context, not the active task list.

For current information, use:

- `../project_info/CURRENT_STATUS.md`
- `../project_info/ToDo.md`
- `../architecture/PROJECT_STRUCTURE.md`

## Project Evolution

### Original Python Version

The first Patient Management System version was created in Python as a programming and Object-Oriented Programming learning project. It focused on basic input handling and application logic without the current Java architecture, MySQL repositories, authentication model, or automated tests.

### Java Rebuild

The project was rebuilt in Java to practice larger application structure, Maven, MySQL, JDBC, authentication, security, controller and repository boundaries, testing, logging, access management, and technical documentation.

## Milestone History

### Application Foundation

- Java 21 Maven project created
- `app.Main` entry point created
- Bootstrap behavior moved into `BootConfigService`
- `FrontController` and request types introduced
- Configuration and authentication routes connected
- Menu and service request types connected
- UI and exit request values reserved

### Configuration and Database

- `.env` existence and value validation implemented
- MySQL JDBC URL creation and connection validation implemented
- Static runtime database settings stored in `DBManager`
- Configuration failure made fatal before authentication
- Recovery-key startup persistence added
- Setup documentation separated into environment and database guides
- `EnvSetup` record added on 2026-06-25 to validate and store all environment values before database settings are exposed

### Starter Accounts

- Missing local-admin and admin accounts detected by role
- Missing starter accounts created automatically
- Starter passwords hashed with BCrypt
- Starter accounts marked as system accounts
- Starter accounts initialized with `waiting_for_password_change`
- First password update activates the account and enables menu access
- Admin starter creation aligned with `ADMIN_PASSWORD_DEFAULT`

### Registration

- Username, email, phone, and password collection implemented
- Registration validators separated from input loops to support unit testing
- Email structure validation expanded
- International phone validation added through libphonenumber
- Pending-account insert implemented
- Confirmation and field-correction flow introduced

### Login, Session, and Security Policy

- Username lookup and BCrypt password verification implemented
- Account-status routing implemented
- Login attempts persisted to MySQL
- Invalid passwords counted over a 24-hour window
- Locked, suspicious, and quarantine status updates implemented
- `Unknown` introduced for active runtime account data
- Static `CurrentSession` introduced for the active user
- Login values loaded through `CollectLoginValues`

### Recovery

- Recovery key added to required environment configuration
- Recovery key hashed and stored at `recovery_keys.id = 1`
- Hidden recovery-key input implemented
- BCrypt key verification implemented
- Four-attempt key limit implemented
- System-account list display implemented
- Selected account password update implemented

### Pending Access Requests

- Department menu and range validation implemented
- Department-specific job menu classes added
- System department display guard added
- Access-request insert implemented with selected department and default job and role values
- Access-request listing query added under `Repository.ServiceRepository.AdminServices`

### First Menu and Service Baseline

- `MenuControllerParrent` introduced for local-admin and admin role routing
- `MenuFlow` introduced for validated numeric choices
- `MenuValues` introduced to transfer role and menu choice
- `ServiceController` introduced as a role-aware dispatcher
- On 2026-06-18, admin option 1 was connected directly to `ShowCurrentRequests`

### Menu Refactor

On 2026-06-24 and 2026-06-25, the menu structure changed:

- Admin and local-admin menus moved under `ServiceMenus/ParrentMenus`
- Empty `RequestMenu` child menu added
- Empty `SubMenuController` added and injected into `FrontController`
- `MenuValues` changed to parent, role, and child context
- Admin parent menu reduced to five options
- Logging calls migrated to the new typed logging facade

As a result of the unfinished refactor, the current `ServiceController` role handlers only log startup. `ShowCurrentRequests` still exists but is no longer called by the active runtime.

### Logging

- Logback dependency added
- `src/main/resources/logback.xml` added with console and per-category file appenders
- Generated `logs/` output excluded from Git
- Original single-switch `LogManager` replaced on 2026-06-25
- Dedicated state enums added for boot, authentication, configuration, security, SQL, system, recovery, menu, account, and other input events
- Logging migration started across bootstrap, configuration, authentication, recovery, menu, and repositories

### Automated Tests

- JUnit Jupiter and Surefire added
- `PasswordServiceTest` added with 11 tests
- `RegistrationServiceTest` added and expanded to 42 tests
- libphonenumber-based validation covered by phone tests
- On 2026-07-18, all 53 tests passed with no failures, errors, or skipped tests

### Build Tooling

- Maven Wrapper 3.3.4 scripts added on 2026-07-17
- Wrapper configured for Maven 3.9.16
- The wrapper-managed Maven distribution successfully ran the test suite
- Direct `mvnw.cmd` startup still has a null-target issue in the current Windows PowerShell environment

### Documentation

- Documentation organized under `docs/project_info`, `docs/setup`, `docs/architecture`, and `docs/archive`
- Current status, roadmap, setup, architecture, and Mermaid flow documents created
- Documentation synchronized with the service and request-listing baseline on 2026-06-18
- `ToDo.md` partially synchronized with registration tests on 2026-06-24
- All files under `docs` synchronized with the source, configuration, tests, and current runtime on 2026-07-18

## Superseded Statements

The following older claims are no longer current:

- There are no automated tests. There are now 53 passing unit tests.
- There is no `src/main/resources` directory. `logback.xml` now exists there.
- Logback uses only default console configuration. Explicit console and file appenders are configured.
- The project contains 76 production Java files. It now contains 89.
- Environment values are stored only in `EnvValidationService`. `EnvSetup` now validates the complete value set first.
- The admin menu contains eight options. It currently contains five parent options.
- Admin option 1 currently lists requests. That was true before the menu refactor; the listing repository is now disconnected.
- `MenuValues` contains only a menu choice and role. It now contains parent, role, and child context.
- `uiController` is the current class name. It was renamed to `UIController`.
- The old `LogType` switch is current. It was replaced by typed methods and state enums.
- A root `Query.sql` file is the database source. SQL files are ignored; `DB_SETUP.md` is the documented schema source.

## Historical Limitations Still Present

- Password creation has an end-to-end array-clearing defect.
- Registration correction can lose the generated password hash.
- Access requests still use default job and role values.
- Approval, rejection, and activation workflows do not exist.
- Recovery's final account lookup is not limited to system accounts.
- Failed-login thresholds exclude the current attempt.
- Menu, submenu, and service routing remain incomplete.
- Logging migration remains partial.
- Naming and repository result handling remain inconsistent.
- Automated tests do not cover complete flows or repositories.
- Patient-management product features are not implemented.
