# Patient Management Program Flow

Markdown note synchronized: 2026-06-18.

The current Mermaid source is maintained in `patient-management-uml.mmd`.

This diagram is an activity / program-flow diagram with an additional class and public-method map. The upper part keeps the runtime control flow readable; the lower part lists the relevant classes and public methods grouped by responsibility.

- program start through `Main` and `BootConfigService`
- system configuration checks and abort points
- authentication menu loop
- registration, login, recovery, and pending-user setup branches
- login status handling and retry behavior
- active session creation
- menu routing by role
- menu-choice transfer through `MenuValues`
- current `ServiceController` routing baseline
- admin option `1` access-request listing through `ShowCurrentRequests`
- remaining service placeholders such as `RouteService`
- all relevant runtime classes and public methods, grouped by package responsibility

Open `patient-management-uml.mmd` in a Mermaid-capable viewer to render the full flow.
