package app.menu.MenuUI;

public class LocalAdminUIScreen {

    public void LocalAdminPanel() {

        System.out.println("\n=======================================");
        System.out.println("        LOCAL ADMIN CONTROL PANEL");
        System.out.println("=======================================\n");

        // User Management
        System.out.println("USER MANAGEMENT");
        System.out.println("1. View all users");
        System.out.println("2. Manage user status (activate/deactivate)");
        System.out.println("3. Manage admin roles (grant/revoke admin)");

        // Logs
        System.out.println("\nSYSTEM LOGS");
        System.out.println("4. View activity logs");

        // System
        System.out.println("\nSYSTEM");
        System.out.println("5. Logout");

        System.out.println("\n=======================================\n");
    }
}