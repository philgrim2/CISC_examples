public class IntegerClient1 {

    public static void main(String[] args)
    {
        for (int iu = 0; iu < 10; iu++)
        {
            if (Integers.isEven(iu))
                System.out.println("Even");
            else
                System.out.println("Odd");
        }
    }
}
