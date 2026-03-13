package app.menu;

public class LocalAdminUIScreen {

    public void LocalAdminPanel() {

        System.out.println("\n=======================================");
        System.out.println("        LOCAL ADMIN CONTROL PANEL");
        System.out.println("=======================================\n");

        // Admin Management
        System.out.println("ADMIN MANAGEMENT");
        System.out.println("1. Create new admin");
        System.out.println("2. Edit admin account");
        System.out.println("3. Delete admin account");
        System.out.println("4. View admin list");
        System.out.println("5. Activate / Deactivate admin");

        // Admin Role
        System.out.println("\nADMIN ROLE MANAGEMENT");
        System.out.println("6. Create admin role");
        System.out.println("7. Edit admin role");
        System.out.println("8. Delete admin role");
        System.out.println("9. Assign role to admin");

        // Admin Permissions
        System.out.println("\nADMIN PERMISSION MANAGEMENT");
        System.out.println("10. Grant admin permission");
        System.out.println("11. Revoke admin permission");

        // System
        System.out.println("\nSYSTEM");
        System.out.println("12. View admin activity logs");
        System.out.println("13. Logout");

        System.out.println("\n=======================================\n");
    }
}