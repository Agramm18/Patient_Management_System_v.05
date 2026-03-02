import java.util.Scanner;

public class menu {
    String UserChoice;

    public menu(String UserChoice) {
        this.UserChoice = UserChoice;
    }

    //Collect Menu Choice via Switch
    public void CollectMenuChoice(Scanner scanner) {

        //Classes from other files
        AddPatient add = new AddPatient();

        //Catch Menu Option and throw error if the value is invalid, none or out of range
        while (true) {

            System.out.println("Please enter a valid Menu Option or type in exit to quit the programm: ");
            this.UserChoice = scanner.nextLine();

            if (UserChoice.equalsIgnoreCase("exit")) {
                System.out.println("The Programm will end now");
                System.out.println("Good bye!");
                break;
            }

            try {
                int MenuChoiceNum = Integer.parseInt(UserChoice);

                //Error Handeling
                if (MenuChoiceNum < 1 || MenuChoiceNum > 10) {
                    throw new IllegalArgumentException("The Input is invalid your choice can't be less than 1 or higher than 10");

                } switch (MenuChoiceNum) {
                    //Call methods from AddPatient.java if case equals menu number x
                        case 1:
                            AddPatient.HeaderMSG();

                            //Loop through Methods to collect errors
                            //And collect valid options

                            while (true) {
                                try {
                                    add.AddBasicInformation(scanner);
                                    break;

                                } catch (IllegalArgumentException invalidInput) {
                                    System.out.println("\nThere is an error of the patient generation");
                                    System.out.println("The error is: " + invalidInput.getMessage() + "\n");
                                }
                            }

                            while (true) {
                                try {
                                    add.CollectDateValues(scanner);
                                    break;
                                } catch (IllegalArgumentException invalidInput) {
                                    System.out.println("\nThere is an error of the patient generation");
                                    System.out.println("The error is: " + invalidInput.getMessage() + "\n");
                                }
                            }

                            while (true) {
                                try {
                                    add.generateBirthDay(scanner);
                                    break;
                                } catch (IllegalArgumentException invalidInput) {
                                    System.out.println("\nThere is an error of the patient generation");
                                    System.out.println("The error is: " + invalidInput.getMessage() + "\n");
                                }
                            }

                            while (true) {
                                try {
                                    add.SetBodyMesaurements(scanner);
                                    break;
                                } catch (IllegalArgumentException invalidInput) {
                                    System.out.println("\nThere is an error of the patient generation");
                                    System.out.println("The error is: " + invalidInput.getMessage() + "\n");
                                }
                            }


                            add.DisplayPatient();
                            break;
                        
                        default:
                            break;
                    }
            break;
            } catch(IllegalArgumentException error) {
                System.out.println("There is an error in your menu choice");
                System.out.println("The error is" + error.getMessage());
            }
        }
    }
}
