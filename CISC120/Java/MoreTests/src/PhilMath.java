/**
 * This class provides Phil's special kind of math.
 */
public class PhilMath {
    /**
     * Doubles the input parameter
     * @param x The number to double
     * @return The input parameter times 2
     */
    public static int doubleIt(int x)
    {
        return x * 2;
    }

    /**
     * Triples the input parameter
     * @param x The number to triple
     * @return The input parameter times 3
     */
    public static int tripleIt(int x)
    {
        return x * 3;
    }

    //Test driver
    public static void main(String[] args)
    {
        System.out.println("4 doubled is " + doubleIt(4));
        System.out.println("4 tripled is " + tripleIt(4));

    }

}
