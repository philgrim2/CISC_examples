public class Print2D {
    public static void main(String[] args) {
        int numRows = 16;
        int numCols = 16;
        boolean[][] boolArray = new boolean[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                boolArray[i][j] = Math.random() < 0.50;
            }
        }

        for (int i = 0; i < boolArray.length; i++) { //for the number of rows of the array do the following
            for (int j = 0; j < boolArray[0].length; j++) {
                if (boolArray[i][j]) {
                    System.out.print("* ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }
}
