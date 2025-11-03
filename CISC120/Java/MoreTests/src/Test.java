public class Test {

    public static void main(String[] args)
    {
        int arg = Integer.parseInt(args[0]);
        int x = (int) (Math.random() * arg);
        System.out.println("{" + PhilMath.doubleIt(x) +
        "," + PhilMath.tripleIt(x) + "}");
    }
}
