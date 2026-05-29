# Current Project Status

Patient Management System V5.01 is currently a Java 21 console application. The project is in an early development stage and focuses mainly on startup configuration, database connection handling, authentication, registration, starter accounts, and first-login access requests.

It is not a complete patient management system yet. Patient records, appointments, billing, reporting, treatment workflows, a graphical UI, and full administration workflows are still planned features.

## Implemented

The current application includes:

- Maven project setup with Java 21.
- Console startup through `app.Main`.
- Boot process through `BootConfigService`.
- Central routing through `FrontController`.
- Configuration startup through `ConfigController`.
- `.env` validation through `EnvValidationService`.
- MySQL connection setup through `SQLValidationService` and `DBManager`.
- Automatic starter account checks through `CheckForDefaultAccounts`.
- Automatic creation of missing `local_admin` and `admin` accounts through `CreateDefaultAccounts`.
- User registration through `RegistrationFlow`, `RegistrationService`, `PasswordFlow`, and `CreateAccount`.
- BCrypt password hashing for registered users and starter accounts.
- Login through `LoginFlow`, `LoginInputCollector`, `LoginVerification`, and `CheckUserInDB`.
- Account status handling for `active`, `disabled`, `pending`, `locked`, `on_quarantine`, and `waiting_for_password_change`.
- First password change for starter accounts.
- Login attempt logging into the `login_attempts` table.
- First-login access request groundwork for pending users.
- Department selection and placeholder department job menus.
- Access request storage through `HandleAccessManagement`.

## Runtime Flow

1. `app.Main` starts the console application and creates a shared `Scanner`.
2. `BootConfigService` displays startup messages and builds the controller objects.
3. `FrontController` first routes into the configuration phase.
4. `ConfigController` validates the `.env` file, builds the SQL connection values, initializes `DBManager`, and checks starter accounts.
5. Missing starter accounts are created automatically with hashed passwords from `.env`.
6. After configuration, the application routes into the authentication phase.
7. `AuthController` shows the authentication menu with registration, login, and exit.
8. Registration collects username, email, phone number, and password, then creates a pending account.
9. Login checks the username, verifies the password with BCrypt, checks the account status, and writes a login attempt log.
10. Pending users enter the first-login access request flow and select a department.
11. Starter accounts with `waiting_for_password_change` must create a new password before becoming active.

## Database And Setup

The application uses MySQL through JDBC. Database setup is documented in `docs/setup/DB_SETUP.md`, and environment variables are documented in `docs/setup/ENV_SETUP.md`.

The current database model includes:

- `roles`
- `account_status`
- `departments`
- `accounts`
- `login_attempts`
- `access_management`

Registered users are currently created as pending accounts with the default intern role and the unassigned department. Starter accounts are created as system accounts and must change their password on first login.

## Current Limitations

- There is no connected main menu after a successful login yet.
- Patient management is not implemented yet.
- Appointment, billing, treatment, reporting, and hospital administration workflows are not implemented yet.
- Job selection is not fully implemented; department job menus are placeholders.
- Role selection and account approval workflows are not active yet.
- Access requests are stored, but there is no approval or rejection workflow yet.
- Failed login counters are handled during the login flow but are not persisted as a full locking system yet.
- `MenuController`, `ServiceController`, `uiController`, `AccountPolicy`, and `SelectJob` are placeholders.
- There are no automated tests yet.
- The application is still console-based; JavaFX and REST API support are planned for later.

## Run The Project

Build the project:

```bash
mvn clean package
```

Compile without packaging:

```bash
mvn -DskipTests compile
```

Start the console application:

```bash
mvn exec:java
```

## Notes

The current focus is authentication, database-backed account handling, and the foundation for role-based access workflows. The project structure and documentation are already separated into setup, architecture, and project information files so the next development steps can build on a clearer base.
