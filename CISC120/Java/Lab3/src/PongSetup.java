public class PongSetup {
    public static void main(String[] args) {
        int WINNER = -1;                            // Determine which player wins.  -1 means no winner yet
        int UPPER_PDL_xLIM = 16;                    // Upper left corner limit
        int UPPER_PDL_yLIM = 9;                     // Upper left corner limit
        int LOWER_PDL_xLIM = 0;                     // Lower right corner limit
        int LOWER_PDL_yLIM = 0;                     // Lower right corner limit
        double PDL_VEL = 0.5;                       // How far to move the paddle per keypress
        double BALL_xVEL = 0.0032 * (Math.random() < 0.5 ? -1 : 1);    // Random x velocity on start
        double BALL_yVEL = 0.0018 * (Math.random() < 0.5 ? -1 : 1);    // Random y velocity on start

        // More variables we need
        int SCREEN_HEIGHT = 720;  // 720p resolution
        int SCREEN_WIDTH = 1280;  // 16:9 aspect ratio

        double PADDLE_HEIGHT = 1.2;  // Height to draw the paddle
        double PADDLE_WIDTH = 0.33;   // Width to draw the paddle
        double PADDLE_1_X   = 1.0;   // Paddles don't move in the x axis
        double PADDLE_2_X   = 15.0;
        double BALL_RADIUS = 0.125;

        // Set up the drawing area
        StdDraw.setCanvasSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        StdDraw.setXscale(LOWER_PDL_xLIM, UPPER_PDL_xLIM);
        StdDraw.setYscale(LOWER_PDL_yLIM, UPPER_PDL_yLIM);
        StdDraw.enableDoubleBuffering();

        // Figure out where to draw the paddles and ball
        // Set paddles to center
        double p1_pdl_y = (UPPER_PDL_yLIM - PADDLE_HEIGHT) / 2.0;
        double p2_pdl_y = (UPPER_PDL_yLIM - PADDLE_HEIGHT) / 2.0;

        // Set ball to center
        double ball_x = UPPER_PDL_xLIM / 2.0;
        double ball_y = UPPER_PDL_yLIM / 2.0;

        // Clear the buffer, draw the picture, send it to the screen
        StdDraw.clear();
        StdDraw.filledRectangle(PADDLE_1_X, p1_pdl_y, PADDLE_WIDTH, PADDLE_HEIGHT);
        StdDraw.filledRectangle(PADDLE_2_X, p2_pdl_y, PADDLE_WIDTH, PADDLE_HEIGHT);
        StdDraw.filledCircle(ball_x,ball_y,BALL_RADIUS);
        StdDraw.show();
    }
}
