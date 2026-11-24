package com.inlt.example.asteroids;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the game entity classes: {@link Ship}, {@link Shot}, {@link Asteroid}, and {@link Ufo}.
 */
public class GameObjectsTest {

    /** Tolerance for floating-point comparisons. */
    private static final double EPSILON = 1e-6;

    /**
     * Creates a new {@code GameObjectsTest}.
     */
    public GameObjectsTest() {
        // Default constructor
    }

    /**
     * Tests {@link Ship} initialization, thrust acceleration, movement, rotation, and wrapping.
     */
    @Test
    public void testShipMovementThrustAndRotation() {
        Ship ship = new Ship(10.0, 600.0, 450.0);
        assertEquals(600.0, ship.getPosition().getX(), EPSILON);
        assertEquals(450.0, ship.getPosition().getY(), EPSILON);
        assertEquals(0.0, ship.getVelocity().getX(), EPSILON);
        assertEquals(0.0, ship.getVelocity().getY(), EPSILON);
        assertEquals(0.0, ship.getDirection().getX(), EPSILON);
        assertEquals(1.0, ship.getDirection().getY(), EPSILON);
        assertFalse(ship.isThrusting());

        // Move without thrust: stays stationary
        ship.move(1200.0, 900.0);
        assertEquals(600.0, ship.getPosition().getX(), EPSILON);
        assertEquals(450.0, ship.getPosition().getY(), EPSILON);

        // Enable thrust and move: accelerates in (0, 1) direction by ACCELERATION (0.15)
        ship.thrust(true);
        assertTrue(ship.isThrusting());
        ship.move(1200.0, 900.0);
        assertEquals(0.0, ship.getVelocity().getX(), EPSILON);
        assertEquals(Ship.ACCELERATION, ship.getVelocity().getY(), EPSILON);
        assertEquals(450.0 + Ship.ACCELERATION, ship.getPosition().getY(), EPSILON);

        // Rotate 90 degrees counter-clockwise: direction becomes (-1, 0)
        ship.rotate(90.0);
        assertEquals(-1.0, ship.getDirection().getX(), EPSILON);
        assertEquals(0.0, ship.getDirection().getY(), EPSILON);

        // Verify both normal and thrusting ship polygons draw cleanly in headless mode
        BufferedImage image = new BufferedImage(1200, 900, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        ship.draw(g2d, 900);
        ship.thrust(false);
        ship.draw(g2d, 900);
        g2d.dispose();
    }

    /**
     * Tests {@link Shot} initialization, velocity calculation, range tracking, and screen wrapping.
     */
    @Test
    public void testShotMovementRangeAndWrapping() {
        Vector2D direction = new Vector2D(1.0, 0.0);
        Vector2D shipVelocity = new Vector2D(0.5, 0.0);
        Shot shot = new Shot(1199.0, 450.0, direction, shipVelocity, 10.0, true);

        assertTrue(shot.isPlayerShot());
        assertEquals(Shot.RADIUS, shot.radius(), EPSILON);
        assertEquals(10.0, shot.getMaxRange(), EPSILON);
        assertEquals(0.0, shot.getTraveled(), EPSILON);
        // Velocity should be 0.5 + 3.5 = 4.0 in X
        assertEquals(4.0, shot.getVelocity().getX(), EPSILON);
        assertEquals(0.0, shot.getVelocity().getY(), EPSILON);
        assertEquals(shot.getPosition(), shot.center());

        // Move once across the right boundary (1199 + 4 = 1203 -> wraps to 3.0)
        shot.move(1200.0, 900.0);
        assertEquals(3.0, shot.getPosition().getX(), EPSILON);
        assertEquals(450.0, shot.getPosition().getY(), EPSILON);
        // Distance traveled must be 4.0 (not 1196 from screen wrap!)
        assertEquals(4.0, shot.getTraveled(), EPSILON);
        assertFalse(shot.isMaxRangeReached());

        // Move two more times (total traveled = 12.0 > 10.0)
        shot.move(1200.0, 900.0);
        shot.move(1200.0, 900.0);
        assertEquals(12.0, shot.getTraveled(), EPSILON);
        assertTrue(shot.isMaxRangeReached());

        // Test rotation and headless drawing on Shot
        shot.rotate(90.0);
        assertEquals(0.0, shot.getDirection().getX(), EPSILON);
        assertEquals(1.0, shot.getDirection().getY(), EPSILON);

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        shot.draw(g2d, 100);
        g2d.dispose();
    }

    /**
     * Tests {@link Asteroid} scaling across sizes 1, 2, and 3, velocity boost, deflection, and movement.
     */
    @Test
    public void testAsteroidSizesDeflectionAndMovement() {
        Asteroid large = new Asteroid(15.0, 200.0, 300.0);
        Asteroid medium = new Asteroid(
                15.0, 200.0, 300.0, Asteroid.MEDIUM_SIZE,
                new Vector2D(1.0, 0.0), false, new Vector2D(1.0, 0.0), null
        );
        Asteroid small = new Asteroid(
                15.0, 200.0, 300.0, Asteroid.SMALL_SIZE,
                new Vector2D(1.0, 0.0), true, new Vector2D(1.0, 0.0), 90.0, new Random(42L)
        );

        assertEquals(Asteroid.LARGE_SIZE, large.getSize());
        assertEquals(Asteroid.MEDIUM_SIZE, medium.getSize());
        assertEquals(Asteroid.SMALL_SIZE, small.getSize());

        // Medium radius is half of large; small radius is 0.2 of large
        assertEquals(large.radius() * 0.5, medium.radius(), EPSILON);
        assertEquals(large.radius() * 0.2, small.radius(), EPSILON);

        // Small asteroid had initial direction (1, 0) deflected by +90 deg -> (0, 1)
        assertEquals(0.0, small.getDirection().getX(), EPSILON);
        assertEquals(1.0, small.getDirection().getY(), EPSILON);
        // And boost added positive Y velocity on top of (1.0, 0.0)
        assertEquals(1.0, small.getVelocity().getX(), EPSILON);
        assertTrue(small.getVelocity().getY() >= Asteroid.VELOCITY_STEP);

        // Move medium asteroid and verify position update
        medium.move(1200.0, 900.0);
        assertEquals(201.0, medium.getPosition().getX(), EPSILON);
        assertEquals(300.0, medium.getPosition().getY(), EPSILON);
    }

    /**
     * Tests {@link Ufo} direction constants, symmetric East/West speed calculation,
     * rotation of both direction and velocity, boundary detection, and shooting.
     */
    @Test
    public void testUfoVelocityRotationMovementAndShooting() {
        // Verify fixed EAST and WEST direction constants
        assertEquals(1.0, Ufo.EAST.getX(), EPSILON);
        assertEquals(0.0, Ufo.EAST.getY(), EPSILON);
        assertEquals(-1.0, Ufo.WEST.getX(), EPSILON);
        assertEquals(0.0, Ufo.WEST.getY(), EPSILON);

        // Verify Eastbound and Westbound UFOs of the same size have identical speeds
        Ufo eastLarge = new Ufo(10.0, 0.0, 300.0, Ufo.LARGE_SIZE, Ufo.EAST);
        Ufo westLarge = new Ufo(10.0, 1200.0, 300.0, Ufo.LARGE_SIZE, Ufo.WEST);
        assertEquals(1.15, eastLarge.getVelocity().getX(), EPSILON);
        assertEquals(-1.15, westLarge.getVelocity().getX(), EPSILON);

        Ufo eastSmall = new Ufo(10.0, 0.0, 300.0, Ufo.SMALL_SIZE, Ufo.EAST);
        Ufo westSmall = new Ufo(10.0, 1200.0, 300.0, Ufo.SMALL_SIZE, Ufo.WEST);
        assertEquals(2.15, eastSmall.getVelocity().getX(), EPSILON);
        assertEquals(-2.15, westSmall.getVelocity().getX(), EPSILON);
        assertEquals(eastLarge.radius() * 0.5, eastSmall.radius(), EPSILON);

        // Verify Ufo.rotate rotates both direction and velocity
        eastLarge.rotate(90.0);
        assertEquals(0.0, eastLarge.getDirection().getX(), EPSILON);
        assertEquals(1.0, eastLarge.getDirection().getY(), EPSILON);
        assertEquals(0.0, eastLarge.getVelocity().getX(), EPSILON);
        assertEquals(1.15, eastLarge.getVelocity().getY(), EPSILON);

        // Verify shooting threshold
        Ufo shooter = new Ufo(10.0, 600.0, 450.0, Ufo.LARGE_SIZE, Ufo.EAST, new Random(1L));
        assertNull(shooter.shoot(1200.0, 900.0));
        shooter.setSinceShot(1200.0 * Ufo.SHOT_FREQUENCY + 1.0);
        Shot ufoShot = shooter.shoot(1200.0, 900.0);
        assertNotNull(ufoShot);
        assertFalse(ufoShot.isPlayerShot());
        assertEquals(0.0, shooter.getSinceShot(), EPSILON);

        // Verify boundary exit detection when UFO moves completely off-screen
        Ufo exitingUfo = new Ufo(10.0, 1200.0 + eastLarge.radius() + 5.0, 450.0, Ufo.LARGE_SIZE, Ufo.EAST);
        assertFalse(exitingUfo.move(1200.0, 900.0));
    }

    /**
     * Tests {@link Ufo} course-change turning logic when traveling straight and at 45-degree angles.
     */
    @Test
    public void testUfoTurningLogic() {
        Ufo ufo = new Ufo(10.0, 200.0, 450.0, Ufo.LARGE_SIZE, Ufo.EAST, new Random(12345L));
        ufo.setSinceTurn(300.0);

        // Step until the random 1-in-4 turn condition triggers
        boolean turnedToDiagonal = false;
        for (int i = 0; i < 50 && !turnedToDiagonal; i++) {
            ufo.setSinceTurn(300.0);
            ufo.move(1200.0, 900.0);
            if (Math.abs(ufo.getDirection().getY()) > 0.5) {
                turnedToDiagonal = true;
            }
        }
        assertTrue(turnedToDiagonal);
        double sqrt2Over2 = Math.sqrt(2.0) / 2.0;
        assertEquals(sqrt2Over2, ufo.getDirection().getX(), EPSILON);
        assertEquals(sqrt2Over2, Math.abs(ufo.getDirection().getY()), EPSILON);

        // Step again until it turns from the 45-degree diagonal back to straight horizontal East (1, 0)
        boolean turnedBackHorizontal = false;
        for (int i = 0; i < 50 && !turnedBackHorizontal; i++) {
            ufo.setSinceTurn(300.0);
            ufo.move(1200.0, 900.0);
            if (Math.abs(ufo.getDirection().getY()) < 0.1) {
                turnedBackHorizontal = true;
            }
        }
        assertTrue(turnedBackHorizontal);
        assertEquals(1.0, ufo.getDirection().getX(), EPSILON);
        assertEquals(0.0, ufo.getDirection().getY(), EPSILON);
    }
}
