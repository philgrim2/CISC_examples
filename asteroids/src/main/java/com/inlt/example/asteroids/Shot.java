package com.inlt.example.asteroids;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Collections;

/**
 * Class to represent a mass-driver shot fired by the player ship or an alien UFO.
 */
public class Shot extends VectorObject {

    /**
     * Radius of a shot projectile in pixels.
     */
    public static final double RADIUS = 3.0;

    /**
     * Muzzle speed added to the firing craft's velocity in the direction of fire.
     */
    public static final double SHOT_SPEED = 3.5;

    /** The cumulative distance traveled by this shot. */
    private double traveled;

    /** The maximum distance this shot can travel before expiring. */
    private final double maxRange;

    /** Indicates whether the player ship was the source of this shot. */
    private final boolean player;

    /**
     * Creates a player-fired shot moving in the specified direction from the given position.
     *
     * <p>Shots are already moving in the direction the ship is facing,
     * so we take a direction and initial velocity to start with.
     *
     * @param x         the initial X coordinate
     * @param y         the initial Y coordinate
     * @param direction the unit direction vector of the shot
     * @param velocity  the initial velocity of the firing craft
     * @param maxRange  the maximum distance the shot can travel before expiring
     */
    public Shot(double x, double y, Vector2D direction, Vector2D velocity, double maxRange) {
        this(x, y, direction, velocity, maxRange, true);
    }

    /**
     * Creates a shot moving in the specified direction from the given position.
     *
     * <p>Shots are already moving in the direction the ship is facing,
     * so we take a direction and initial velocity to start with.
     *
     * @param x         the initial X coordinate
     * @param y         the initial Y coordinate
     * @param direction the unit direction vector of the shot
     * @param velocity  the initial velocity of the firing craft
     * @param maxRange  the maximum distance the shot can travel before expiring
     * @param player    {@code true} if fired by the player ship, {@code false} if fired by a UFO
     */
    public Shot(double x, double y, Vector2D direction, Vector2D velocity, double maxRange, boolean player) {
        super(Collections.emptyList(), 1.0);
        this.position = new Vector2D(x, y);
        this.traveled = 0.0;
        this.direction = new Vector2D(direction.getX(), direction.getY());
        this.velocity = velocity.add(this.direction.multiply(SHOT_SPEED));
        this.maxRange = maxRange;
        this.player = player;
    }

    /**
     * Override the vector object center function - our center is just {@code (x, y)}.
     *
     * @return a {@link Vector2D} representing the shot's current center position
     */
    @Override
    public Vector2D center() {
        return new Vector2D(this.position.getX(), this.position.getY());
    }

    /**
     * Moves the shot by its velocity vector, accumulates the distance traveled,
     * and wraps its position around the screen boundaries.
     *
     * @param limitX the horizontal screen boundary
     * @param limitY the vertical screen boundary
     */
    public void move(double limitX, double limitY) {
        Vector2D previousPosition = new Vector2D(this.position.getX(), this.position.getY());
        this.position = this.position.add(this.velocity);
        this.traveled += this.position.distanceTo(previousPosition);
        this.position.wrap(limitX, limitY);
    }

    /**
     * Rotates the shot's direction and velocity vectors by the specified angle.
     *
     * @param angle the rotation angle in degrees
     */
    @Override
    public void rotate(double angle) {
        this.direction.rotate(angle);
        this.velocity.rotate(angle);
    }

    /**
     * Returns whether this shot has exceeded its maximum travel range.
     *
     * @return {@code true} if traveled distance is strictly greater than {@code maxRange}
     */
    public boolean isMaxRangeReached() {
        return this.traveled > this.maxRange;
    }

    /**
     * Draws the shot as a filled circle onto the given graphics context.
     *
     * @param graphics     the graphics context on which to draw
     * @param screenHeight the screen height in pixels for Y-coordinate conversion
     */
    @Override
    public void draw(Graphics2D graphics, int screenHeight) {
        double screenX = this.position.getX() - RADIUS;
        double screenY = (screenHeight - this.position.getY()) - RADIUS;
        Ellipse2D.Double circle = new Ellipse2D.Double(screenX, screenY, RADIUS * 2.0, RADIUS * 2.0);
        graphics.fill(circle);
    }

    /**
     * Returns the collision radius of the shot.
     *
     * @return the shot radius {@link #RADIUS}
     */
    @Override
    public double radius() {
        return RADIUS;
    }

    /**
     * Returns {@code true} if the player ship was the source of the shot, {@code false} otherwise.
     *
     * @return {@code true} if fired by the player, {@code false} if fired by a UFO
     */
    public boolean isPlayerShot() {
        return this.player;
    }

    /**
     * Returns the total distance traveled by this shot so far.
     *
     * @return the cumulative distance traveled
     */
    public double getTraveled() {
        return this.traveled;
    }

    /**
     * Returns the maximum range configured for this shot.
     *
     * @return the maximum travel range
     */
    public double getMaxRange() {
        return this.maxRange;
    }
}
