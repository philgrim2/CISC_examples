public class RumorSimulator {
    public static void main(String[] args) {


//we first check if any command-line arguments were passed to the program.
//If no arguments were passed, we print a usage message and exit the program
        if (args.length == 0) {
            System.out.println("Usage: java RumorSimulator <numGuests");
            return;
        }
        int numGuests = Integer.parseInt(args[0]); // get the number of guests
//from the command line (excluding Alice)
//Verify that the input is valid. We cannot have just one person
//or a negative number of people, at the party. If not valid, end
//the program.
        if (numGuests < 2) {
            System.out.println("Invalid input. There must be at least 2 people at the party.");
            return;
        }