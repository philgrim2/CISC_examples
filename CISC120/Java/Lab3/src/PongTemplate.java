public class PongTemplate {
    public static void main(String[] args) {
        int WINNER = -1;                            // Determine which player wins.  -1 means no winner yet
        int UPPER_PDL_xLIM = 16;                    // Upper left corner
        int UPPER_PDL_yLIM = 9;                     // Upper left corner
        int LOWER_PDL_xLIM = 0;                     // Lower right corner
        int LOWER_PDL_yLIM = 0;                     // Lower right corner
        double PDL_VEL = 0.5;                       // How far to move the paddle per keypress
        double BALL_xVEL = 0.0032 * (Math.random() < 0.5 ? -1 : 1);    // Random x velocity on start
        double BALL_yVEL = 0.0018 * (Math.random() < 0.5 ? -1 : 1);    // Random y velocity on start



    }
}
