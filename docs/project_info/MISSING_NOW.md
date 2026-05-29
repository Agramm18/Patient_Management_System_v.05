# Missing Now

This file separates the currently missing work into two groups:

- **Short-Term Missing Now**: things that are missing in the current implementation and are part of the active development focus.
- **Long-Term Missing Now**: core production goals that should be completed before moving on to bigger topics like a GUI, REST API, Docker, monitoring, or AI features.

## Short-Term Missing Now

These items are directly connected to the current console application, authentication flow, and access request groundwork.

### Authentication And Access Flow

- Complete job selection during the first-login access request.
- Complete role selection during the first-login access request.
- Connect `SelectJob` to the pending-user setup flow.
- Connect `RoleValidation`, `roleMenu`, and `CheckRoles` to the active access request flow or replace them with a cleaner implementation.
- Store the requested job and requested role instead of using the current default values.
- Add a workflow for approving or rejecting pending access requests.
- Activate users after an access request has been approved.
- Add admin actions to unlock, disable, quarantine, or reactivate accounts.
- Persist failed login counters and account locking after too many failed login attempts.

### Runtime And Controller Flow

- Add a connected main menu after successful login.
- Add department-based navigation after authentication.
- Implement real behavior behind `MenuController`.
- Implement real behavior behind `ServiceController`.
- Decide whether `uiController` is needed for the console version or should stay reserved for a later UI layer.
- Replace placeholder department job menus with real selectable options.
- Decide how active users are routed after login based on department, job, role, and permission.

### Validation And Stability

- Improve input validation across all current console flows.
- Add a consistent exception handling strategy.
- Stop startup more cleanly when `.env` validation or database validation fails.
- Improve password input handling for environments where `System.console()` is not available.
- Clean up typos and inconsistent class or method names.
- Review duplicate or unused classes such as `CheckSystemAccounts` and `AccountPolicy`.

### Tests And Documentation

- Add unit tests for password validation, registration validation, login checks, and access request logic.
- Add integration tests for database-backed flows.
- Document placeholder classes until they are implemented or removed.
- Extend diagrams once the access request, role, and menu flows are connected.

## Long-Term Missing Now

These items are the core production goals that should be finished before starting large next-stage work such as a graphical UI or external service interfaces.

### Core Application Features

- Patient management.
- Appointment scheduling.
- Department management.
- Staff management.
- Patient administration for medical, financial, and administrative tasks.
- Main menu implementation with real application workflows.

### Access, Roles, And Security

- Complete access request workflow.
- Role and permission management.
- Permission assignment based on role, department, job, and account state.
- Core security concepts and policies.
- Stable database modeling for RBAC and access delegation.

### Logging And Auditability

- Audit logging.
- Log collection through database persistence or application-level logging.
- Clear tracking of important security and account events.

### Production Readiness

- Error handling and exception strategy.
- Input validation strategy.
- Documentation and architecture diagrams for the finished core console application.
