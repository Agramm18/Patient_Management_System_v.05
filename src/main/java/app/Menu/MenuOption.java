package app.Menu;

import app.Menu.Enums.ServiceAction;

public record MenuOption(
        String label,
        ServiceAction action
) {
}
