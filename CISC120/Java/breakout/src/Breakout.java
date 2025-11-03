import java.awt.*;

/**
 * Breakout.java, by Phil Grim
 *
 * Simple implementation of the original arcade game Breakout.
 * Player controls include:
 *    s:  Serve the ball
 *    a:  Move the paddle left
 *    d:  Move the paddle right
 *
 * Requires the textbook libraries from 'Introduction to Programming in Java: An Interdisciplinary Approach, 1st ed.'
 * by Sedgewick and Wayne, which can be found on the book's website at
 * https://introcs.cs.princeton.edu/java/stdlib/
 *
 * Outstanding issues:
 *   Collision detection with bricks and paddles can be wonky, and could be optimized better
 *   Key repeat doesn't work in stddraw
 *
 * Possible improvements:
 *   Add 'english' to the ball/paddle interaction
 *   Add sound
 *
 * Possible practical uses:
 *   As a lab assignment/take-home exam for students to implement
 *   As a lab assignment for students to convert from structured to object oriented
 *
 * """
 */
public class Breakout {
    // Set up the canvas size
    protected static final int SCREEN_WIDTH = 700;
    protected static final int SCREEN_HEIGHT = 980;

    // Set the size of the playing field sidelines
    protected static final int SIDELINE_SIZE = 20;

    // Set the size of the paddles
    protected static final int PADDLE_HEIGHT = 20;

    // Scale paddle width with screen
    protected static final int PADDLE_WIDTH = 25 + SCREEN_WIDTH / 7;

    // Also set the offset of the paddles from the bottom of the playing field
    protected static final int PADDLE_OFFSET = 30;

    // Paddle movement speed
    protected static final int PADDLE_DELTA_X = 10;

    // Number of rows of bricks
    protected static final int BRICK_ROWS = 6;

    // Number of columns of bricks
    protected static final int BRICK_COLS=8;

    // Margin between bricks
    protected static final int BRICK_MARGIN = 8;

    // Set the size of the bricks
    protected static final int BRICK_HEIGHT = 20;

    // Scale brick width with screen
    protected static final int BRICK_WIDTH = ((SCREEN_WIDTH - (SIDELINE_SIZE * 2)) / BRICK_COLS) - BRICK_MARGIN;

    // Set the size of the ball
    protected static final int BALL_SIZE = 20;

    // Set a scaled font size for dialog boxes
    protected static final int DIALOG_FONT_SIZE = SCREEN_HEIGHT / 32;
    // Make the font
    protected static final Font DIALOG_FONT = new Font("SansSerif", Font.PLAIN, DIALOG_FONT_SIZE);

    // Set a scaled font size for score output
    protected static final int SCORE_FONT_SIZE = PADDLE_OFFSET - BRICK_MARGIN;
    // Make the font
    protected static final Font SCORE_FONT = new Font("SansSerif", Font.PLAIN, SCORE_FONT_SIZE);

    // Set the time for each frame in milliseconds
    protected static final int FRAME_LENGTH = 10;

    // Number of lives for the player
    protected static final int  MAX_LIVES = 3;

    /**
     * Displays a dialog box that shows the game over message, then waits for a keypress.
     */
    protected static void endGameDialog(int score) {
        StdDraw.setFont(DIALOG_FONT);
        StdDraw.rectangle(SCREEN_WIDTH * 0.25, SCREEN_HEIGHT * 0.10, SCREEN_WIDTH * 0.5, SCREEN_HEIGHT * 0.20);
        StdDraw.text(SCREEN_WIDTH / 2.0, SCREEN_HEIGHT * 0.25, "Game Over");
        StdDraw.text(SCREEN_WIDTH / 2.0, SCREEN_HEIGHT * 0.15, String.format("Final Score: %d", score));
        StdDraw.show();
        boolean moreElectricity = true;
        while (moreElectricity) {
            StdDraw.pause(10);
            if (StdDraw.hasNextKeyTyped()) moreElectricity = false;
        }
    }

    /**
     * Chooses a random direction for the ball service.
     * @return A Vector2D object containing the random velocity vector.
     */
    protected static Vector2D randomServe() {
        double xvel = 1.0 + Math.random() + Math.random();
        double which = Math.random();
        if (which > 0.5) xvel = -xvel;

        double yvel = 2.0 + Math.random();

        return new Vector2D(xvel, yvel);
    }

    /**
     *  Checks to see if the center of the ball has reached the bottom of the
     *  playing field, and if so, deducts a life
     *
     * @return The number of remaining lives.
     */
    protected static int checkLife(double ballY, int lives) {
        double ballRadius = BALL_SIZE / 2.0;
        if (ballY + ballRadius < 0) return lives - 1;
        else                        return lives;
    }

    /**
     * Draw the current state of the game
     *
     */
    protected static void draw(int paddle_x, int paddle_y, int ball_x, int ball_y, boolean[][] bricks, int lives, int score, boolean serve) {
        // Set the background and drawing colors
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.LIGHT_GRAY);

        // Draw the sidelines
        StdDraw.filledRectangle(0, SCREEN_HEIGHT - SIDELINE_SIZE, SCREEN_WIDTH, SIDELINE_SIZE);
        StdDraw.filledRectangle(0, 0, SIDELINE_SIZE, SCREEN_HEIGHT);
        StdDraw.filledRectangle(SCREEN_WIDTH - SIDELINE_SIZE, 0, SIDELINE_SIZE, SCREEN_HEIGHT);

        // Draw the paddle
        int pllx = paddle_x - PADDLE_WIDTH / 2;
        int plly = paddle_y - PADDLE_HEIGHT / 2;
        StdDraw.filledRectangle(pllx, plly, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw the ball.
        int ballRadius = BALL_SIZE / 2;
        StdDraw.filledCircle(ball_x, ball_y, ballRadius);

        // Draw the score
        StdDraw.setFont(SCORE_FONT);
        StdDraw.text(SIDELINE_SIZE + SCORE_FONT_SIZE * 2, PADDLE_OFFSET / 2, String.format("Score: %d", score));

        int lifeOffset = SIDELINE_SIZE + SCORE_FONT_SIZE;

        // Draw the lives indicator
        for (int spot = 1; spot < MAX_LIVES + 1; spot++) {
            if (lives > spot)
                StdDraw.filledCircle(SCREEN_WIDTH - lifeOffset, PADDLE_OFFSET / 2, ballRadius);
            else
                StdDraw.circle(SCREEN_WIDTH - lifeOffset, PADDLE_OFFSET / 2, ballRadius);
           lifeOffset += BALL_SIZE + BRICK_MARGIN;
        }

        // Draw bricks
        int row_y_offset = SCREEN_HEIGHT - (SIDELINE_SIZE + PADDLE_OFFSET + BRICK_HEIGHT);
        int row_x_offset_left = SIDELINE_SIZE + (BRICK_MARGIN / 2) + (
            (SCREEN_WIDTH - (2 * SIDELINE_SIZE) - (BRICK_COLS * BRICK_WIDTH) - (BRICK_COLS * BRICK_MARGIN)) / 2);
        for (boolean[] row: bricks) {
            int row_x_offset = row_x_offset_left;
            for (boolean column : row) {
                if (column)
                    StdDraw.filledRectangle(row_x_offset, row_y_offset, BRICK_WIDTH, BRICK_HEIGHT);
                row_x_offset += BRICK_WIDTH + BRICK_MARGIN;
            }
            row_y_offset -= BRICK_HEIGHT + BRICK_MARGIN;
        }

        // If we're waiting for a serve, draw the dialog
        if (serve) {
            StdDraw.setFont(DIALOG_FONT);
            StdDraw.rectangle(SCREEN_WIDTH * 0.33, SCREEN_HEIGHT * 0.10, SCREEN_WIDTH * 0.33, SCREEN_HEIGHT * 0.10);
            StdDraw.text(SCREEN_WIDTH / 2, SCREEN_HEIGHT * 0.15, "Press S to serve.");
        }

        //Update screen
        StdDraw.show();
        StdDraw.pause(FRAME_LENGTH);
    }

    /***
     *     Check for key input, and move the paddle if there is any.
     *     If the ball isn't moving, move it with the paddle.
     *     Serve if needed
     *
     *     The info object is updated with the new information
     */
    protected static void playerInput(Info info) {
        Vector2D ballVelocity = info.getBallVelocity();
        if (StdDraw.hasNextKeyTyped()) {
            char key = Character.toLowerCase(StdDraw.nextKeyTyped());

            if (key == 's' && ballVelocity.getX() == 0 && ballVelocity.getY() == 0) {
                // Serve the ball
                ballVelocity = randomServe();
            } else if (key == 'a') {
                // Move paddle to the left, but prevent it from going off the screen
                int paddle_x = info.getPaddleX() - PADDLE_DELTA_X;
                if (paddle_x < SIDELINE_SIZE + PADDLE_WIDTH / 2)
                    paddle_x = SIDELINE_SIZE + PADDLE_WIDTH / 2;
                info.setPaddleX(paddle_x);
            } else if (key == 'd') {
                // Move paddle to the left, but prevent it from going off the screen
                int paddle_x = info.getPaddleX() + PADDLE_DELTA_X;
                if (paddle_x > SCREEN_WIDTH - (SIDELINE_SIZE + PADDLE_WIDTH / 2))
                    paddle_x = SCREEN_WIDTH - (SIDELINE_SIZE + PADDLE_WIDTH / 2);
                info.setPaddleX(paddle_x);
            }
        }
        if (ballVelocity.getX() == 0 && ballVelocity.getY() == 0)
            info.setBallX(info.getPaddleX());
    }

    /**
     * Count remaining bricks
     * @return Integer representing the number of bricks remaining
     */
    protected static int count(boolean[][] bricks) {
        int sumb = 0;
        for (boolean[] row: bricks)
            for(boolean column: row)
                if (column) sumb++;
        return sumb;
    }

    /**
     *     Updates the position of the ball based on the current x and y velocities,
     *     and checks for collisions.
     *
     *     The info object is updated directly.
     *
     * @param info
     */
    protected static void ball(Info info) {
        // Move the ball
    ball_x += ball_xvel
    ball_y += ball_yvel

    #Now check for wall and top collision
    newDeltaX = ball_xvel
            newDeltaY = ball_yvel
    ballRadius = BALL_SIZE // 2

    # Check top
    if ball_y + ballRadius >= SCREEN_HEIGHT - SIDELINE_SIZE:
    newDeltaY = -newDeltaY

    # Check left and right
    if ball_x - ballRadius <= SIDELINE_SIZE or ball_x + ballRadius >= SCREEN_WIDTH - SIDELINE_SIZE:
    newDeltaX = -newDeltaX

    # Check for paddle
    if (ball_x - ballRadius >= paddle_x - PADDLE_WIDTH // 2 and
    ball_x + ballRadius <= paddle_x + PADDLE_WIDTH // 2 and
    ball_y - ballRadius <= paddle_y + PADDLE_HEIGHT // 2 and
    ball_y - ballRadius >= paddle_y):
    newDeltaY = -newDeltaY

    #Finally check for brick collision
    #Can probably optimize this
    row_y_offset = SCREEN_HEIGHT - (SIDELINE_SIZE + PADDLE_OFFSET + BRICK_HEIGHT)
    row_x_offset_left = SIDELINE_SIZE + (BRICK_MARGIN // 2) + (
    (SCREEN_WIDTH - (2 * SIDELINE_SIZE) - (BRICK_COLS * BRICK_WIDTH) - (BRICK_COLS * BRICK_MARGIN)) // 2)
            for row in bricks:
    row_x_offset = row_x_offset_left
        for brick in range(len(row)):
            # Skip if missing
            if row[brick]:
            #Top
                if (ball_x - ballRadius >= row_x_offset and
    ball_x + ballRadius <= row_x_offset + BRICK_WIDTH and
    ball_y - ballRadius <= row_y_offset + BRICK_HEIGHT and
    ball_y - ballRadius >= row_y_offset + BRICK_HEIGHT - BRICK_MARGIN):
    newDeltaY = -newDeltaY
    row[brick] = False
    score += 1
            #Bottom
    elif (ball_x - ballRadius >= row_x_offset and
                    ball_x + ballRadius <= row_x_offset + BRICK_WIDTH and
                    ball_y + ballRadius <= row_y_offset + BRICK_MARGIN and
                    ball_y + ballRadius >= row_y_offset):
    newDeltaY = -newDeltaY
    row[brick] = False
    score += 1
            #Left
    elif (ball_x + ballRadius >= row_x_offset and
                    ball_x + ballRadius <= row_x_offset + BRICK_MARGIN and
                    ball_y <= row_y_offset + BRICK_HEIGHT and
                    ball_y >= row_y_offset):
    newDeltaX = -newDeltaX
    row[brick] = False
    score += 1
            #Right
    elif (ball_x - ballRadius <= row_x_offset + BRICK_WIDTH and
                    ball_x - ballRadius >= row_x_offset + BRICK_WIDTH - BRICK_MARGIN and
                    ball_y <= row_y_offset + BRICK_HEIGHT and
                    ball_y >= row_y_offset):
    newDeltaX = -newDeltaX
    row[brick] = False
    score += 1



    row_x_offset += BRICK_WIDTH + BRICK_MARGIN
    row_y_offset -= BRICK_HEIGHT + BRICK_MARGIN

    return (ball_x, ball_y, newDeltaX, newDeltaY, score)
    }

    public static void main(String[] args) {

    }

    public static class Info {
        int ballX;
        int ballY;
        int paddleX;
        int paddleY;
        boolean[][] bricks;
        Vector2D ballVelocity;
        int score;
        public Info(int ballX, int ballY, int paddleX, int paddleY, Vector2D ballVelocity, boolean[][] bricks, int score)
        {
            this.ballX = ballX;
            this.ballY = ballY;
            this.paddleX = paddleX;
            this.paddleY = paddleY;
            this.bricks = bricks;
            this.ballVelocity = ballVelocity;
            this.score = score;
        }

        public int getBallX() {
            return ballX;
        }

        public void setBallX(int ballX) {
            this.ballX = ballX;
        }

        public int getPaddleX() {
            return paddleX;
        }

        public void setPaddleX(int paddleX) {
            this.paddleX = paddleX;
        }

        public Vector2D getBallVelocity() {
            return ballVelocity;
        }

        public void setBallVelocity(Vector2D ballVelocity) {
            this.ballVelocity = ballVelocity;
        }

        public int getBallY() {
            return ballY;
        }

        public void setBallY(int ballY) {
            this.ballY = ballY;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getPaddleY() {
            return paddleY;
        }

        public void setPaddleY(int paddleY) {
            this.paddleY = paddleY;
        }

        public boolean[][] getBricks() {
            return bricks;
        }

        public void setBricks(boolean[][] bricks) {
            this.bricks = bricks;
        }
    }
}
