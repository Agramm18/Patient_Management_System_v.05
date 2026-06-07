# Future Plans

Last synchronized: 2026-06-07.

The project is currently building the account, security, and access-management foundation required before patient-management features are added.

## Immediate Direction

The next stable application chain should be:

```text
valid registration
-> authenticated pending user
-> complete access request
-> admin approval or rejection
-> account activation
-> role-aware main menu
```

## Near-Term Priorities

- Finish the migration to consistent application logging.
- Fix registration reconfirmation and reject incomplete registration data.
- Align the admin starter-account password environment key.
- Add username and email uniqueness checks before registration inserts.
- Make failed-login thresholds include the current attempt and define reset behavior.
- Enforce system-account-only recovery or rename the feature to reflect broader recovery.
- Implement job selection and return a selected job value.
- Implement role selection and return a selected role ID.
- Store complete access requests.
- Add admin approval and rejection workflows.
- Route active users into a real main menu.
- Add automated tests for core authentication and security behavior.

## First Functional Release Goals

- Stable registration, login, recovery, and account-status behavior
- Complete access-request and approval workflow
- Role, department, permission, and menu-access enforcement
- Main console menu
- Patient records and patient search
- Appointment scheduling
- Staff and department management
- Audit and security event review
- Consistent error handling and logging
- Automated unit and repository integration tests

## Engineering Improvements

- Add JUnit and test structure.
- Add Maven Wrapper.
- Add a Logback configuration under `src/main/resources`.
- Add formatter and static-analysis tooling.
- Replace magic numeric IDs with named constants or typed values.
- Normalize package, class, and method naming.
- Replace global database configuration with dependency injection when the architecture is ready.
- Add CI after the first automated tests exist.

## Long-Term Plans

### User Interface and APIs

- JavaFX graphical user interface
- REST API
- External service integration

### Infrastructure

- Docker
- Kubernetes
- Redis
- Cloud deployment

### Monitoring and Security

- Structured audit logging
- Application metrics
- Prometheus
- Grafana
- Security monitoring

### Data Processing

- Reporting and analytics
- Python-based data processing
- Machine-learning experiments where a clear patient-management use case exists
