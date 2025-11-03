public class Coerce {

    public static void main(String[] args)
    {
        double start = 5;
        int result = (int) Math.round(start * 50);

        System.out.println((result < 40) ? "Small" : "Big");


    }

}
