package com.inlt.example.asteroids;

import java.util.Objects;

/**
 * Implements a two-dimensional mathematical vector for representing positions,
 * velocities, directions, and polygon vertices in the Asteroids game.
 *
 * <p>Vector rotation math is adapted from:
 * https://stackoverflow.com/questions/28458145/rotate-2d-polygon-without-changing-its-position
 */
public class Vector2D {

    /** The X coordinate of this vector. */
    private double x;

    /** The Y coordinate of this vector. */
    private double y;

    /**
     * Creates a new {@code Vector2D} initialized to the origin {@code (0.0, 0.0)}.
     */
    public Vector2D() {
        this(0.0, 0.0);
    }

    /**
     * Creates a new {@code Vector2D} with the specified X and Y coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new {@code Vector2D} as a copy of an existing vector.
     *
     * @param other the vector to copy
     */
    public Vector2D(Vector2D other) {
        this(other.x, other.y);
    }

    /**
     * Rotate the vector by a specified angle in degrees.
     *
     * @param angle the angle in degrees by which to rotate the vector counter-clockwise
     */
    public void rotate(double angle) {
        double radians = Math.toRadians(angle);
        double sin = Math.sin(radians);
        double cos = Math.cos(radians);
        double currentX = this.x;
        double currentY = this.y;
        this.x = currentX * cos - currentY * sin;
        this.y = currentX * sin + currentY * cos;
    }

    /**
     * Wraps the vector's coordinates around the specified screen boundaries
     * so that coordinates stay within {@code [0, limitX)} and {@code [0, limitY)}.
     *
     * @param limitX the horizontal boundary limit
     * @param limitY the vertical boundary limit
     */
    public void wrap(double limitX, double limitY) {
        if (limitX > 0.0) {
            this.x = ((this.x % limitX) + limitX) % limitX;
        }
        if (limitY > 0.0) {
            this.y = ((this.y % limitY) + limitY) % limitY;
        }
    }

    /**
     * Calculate distance to another {@code Vector2D}.
     *
     * @param other the target {@code Vector2D}
     * @return the Euclidean distance between this vector and {@code other}
     */
    public double distanceTo(Vector2D other) {
        return distanceTo(other.x, other.y);
    }

    /**
     * Calculate distance to an {@code (x, y)} coordinate.
     *
     * @param otherX the X coordinate of the target point
     * @param otherY the Y coordinate of the target point
     * @return the Euclidean distance between this vector and {@code (otherX, otherY)}
     */
    public double distanceTo(double otherX, double otherY) {
        double dx = this.x - otherX;
        double dy = this.y - otherY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Adds this vector to the other vector and returns the resulting vector.
     *
     * @param other the vector to add
     * @return a new {@code Vector2D} representing the component-wise sum
     */
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    /**
     * Adds a scalar value to both components of this vector and returns the resulting vector.
     *
     * @param scalar the scalar value to add to both X and Y
     * @return a new {@code Vector2D} with {@code scalar} added to each component
     */
    public Vector2D add(double scalar) {
        return new Vector2D(this.x + scalar, this.y + scalar);
    }

    /**
     * Subtracts another vector from this vector and returns the resulting vector.
     *
     * @param other the vector to subtract
     * @return a new {@code Vector2D} representing the component-wise difference
     */
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    /**
     * Multiplies this vector by another vector component-wise and returns the resulting vector.
     *
     * @param other the vector by which to multiply
     * @return a new {@code Vector2D} representing the component-wise product
     */
    public Vector2D multiply(Vector2D other) {
        return new Vector2D(this.x * other.x, this.y * other.y);
    }

    /**
     * Multiplies this vector by a scalar factor and returns the resulting vector.
     *
     * @param scalar the scalar multiplier
     * @return a new {@code Vector2D} scaled by {@code scalar}
     */
    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    /**
     * Returns the X coordinate of this vector.
     *
     * @return the X coordinate
     */
    public double getX() {
        return this.x;
    }

    /**
     * Sets the X coordinate of this vector.
     *
     * @param x the new X coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the Y coordinate of this vector.
     *
     * @return the Y coordinate
     */
    public double getY() {
        return this.y;
    }

    /**
     * Sets the Y coordinate of this vector.
     *
     * @param y the new Y coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns a string representation of this vector in the form {@code (x,y)}.
     *
     * @return the string representation of this vector
     */
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    /**
     * Indicates whether another object is equal to this {@code Vector2D}.
     *
     * @param obj the object to compare with
     * @return {@code true} if {@code obj} is a {@code Vector2D} with identical coordinates
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vector2D)) {
            return false;
        }
        Vector2D other = (Vector2D) obj;
        return Double.compare(this.x, other.x) == 0
                && Double.compare(this.y, other.y) == 0;
    }

    /**
     * Returns a hash code value for this vector.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
