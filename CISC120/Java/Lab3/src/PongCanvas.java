public class PongCanvas {
    public static void main(String[] args) {
        int WINNER = -1;                            // Determine which player wins.  -1 means no winner yet
        int UPPER_PDL_xLIM = 16;                    // Upper left corner limit
        int UPPER_PDL_yLIM = 9;                     // Upper left corner limit
        int LOWER_PDL_xLIM = 0;                     // Lower right corner limit
        int LOWER_PDL_yLIM = 0;                     // Lower right corner limit
        double PDL_VEL = 0.5;                       // How far to move the paddle per keypress
        double BALL_xVEL = 0.0032 * (Math.random() < 0.5 ? -1 : 1);    // Random x velocity on start
        double BALL_yVEL = 0.0018 * (Math.random() < 0.5 ? -1 : 1);    // Random y velocity on start

        int SCREEN_HEIGHT = 720;  // 720p resolution
        int SCREEN_WIDTH = 1280;  // 16:9 aspect ratio

        // Set up the drawing area
        StdDraw.setCanvasSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        StdDraw.setXscale(LOWER_PDL_xLIM, UPPER_PDL_xLIM);
        StdDraw.setYscale(LOWER_PDL_yLIM, UPPER_PDL_yLIM);


    }
}
