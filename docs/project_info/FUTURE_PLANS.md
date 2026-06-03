# Future Plans

Last synchronized: 2026-06-03.

The project is currently in the authentication, account-management, recovery, and access-request foundation stage. Product features should stay behind the access-management foundation until login, approval, activation, and routing are stable.

## Near-Term Priorities

These items are required before patient-management features should be added:

- Registration data integrity and duplicate-account checks
- Real job selection during pending-user first login
- Role selection that returns and stores the selected role ID
- Complete access request persistence with department, job, role, and optional reason
- Admin approval and rejection workflow
- Account activation based on approved access requests
- Main-menu routing for active users
- Persistent failed-login status policy
- Automated tests for password, registration, recovery, login, and access-request behavior

## Core Production Goals

Features required before the first functional release:

- Patient management
- Appointment scheduling
- Department management
- Staff management
- Role and permission management
- Audit logging
- Main menu implementation
- Complete access request workflow
- Patient administration including medical, financial, and administrative tasks
- Log collection through database persistence or application-level logging
- Core security concepts and policies
- Stable database modeling for RBAC and access delegation
- Error handling and exception strategy
- Input validation strategy
- Documentation and architecture diagrams

## Short-Term Plans

### Testing and Development

- Unit tests
- Integration tests
- Debugging setup
- Automated testing pipeline
- Maven Wrapper
- Formatting or linting setup

### User Interface

- Console main menu first
- JavaFX graphical user interface later

## Long-Term Plans

### Backend and Infrastructure

- REST API support
- Redis integration for caching and session management
- Docker containerization
- Kubernetes deployment support
- Cloud compatibility with AWS, Azure, and similar platforms

### Monitoring and Security

- Security monitoring
- Grafana integration
- Prometheus integration
- Custom application metrics

### AI and Data Processing

- Machine learning integration into application workflows
- Python-based data processing
- JSON-based service communication
