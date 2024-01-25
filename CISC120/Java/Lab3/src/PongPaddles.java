public class PongPaddles {
    public static void main(String[] args) {
        int UPPER_PDL_xLIM = 16;                    // Upper left corner limit
        int UPPER_PDL_yLIM = 9;                     // Upper left corner limit
        int LOWER_PDL_xLIM = 0;                     // Lower right corner limit
        int LOWER_PDL_yLIM = 0;                     // Lower right corner limit
        double PDL_VEL = 0.5;                       // How far to move the paddle per keypress
        // All of these can change, so making them look like variables rather than constants
        int winner = -1;                            // Determine which player wins.  -1 means no winner yet
        double ball_xvel = 0.0032 * (Math.random() < 0.5 ? -1 : 1);    // Random ball x velocity on start
        double ball_yvel = 0.0018 * (Math.random() < 0.5 ? -1 : 1);    // Random ball y velocity on start

        // More constants we need, to avoid magic numbers
        int SCREEN_HEIGHT = 720;  // 720p resolution
        int SCREEN_WIDTH = 1280;  // 16:9 aspect ratio

        double PADDLE_HEIGHT = 1.2;  // Height to draw the paddle
        double PADDLE_WIDTH = 0.33;   // Width to draw the paddle
        double PADDLE_1_X   = 1.0;   // Paddles don't move in the x axis
        double PADDLE_2_X   = 15.0;
        double BALL_RADIUS = 0.125;

        int FRAME_LENGTH = 5; // Pause between frames, in ms

        // Set up the drawing area
        StdDraw.setCanvasSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        StdDraw.setXscale(LOWER_PDL_xLIM, UPPER_PDL_xLIM);
        StdDraw.setYscale(LOWER_PDL_yLIM, UPPER_PDL_yLIM);
        StdDraw.enableDoubleBuffering();

        // Figure out where to draw the paddles and ball
        // Set paddles to center
        double p1_pdl_y = (UPPER_PDL_yLIM - PADDLE_HEIGHT) / 2.0 + PADDLE_HEIGHT / 2.0;
        double p2_pdl_y = (UPPER_PDL_yLIM - PADDLE_HEIGHT) / 2.0 + PADDLE_HEIGHT / 2.0;

        // Set ball to center
        double ball_x = UPPER_PDL_xLIM / 2.0;
        double ball_y = UPPER_PDL_yLIM / 2.0;

        // Set up the game loop
        while (winner < 0)
        {
            // Update the ball's position
            ball_x += ball_xvel;
            ball_y += ball_yvel;

            char keypress = 0;
            // Check for keypresses
            if (StdDraw.hasNextKeyTyped()) {
                keypress = Character.toUpperCase(StdDraw.nextKeyTyped());
            }

            // Move the paddles based on the keypress
            switch (keypress) {
                case 'W' -> {     // player 1 up
                    StdOut.println("Player 1 up");
                    p1_pdl_y += PDL_VEL;
                    if (p1_pdl_y + PADDLE_HEIGHT >= UPPER_PDL_yLIM) {
                        p1_pdl_y = UPPER_PDL_yLIM - PADDLE_HEIGHT;
                    }
                }
                case 'S' -> {     // player 1 down
                    StdOut.println("Player 1 down");
                    p1_pdl_y -= PDL_VEL;
                    if (p1_pdl_y < 0) {
                        p1_pdl_y = 0;
                    }
                }
                case 'I' -> {     // player 2 up
                    StdOut.println("Player 2 up");
                    p2_pdl_y += PDL_VEL;
                    if (p2_pdl_y + PADDLE_HEIGHT >= UPPER_PDL_yLIM) {
                        p2_pdl_y = UPPER_PDL_yLIM - PADDLE_HEIGHT;
                    }
                }
                case 'K' -> {     // player 2 down
                    StdOut.println("Player 2 down");
                    p2_pdl_y -= PDL_VEL;
                    if (p2_pdl_y < 0) {
                        p2_pdl_y = 0;
                    }
                }
            }

            // Clear the buffer, draw the picture, send it to the screen
            StdDraw.clear();
            StdDraw.filledRectangle(PADDLE_1_X, p1_pdl_y, PADDLE_WIDTH, PADDLE_HEIGHT);
            StdDraw.filledRectangle(PADDLE_2_X, p2_pdl_y, PADDLE_WIDTH, PADDLE_HEIGHT);
            StdDraw.filledCircle(ball_x,ball_y,BALL_RADIUS);
            StdDraw.show();
            // Set the frame rate
            StdDraw.pause(FRAME_LENGTH);

        }
    }
}
