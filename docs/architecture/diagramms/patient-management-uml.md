# Patient Management Program Flow

Last synchronized: 2026-07-23.

The renderable Mermaid source is maintained in `patient-management-uml.mmd`.

The diagram shows the current connected console runtime rather than a proposed final architecture. It covers:

- Startup through `Main`, `BootConfigService`, and `FrontController`
- `.env` validation, JDBC setup, recovery-key persistence, and starter-account creation
- Registration with the corrected password conversion/clearing order
- The current login split across `CollectLoginValues`, `SetupCurrentSession`, `HandleAccountStatus`, and `StoreLogs`
- `SessionAccount` and static `CurrentSession` creation for active accounts
- The failed-login reason mismatch that prevents new wrong-password rows from advancing the stored threshold count
- Pending access-request setup and system-account recovery
- Action-based routing through `ServiceAction`, `MenuOption`, and `MenuContextStructure`
- The connected `ADMIN_USER_REQUESTS -> ShowCurrentRequests` service
- Fatal routing for all other currently unsupported service actions
- Disconnected session clearing, unhandled `UI`/`EXIT` request types, empty placeholders, logging, and the 55-test baseline

## Reading the Current Flow

The most important runtime boundaries are:

1. `FrontController(CONFIG)` must succeed before authentication starts.
2. `AuthController` keeps showing its menu until an active account exists in `CurrentSession`, has menu access, and has status ID 1.
3. `LoginFlow` processes one attempt; the outer authentication controller provides repetition.
4. Pending and password-change outcomes are stored as successful login attempts because their `LogsForDB.canUseSystem` value is `true`, but neither creates a usable session.
5. `MenuControllerParent` converts role-specific menu selection into a `ServiceAction`.
6. `ServiceController` implements only `ADMIN_USER_REQUESTS`.
7. The request-list action runs once and reaches the end of `main`. Unsupported actions throw, are caught by `BootConfigService`, and exit with status 1.

Dashed links identify logging, tests, or code that exists but is not connected to the primary runtime. Warning and error nodes identify observed behavior in the current source, not future design goals.

Open `patient-management-uml.mmd` in a Mermaid-capable editor or viewer to render the diagram.
