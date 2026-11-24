package com.inlt.example.asteroids;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class to represent the alien ships (UFOs).
 */
public class Ufo extends VectorObject {

    /**
     * Unit vector representing eastward (rightward) movement.
     */
    public static final Vector2D EAST = new Vector2D(1.0, 0.0);

    /**
     * Unit vector representing westward (leftward) movement.
     */
    public static final Vector2D WEST = new Vector2D(-1.0, 0.0);

    /**
     * UFO will shoot when it has traveled at least this percent of the limit area.
     */
    public static final double SHOT_FREQUENCY = 0.05;

    /**
     * Minimum fraction of the horizontal limit area that must be traveled before turning.
     */
    public static final double TURN_THRESHOLD_FACTOR = 0.2;

    /**
     * Range of a UFO shot as a fraction of the horizontal screen limit.
     */
    public static final double SHOT_RANGE_FACTOR = 0.75;

    /**
     * Ship base speed scalar added to size-based speed.
     */
    public static final double BASE_SPEED = 0.15;

    /**
     * Size identifier for a large UFO.
     */
    public static final int LARGE_SIZE = 1;

    /**
     * Size identifier for a small UFO.
     */
    public static final int SMALL_SIZE = 2;

    /**
     * Define the canonical ship polygon, which will get scaled based on the screen size.
     */
    public static final List<Vector2D> SHIP_POINTS = Collections.unmodifiableList(Arrays.asList(
            new Vector2D(0.0, 1.0),
            new Vector2D(2.0, 1.0),
            new Vector2D(2.5, 0.0),
            new Vector2D(2.0, -1.0),
            new Vector2D(-2.0, -1.0),
            new Vector2D(-2.5, 0.0),
            new Vector2D(-2.0, 1.0),
            new Vector2D(0.0, 1.0),
            new Vector2D(-1.0, 1.0),
            new Vector2D(-0.6, 1.75),
            new Vector2D(0.6, 1.75),
            new Vector2D(1.0, 1.0),
            new Vector2D(0.0, 1.0)
    ));

    /** The size category of this UFO (1 for large, 2 for small). */
    private final int size;

    /** Distance traveled since the last shot was fired. */
    private double sinceShot;

    /** Distance traveled since the last course change. */
    private double sinceTurn;

    /** Random number generator used for turning and shooting decisions. */
    private final Random random;

    /**
     * Creates a large westbound UFO at the specified scale factor and coordinates.
     *
     * @param scale the base scale factor for the UFO polygon
     * @param x     the initial X coordinate
     * @param y     the initial Y coordinate
     */
    public Ufo(double scale, double x, double y) {
        this(scale, x, y, LARGE_SIZE, WEST);
    }

    /**
     * Creates an instance of the alien UFO at the specified scale factor, size, and direction.
     *
     * @param scale     the base scale factor for the UFO polygon
     * @param x         the initial X coordinate
     * @param y         the initial Y coordinate
     * @param size      the UFO size category ({@code 1} for large, {@code 2} for small)
     * @param direction the initial travel direction vector
     */
    public Ufo(double scale, double x, double y, int size, Vector2D direction) {
        this(scale, x, y, size, direction, new Random());
    }

    /**
     * Creates an instance of the alien UFO with a custom {@link Random} instance.
     *
     * @param scale     the base scale factor for the UFO polygon
     * @param x         the initial X coordinate
     * @param y         the initial Y coordinate
     * @param size      the UFO size category ({@code 1} for large, {@code 2} for small)
     * @param direction the initial travel direction vector
     * @param random    the random number generator for movement and firing
     */
    public Ufo(double scale, double x, double y, int size, Vector2D direction, Random random) {
        super(SHIP_POINTS, size == SMALL_SIZE ? scale * 0.5 : scale);
        this.size = size;
        this.position = new Vector2D(x, y);
        translate(this.position);
        this.direction = new Vector2D(direction.getX(), direction.getY());
        // Small ship moves faster; scale velocity along the travel direction
        this.velocity = this.direction.multiply(BASE_SPEED + size);
        this.sinceShot = 0.0;
        this.sinceTurn = 0.0;
        this.random = random;
    }

    /**
     * Updates the UFO's position. Will randomly change direction occasionally.
     *
     * @param limitX the horizontal screen boundary
     * @param limitY the vertical screen boundary
     * @return {@code false} if the UFO has left the limit area, {@code true} otherwise
     */
    public boolean move(double limitX, double limitY) {
        // If the UFO has traveled at least 20% of the limit area since the last turn, it can turn
        if (this.random.nextInt(4) == 0 && this.sinceTurn > limitX * TURN_THRESHOLD_FACTOR) {
            // Going to turn. Can only move either straight east-west, or 45 degrees north/south.
            double rot = 45.0;
            if (Math.abs(this.direction.getX()) > 0.9 && Math.abs(this.direction.getX()) < 1.1) {
                // This is straight, so bend 45 degrees up or down
                if (this.random.nextInt(2) == 0) {
                    rot *= -1.0;
                }
            } else if (this.direction.getY() > 0.0) {
                if (this.direction.getX() > 0.0) {
                    rot *= -1.0;
                }
            } else {
                if (this.direction.getX() < 0.0) {
                    rot *= -1.0;
                }
            }

            rotate(rot);
            this.sinceTurn = 0.0;
        }

        Vector2D previousPosition = new Vector2D(this.position.getX(), this.position.getY());
        this.position = this.position.add(this.velocity);
        double stepDistance = previousPosition.distanceTo(this.position);
        this.sinceShot += stepDistance;
        this.sinceTurn += stepDistance;
        translate(this.position);

        double r = radius();
        return !(this.position.getX() - r > limitX
                || this.position.getX() + r < 0.0
                || this.position.getY() - r > limitY
                || this.position.getY() + r < 0.0);
    }

    /**
     * Fires a shot from the UFO's mass driver in a random direction if the UFO
     * has traveled at least the required distance since its last shot.
     *
     * @param limitX the horizontal screen boundary
     * @param limitY the vertical screen boundary
     * @return a new {@link Shot} if ready to fire, or {@code null} otherwise
     */
    public Shot shoot(double limitX, double limitY) {
        // If the UFO has traveled at least the required distance, fire a random shot
        if (this.sinceShot > limitX * SHOT_FREQUENCY) {
            Vector2D shotDirection = new Vector2D(this.direction.getX(), this.direction.getY());
            shotDirection.rotate(this.random.nextInt(360));
            Vector2D muzzle = this.position.add(shotDirection.multiply(radius()));
            Shot shot = new Shot(
                    muzzle.getX(),
                    muzzle.getY(),
                    shotDirection,
                    this.velocity,
                    limitX * SHOT_RANGE_FACTOR,
                    false
            );
            this.sinceShot = 0.0;
            return shot;
        }
        return null;
    }

    /**
     * Change travel direction and velocity vector without rotating the UFO's polygon orientation.
     *
     * @param angle the angle in degrees by which to rotate the direction and velocity
     */
    @Override
    public void rotate(double angle) {
        this.direction.rotate(angle);
        this.velocity.rotate(angle);
    }

    /**
     * Returns the size category of this UFO ({@code 1} for large, {@code 2} for small).
     *
     * @return the UFO size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Returns the distance traveled since this UFO last fired a shot.
     *
     * @return distance traveled since last shot
     */
    public double getSinceShot() {
        return this.sinceShot;
    }

    /**
     * Sets the distance traveled since this UFO last fired a shot.
     *
     * @param sinceShot distance traveled since last shot
     */
    public void setSinceShot(double sinceShot) {
        this.sinceShot = sinceShot;
    }

    /**
     * Returns the distance traveled since this UFO last changed direction.
     *
     * @return distance traveled since last turn
     */
    public double getSinceTurn() {
        return this.sinceTurn;
    }

    /**
     * Sets the distance traveled since this UFO last changed direction.
     *
     * @param sinceTurn distance traveled since last turn
     */
    public void setSinceTurn(double sinceTurn) {
        this.sinceTurn = sinceTurn;
    }
}
