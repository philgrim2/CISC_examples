public class BetterUseThree {

    public static void main(String[] args) {
        String arg1 = args[0];
        String arg2 = args[1];
        String arg3 = args[2];

        StdOut.printf("Hello, %s, %s, and %s! How are you?\n", arg3, arg2, arg1);

        System.out.println(String.format("Hello, %s, %s, and %s! How are you?\n", arg3, arg2, arg1));
    }
}
