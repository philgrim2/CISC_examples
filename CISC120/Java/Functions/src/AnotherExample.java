public class AnotherExample {

    public static boolean isPrime(int n)
    {
        boolean retval = true;

        if (n < 2) retval = false;
        else {
            for (int i = 2; i <= n/i; i++)
                if (n % i == 0) {
                    retval = false;
                    break;
                }
        }

        return retval;
    }

    public static void happy()
    {
        System.out.println("Happy Birthday to you!");
    }

    public static void happy(String person)
    {
        System.out.println("Happy Birthday, dear " + person);
    }

    public static void main(String[] args)
    {
        happy();
        happy();
        happy(args[0]);
        happy();
    }

}
