/**
 * This solution closely follows the structure of the lab document.
 * The main() function serves as the question-asking component, and
 * calls the subordinate functions to make decisions based on the
 * answers.
 *
 * Requires the StdIn and StdOut classes from stdlib.jar provided
 * by the textbook, found at https://introcs.cs.princeton.edu/java/stdlib/
 */
public class Personal {
    /** Harrisburg zip codes for lookup */
    public static final int[] HARRISBURG_ZIP_CODES = {
            17101, 17102, 17103, 17104, 17110, 17111, 17120
    };

    /** Philadelphia zip codes for lookup */
    public static final int[] PHL_ZIP_CODES ={
            19102,  19103,  19104,  19106,  19107,  19109,  19111,  19112,
            19114,  19115,  19116,  19118,  19119,  19120,  19121,  19122,    19123,    19124,    19125,
            19126,  19127,  19128,  19129,  19130,  19131,  19132,  19133,    19134,    19135,    19136,
            19137,  19138,  19139,  19140,  19141,  19142,  19143,  19144,    19145,    19146,    19147,
            19148,  19149,  19150,  19151,  19152,  19153,  19154
    };

    /**
     * Asks a question (intended to be a yes or no question) that is
     * passed as the parameter.  Will accept any version of the word
     * "yes" (i.e. YES, Yes, yes, YeS, yEs).
     *
     * @param question A yes-or-no question.
     * @return True if the answer matches any of the spellings of
     *         the word Yes, false otherwise.
     */
    public static boolean strQuestion(String question) {
        StdOut.printf("%s ", question);
        String user_input = StdIn.readLine().toLowerCase();
        return user_input.equals("yes");
    }

    /**
     * Asks a question (intended to require a non-negative integer answer)
     * that is passed as the parameter.  Will continue to prompt the user
     * until a valid non-negative integer is received.
     *
     * @param question A question requiring an integral answer.
     * @return The integer response from the user
     */
    public static int intQuestion(String question) {
        int retval = -1;
        while (retval == -1)
        {
            StdOut.printf("%s ", question);
            String answer = StdIn.readString();
            if (answer.matches("^[0-9]+$"))
            {
                retval = Integer.parseInt(answer);
            }
            else
            {
                StdOut.println("Invalid response.  Please enter a valid whole number");
            }
        }
        return retval;
    }

    /**
     * Asks a question that is passed as the parameter, giving
     * the choices in the array.  Returns a string representing
     * the user's choice.
     *
     * @param question A question.
     * @param choices  An array of possible answers.
     * @return A String representing the user's answer.
     */
    public static String multipleChoiceQuestion(String question, String[] choices) {
        StdOut.println(question);
        for (int i = 1; i <= choices.length; i++)
        {
            StdOut.printf("%d: %s\n", i, choices[i-1]);
        }

        int choice = -1;
        while (choice < 0)
        {
            StdOut.print("Your choice: ");
            int input = StdIn.readInt();
            if (input > 0 && input <= choices.length)
            {
                choice = input - 1;
            }
            else {
                StdOut.println("Invalid choice.  Please choose again.");
            }
        }
        return choices[choice];
    }

    /**
     * Helper function to determine if a value is in an array.
     *
     * @param array The array to search
     * @param value The value to search for
     * @return True if the value is in the array, false otherwise
     *
     */
    private static boolean contains(int[] array, int value){
        boolean retval = false;
        for (int j : array) {
            if (j == value) {
                retval = true;
                break;
            }
        }
        return retval;
    }


    /**
     * Prints a generic ad.
     */
    public static void regularAd()
    {
        StdOut.println("Ad:  Buy our product!  You know you want to!");
    }

    /**
     * Prints an ad for dog food.
     */
    public static void dogAd()
    {
        StdOut.println("Dog Food Ad:  Feed your dog MuttMunchies! Hardly any roadkill!");
    }

    /**
     * Prints an ad for cat food.
     */
    public static void catAd()
    {
        StdOut.println("Cat Food Ad:  Give your kitty MeowBites! Made from real mouse bits!");
    }

    /**
     * Prints an ad for an expensive product.
     */
    public static void expensiveAd()
    {
        StdOut.println("Expensive Product Ad:  New Bauble 6.0XL - you know you need it.");
    }

    /**
     * Prints an ad for a cheap product.
     */
    public static void cheapAd()
    {
        StdOut.println("Cheap Product Ad:  Widget 8.5 - Does everything the Bauble 6 does at half the price.");
    }

    /**
     * Prints an ad for tourism.
     */
    public static void tourAd()
    {
        StdOut.println("Tourism Ad:  You're not from around here. Come visit the birthplace of the nation!");
    }

    /** Prints an ad for men's shampoo */
    public static void customAd1()
    {
        StdOut.println("Custom Ad[1]:  Use JustShampoo for Men!  It's not complicated.");
    }

    /** Prints an ad for women's shampoo */
    public static void customAd2()
    {
        StdOut.println("Custom Ad[2]:  Use NotJustShampoo for Women!  It's maybe a little complicated, but that's ok!");
    }

    /** Prints an ad for non-gender-specific shampoo */
    public static void customAd3()
    {
        StdOut.println("Custom Ad[3]:  Use Shampoo for Everybody!  Don't worry about complications.");
    }

    /** Prints an ad for children's shampoo */
    public static void customAd4()
    {
        StdOut.println("Custom Ad[4]:  Use KidShampoo!  It's complicated because we don't want it to burn your eyes.");
    }

    /** Prints an ad for generic shampoo */
    public static void customAd5()
    {
        StdOut.println("Custom Ad[5]:  Use GenericShampoo!  It'll probably make your hair clean.");
    }

    /**
     * Given the information of whether the customer owns a dog,
     * chooses from a generic ad (no dog) or a dog food ad (dog).
     * @param dog Whether the customer owns a dog.
     */
    public static void lowPersonal(boolean dog) {
        if (dog) {
            dogAd();
        }
        else {
            regularAd();
        }
    }

    /**
     * Infers cat or dog ownership from the number of friends a customer
     * has.
     *   more than 500 friends:  probable dog owner
     *   less than 50 friends:   probable cat owner
     *   otherwise:              regular ad
     * Prints an appropriate ad from the inference.
     *
     * @param friends  The number of friends the customer reports having.
     */
    public static void medPersonal(int friends) {
        if (friends >= 500) {
            dogAd();
        }
        else if (friends < 50) {
            catAd();
        }
        else {
            regularAd();
        }
    }


    /**
     * Makes a decision based on the zip code and age of the customer.
     * Customers who are located in Harrisburg or Philadelphia and are
     * under 25 get the cheap product ad.  If they are in Harrisburg or
     * Philadelphia and at least 25 they get the expensive product ad.  If
     * they are from outside the area, they get the tourism ad.
     *
     * @param age  The customer's integer age.
     * @param zip  The customer's zip code.
     */
    public static void highPersonal(int age, int zip) {
        if (contains(HARRISBURG_ZIP_CODES,zip) ||
                contains(PHL_ZIP_CODES, zip)) {
            if (age < 25) {
                cheapAd();
            }
            else {
                expensiveAd();
            }
        }
        else {
            tourAd();
        }
    }

    /**
     * Makes a decision based on the age, gender identity and income of the
     * user, and chooses a shampoo ad based on the data:
     *   Identified males over 12 with income over 20,000 get men's shampoo
     *   Identified females over 12 with income over 20,000 get women's shampoo
     *   Other gender identity over 12 with income over 20,000 get non-specific shampoo
     *   Under 12 with identified income over 20,000 get kid's shampoo
     *   Under 20,000 income gets generic shampoo.
     *
     * @param age  The customer's integer age.
     * @param gender  The customer's identified gender.
     * @param income  The customer's integer annual income
     */
    public static void extremePersonal(int age, String gender, int income) {
        if (income < 20000) {
            customAd5();
        } else if (age < 13) {
            customAd4();
        } else if (gender.equals("Female")) {
            customAd2();
        } else if (gender.equals("Male")) {
            customAd1();
        } else {
            customAd3();
        }
    }


    /**
     * Main function for the Personal Ad lab.  Asks the user
     * personal questions and chooses appropriate ads given the
     * answers.
     *
     * @param args Command-line arguments - ignored for this program.
     */
    public static void main(String[] args) {
        // Get the user's level of comfort for personalized ads
        int level = -1;
        while (level < 0) {
            int answer = intQuestion("What level of personalization " +
                    "are you comfortable with? Choose from 0 (none) " +
                    "to 4 (a lot):");
            if (answer < 0 || answer > 4) {
                StdOut.print("Not a valid choice. Choose again.");
            } else {
                level = answer;
            }
        }
        // Gather information and call the appropriate decision function.
        switch (level) {
            case 0:
                regularAd();
                break;
            case 1:
                // Ask about dog, call lowPersonal()
                boolean dog = strQuestion("Do you have a dog?");
                lowPersonal(dog);
                break;
            case 2:
                // Ask about friends, call medPersonal()
                int friends = intQuestion("How many Facebook friends do you have?");
                medPersonal(friends);
                break;
            case 3:
                // Ask about age and zip code, call highPersonal()
                int age = intQuestion("How old are you?");
                int zip = intQuestion("What is your 5-digit zip code?");
                highPersonal(age, zip);
                break;
            default:
                // Ask about age, gender, and income, call extremePersonal
                final String[] genders = {"Female", "Male", "Other", "None"};
                int age2 = intQuestion("How old are you?");
                String gender = multipleChoiceQuestion("What is your gender identity?", genders);
                int income = intQuestion("What is your household annual income in dollars?");
                extremePersonal(age2, gender, income);
        }
    }
}