import java.time.*;
import java.util.Scanner;

public class AddPatient {

    //Name Information
    private String fname;
    private String lname;

    private String gender;

    private String email;
    private String phone;
    
    //Age Information
    private int age;
    
    private LocalDate birthdate;

    private int birthday_day;
    private int birthday_month;
    private int birthday_year;

    //Body details
    private double heihgt_m;
    private double weight_kg;

    //Address Information
    private String country;
    private String county;
    private String city;
    private String PostalCode;
    private String StreetName;
    private String StreetNumber;

    public AddPatient() {
        //Values come via User Input
    }

    //Hello MSG
    public static void HeaderMSG() {
        System.out.println("\n==================================================");
        System.out.println("=== You can now Add your patient to the System ===");
        System.out.println("==================================================\n");
    }

    //Set basic values for name
    public void AddBasicInformation(Scanner scanner) {
        while (true) {
                //Set Values
                System.out.println("Please type in your first name: ");
                String fname = scanner.nextLine();

                if (fname == null || fname.isBlank()) {
                    throw new IllegalArgumentException("Your first name seems to be empty");
                }

                System.out.println("Please type in your last name: ");
                String lname = scanner.nextLine();

                 //Error Handeling
                if (lname == null || lname.isBlank()) {
                    throw new IllegalArgumentException("Your last name seems to be empty");
                }

                System.out.println("Please type in your gender (male/female): ");
                String gender = scanner.nextLine();

                if (gender == null || gender.isBlank()) {
                    throw new IllegalArgumentException("It seems that your gender is none or empty");
                } 
                
                gender = gender.trim().toLowerCase();
                
                if (!gender.equals("male") && !gender.equals("female")) {
                    throw new IllegalArgumentException("Only your Biological gender (male/female) is allowed for medical reasons");
                }

                System.out.println("Please type in your E-Mail: ");
                String PersonalEmail = scanner.nextLine();

                if (PersonalEmail.isBlank()) {
                    throw new IllegalArgumentException("Your E-Mail can't be empty!");
                }

                System.out.println("Please type in your Phone Number");
                String PhoneNumber = scanner.nextLine();

                if (PhoneNumber.isBlank()) {
                    throw new IllegalArgumentException("Your Phone can't be empty!");
                }

                this.fname = fname;
                this.lname = lname;
                this.gender = gender;

                this.email = PersonalEmail;
                this.phone = PhoneNumber;
                break;
            }
    }

    public void CollectDateValues(Scanner scanner) {
        //Set the aptients age
        while (true) {
            //String values to check if input is empty or not
            String birthdayDayString;
            String birthdayMonthString;
            String birthdayYearString;

            //int values to validate date and set birhtday/ age
            int birthDay;
            int birthMonth;
            int birthYear;

            //Collect the values via user Input
            System.out.println("Please type in the day from your birthday: ");
            birthdayDayString = scanner.nextLine();

            System.out.println("Please type in the month from your birthday: ");
            birthdayMonthString = scanner.nextLine();

            System.out.println("Please type in the year where you born: ");
            birthdayYearString = scanner.nextLine();

            //Check if user_input is empty
            if (birthdayDayString.isBlank() || birthdayMonthString.isBlank() || birthdayYearString.isBlank()) {
                throw new IllegalArgumentException("One of your inputs are blank please try again");
            } else {
                //If not parse the integer values
                birthDay= Integer.parseInt(birthdayDayString);
                birthMonth = Integer.parseInt(birthdayMonthString);
                birthYear = Integer.parseInt(birthdayYearString);

                //Check if User input values are valid
                if (birthDay< 1 || birthMonth < 1 || birthYear < 1) {
                    throw new IllegalArgumentException("The Day/Month/Year can't be less than 1");
                } else if (birthYear < 1900) {
                    throw new IllegalArgumentException("Are you realy this old? the year is invalid cause it's behind 1900");
                } else if (birthYear > LocalDate.now().getYear()) {
                    throw new IllegalArgumentException("Are you from the future? the year is invalid cause it's from the future");
                } else if (birthMonth > 12) {
                    throw new IllegalArgumentException("Your month is out of range only 1-12 are valid");
                } else if (birthMonth == 2 && Year.isLeap(birthYear) || birthDay > 29) {
                    throw new IllegalArgumentException("The Year: " + birthYear + "is a leap year and the month febraury can't have more than 29 days");
                }
            }
                //Set the values if user input is valid
                this.birthday_day = birthDay;
                this.birthday_month = birthMonth;
                this.birthday_year = birthYear;
                break;
        }
    }

    public void generateBirthDay(Scanner scanner) {
        while (true) {
            this.birthdate = LocalDate.of(this.birthday_year, this.birthday_month, this.birthday_day);
            LocalDate DateNow = LocalDate.now();

            this.age = Period.between(birthdate, DateNow).getYears();
            break;
        }
    }


    public void SetBodyMesaurements(Scanner scanner) {
        while (true) {
            double height;
            double weight;

            System.out.println("Please type in your height in m (1,20 e.g): ");
            height = scanner.nextDouble();

            if (height < 0.24) {
                throw new IllegalArgumentException("You reached the limit the limit is 0.24m or 24cm");
            } else if (height > 300) {
                throw new IllegalArgumentException("You reached the limit are you a giant or what the limit is 3m");
            }

            System.out.println("Please type in your weight in kg");
            weight = scanner.nextDouble();

            if (weight < 0.45) {
                throw new IllegalArgumentException("You reached the limit the limit is 0.45 kg or 450g");
            } else if (weight > 650) {
                throw new IllegalArgumentException("You reached the limit the limit is 650 kg");
            }
            scanner.nextLine();
            this.heihgt_m = height;
            this.weight_kg = weight;
            break;
        }
    }

    public void SetAddress(Scanner scanner) {
        while (true) {
            String country;
            String county;
            String city;
            String PostalCode;
            String streetName;
            String streetNumber;
            

            System.out.println("Please type in the country where you live: ");
            country = scanner.nextLine();

            if (country.isBlank()) {
                throw new IllegalArgumentException("The country can't be empty!");
            }

            System.out.println("Please type in the county where you live: ");
            county = scanner.nextLine();

            if (county.isBlank()) {
                throw new IllegalArgumentException("The county can't be empty!");
            }
            
            System.out.println("Please type in the city where you live: ");
            city = scanner.nextLine();

            if (city.isBlank()) {
                throw new IllegalArgumentException("The City can't be empty!");
            }

            System.out.println("Please type in your postal code: ");
            PostalCode = scanner.nextLine();

            if (PostalCode.isBlank()) {
                throw new IllegalArgumentException("The Postal Code can't be empty!");
            } else {
                int PostalCodeINT = Integer.parseInt(PostalCode);
                
                if (PostalCodeINT < 1) {
                    throw new IllegalArgumentException("The Postal Code can't be less than 1!");
                }
            }

            System.out.println("Please type in the name from your street: ");
            streetName = scanner.nextLine();

            if (streetName.isBlank()) {
                throw new IllegalArgumentException("Your Streetname can't be empty!");
            }

            System.out.println("Please type in your street number: ");
            streetNumber = scanner.nextLine();

            if (streetNumber.isBlank()) {
                throw new IllegalArgumentException("Your street number can't be empty!");
            }
            this.country = country;
            this.county = county;
            this.city = city;
            this.PostalCode = PostalCode;
            this.StreetName = streetName;
            this.StreetNumber = streetNumber;
            break;
        }
    }

    //Display Data
    public void DisplayPatient() {
        System.out.println("\n------------------The Patient------------\n");
        System.out.println("First Name: " + this.fname);
        System.out.println("Last Name: " + this.lname);
        System.out.println("Gender: " + this.gender);
        System.out.println("E-Mail: " + this.email);
        System.out.println("Phone Number " + this.phone + "\n");

        System.out.println("Birthday day: " + this.birthday_day);
        System.out.println("Birthday month: " + this.birthday_month);
        System.out.println("Birthday year: " + this.birthday_year);
        System.out.println("\nBirthday date: " + birthdate);
        System.out.println("Age: " + this.age);

        System.out.println("\nHeight: " + this.heihgt_m + " m");
        System.out.println("Weight: " + this.weight_kg + " kg\n");

        System.out.println("\nCountry: " + this.country);
        System.out.println("County: " + this.county);
        System.out.println("City: " + this.city);
        System.out.println("Postal Code:" + this.PostalCode);
        System.out.println("Street Name: " + this.StreetName);
        System.out.println("Street Number" + this.StreetNumber + "\n");
    }

    //Add to Data Base
    //Coming Later....

}
