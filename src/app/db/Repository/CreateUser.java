package app.db.Repository;

public class CreateUser {
    private String DBUser;
    private String DBEmail;
    private String DBPhone;
    private String PWSDHashed; 

    public CreateUser(String DBUser, String DBEmail, String DBPhone, String PWSDHashed) {
        this.DBUser = DBUser;
        this.DBEmail = DBEmail;
        this.DBPhone = DBPhone;
        this.PWSDHashed = PWSDHashed;
    }

    public void InsertData() {
        System.out.println("\nThe follwoing Data will be insert in the DB");

        System.out.println("Username: " + this.DBUser);
        System.out.println("Email: " + this.DBEmail);
        System.out.println("Phone: " + this.DBPhone);
        System.out.println("PWSD: "  + this.PWSDHashed + "\n");
    }

    public void InsertUser() {
        
    }
}
