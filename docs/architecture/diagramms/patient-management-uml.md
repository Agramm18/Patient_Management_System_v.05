# Patient Management Program Flow

Last synchronized: 2026-07-18.

The renderable Mermaid source is maintained in `patient-management-uml.mmd`.

The diagram reflects the current connected runtime and distinguishes it from classes that are present but not invoked. It covers:

- Startup through `Main`, `BootConfigService`, and `FrontController`
- `.env`, `EnvSetup`, JDBC, recovery-key, and starter-account configuration
- Registration and current password/hash risks
- Login verification, status routing, login-attempt persistence, and threshold timing
- Active `CurrentUser` and `CurrentSession` creation
- Pending-user department requests with default job and role values
- Recovery-key retry behavior and the unrestricted final account lookup
- Local-admin and five-option admin parent menus
- `MenuValues` parent, role, and unused child context
- Current role-based `ServiceController` handlers, which only log startup
- Disconnected `SubMenuController`, `RequestMenu`, `ShowCurrentRequests`, and `RouteService`
- Typed logging, Logback output, and the 53-test validation baseline

Open `patient-management-uml.mmd` in a Mermaid-capable editor or viewer to render the full diagram.
