package com.inlt.example.asteroids;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class to represent an on-screen asteroid. Can be scaled to various sizes.
 */
public class Asteroid extends VectorObject {

    /**
     * Define the canonical polygon for an asteroid.
     */
    public static final List<Vector2D> ASTEROID_POINTS = Collections.unmodifiableList(Arrays.asList(
            new Vector2D(0.0, 4.0),
            new Vector2D(1.0, 3.5),
            new Vector2D(1.2, 3.0),
            new Vector2D(2.0, 2.0),
            new Vector2D(2.5, 3.2),
            new Vector2D(3.6, -0.6),
            new Vector2D(3.25, -2.6),
            new Vector2D(2.25, -1.7),
            new Vector2D(1.25, -3.2),
            new Vector2D(0.75, -2.6),
            new Vector2D(-1.0, -3.2),
            new Vector2D(-2.24, -2.67),
            new Vector2D(-3.5, -1.15),
            new Vector2D(-2.4, 2.6),
            new Vector2D(-1.8, 2.25),
            new Vector2D(-0.5, 3.0)
    ));

    /**
     * Velocity step (5 speeds possible).
     */
    public static final double VELOCITY_STEP = 0.25;

    /**
     * Size constant for a large (initial) asteroid.
     */
    public static final int LARGE_SIZE = 1;

    /**
     * Size constant for a medium asteroid produced when a large asteroid splits.
     */
    public static final int MEDIUM_SIZE = 2;

    /**
     * Size constant for a small asteroid produced when a medium asteroid splits.
     */
    public static final int SMALL_SIZE = 3;

    /**
     * Scale multiplier for medium asteroids (size 2).
     */
    public static final double MEDIUM_SCALE_FACTOR = 0.5;

    /**
     * Scale multiplier for small asteroids (size 3).
     */
    public static final double SMALL_SCALE_FACTOR = 0.2;

    /** The size category of this asteroid (1 = large, 2 = medium, 3 = small). */
    private final int size;

    /**
     * Creates a large (size 1) asteroid at the specified position with random orientation and velocity.
     *
     * @param scale the base scale factor for the asteroid polygon
     * @param x     the initial X coordinate
     * @param y     the initial Y coordinate
     */
    public Asteroid(double scale, double x, double y) {
        this(scale, x, y, LARGE_SIZE, null, false, null, null);
    }

    /**
     * Creates an asteroid with the specified parameters.
     *
     * @param scale     the base scale factor for a size-1 asteroid
     * @param x         the initial X coordinate
     * @param y         the initial Y coordinate
     * @param size      the size category ({@code 1} for large, {@code 2} for medium, {@code 3} for small)
     * @param velocity  the initial velocity vector, or {@code null} to generate a random velocity
     * @param boost     {@code true} to add an extra velocity boost in the asteroid's direction
     * @param direction the initial direction vector, or {@code null} to pick a random direction
     * @param deflect   an optional deflection angle in degrees applied to {@code direction}, or {@code null}
     */
    public Asteroid(
            double scale,
            double x,
            double y,
            int size,
            Vector2D velocity,
            boolean boost,
            Vector2D direction,
            Double deflect) {
        this(scale, x, y, size, velocity, boost, direction, deflect, new Random());
    }

    /**
     * Creates an asteroid with the specified parameters and a custom {@link Random} source.
     *
     * @param scale     the base scale factor for a size-1 asteroid
     * @param x         the initial X coordinate
     * @param y         the initial Y coordinate
     * @param size      the size category ({@code 1} for large, {@code 2} for medium, {@code 3} for small)
     * @param velocity  the initial velocity vector, or {@code null} to generate a random velocity
     * @param boost     {@code true} to add an extra velocity boost in the asteroid's direction
     * @param direction the initial direction vector, or {@code null} to pick a random direction
     * @param deflect   an optional deflection angle in degrees applied to {@code direction}, or {@code null}
     * @param random    the random number generator used for orientation and speed selection
     */
    public Asteroid(
            double scale,
            double x,
            double y,
            int size,
            Vector2D velocity,
            boolean boost,
            Vector2D direction,
            Double deflect,
            Random random) {
        super(ASTEROID_POINTS, computeEffectiveScale(scale, size));
        this.size = size;
        this.position = new Vector2D(x, y);
        translate(this.position);
        // Random rotation so they look different
        rotate(random.nextInt(360));

        this.velocity = new Vector2D(0.0, 0.0);
        if (direction == null) {
            this.direction = new Vector2D(0.0, 1.0);
            // Rotate the direction vector randomly
            this.direction.rotate(random.nextInt(360));
        } else {
            this.direction = new Vector2D(direction.getX(), direction.getY());
        }

        if (deflect != null) {
            this.direction.rotate(deflect);
        }

        if (velocity == null) {
            // Add in some velocity - either 1 or 2 times the velocity step
            int multiplier = random.nextInt(2) + 1;
            this.velocity = this.velocity.add(this.direction.multiply(VELOCITY_STEP * multiplier));
        } else {
            this.velocity = new Vector2D(velocity.getX(), velocity.getY());
        }

        if (boost) {
            // Add in some velocity - either 1 or 2 times the velocity step
            int multiplier = random.nextInt(2) + 1;
            this.velocity = this.velocity.add(this.direction.multiply(VELOCITY_STEP * multiplier));
        }
    }

    /**
     * Computes the effective polygon scale factor based on the asteroid size category.
     *
     * @param baseScale the base scale for a size-1 asteroid
     * @param size      the asteroid size category (1, 2, or 3)
     * @return the effective scale factor
     */
    private static double computeEffectiveScale(double baseScale, int size) {
        if (size == MEDIUM_SIZE) {
            return baseScale * MEDIUM_SCALE_FACTOR;
        } else if (size == SMALL_SIZE) {
            return baseScale * SMALL_SCALE_FACTOR;
        }
        return baseScale;
    }

    /**
     * Moves the asteroid according to its velocity and wraps its position around the screen limits.
     *
     * @param limitX the horizontal screen boundary
     * @param limitY the vertical screen boundary
     */
    public void move(double limitX, double limitY) {
        this.position = this.position.add(this.velocity);
        this.position.wrap(limitX, limitY);
        translate(this.position);
    }

    /**
     * Returns the size category of this asteroid ({@code 1}, {@code 2}, or {@code 3}).
     *
     * @return the asteroid size
     */
    public int getSize() {
        return this.size;
    }
}
