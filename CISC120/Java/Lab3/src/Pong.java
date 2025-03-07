public class Pong {

    // More constants we need, to avoid magic numbers
    static final int SCREEN_HEIGHT = 720;  // 720p resolution
    static final int SCREEN_WIDTH = 1280;  // 16:9 aspect ratio
    static final double PADDLE_HEIGHT = SCREEN_HEIGHT / 6.0;  // Height to draw the paddle
    static final double PADDLE_WIDTH = SCREEN_WIDTH / 50.0;   // Width to draw the paddle
    static final double PADDLE_1_X   = PADDLE_WIDTH * 2;   // Paddles don't move in the x axis
    static final double PADDLE_2_X   = SCREEN_WIDTH - 2 * PADDLE_WIDTH;
    static final double PDL_VEL = SCREEN_HEIGHT / 12.0;   // How far to move the paddle per keypress
    static final double BALL_RADIUS = PADDLE_WIDTH;
    static final int FRAME_LENGTH = 5; // Pause between frames, in ms

    /**
     * Initializes the canvas size and scale.
     * Initialized the size and position of game objects.
     * @param params Structure containing the attributes of the ball and paddles.
     */
    public static void setup(Params params)
    {
        // Set up the drawing area
        StdDraw.setCanvasSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        StdDraw.setXscale(0, SCREEN_WIDTH);
        StdDraw.setYscale(0, SCREEN_HEIGHT);
        StdDraw.enableDoubleBuffering();

        params.ball_xvel = 3.2 * (Math.random() < 0.5 ? -1 : 1);    // Random ball x velocity on start
        params.ball_yvel = 1.8 * (Math.random() < 0.5 ? -1 : 1);    // Random ball y velocity on start
        // Figure out where to draw the paddles and ball
        // Set paddles to center
        params.p1_pdl_y = (SCREEN_HEIGHT - PADDLE_HEIGHT) / 2.0 + PADDLE_HEIGHT / 2.0;
        params.p2_pdl_y = (SCREEN_HEIGHT - PADDLE_HEIGHT) / 2.0 + PADDLE_HEIGHT / 2.0;
        // Set ball to center
        params.ball_x = SCREEN_WIDTH / 2.0;
        params.ball_y = SCREEN_HEIGHT / 2.0;
    }

    public static int collide(Params params)
    {
        // Check for collisions
        // Right and Left - winning happens
        if (params.ball_x - BALL_RADIUS <= 0) {
            return 0;
        }
        else if (params.ball_x + BALL_RADIUS >= SCREEN_WIDTH) {
            return 1;
        }

        // Top and bottom
        if (params.ball_y + BALL_RADIUS >= SCREEN_HEIGHT ||
                params.ball_y - BALL_RADIUS <= 0) {
            params.ball_yvel = -params.ball_yvel;
        }

        // Collide with paddle if:
        // Ball is above the bottom of the paddle
        // and below the top of the paddle
        // and the edge of the ball is past the face of the paddle
        // and the center of the ball isn't past the face of the paddle
        // and the ball is moving towards the face of the paddle
        if ( (params.ball_y > params.p1_pdl_y) &&
                (params.ball_y < params.p1_pdl_y + PADDLE_HEIGHT) &&
                (params.ball_x - BALL_RADIUS < PADDLE_1_X + PADDLE_WIDTH) &&
                //(ball_x > PADDLE_1_X + PADDLE_WIDTH) &&

                (params.ball_xvel < 0) ) {
            params.ball_xvel = -params.ball_xvel;
        }
        else if (params.ball_y > params.p2_pdl_y &&
                params.ball_y < params.p2_pdl_y + PADDLE_HEIGHT &&
                params.ball_x + BALL_RADIUS > PADDLE_2_X &&
                //ball_x < PADDLE_2_X  &&
                params.ball_xvel > 0) {
            params.ball_xvel = -params.ball_xvel;
        }
        return -1;
    }

    public static void input(Params params)
    {
        char keypress = 0;
        // Check for keypresses
        if (StdDraw.hasNextKeyTyped()) {
            keypress = Character.toUpperCase(StdDraw.nextKeyTyped());
        }

        // Move the paddles based on the keypress
        switch (keypress) {
            case 'W' -> {     // player 1 up
                StdOut.println("Player 1 up");
                params.p1_pdl_y += PDL_VEL;
                if (params.p1_pdl_y + PADDLE_HEIGHT >= SCREEN_HEIGHT) {
                    params.p1_pdl_y = SCREEN_HEIGHT - PADDLE_HEIGHT;
                }
            }
            case 'S' -> {     // player 1 down
                StdOut.println("Player 1 down");
                params.p1_pdl_y -= PDL_VEL;
                if (params.p1_pdl_y < 0) {
                    params.p1_pdl_y = 0;
                }
            }
            case 'I' -> {     // player 2 up
                StdOut.println("Player 2 up");
                params.p2_pdl_y += PDL_VEL;
                if (params.p2_pdl_y + PADDLE_HEIGHT >= SCREEN_HEIGHT) {
                    params.p2_pdl_y = SCREEN_HEIGHT - PADDLE_HEIGHT;
                }
            }
            case 'K' -> {     // player 2 down
                StdOut.println("Player 2 down");
                params.p2_pdl_y -= PDL_VEL;
                if (params.p2_pdl_y < 0) {
                    params.p2_pdl_y = 0;
                }
            }
        }
    }

    public static void move_ball(Params params)
    {
        // Update the ball's position
        params.ball_x += params.ball_xvel;
        params.ball_y += params.ball_yvel;
    }

    /**
     * The draw function renders all game objects to the back buffer and then
     * displays the back buffer on the screen for FRAME_LENGTH milliseconds.
     *
     * @param params
     */
    public static void draw(Params params)
    {
        // Clear the buffer, draw the picture, send it to the screen
        StdDraw.clear();
        StdDraw.filledRectangle(PADDLE_1_X, params.p1_pdl_y, PADDLE_WIDTH, PADDLE_HEIGHT);
        StdDraw.filledRectangle(PADDLE_2_X, params.p2_pdl_y, PADDLE_WIDTH, PADDLE_HEIGHT);
        StdDraw.filledCircle(params.ball_x,params.ball_y,BALL_RADIUS);
        StdDraw.show();
        // Set the frame rate
        StdDraw.pause(FRAME_LENGTH);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Params params = new Params();
        setup(params);
        // All of these can change, so making them look like variables rather than constants
        int winner = -1;                            // Determine which player wins.  -1 means no winner yet

        // Set up the game loop
        while (winner < 0)
        {
            // Move ball
            move_ball(params);
            // Check for input
            input(params);
            // Check for collisions
            winner = collide(params);
            // Update the screen
            draw(params);
        }
        if (winner == 0) {
            StdOut.println("Player 2 Wins!");
        }
        else {
            StdOut.println("Player 1 Wins!");
        }
    }

    static class Params {
        public double ball_x;
        public double ball_y;
        public double ball_xvel;
        public double ball_yvel;

        public double p1_pdl_y;
        public double p2_pdl_y;
    }
}
