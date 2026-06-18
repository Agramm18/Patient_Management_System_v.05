# About This Project

Last synchronized: 2026-06-18.

## Project Purpose

Patient Management System V5.01 is a long-term learning and portfolio project. It is designed to provide practical experience with the structure and technical concerns of a larger business application instead of focusing only on isolated programming exercises.

The project currently concentrates on the foundation required before patient-management features can be implemented safely:

- Application bootstrap and controller routing
- Runtime configuration validation
- MySQL and JDBC integration
- Registration and authentication
- Password hashing and password-change flows
- Recovery-key based system-account recovery
- Login attempt auditing and account-status policies
- Runtime session creation for active users
- Initial role-aware menu routing for local admin and admin accounts
- First menu-to-service routing baseline
- Admin access-request listing baseline
- Pending-user access requests
- Application-level logging

## Background

The first version of the project was written in Python while learning programming fundamentals and Object-Oriented Programming. That version focused on input handling and basic application logic.

The Java rebuild started during vocational training in application development. The goal of the rebuild is to improve the architecture, persistence model, security behavior, and maintainability while gaining practical experience with Java and enterprise-oriented development concepts.

The original Python project is available at:

https://github.com/Agramm18/Patient-Management-System

## Current Stage

The current implementation is a Java 21 console application. It is not yet a complete hospital or patient-management product.

The active runtime covers configuration, authentication, session creation for active users, first menu routing for local admin and admin roles, and the first admin service action for listing access-management requests. Patient records, appointments, treatment workflows, billing, reporting, complete admin workflows, JavaFX, REST APIs, and deployment automation are planned but not implemented.

## Learning Goals

The project is used to practice and demonstrate:

- Object-Oriented Programming
- Controller, service, and repository separation
- MySQL schema design and JDBC access
- Authentication and authorization foundations
- BCrypt password and recovery-key hashing
- Role-Based Access Control concepts
- Runtime session modeling
- Menu-to-service routing
- Security policy implementation
- Logging with SLF4J and Logback
- Maven dependency management
- Technical documentation with Markdown and Mermaid
- Incremental refactoring and quality improvement

## Documentation

Use the following documents for the current state:

- `CURRENT_STATUS.md` for implemented behavior and limitations
- `ToDo.md` for current priorities and known defects
- `../architecture/PROJECT_STRUCTURE.md` for package responsibilities
- `../architecture/TECHNICHAL.md` for technical details
- `../setup/ENV_SETUP.md` and `../setup/DB_SETUP.md` for local setup
