package com.inlt.example.asteroids;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link VectorObject} geometry operations including center calculation,
 * radius computation, scaling, translation, rotation, and headless rendering.
 */
public class VectorObjectTest {

    /** Tolerance for floating-point comparisons. */
    private static final double EPSILON = 1e-6;

    /**
     * Creates a new {@code VectorObjectTest}.
     */
    public VectorObjectTest() {
        // Default constructor
    }

    /**
     * Tests calculating the center and bounding radius of a square polygon with unit scale.
     */
    @Test
    public void testCenterAndRadius() {
        List<Vector2D> square = Arrays.asList(
                new Vector2D(0.0, 0.0),
                new Vector2D(4.0, 0.0),
                new Vector2D(4.0, 4.0),
                new Vector2D(0.0, 4.0)
        );
        VectorObject obj = new VectorObject(square);
        Vector2D center = obj.center();
        assertEquals(2.0, center.getX(), EPSILON);
        assertEquals(2.0, center.getY(), EPSILON);

        // Distance from (2, 2) to (4, 4) is sqrt(8) = 2 * sqrt(2)
        assertEquals(Math.sqrt(8.0), obj.radius(), EPSILON);
        assertEquals(1.0, obj.getScaleFactor(), EPSILON);
        assertEquals(4, obj.getPoints().size());
    }

    /**
     * Tests scaling a polygon relative to its center.
     */
    @Test
    public void testScalePreservesCenterAndScalesRadius() {
        List<Vector2D> points = Arrays.asList(
                new Vector2D(0.0, 2.0),
                new Vector2D(1.0, -1.0),
                new Vector2D(0.0, 0.0),
                new Vector2D(-1.0, -1.0)
        );
        VectorObject obj = new VectorObject(points, 10.0);
        assertEquals(10.0, obj.getScaleFactor(), EPSILON);

        Vector2D center = obj.center();
        assertEquals(0.0, center.getX(), EPSILON);
        assertEquals(0.0, center.getY(), EPSILON);
        assertEquals(20.0, obj.radius(), EPSILON);
    }

    /**
     * Tests translating a polygon to a new position and verifying its center and radius.
     */
    @Test
    public void testTranslateUpdatesCenterAndPosition() {
        List<Vector2D> points = Arrays.asList(
                new Vector2D(0.0, 2.0),
                new Vector2D(1.0, -1.0),
                new Vector2D(0.0, 0.0),
                new Vector2D(-1.0, -1.0)
        );
        VectorObject obj = new VectorObject(points, 5.0);
        double expectedRadius = obj.radius();

        Vector2D target = new Vector2D(300.0, 450.0);
        obj.translate(target);

        assertEquals(target.getX(), obj.center().getX(), EPSILON);
        assertEquals(target.getY(), obj.center().getY(), EPSILON);
        assertEquals(target.getX(), obj.getPosition().getX(), EPSILON);
        assertEquals(target.getY(), obj.getPosition().getY(), EPSILON);
        assertEquals(expectedRadius, obj.radius(), EPSILON);
    }

    /**
     * Tests rotating a polygon around its center without shifting its center or altering its radius.
     */
    @Test
    public void testRotateAroundCenter() {
        List<Vector2D> points = Arrays.asList(
                new Vector2D(0.0, 2.0),
                new Vector2D(1.0, -1.0),
                new Vector2D(0.0, 0.0),
                new Vector2D(-1.0, -1.0)
        );
        VectorObject obj = new VectorObject(points, 10.0);
        obj.translate(new Vector2D(100.0, 200.0));

        obj.rotate(90.0);
        assertEquals(100.0, obj.center().getX(), EPSILON);
        assertEquals(200.0, obj.center().getY(), EPSILON);
        assertEquals(20.0, obj.radius(), EPSILON);

        // Nose point (originally at (100, 220)) rotated +90 deg around (100, 200) -> (80, 200)
        Vector2D nose = obj.getPoints().get(0);
        assertEquals(80.0, nose.getX(), EPSILON);
        assertEquals(200.0, nose.getY(), EPSILON);
    }

    /**
     * Tests that an empty {@link VectorObject} handles operations safely without exceptions.
     */
    @Test
    public void testEmptyVectorObjectSafety() {
        VectorObject empty = new VectorObject(Collections.emptyList(), 2.0);
        assertEquals(0.0, empty.radius(), EPSILON);
        empty.translate(new Vector2D(50.0, 60.0));
        assertEquals(50.0, empty.center().getX(), EPSILON);
        assertEquals(60.0, empty.center().getY(), EPSILON);
        empty.rotate(45.0);
        empty.scale(3.0);

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        empty.draw(g2d, 100);
        g2d.dispose();
    }

    /**
     * Tests drawing a {@link VectorObject} onto an off-screen {@link BufferedImage} in headless mode.
     */
    @Test
    public void testDrawHeadless() {
        VectorObject obj = new VectorObject(Ship.SHIP_POINTS, 10.0);
        obj.translate(new Vector2D(50.0, 50.0));

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        obj.draw(g2d, 100);
        g2d.dispose();
    }
}
