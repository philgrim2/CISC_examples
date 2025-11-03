/******************************************************************************
 *  Compilation:  javac AddInts.java
 *  Execution:    java AddInts
 *  Dependencies: StdIn.java StdOut.java
 *
 *  This program takes a command-line argument n, reads in n integers,
 *  and prints out their sum.
 *
 *  % java AddInts n
 *
 ******************************************************************************/

public class AddInts {
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            //StdOut.printf("Enter integer %d of %d: ", i+1, n);
            int value = StdIn.readInt();
            sum = sum + value;
        }
        StdOut.println("Sum is " + sum);
    }
}
