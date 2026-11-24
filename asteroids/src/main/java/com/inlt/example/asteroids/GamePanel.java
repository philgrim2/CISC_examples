package com.inlt.example.asteroids;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JPanel;

/**
 * Custom Swing panel that renders the Asteroids game state, including the player ship,
 * UFO, asteroids, shots, score, remaining lives, and the end-game dialog.
 */
public class GamePanel extends JPanel {

    /** Reference to the main game instance whose state is rendered. */
    private final Asteroids game;

    /** Font used to render the player's current score. */
    private final Font scoreFont;

    /** Font used to render the Game Over dialog text. */
    private final Font dialogFont;

    /** Standard stroke used for drawing game objects. */
    private final BasicStroke standardStroke;

    /** Thin stroke used for drawing life indicator icons. */
    private final BasicStroke thinStroke;

    /**
     * Creates a new {@code GamePanel} bound to the given {@link Asteroids} game instance.
     *
     * @param game the {@link Asteroids} game instance to render
     */
    public GamePanel(Asteroids game) {
        this.game = game;
        this.scoreFont = new Font("Monospaced", Font.PLAIN, Asteroids.SCORE_FONT_SIZE);
        this.dialogFont = new Font("Monospaced", Font.PLAIN, Asteroids.DIALOG_FONT_SIZE);
        this.standardStroke = new BasicStroke(1.5f);
        this.thinStroke = new BasicStroke(1.0f);
        setPreferredSize(new Dimension(Asteroids.SCREEN_WIDTH, Asteroids.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    /**
     * Paints the current game frame onto the panel.
     *
     * @param graphics the {@link Graphics} context provided by Swing
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the background and drawing colors
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Asteroids.SCREEN_WIDTH, Asteroids.SCREEN_HEIGHT);
        g2d.setColor(Color.LIGHT_GRAY);

        // Draw the score
        g2d.setFont(this.scoreFont);
        String scoreText = Integer.toString(this.game.getScore());
        FontMetrics scoreMetrics = g2d.getFontMetrics(this.scoreFont);
        int scoreX = Asteroids.MARGIN_H - scoreMetrics.stringWidth(scoreText) / 2;
        if (scoreX < 10) {
            scoreX = 10;
        }
        int scoreY = (Asteroids.MARGIN_V / 2) + scoreMetrics.getAscent() / 2;
        g2d.drawString(scoreText, scoreX, scoreY);

        // Draw the lives
        g2d.setStroke(this.thinStroke);
        List<Ship> lives = this.game.getLives();
        for (Ship life : lives) {
            life.draw(g2d, Asteroids.SCREEN_HEIGHT);
        }
        g2d.setStroke(this.standardStroke);

        // Draw the ship, if it's spawned
        Ship ship = this.game.getShip();
        if (ship != null) {
            ship.draw(g2d, Asteroids.SCREEN_HEIGHT);
        }

        // Draw the UFO if there is one
        Ufo ufo = this.game.getUfo();
        if (ufo != null) {
            ufo.draw(g2d, Asteroids.SCREEN_HEIGHT);
        }

        // Draw the asteroids
        for (Asteroid asteroid : this.game.getAsteroids()) {
            asteroid.draw(g2d, Asteroids.SCREEN_HEIGHT);
        }

        // Draw the shots
        for (Shot shot : this.game.getShots()) {
            shot.draw(g2d, Asteroids.SCREEN_HEIGHT);
        }

        // Draw the end game dialog if the game has ended
        if (this.game.isGameOver()) {
            drawEndGameDialog(g2d);
        }

        g2d.dispose();
    }

    /**
     * Draws the centered Game Over dialog box displaying the final score.
     *
     * @param g2d the {@link Graphics2D} context on which to draw
     */
    private void drawEndGameDialog(Graphics2D g2d) {
        int rectX = (int) (Asteroids.SCREEN_WIDTH * 0.25);
        int rectY = (int) (Asteroids.SCREEN_HEIGHT * 0.40);
        int rectWidth = (int) (Asteroids.SCREEN_WIDTH * 0.50);
        int rectHeight = (int) (Asteroids.SCREEN_HEIGHT * 0.20);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(rectX, rectY, rectWidth, rectHeight);

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(this.dialogFont);
        g2d.drawRect(rectX, rectY, rectWidth, rectHeight);

        FontMetrics metrics = g2d.getFontMetrics(this.dialogFont);
        String gameOverText = "Game Over";
        int gameOverX = (Asteroids.SCREEN_WIDTH - metrics.stringWidth(gameOverText)) / 2;
        int gameOverY = (int) (Asteroids.SCREEN_HEIGHT * 0.45) + metrics.getAscent() / 2;
        g2d.drawString(gameOverText, gameOverX, gameOverY);

        String finalScoreText = "Final Score: " + this.game.getScore();
        int finalScoreX = (Asteroids.SCREEN_WIDTH - metrics.stringWidth(finalScoreText)) / 2;
        int finalScoreY = (int) (Asteroids.SCREEN_HEIGHT * 0.55) + metrics.getAscent() / 2;
        g2d.drawString(finalScoreText, finalScoreX, finalScoreY);
    }
}
