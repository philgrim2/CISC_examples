/**
 * The PhilMath class provides functions that do
 * basic math Phil's way.
 */
public class PhilMath {
    /**
     * Calculates two times the provided value.
     * @param x The value to double
     * @return An integer representing two times
     * the provided value.
     */
    public static int doubleIt(int x)
    {
        return x * 2;
    }

    /**
     * Calculates three times the provided value.
     * @param x The value to triple
     * @return An integer representing three times
     * the provided value.
     */
    public static int tripleIt(int x)
    {
        return x * 3;
    }

    public static void main(String[] args){
        int arg = Integer.parseInt(args[0]);
        System.out.println(arg + " doubled is " + doubleIt(arg));
        System.out.println(arg + " tripled is " + tripleIt(arg));
    }


}
