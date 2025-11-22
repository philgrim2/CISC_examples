public class TowersOfHanoi {

    // print out instructions for moving n discs to
    // the left (if left is true) or right (if left is false)
    public static void moves(int n, boolean left) {
        if (n == 0) return;
        moves(n-1, !left);
        if (left) StdOut.println(n + " left");
        else      StdOut.println(n + " right");
        try {

        Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        moves(n-1, !left);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        long now = System.currentTimeMillis();
        moves(n, true);
        long then = System.currentTimeMillis();
        System.out.println((then - now) + " ms" );
    }

}
