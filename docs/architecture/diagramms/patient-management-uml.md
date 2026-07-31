# Patient Management Program Flow

Last synchronized: 2026-07-31.

The renderable Mermaid source is maintained in `patient-management-uml.mmd`.

The diagram represents the connected console runtime, not a proposed final architecture. It covers:

- Startup through `Main`, `BootConfigService`, and `FrontController`
- Environment validation, JDBC initialization, recovery-key persistence, and starter-account creation
- Registration and BCrypt password creation
- Login through `CollectLoginValues`, `CheckInput`, `SetupCurrentSession`, and `HandleAccountStatus`
- Typed `LoginOutcome` and `StoreLogs` results
- Successful-attempt persistence only for `PERMITTED`
- `SessionAccount` and static `CurrentSession` creation for active accounts
- The six-window password policy using `PolicyThreshold`, `TimePeriod`, and `PolicieThresholdStructure`
- Canonical `INVALID_PASSWORD` persistence and in-memory inclusion of the current attempt
- Pending access requests and system-account recovery
- Action routing through `ServiceAction`, `MenuOption`, and `MenuContextStructure`
- The connected `ADMIN_USER_REQUESTS -> ShowCurrentRequests` service
- Fatal handling for currently unsupported actions
- Disconnected session clearing, unhandled `UI`/`EXIT` requests, placeholders, logging, and the 55-test baseline

## Reading the Current Flow

1. Configuration must succeed before authentication starts.
2. `AuthController` returns only when `CurrentSession` contains an active, menu-enabled account.
3. `LoginFlow` repeats rejected or invalid credential outcomes internally.
4. `PASSWORD_CHANGED` and `PENDING_REQUEST` are stored as unsuccessful and return to the authentication menu without a session.
5. Only `PERMITTED` is stored as a successful login attempt.
6. Invalid-password counts are read before the attempt row is inserted; `includingAttempt()` adds that attempt in memory for threshold evaluation.
7. `MenuControllerParent` converts a role-specific selection into a `ServiceAction`.
8. `ServiceController` implements only `ADMIN_USER_REQUESTS`.
9. Request listing runs once and reaches the end of `main`; unsupported actions throw and exit through bootstrap's generic fatal handler.

Dashed links in the Mermaid source identify logging, tests, or code that exists but is not connected to the primary runtime. Warning and error nodes describe current source behavior.

Open `patient-management-uml.mmd` in a Mermaid-capable editor or viewer to render the diagram.
