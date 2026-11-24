package com.inlt.example.asteroids;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for the game objects. A {@code VectorObject} is defined by a list of
 * {@link Vector2D} points and maintains its position, velocity, and direction vectors.
 */
public class VectorObject {

    /** The polygon vertices defining the shape of this object in world coordinates. */
    protected final List<Vector2D> points;

    /** The current scale factor applied to the reference points. */
    protected double scaleFactor;

    /** The current position of this object in world coordinates. */
    protected Vector2D position;

    /** The current velocity vector of this object. */
    protected Vector2D velocity;

    /** The current unit direction vector of this object. */
    protected Vector2D direction;

    /**
     * Creates a {@code VectorObject} with the specified polygon vertices and unit scale {@code 1.0}.
     *
     * @param points the list of polygon vertices defining this object
     */
    public VectorObject(List<Vector2D> points) {
        this(points, 1.0);
    }

    /**
     * Creates a {@code VectorObject} with the specified polygon vertices and scale factor.
     *
     * @param points      the list of polygon vertices defining this object
     * @param scaleFactor the scale factor to apply to the reference points
     */
    public VectorObject(List<Vector2D> points, double scaleFactor) {
        this.points = new ArrayList<>();
        if (points != null) {
            for (Vector2D point : points) {
                this.points.add(new Vector2D(point));
            }
        }
        this.position = new Vector2D(0.0, 0.0);
        this.velocity = new Vector2D(0.0, 0.0);
        this.direction = new Vector2D(0.0, 1.0);
        scale(scaleFactor);
        if (!this.points.isEmpty()) {
            this.position = center();
        }
    }

    /**
     * Rotate the object around its center by the specified angle (in degrees).
     *
     * @param angle the rotation angle in degrees (counter-clockwise)
     */
    public void rotate(double angle) {
        if (this.points.isEmpty()) {
            return;
        }
        Vector2D centerPoint = center();
        for (Vector2D point : this.points) {
            point.setX(point.getX() - centerPoint.getX());
            point.setY(point.getY() - centerPoint.getY());
            point.rotate(angle);
            point.setX(point.getX() + centerPoint.getX());
            point.setY(point.getY() + centerPoint.getY());
        }
    }

    /**
     * Warp the object to a new position.
     *
     * @param newPosition the target center position for the object
     */
    public void translate(Vector2D newPosition) {
        this.position = new Vector2D(newPosition);
        if (this.points.isEmpty()) {
            return;
        }
        Vector2D centerPoint = center();
        for (int i = 0; i < this.points.size(); i++) {
            Vector2D point = this.points.get(i);
            point.setX(point.getX() - centerPoint.getX());
            point.setY(point.getY() - centerPoint.getY());
            this.points.set(i, point.add(newPosition));
        }
    }

    /**
     * Find the center of the polygon by averaging all vertex coordinates.
     *
     * @return a {@link Vector2D} representing the centroid of the vertices
     */
    public Vector2D center() {
        if (this.points.isEmpty()) {
            return new Vector2D(this.position);
        }
        double totalX = 0.0;
        double totalY = 0.0;
        for (Vector2D point : this.points) {
            totalX += point.getX();
            totalY += point.getY();
        }
        int lenPoints = this.points.size();
        return new Vector2D(totalX / lenPoints, totalY / lenPoints);
    }

    /**
     * Calculates the bounding radius of the object as the maximum distance from
     * its center to any of its polygon vertices.
     *
     * @return the bounding radius of this object
     */
    public double radius() {
        if (this.points.isEmpty()) {
            return 0.0;
        }
        Vector2D centerPoint = center();
        double maxDist = 0.0;
        for (Vector2D point : this.points) {
            double thisDist = centerPoint.distanceTo(point);
            if (thisDist > maxDist) {
                maxDist = thisDist;
            }
        }
        return maxDist;
    }

    /**
     * Scale the reference points relative to the polygon's center by the specified scale factor.
     *
     * @param scaleFactor the scale multiplier to apply
     */
    public void scale(double scaleFactor) {
        this.scaleFactor = scaleFactor;
        if (this.points.isEmpty()) {
            return;
        }
        Vector2D centerPoint = center();
        for (Vector2D point : this.points) {
            double offX = point.getX() - centerPoint.getX();
            double offY = point.getY() - centerPoint.getY();
            offX *= this.scaleFactor;
            offY *= this.scaleFactor;
            point.setX(centerPoint.getX() + offX);
            point.setY(centerPoint.getY() + offY);
        }
    }

    /**
     * Draws this polygon onto the given {@link Graphics2D} context, converting
     * Cartesian Y coordinates (origin at bottom-left) into screen Y coordinates
     * (origin at top-left).
     *
     * @param graphics     the graphics context on which to draw
     * @param screenHeight the height of the screen in pixels for Y-axis inversion
     */
    public void draw(Graphics2D graphics, int screenHeight) {
        if (this.points.isEmpty()) {
            return;
        }
        Path2D.Double path = new Path2D.Double();
        Vector2D first = this.points.get(0);
        path.moveTo(first.getX(), screenHeight - first.getY());
        for (int i = 1; i < this.points.size(); i++) {
            Vector2D pt = this.points.get(i);
            path.lineTo(pt.getX(), screenHeight - pt.getY());
        }
        path.closePath();
        graphics.draw(path);
    }

    /**
     * Returns a copy of this object's current position vector.
     *
     * @return a new {@link Vector2D} with the current position
     */
    public Vector2D getPosition() {
        return new Vector2D(this.position);
    }

    /**
     * Returns a copy of this object's current velocity vector.
     *
     * @return a new {@link Vector2D} with the current velocity
     */
    public Vector2D getVelocity() {
        return new Vector2D(this.velocity);
    }

    /**
     * Returns a copy of this object's current direction vector.
     *
     * @return a new {@link Vector2D} with the current direction
     */
    public Vector2D getDirection() {
        return new Vector2D(this.direction);
    }

    /**
     * Returns an unmodifiable view of the polygon vertices of this object.
     *
     * @return the list of vertices
     */
    public List<Vector2D> getPoints() {
        return Collections.unmodifiableList(this.points);
    }

    /**
     * Returns the current scale factor of this object.
     *
     * @return the scale factor
     */
    public double getScaleFactor() {
        return this.scaleFactor;
    }
}
