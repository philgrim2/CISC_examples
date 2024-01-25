public class KeyDemo {

    public static final int NROWS = 6;
    public static final int NCOLS = 6;

    public static final char[][] KEYS = {
            {'A','B','C','D','E','F'},
            {'G','H','I','J','K','L'},
            {'M','N','O','P','Q','R'},
            {'S','T','U','V','W','X'},
            {'Y','Z','0','1','2','3'},
            {'4','5','6','7','8','9'}
    };

    public static final double KEYSIZE = 0.95;

    public static void main(String[] args)
    {
        StdDraw.setXscale(0,NROWS);
        StdDraw.setYscale(0,NCOLS);
        StdDraw.enableDoubleBuffering();

        while (true) {
            // Clear the back buffer
            StdDraw.clear();

            char keypress = 0;
            double mouseX = 0.0;
            double mouseY = 0.0;

            if (StdDraw.hasNextKeyTyped()) {
                keypress = Character.toUpperCase(StdDraw.nextKeyTyped());
                StdOut.printf("KeyPressed: %c\n", keypress);
            }
            if (StdDraw.isMousePressed()) {
                mouseX = StdDraw.mouseX();
                mouseY = StdDraw.mouseY();
                StdOut.printf("Mouse pressed:(%.4f, %.4f)\n", mouseX, mouseY);
            }
            
            for (int row = 0; row < NROWS; row++) {
                for (int col = 0; col < NCOLS; col++) {
                    if (keypress == KEYS[row][col]) {
                        StdDraw.setPenColor(StdDraw.CYAN);
                        StdDraw.filledSquare(col + 0.5, row + 0.5, KEYSIZE / 2);
                    }
                    if ((col + 0.025) < mouseX && mouseX < (col + 0.975) && (row + 0.025) < mouseY && mouseY < (row + 0.975)) {
                        StdDraw.setPenColor(StdDraw.YELLOW);
                        StdDraw.filledSquare(col + 0.5, row + 0.5, KEYSIZE / 2);
                    }
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.square(col + 0.5, row + 0.5, KEYSIZE / 2);
                    StdDraw.text(col + 0.5, row + 0.5, String.valueOf(KEYS[row][col]));
                }
            }

            StdDraw.show();
            StdDraw.pause(20);
        }
        
    }

}