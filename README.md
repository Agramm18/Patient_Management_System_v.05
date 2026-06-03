# Patient Management System V5.01

Patient Management System V5.01 is a Java 21 console application that simulates selected foundations of a hospital management system.

The project is being developed as a learning and portfolio project to gain practical experience in software architecture, database design, authentication systems, security concepts, and enterprise application development.

## Current Scope

The current implementation focuses on:

- Bootstrap and controller-based startup flow
- `.env` validation and MySQL connection validation
- Database-backed starter account creation
- Registration of pending user accounts
- Login with BCrypt password verification
- Login attempt logging
- Starter account first-login password change
- Recovery-key based system-account password reset baseline
- Pending-user access request groundwork with department selection

The application is still console-based. Main-menu routing, patient records, appointments, billing, reporting, JavaFX, REST APIs, Docker, and deployment workflows are planned but not implemented yet.

## Current Limitations

- Active users are not routed into a real main menu yet.
- Pending users can create access requests, but job and role still use default values.
- Admin approval and rejection workflows are not implemented yet.
- Failed-login thresholds are detected but do not persist account status changes yet.
- There are no automated tests yet.

## Technologies

- Java 21
- Maven
- MySQL
- JDBC
- BCrypt
- dotenv-java
- Git
- GitHub

## Documentation

- `docs/project_info/CURRENT_STATUS.md` - Current development status and implemented features
- `docs/project_info/ToDo.md` - Current implementation priorities and open decisions
- `docs/architecture/PROJECT_STRUCTURE.md` - Project and package structure overview
- `docs/architecture/TECHNICHAL.md` - Technical implementation details and architecture concepts
- `docs/architecture/diagramms/patient-management-uml.md` - GitHub-renderable Mermaid class diagram
- `docs/architecture/diagramms/patient-management-uml.mmd` - Raw Mermaid class diagram source
- `docs/setup/ENV_SETUP.md` - Environment variable and `.env` configuration
- `docs/setup/DB_SETUP.md` - Database setup, schema, seed data, migrations, and verification queries
- `docs/project_info/RECRUITER.md` - Project goals, learning objectives, and skills demonstrated
- `docs/project_info/FUTURE_PLANS.md` - Planned features and project roadmap

## Run Commands

Compile without tests:

```bash
mvn -DskipTests compile
```

Run the console application:

```bash
mvn exec:java
```

## Notes

Some documentation files are maintained with the assistance of OpenAI Codex and manually reviewed before being committed.
