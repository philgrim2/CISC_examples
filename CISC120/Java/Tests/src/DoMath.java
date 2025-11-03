public class DoMath {

    public static void main(String[] args)
    {
        double fac = Math.random();
        for (int i = 0; i<10; i++)
        {
            int x = (int) Math.floor(i * fac);
            System.out.println("{" + PhilMath.doubleIt(x)
                               + "," + PhilMath.tripleIt(x)
                               + "}");
        }
    }
}
