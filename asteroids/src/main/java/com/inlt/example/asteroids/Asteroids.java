package com.inlt.example.asteroids;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Simple implementation of the original arcade game Asteroids.
 *
 * <p>Player controls include:
 * <ul>
 *   <li>Up arrow or W: Thrusters</li>
 *   <li>Left arrow or A: Rotate left</li>
 *   <li>Right arrow or D: Rotate right</li>
 *   <li>Space Bar: Fire mass driver</li>
 * </ul>
 *
 * <p>Extra life earned every 10,000 points (up to {@link #MAX_LIVES}).
 *
 * <p>Implementation class for the game itself. Holds all of the constants required
 * for setting up the game.
 *
 * <p>References:
 * <ul>
 *   <li>Sounds from https://www.classicgaming.cc/classics/asteroids/</li>
 *   <li>Vector math from https://stackoverflow.com/questions/28458145/rotate-2d-polygon-without-changing-its-position</li>
 * </ul>
 */
public class Asteroids {

    /** Canvas width in pixels. */
    public static final int SCREEN_WIDTH = 1200;

    /** Canvas height in pixels. */
    public static final int SCREEN_HEIGHT = 900;

    /** Scaled font size for dialog boxes. */
    public static final int DIALOG_FONT_SIZE = SCREEN_HEIGHT / 32;

    /** Scaled font size for score output. */
    public static final int SCORE_FONT_SIZE = SCREEN_HEIGHT / 48;

    /** Scale for player ship. */
    public static final double SHIP_SCALE = SCREEN_WIDTH / 120.0;

    /** Scale for largest asteroid size. */
    public static final double ASTEROID_SCALE = SCREEN_WIDTH / 80.0;

    /** Base score for destroying an asteroid. */
    public static final int ASTEROID_SCORE = 15;

    /** Base score for destroying a UFO. */
    public static final int UFO_SCORE = 50;

    /** Horizontal margin for score and lives display. */
    public static final int MARGIN_H = 40;

    /** Vertical margin for score and lives display. */
    public static final int MARGIN_V = 25;

    /** Time for each frame in milliseconds. */
    public static final int FRAME_LENGTH = 10;

    /** Maximum number of lives for the player. */
    public static final int MAX_LIVES = 10;

    /** Player starting lives. */
    public static final int STARTING_LIVES = 3;

    /** Score interval for earning an extra life. */
    public static final int EXTRA_LIFE_INTERVAL = 10000;

    /** Number of asteroids per spawn wave. */
    public static final int MAX_ASTEROIDS = 6;

    /** Minimum distance that an asteroid can spawn from the player's ship. */
    public static final double MIN_ASTEROID_DISTANCE = 150.0;

    /** Clear area radius required for spawning a new ship or UFO. */
    public static final double MIN_CLEAR_RADIUS = 100.0;

    /** Spawn delay after death (in milliseconds). */
    public static final int SPAWN_INTERVAL = 2000;

    /** Point interval for spawning a UFO. */
    public static final int UFO_INTERVAL = 1500;

    /** Player remaining life icons. */
    private final List<Ship> lives;

    /** Player current score. */
    private int score;

    /** Active player ship, or {@code null} when destroyed and awaiting respawn. */
    private Ship ship;

    /** List of current on-screen asteroids. */
    private final List<Asteroid> asteroids;

    /** List of current mass-driver shots in flight. */
    private final List<Shot> shots;

    /** Active alien UFO, or {@code null} if none is currently present. */
    private Ufo ufo;

    /** Score threshold at which the next extra life will be awarded. */
    private int nextNewLife;

    /** Remaining countdown time in milliseconds before attempting to respawn the ship. */
    private int spawnCountdown;

    /** Remaining score countdown before spawning a UFO. */
    private int ufoCountdown;

    /** Flag indicating whether the game is over. */
    private boolean gameOver;

    /** Keyboard listener handling player input. */
    private final GameKeyListener keyListener;

    /** Random number generator for spawning and asteroid splitting. */
    private final Random random;

    /** Swing application window (initialized only when running with a display). */
    private JFrame frame;

    /** Swing rendering panel (initialized only when running with a display). */
    private GamePanel gamePanel;

    /**
     * Creates a new {@code Asteroids} game instance initialized with default starting state.
     *
     * <p>Does not open a graphical window until {@link #run()} is invoked, allowing
     * full game logic and collision testing in headless environments.
     */
    public Asteroids() {
        this(new Random());
    }

    /**
     * Creates a new {@code Asteroids} game instance using the specified {@link Random} source.
     *
     * @param random the random number generator to use for game events
     */
    public Asteroids(Random random) {
        this.random = random;
        this.lives = new CopyOnWriteArrayList<>();
        this.asteroids = new CopyOnWriteArrayList<>();
        this.shots = new CopyOnWriteArrayList<>();
        this.score = 0;
        this.ship = null;
        this.ufo = null;
        this.gameOver = false;
        this.keyListener = new GameKeyListener();

        for (int i = 0; i < STARTING_LIVES; i++) {
            addLife();
        }

        this.nextNewLife = EXTRA_LIFE_INTERVAL;
        this.spawnCountdown = 0;
        this.ufoCountdown = UFO_INTERVAL;
    }

    /**
     * Adds an extra life to the player's life reserve, up to {@link #MAX_LIVES}.
     */
    public void addLife() {
        if (this.lives.size() >= MAX_LIVES) {
            return;
        }
        Ship newLife = new Ship(SHIP_SCALE / 2.0, 0.0, 0.0);
        double lifeOffset = MARGIN_H;
        for (int i = 0; i < this.lives.size(); i++) {
            lifeOffset += newLife.radius() * 2.0;
        }
        Vector2D newPos = new Vector2D(SCREEN_WIDTH - lifeOffset, SCREEN_HEIGHT - MARGIN_V);
        newLife.translate(newPos);
        this.lives.add(newLife);
    }

    /**
     * Spawns a new wave of large asteroids at random positions at least
     * {@link #MIN_ASTEROID_DISTANCE} away from the player's ship (or screen center if unspawned).
     */
    public void spawnAsteroids() {
        Vector2D referenceCenter = (this.ship != null)
                ? this.ship.center()
                : new Vector2D(SCREEN_WIDTH / 2.0, SCREEN_HEIGHT / 2.0);
        while (this.asteroids.size() < MAX_ASTEROIDS) {
            double x = this.random.nextInt(SCREEN_WIDTH);
            double y = this.random.nextInt(SCREEN_HEIGHT);
            if (referenceCenter.distanceTo(x, y) >= MIN_ASTEROID_DISTANCE) {
                this.asteroids.add(new Asteroid(
                        ASTEROID_SCALE,
                        x,
                        y,
                        Asteroid.LARGE_SIZE,
                        null,
                        false,
                        null,
                        null,
                        this.random
                ));
            }
        }
    }

    /**
     * Spawns the player's ship at the center of the screen after verifying
     * that there is a minimum clear area free of asteroids.
     */
    public void spawnShip() {
        Vector2D center = new Vector2D(SCREEN_WIDTH / 2.0, SCREEN_HEIGHT / 2.0);
        double closest = SCREEN_WIDTH;
        for (Asteroid asteroid : this.asteroids) {
            double dist = asteroid.getPosition().distanceTo(center);
            if (dist < closest) {
                closest = dist;
            }
        }
        if (closest > MIN_CLEAR_RADIUS) {
            this.ship = new Ship(SHIP_SCALE, SCREEN_WIDTH / 2.0, SCREEN_HEIGHT / 2.0);
        }
    }

    /**
     * Spawns a UFO of one of two random sizes at one of four random entry points,
     * provided there is a minimum clear area free of asteroids at the entry point.
     */
    public void spawnUfo() {
        // Pick random size and position
        int eastWest = this.random.nextInt(2) + 1;
        int northSouth = this.random.nextInt(2) + 1;
        int which = this.random.nextInt(10) + 1;

        double x;
        double dirX;
        if (eastWest == 1) {
            x = 0.0;
            dirX = 1.0;
        } else {
            x = SCREEN_WIDTH;
            dirX = -1.0;
        }

        double y;
        if (northSouth == 1) {
            y = SCREEN_HEIGHT / 4.0;
        } else {
            y = SCREEN_HEIGHT - (SCREEN_HEIGHT / 4.0);
        }

        int size = (which > 6) ? Ufo.SMALL_SIZE : Ufo.LARGE_SIZE;
        Vector2D direction = new Vector2D(dirX, 0.0);

        Vector2D entryCenter = new Vector2D(x, y);
        double closest = SCREEN_WIDTH;
        for (Asteroid asteroid : this.asteroids) {
            double dist = asteroid.getPosition().distanceTo(entryCenter);
            if (dist < closest) {
                closest = dist;
            }
        }
        if (closest > MIN_CLEAR_RADIUS) {
            this.ufo = new Ufo(SHIP_SCALE, x, y, size, direction, this.random);
        }
    }

    /**
     * Check for key input, and update the ship accordingly.
     */
    public void playerInput() {
        if (this.ship == null) {
            // Clear out key buffer while ship is not spawned
            this.keyListener.clearKeyBuffer();
        } else {
            while (this.keyListener.hasNextKeyTyped()) {
                char key = this.keyListener.nextKeyTyped();
                // One keypress equals one shot; we don't use key repeat for firing
                if (key == ' ') {
                    Vector2D muzzle = this.ship.getPosition().add(
                            this.ship.getDirection().multiply(SHIP_SCALE)
                    );
                    this.shots.add(new Shot(
                            muzzle.getX(),
                            muzzle.getY(),
                            this.ship.getDirection(),
                            this.ship.getVelocity(),
                            SCREEN_HEIGHT * 0.8
                    ));
                }
            }

            if (this.keyListener.isRotateLeftPressed()) {
                // Rotate ship counterclockwise
                this.ship.rotate(Ship.SHIP_ROTATE_DEGREES);
            } else if (this.keyListener.isRotateRightPressed()) {
                // Rotate ship clockwise
                this.ship.rotate(-Ship.SHIP_ROTATE_DEGREES);
            }

            if (this.keyListener.isThrustPressed()) {
                // Fire thrusters
                this.ship.thrust(true);
            } else {
                this.ship.thrust(false);
            }
        }
    }

    /**
     * Splits a destroyed asteroid of size 1 or 2 into three smaller child asteroids.
     *
     * @param parent the destroyed asteroid
     */
    private void splitAsteroid(Asteroid parent) {
        if (parent.getSize() < Asteroid.SMALL_SIZE) {
            for (int i = 0; i < 3; i++) {
                double deflection = this.random.nextInt(61) - 30;
                Asteroid rock = new Asteroid(
                        ASTEROID_SCALE,
                        parent.getPosition().getX(),
                        parent.getPosition().getY(),
                        parent.getSize() + 1,
                        parent.getVelocity(),
                        true,
                        parent.getDirection(),
                        deflection,
                        this.random
                );
                this.asteroids.add(rock);
            }
        }
    }

    /**
     * Handles the destruction of the player's ship: clears the active ship,
     * removes one life icon if available, and resets the respawn countdown.
     */
    private void destroyPlayerShip() {
        this.ship = null;
        if (!this.lives.isEmpty()) {
            this.lives.remove(this.lives.size() - 1);
        }
        this.spawnCountdown = SPAWN_INTERVAL;
    }

    /**
     * Check for collisions, and remove shots that have reached max range.
     * Asteroids don't collide with each other.
     */
    public void collide() {
        // Create copies of the lists to iterate over. Hitting an asteroid
        // consumes the shot, and the asteroid is either split or destroyed.
        List<Shot> tempShots = new ArrayList<>(this.shots);
        List<Asteroid> tempAsteroids = new ArrayList<>(this.asteroids);

        // 1. Check asteroid collisions with UFO and with shots
        for (Asteroid asteroid : tempAsteroids) {
            // If there's an active UFO, check for it hitting an asteroid
            if (this.ufo != null && this.asteroids.contains(asteroid)) {
                double dist = this.ufo.getPosition().distanceTo(asteroid.getPosition());
                if (dist < this.ufo.radius() + asteroid.radius()) {
                    this.asteroids.remove(asteroid);
                    splitAsteroid(asteroid);
                    this.ufo = null;
                    this.ufoCountdown = UFO_INTERVAL;
                    continue;
                }
            }

            // Check for shots hitting this asteroid
            for (Shot shot : tempShots) {
                if (this.shots.contains(shot) && this.asteroids.contains(asteroid)) {
                    double dist = shot.getPosition().distanceTo(asteroid.getPosition());
                    if (dist < shot.radius() + asteroid.radius()) {
                        this.shots.remove(shot);
                        this.asteroids.remove(asteroid);
                        if (shot.isPlayerShot()) {
                            int pointsEarned = asteroid.getSize() * ASTEROID_SCORE;
                            this.score += pointsEarned;
                            this.ufoCountdown -= pointsEarned;
                        }
                        splitAsteroid(asteroid);
                        break;
                    }
                }
            }
        }

        // 2. Check remaining shots hitting the UFO or player ship, or expiring at max range
        for (Shot shot : tempShots) {
            if (!this.shots.contains(shot)) {
                continue;
            }
            if (this.ufo != null && shot.isPlayerShot()) {
                double dist = shot.getPosition().distanceTo(this.ufo.getPosition());
                if (dist < shot.radius() + this.ufo.radius()) {
                    this.shots.remove(shot);
                    this.score += this.ufo.getSize() * UFO_SCORE;
                    this.ufoCountdown = UFO_INTERVAL;
                    this.ufo = null;
                    continue;
                }
            } else if (this.ship != null && !shot.isPlayerShot()) {
                // See if a UFO shot hits the player
                double dist = shot.getPosition().distanceTo(this.ship.getPosition());
                if (dist < this.ship.radius() + shot.radius()) {
                    this.shots.remove(shot);
                    destroyPlayerShip();
                    continue;
                }
            }

            // Check and remove shots that are out of range
            if (shot.isMaxRangeReached()) {
                this.shots.remove(shot);
            }
        }

        // 3. Check player ship colliding with asteroids
        if (this.ship != null) {
            List<Asteroid> currentAsteroids = new ArrayList<>(this.asteroids);
            for (Asteroid asteroid : currentAsteroids) {
                if (this.asteroids.contains(asteroid)) {
                    double dist = this.ship.getPosition().distanceTo(asteroid.getPosition());
                    if (dist < this.ship.radius() + asteroid.radius()) {
                        this.asteroids.remove(asteroid);
                        destroyPlayerShip();
                        splitAsteroid(asteroid);
                        break;
                    }
                }
            }
        }

        // 4. Check player ship colliding directly with UFO
        if (this.ship != null && this.ufo != null) {
            double dist = this.ship.getPosition().distanceTo(this.ufo.getPosition());
            if (dist < this.ship.radius() + this.ufo.radius()) {
                destroyPlayerShip();
                this.ufo = null;
                this.ufoCountdown = UFO_INTERVAL;
            }
        }
    }

    /**
     * Advances the game simulation by a single frame: handles respawning, processes player input,
     * moves all active objects, resolves collisions, and awards extra lives when score thresholds
     * are reached.
     */
    public void updateFrame() {
        // If there's no ship, spawn one at the end of the countdown
        if (this.ship == null) {
            if (this.spawnCountdown <= 0) {
                spawnShip();
            } else {
                this.spawnCountdown -= FRAME_LENGTH;
            }
        }

        // If it's time to spawn a new UFO, spawn it
        if (this.ufo == null && this.ufoCountdown <= 0) {
            spawnUfo();
        }

        // Check for keyboard input
        playerInput();

        // Spawn some asteroids if none remain
        if (this.asteroids.isEmpty()) {
            spawnAsteroids();
        }

        // Move asteroids
        for (Asteroid asteroid : this.asteroids) {
            asteroid.move(SCREEN_WIDTH, SCREEN_HEIGHT);
        }

        // Move shots
        for (Shot shot : this.shots) {
            shot.move(SCREEN_WIDTH, SCREEN_HEIGHT);
        }

        // Move player ship
        if (this.ship != null) {
            this.ship.move(SCREEN_WIDTH, SCREEN_HEIGHT);
        }

        // Move UFO and let it shoot
        if (this.ufo != null) {
            boolean onScreen = this.ufo.move(SCREEN_WIDTH, SCREEN_HEIGHT);
            Shot ufoShot = this.ufo.shoot(SCREEN_WIDTH, SCREEN_HEIGHT);
            if (ufoShot != null) {
                this.shots.add(ufoShot);
            }
            // Remove the UFO if it has left the screen
            if (!onScreen) {
                this.ufo = null;
                this.ufoCountdown = UFO_INTERVAL;
            }
        }

        // Check for collisions
        collide();

        // Award extra life every EXTRA_LIFE_INTERVAL points
        while (this.score >= this.nextNewLife) {
            addLife();
            this.nextNewLife += EXTRA_LIFE_INTERVAL;
        }

        if (this.lives.isEmpty()) {
            this.gameOver = true;
        }
    }

    /**
     * Initializes the Swing window and drawing panel when running in a graphical environment.
     */
    private void initGui() {
        if (GraphicsEnvironment.isHeadless() || this.frame != null) {
            return;
        }
        this.frame = new JFrame("Asteroids");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.gamePanel = new GamePanel(this);
        this.gamePanel.addKeyListener(this.keyListener);
        this.frame.addKeyListener(this.keyListener);
        this.frame.setContentPane(this.gamePanel);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.gamePanel.requestFocusInWindow();
    }

    /**
     * Draws the ship, asteroids, shots, and score by requesting a repaint of the game panel
     * and pausing for {@link #FRAME_LENGTH} milliseconds.
     */
    public void draw() {
        if (this.gamePanel != null) {
            this.gamePanel.repaint();
            sleepQuietly(FRAME_LENGTH);
        }
    }

    /**
     * Displays a dialog box that shows the game over message, then waits for a keypress.
     */
    public void endGameDialog() {
        this.gameOver = true;
        if (this.gamePanel == null) {
            return;
        }
        this.gamePanel.repaint();
        // Clear key buffer after a pause
        sleepQuietly(1000);
        this.keyListener.clearKeyBuffer();
        sleepQuietly(1000);
        this.keyListener.clearKeyBuffer();

        boolean waitingForKey = true;
        while (waitingForKey && this.frame != null && this.frame.isDisplayable()) {
            this.gamePanel.repaint();
            sleepQuietly(FRAME_LENGTH);
            if (this.keyListener.hasNextKeyTyped()) {
                waitingForKey = false;
            }
        }
        if (this.frame != null) {
            this.frame.dispose();
        }
    }

    /**
     * Sleeps the current thread for the specified duration in milliseconds.
     *
     * @param millis the number of milliseconds to sleep
     */
    private static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Runs the main game loop until the player is out of lives, then displays the end-game dialog.
     */
    public void run() {
        initGui();
        // Loop until the player is out of lives
        while (!this.lives.isEmpty()) {
            draw();
            updateFrame();
        }
        // Show the end game dialog
        draw();
        endGameDialog();
    }

    /**
     * Returns the list of remaining life ships.
     *
     * @return the list of life indicator ships
     */
    public List<Ship> getLives() {
        return this.lives;
    }

    /**
     * Returns the player's current score.
     *
     * @return the current score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Sets the player's current score.
     *
     * @param score the new score value
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the active player ship, or {@code null} if not currently spawned.
     *
     * @return the player {@link Ship} or {@code null}
     */
    public Ship getShip() {
        return this.ship;
    }

    /**
     * Sets the active player ship.
     *
     * @param ship the player {@link Ship} or {@code null}
     */
    public void setShip(Ship ship) {
        this.ship = ship;
    }

    /**
     * Returns the mutable list of active asteroids.
     *
     * @return the list of {@link Asteroid} objects
     */
    public List<Asteroid> getAsteroids() {
        return this.asteroids;
    }

    /**
     * Returns the mutable list of active shots.
     *
     * @return the list of {@link Shot} objects
     */
    public List<Shot> getShots() {
        return this.shots;
    }

    /**
     * Returns the active alien UFO, or {@code null} if none is on screen.
     *
     * @return the active {@link Ufo} or {@code null}
     */
    public Ufo getUfo() {
        return this.ufo;
    }

    /**
     * Sets the active alien UFO.
     *
     * @param ufo the {@link Ufo} instance or {@code null}
     */
    public void setUfo(Ufo ufo) {
        this.ufo = ufo;
    }

    /**
     * Returns the score threshold for the next extra life.
     *
     * @return the next extra-life score threshold
     */
    public int getNextNewLife() {
        return this.nextNewLife;
    }

    /**
     * Sets the score threshold for the next extra life.
     *
     * @param nextNewLife the next extra-life score threshold
     */
    public void setNextNewLife(int nextNewLife) {
        this.nextNewLife = nextNewLife;
    }

    /**
     * Returns the remaining ship spawn countdown in milliseconds.
     *
     * @return the spawn countdown in milliseconds
     */
    public int getSpawnCountdown() {
        return this.spawnCountdown;
    }

    /**
     * Sets the remaining ship spawn countdown in milliseconds.
     *
     * @param spawnCountdown the spawn countdown in milliseconds
     */
    public void setSpawnCountdown(int spawnCountdown) {
        this.spawnCountdown = spawnCountdown;
    }

    /**
     * Returns the remaining score countdown before a UFO spawns.
     *
     * @return the UFO score countdown
     */
    public int getUfoCountdown() {
        return this.ufoCountdown;
    }

    /**
     * Sets the remaining score countdown before a UFO spawns.
     *
     * @param ufoCountdown the UFO score countdown
     */
    public void setUfoCountdown(int ufoCountdown) {
        this.ufoCountdown = ufoCountdown;
    }

    /**
     * Returns whether the game is over.
     *
     * @return {@code true} if the game is over, {@code false} otherwise
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Sets whether the game is over.
     *
     * @param gameOver {@code true} if the game is over
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Returns the keyboard input listener associated with this game instance.
     *
     * @return the {@link GameKeyListener}
     */
    public GameKeyListener getKeyListener() {
        return this.keyListener;
    }

    /**
     * Main program entry point. Creates the game object and runs it.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Asteroids game = new Asteroids();
        game.run();
    }
}
