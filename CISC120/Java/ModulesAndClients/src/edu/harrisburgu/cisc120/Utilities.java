package edu.harrisburgu.cisc120;

/**
 * Utility class to demonstrate modular programming.
 */
public class Utilities {

    /**
     * Renders a string in the internet-standard sarcasm case.
     *
     * @param regular String to be rendered sarcastic.
     * @return A new string that is sarcastic.
     */
    public static String sarcastic(String regular){
        StringBuilder retval = new StringBuilder();

        for (int i = 0; i < regular.length(); i++)
        {
            if (i % 2 == 0)
               retval.append(("" + regular.charAt(i)).toLowerCase());
            else
               retval.append(("" + regular.charAt(i)).toUpperCase());
        }

        return retval.toString();
    }

    /**
     * Main function contains tests for all utilities.
     * @param args Array containing command line arguments.
     */
    public static void main(String[] args)
    {
        // Test sarcasm
        String in = "Hello There!";
        String out = Utilities.sarcastic(in);
        System.out.println(out);
    }
}
