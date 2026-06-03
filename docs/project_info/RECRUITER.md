# Recruiter Overview

Last synchronized: 2026-06-03.

Patient Management System V5.01 is a Java 21 console-based learning and portfolio project. It demonstrates how a larger application can be structured with controllers, services, repositories, database setup documentation, authentication flows, and security-oriented account handling.

## Project Purpose

The project is built to practice and demonstrate:

- Object-Oriented Programming in Java
- Maven-based project organization
- MySQL schema design and JDBC integration
- Authentication and password hashing with BCrypt
- Runtime configuration through `.env`
- Controller, service, and repository separation
- Technical documentation in Markdown and Mermaid
- Incremental development of RBAC and access-management concepts

## Current Technical Highlights

Implemented foundations include:

- Controller-based startup flow through `Main`, `BootConfigService`, and `FrontController`
- Environment validation and database connection checks before authentication starts
- Automatic starter account creation for missing `local_admin` and `admin` accounts
- Registration flow that creates pending accounts
- Login flow with BCrypt password verification and account status handling
- Login attempt persistence in MySQL
- First-login password change for starter accounts
- Recovery-key based system-account password reset baseline
- Pending-user access request groundwork with department selection
- Dedicated setup and architecture documentation under `docs/`

## Current Development Stage

The project is not a finished hospital product yet. The current stage is focused on stabilizing authentication, recovery, access requests, account activation, and future menu routing before patient-management features are added.

The next major implementation work is:

- Complete job and role selection for pending-user access requests
- Build an admin approval and rejection workflow
- Activate accounts based on approved access requests
- Route active users into a real main menu
- Add automated tests

## Not Implemented Yet

The following areas are planned but not implemented:

- Patient records
- Appointment scheduling
- Treatment workflows
- Billing and finance workflows
- Full administration dashboard
- JavaFX UI
- REST API
- Docker or cloud deployment
- CI pipeline

## How To Read The Repository

Start with these files:

- `README.md` for the project overview
- `docs/project_info/CURRENT_STATUS.md` for the implemented runtime state
- `docs/project_info/ToDo.md` for the current development priorities
- `docs/setup/ENV_SETUP.md` for local configuration
- `docs/setup/DB_SETUP.md` for the database schema and required seed data
- `docs/architecture/PROJECT_STRUCTURE.md` for package responsibilities
