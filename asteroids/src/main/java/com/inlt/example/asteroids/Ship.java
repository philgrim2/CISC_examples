package com.inlt.example.asteroids;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class to represent the player ship.
 */
public class Ship extends VectorObject {

    /**
     * Define the canonical ship polygon, which will get scaled based on the screen size.
     */
    public static final List<Vector2D> SHIP_POINTS = Collections.unmodifiableList(Arrays.asList(
            new Vector2D(0.0, 2.0),
            new Vector2D(1.0, -1.0),
            new Vector2D(0.0, 0.0),
            new Vector2D(-1.0, -1.0)
    ));

    /**
     * Define a ship polygon with a thrust flame.
     */
    public static final List<Vector2D> THRUST_POINTS = Collections.unmodifiableList(Arrays.asList(
            new Vector2D(0.0, 2.0),
            new Vector2D(1.0, -1.0),
            new Vector2D(0.0, 0.0),
            new Vector2D(-0.4, -0.4),
            new Vector2D(0.0, -0.7),
            new Vector2D(0.4, -0.4),
            new Vector2D(0.0, 0.0),
            new Vector2D(-1.0, -1.0)
    ));

    /**
     * Ship acceleration per frame.
     */
    public static final double ACCELERATION = 0.15;

    /**
     * Ship rotational speed in degrees per frame.
     */
    public static final double SHIP_ROTATE_DEGREES = 3.0;

    /** Indicates whether the ship's thrusters are currently firing. */
    private boolean thrust;

    /** The polygon object representing the ship with an active thruster flame. */
    private final VectorObject thrusting;

    /**
     * Creates an instance of the ship at the specified scale factor and position.
     *
     * @param scale the scale factor for the ship polygon
     * @param x     the initial X coordinate
     * @param y     the initial Y coordinate
     */
    public Ship(double scale, double x, double y) {
        super(SHIP_POINTS, scale);
        this.position = new Vector2D(x, y);
        this.velocity = new Vector2D(0.0, 0.0);
        this.direction = new Vector2D(0.0, 1.0);
        this.thrust = false;
        this.thrusting = new VectorObject(THRUST_POINTS, scale);
        translate(this.position);
    }

    /**
     * Draws the ship (either normal or with thruster flame) onto the given graphics context.
     *
     * @param graphics     the graphics context on which to draw
     * @param screenHeight the screen height in pixels for Y-coordinate conversion
     */
    @Override
    public void draw(Graphics2D graphics, int screenHeight) {
        if (this.thrust) {
            this.thrusting.draw(graphics, screenHeight);
        } else {
            super.draw(graphics, screenHeight);
        }
    }

    /**
     * Rotates the ship, its thruster polygon, and its heading direction vector by the
     * specified angle in degrees.
     *
     * @param angle the rotation angle in degrees (counter-clockwise)
     */
    @Override
    public void rotate(double angle) {
        super.rotate(angle);
        this.thrusting.rotate(angle);
        this.direction.rotate(angle);
    }

    /**
     * Warps both the ship polygon and the thruster polygon to a new position.
     *
     * @param newPosition the target center position for the ship
     */
    @Override
    public void translate(Vector2D newPosition) {
        super.translate(newPosition);
        if (this.thrusting != null) {
            this.thrusting.translate(newPosition);
        }
    }

    /**
     * Enables or disables the ship's thrusters.
     *
     * @param thrusting {@code true} if the thrusters are firing, {@code false} otherwise
     */
    public void thrust(boolean thrusting) {
        this.thrust = thrusting;
    }

    /**
     * Returns whether the ship's thrusters are currently firing.
     *
     * @return {@code true} if thrusting, {@code false} otherwise
     */
    public boolean isThrusting() {
        return this.thrust;
    }

    /**
     * Updates the ship's velocity (if thrusting) and moves the ship within the screen bounds.
     *
     * @param limitX the horizontal screen boundary
     * @param limitY the vertical screen boundary
     */
    public void move(double limitX, double limitY) {
        if (this.thrust) {
            this.velocity = this.velocity.add(this.direction.multiply(ACCELERATION));
        }
        this.position = this.position.add(this.velocity);
        this.position.wrap(limitX, limitY);
        translate(this.position);
    }

    /**
     * Returns the {@link VectorObject} used to render the ship while thrusting.
     *
     * @return the thrusting polygon object
     */
    public VectorObject getThrustingObject() {
        return this.thrusting;
    }
}
