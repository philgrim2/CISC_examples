package com.inlt.example.asteroids;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Vector2D} class covering vector arithmetic,
 * rotation, Euclidean distance, coordinate wrapping, and value semantics.
 */
public class Vector2DTest {

    /** Tolerance for floating-point comparisons. */
    private static final double EPSILON = 1e-6;

    /**
     * Creates a new {@code Vector2DTest}.
     */
    public Vector2DTest() {
        // Default constructor
    }

    /**
     * Tests the default, parameterized, and copy constructors of {@link Vector2D}.
     */
    @Test
    public void testConstructorsAndAccessors() {
        Vector2D zero = new Vector2D();
        assertEquals(0.0, zero.getX(), EPSILON);
        assertEquals(0.0, zero.getY(), EPSILON);

        Vector2D vec = new Vector2D(3.5, -7.25);
        assertEquals(3.5, vec.getX(), EPSILON);
        assertEquals(-7.25, vec.getY(), EPSILON);

        Vector2D copy = new Vector2D(vec);
        assertNotSame(vec, copy);
        assertEquals(vec.getX(), copy.getX(), EPSILON);
        assertEquals(vec.getY(), copy.getY(), EPSILON);

        copy.setX(10.0);
        copy.setY(20.0);
        assertEquals(10.0, copy.getX(), EPSILON);
        assertEquals(20.0, copy.getY(), EPSILON);
        assertEquals(3.5, vec.getX(), EPSILON);
    }

    /**
     * Tests counter-clockwise and clockwise rotation of vectors by cardinal and diagonal angles.
     */
    @Test
    public void testRotateCardinalAndDiagonalAngles() {
        Vector2D v = new Vector2D(0.0, 1.0);

        // Rotate 90 degrees counter-clockwise: (0, 1) -> (-1, 0)
        v.rotate(90.0);
        assertEquals(-1.0, v.getX(), EPSILON);
        assertEquals(0.0, v.getY(), EPSILON);

        // Rotate another 90 degrees counter-clockwise: (-1, 0) -> (0, -1)
        v.rotate(90.0);
        assertEquals(0.0, v.getX(), EPSILON);
        assertEquals(-1.0, v.getY(), EPSILON);

        // Rotate 180 degrees: (0, -1) -> (0, 1)
        v.rotate(180.0);
        assertEquals(0.0, v.getX(), EPSILON);
        assertEquals(1.0, v.getY(), EPSILON);

        // Rotate 90 degrees clockwise (-90): (0, 1) -> (1, 0)
        v.rotate(-90.0);
        assertEquals(1.0, v.getX(), EPSILON);
        assertEquals(0.0, v.getY(), EPSILON);

        // Rotate 45 degrees counter-clockwise: (1, 0) -> (cos 45, sin 45)
        v.rotate(45.0);
        double sqrt2Over2 = Math.sqrt(2.0) / 2.0;
        assertEquals(sqrt2Over2, v.getX(), EPSILON);
        assertEquals(sqrt2Over2, v.getY(), EPSILON);
    }

    /**
     * Tests that rotating a vector by 360 degrees or 0 degrees returns it to its original coordinates
     * and preserves its magnitude across arbitrary rotations.
     */
    @Test
    public void testRotatePreservesMagnitude() {
        Vector2D origin = new Vector2D(0.0, 0.0);
        Vector2D v = new Vector2D(3.0, 4.0);
        double initialLength = origin.distanceTo(v);
        assertEquals(5.0, initialLength, EPSILON);

        v.rotate(37.5);
        assertEquals(initialLength, origin.distanceTo(v), EPSILON);

        v.rotate(322.5);
        assertEquals(3.0, v.getX(), EPSILON);
        assertEquals(4.0, v.getY(), EPSILON);
    }

    /**
     * Tests coordinate wrapping for positive overflow, negative underflow, and exact boundaries.
     */
    @Test
    public void testWrapPositiveAndNegativeCoordinates() {
        // Within bounds remains unchanged
        Vector2D inside = new Vector2D(500.0, 400.0);
        inside.wrap(1200.0, 900.0);
        assertEquals(500.0, inside.getX(), EPSILON);
        assertEquals(400.0, inside.getY(), EPSILON);

        // Positive overflow wraps around to near 0
        Vector2D overflow = new Vector2D(1205.5, 910.25);
        overflow.wrap(1200.0, 900.0);
        assertEquals(5.5, overflow.getX(), EPSILON);
        assertEquals(10.25, overflow.getY(), EPSILON);

        // Negative coordinates wrap around to the upper boundary (fixes Java negative % behavior)
        Vector2D negative = new Vector2D(-5.0, -15.0);
        negative.wrap(1200.0, 900.0);
        assertEquals(1195.0, negative.getX(), EPSILON);
        assertEquals(885.0, negative.getY(), EPSILON);

        // Exact limit wraps to 0.0
        Vector2D onBoundary = new Vector2D(1200.0, 900.0);
        onBoundary.wrap(1200.0, 900.0);
        assertEquals(0.0, onBoundary.getX(), EPSILON);
        assertEquals(0.0, onBoundary.getY(), EPSILON);
    }

    /**
     * Tests Euclidean distance calculations to another {@link Vector2D} and to raw {@code (x, y)} coordinates.
     */
    @Test
    public void testDistanceTo() {
        Vector2D a = new Vector2D(1.0, 2.0);
        Vector2D b = new Vector2D(4.0, 6.0);

        assertEquals(5.0, a.distanceTo(b), EPSILON);
        assertEquals(5.0, b.distanceTo(a), EPSILON);
        assertEquals(5.0, a.distanceTo(4.0, 6.0), EPSILON);
        assertEquals(0.0, a.distanceTo(a), EPSILON);
        assertEquals(0.0, a.distanceTo(1.0, 2.0), EPSILON);
    }

    /**
     * Tests vector addition, scalar addition, and vector subtraction without mutating operands.
     */
    @Test
    public void testAddAndSubtract() {
        Vector2D a = new Vector2D(2.0, 3.0);
        Vector2D b = new Vector2D(-1.5, 4.5);

        Vector2D sumVec = a.add(b);
        assertEquals(0.5, sumVec.getX(), EPSILON);
        assertEquals(7.5, sumVec.getY(), EPSILON);
        // Original vectors must be unchanged
        assertEquals(2.0, a.getX(), EPSILON);
        assertEquals(3.0, a.getY(), EPSILON);

        Vector2D sumScalar = a.add(5.0);
        assertEquals(7.0, sumScalar.getX(), EPSILON);
        assertEquals(8.0, sumScalar.getY(), EPSILON);

        Vector2D diff = a.subtract(b);
        assertEquals(3.5, diff.getX(), EPSILON);
        assertEquals(-1.5, diff.getY(), EPSILON);
    }

    /**
     * Tests component-wise vector multiplication and scalar multiplication.
     */
    @Test
    public void testMultiply() {
        Vector2D a = new Vector2D(3.0, -4.0);
        Vector2D b = new Vector2D(2.0, 0.5);

        Vector2D prodVec = a.multiply(b);
        assertEquals(6.0, prodVec.getX(), EPSILON);
        assertEquals(-2.0, prodVec.getY(), EPSILON);

        Vector2D prodScalar = a.multiply(2.5);
        assertEquals(7.5, prodScalar.getX(), EPSILON);
        assertEquals(-10.0, prodScalar.getY(), EPSILON);
    }

    /**
     * Tests {@link Vector2D#equals(Object)}, {@link Vector2D#hashCode()}, and {@link Vector2D#toString()}.
     */
    @Test
    public void testEqualsHashCodeAndToString() {
        Vector2D v1 = new Vector2D(1.5, 2.5);
        Vector2D v2 = new Vector2D(1.5, 2.5);
        Vector2D v3 = new Vector2D(1.5, -2.5);

        assertEquals(v1, v1);
        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
        assertNotEquals(v1, v3);
        assertNotEquals(v1, null);
        assertNotEquals(v1, "(1.5,2.5)");
        assertEquals("(1.5,2.5)", v1.toString());
    }
}
