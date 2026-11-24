package com.inlt.example.asteroids;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for {@link Asteroids} collision detection, spawning algorithms,
 * scoring, extra-life awarding, player input handling, and headless rendering.
 */
public class AsteroidsCollisionAndGameTest {

    /** Tolerance for floating-point comparisons. */
    private static final double EPSILON = 1e-6;

    /**
     * Creates a new {@code AsteroidsCollisionAndGameTest}.
     */
    public AsteroidsCollisionAndGameTest() {
        // Default constructor
    }

    /**
     * Tests initial game state, instance isolation, and life management up to {@link Asteroids#MAX_LIVES}.
     */
    @Test
    public void testInitialStateAndMaxLivesCap() {
        Asteroids game1 = new Asteroids(new Random(1L));
        Asteroids game2 = new Asteroids(new Random(2L));

        assertEquals(Asteroids.STARTING_LIVES, game1.getLives().size());
        assertEquals(0, game1.getScore());
        assertNull(game1.getShip());
        assertNull(game1.getUfo());
        assertTrue(game1.getAsteroids().isEmpty());
        assertTrue(game1.getShots().isEmpty());
        assertEquals(Asteroids.EXTRA_LIFE_INTERVAL, game1.getNextNewLife());
        assertEquals(0, game1.getSpawnCountdown());
        assertEquals(Asteroids.UFO_INTERVAL, game1.getUfoCountdown());

        // Verify mutable lists are not shared across Asteroids instances
        game1.addLife();
        assertEquals(4, game1.getLives().size());
        assertEquals(3, game2.getLives().size());

        // Add lives beyond MAX_LIVES and verify cap at MAX_LIVES (10)
        for (int i = 0; i < 20; i++) {
            game1.addLife();
        }
        assertEquals(Asteroids.MAX_LIVES, game1.getLives().size());
    }

    /**
     * Tests collision of a player shot with large (size 1), medium (size 2), and small (size 3) asteroids.
     */
    @Test
    public void testPlayerShotCollidingWithAsteroids() {
        Asteroids game = new Asteroids(new Random(42L));

        // 1. Player shot hits a large (size 1) asteroid at (300, 300)
        Asteroid largeAsteroid = new Asteroid(Asteroids.ASTEROID_SCALE, 300.0, 300.0);
        game.getAsteroids().add(largeAsteroid);

        Shot shot1 = new Shot(300.0, 300.0, new Vector2D(0.0, 1.0), new Vector2D(0.0, 0.0), 500.0, true);
        game.getShots().add(shot1);

        game.collide();

        assertTrue(game.getShots().isEmpty());
        assertFalse(game.getAsteroids().contains(largeAsteroid));
        // Should split into 3 medium (size 2) asteroids
        assertEquals(3, game.getAsteroids().size());
        for (Asteroid child : game.getAsteroids()) {
            assertEquals(Asteroid.MEDIUM_SIZE, child.getSize());
        }
        assertEquals(Asteroids.ASTEROID_SCORE, game.getScore());
        assertEquals(Asteroids.UFO_INTERVAL - Asteroids.ASTEROID_SCORE, game.getUfoCountdown());

        // 2. Player shot hits one of the medium (size 2) asteroids
        game.getAsteroids().clear();
        Asteroid mediumAsteroid = new Asteroid(
                Asteroids.ASTEROID_SCALE, 400.0, 400.0, Asteroid.MEDIUM_SIZE,
                new Vector2D(0.0, 0.0), false, new Vector2D(0.0, 1.0), null
        );
        game.getAsteroids().add(mediumAsteroid);
        Shot shot2 = new Shot(400.0, 400.0, new Vector2D(0.0, 1.0), new Vector2D(0.0, 0.0), 500.0, true);
        game.getShots().add(shot2);

        int scoreBeforeMedium = game.getScore();
        game.collide();

        assertTrue(game.getShots().isEmpty());
        assertEquals(3, game.getAsteroids().size());
        for (Asteroid child : game.getAsteroids()) {
            assertEquals(Asteroid.SMALL_SIZE, child.getSize());
        }
        assertEquals(scoreBeforeMedium + 2 * Asteroids.ASTEROID_SCORE, game.getScore());

        // 3. Player shot hits a small (size 3) asteroid: destroyed completely without spawning smaller ones
        game.getAsteroids().clear();
        Asteroid smallAsteroid = new Asteroid(
                Asteroids.ASTEROID_SCALE, 500.0, 500.0, Asteroid.SMALL_SIZE,
                new Vector2D(0.0, 0.0), false, new Vector2D(0.0, 1.0), null
        );
        game.getAsteroids().add(smallAsteroid);
        Shot shot3 = new Shot(500.0, 500.0, new Vector2D(0.0, 1.0), new Vector2D(0.0, 0.0), 500.0, true);
        game.getShots().add(shot3);

        int scoreBeforeSmall = game.getScore();
        game.collide();

        assertTrue(game.getShots().isEmpty());
        assertTrue(game.getAsteroids().isEmpty());
        assertEquals(scoreBeforeSmall + 3 * Asteroids.ASTEROID_SCORE, game.getScore());
    }

    /**
     * Tests that a UFO-fired shot hitting an asteroid splits the asteroid and consumes the shot
     * without awarding points to the player.
     */
    @Test
    public void testUfoShotCollidingWithAsteroidDoesNotAwardPoints() {
        Asteroids game = new Asteroids(new Random(42L));
        Asteroid largeAsteroid = new Asteroid(Asteroids.ASTEROID_SCALE, 300.0, 300.0);
        game.getAsteroids().add(largeAsteroid);

        Shot ufoShot = new Shot(300.0, 300.0, new Vector2D(1.0, 0.0), new Vector2D(0.0, 0.0), 500.0, false);
        game.getShots().add(ufoShot);

        game.collide();

        assertTrue(game.getShots().isEmpty());
        assertEquals(3, game.getAsteroids().size());
        assertEquals(0, game.getScore());
        assertEquals(Asteroids.UFO_INTERVAL, game.getUfoCountdown());
    }

    /**
     * Tests a UFO colliding directly with an asteroid: UFO is destroyed, asteroid splits,
     * and UFO countdown resets.
     */
    @Test
    public void testUfoCollidingWithAsteroid() {
        Asteroids game = new Asteroids(new Random(42L));
        Asteroid largeAsteroid = new Asteroid(Asteroids.ASTEROID_SCALE, 350.0, 350.0);
        game.getAsteroids().add(largeAsteroid);

        Ufo ufo = new Ufo(Asteroids.SHIP_SCALE, 350.0, 350.0, Ufo.LARGE_SIZE, Ufo.EAST);
        game.setUfo(ufo);
        game.setUfoCountdown(0);

        game.collide();

        assertNull(game.getUfo());
        assertEquals(Asteroids.UFO_INTERVAL, game.getUfoCountdown());
        assertFalse(game.getAsteroids().contains(largeAsteroid));
        assertEquals(3, game.getAsteroids().size());
    }

    /**
     * Tests a player shot hitting a UFO even when no asteroids are present on screen
     * (verifies fix for shot-vs-UFO check previously nested inside the asteroid loop).
     */
    @Test
    public void testPlayerShotCollidingWithUfoWithoutAsteroids() {
        Asteroids game = new Asteroids(new Random(42L));
        assertTrue(game.getAsteroids().isEmpty());

        Ufo smallUfo = new Ufo(Asteroids.SHIP_SCALE, 500.0, 500.0, Ufo.SMALL_SIZE, Ufo.WEST);
        game.setUfo(smallUfo);
        game.setUfoCountdown(0);

        Shot playerShot = new Shot(500.0, 500.0, new Vector2D(0.0, 1.0), new Vector2D(0.0, 0.0), 500.0, true);
        game.getShots().add(playerShot);

        game.collide();

        assertNull(game.getUfo());
        assertTrue(game.getShots().isEmpty());
        assertEquals(Ufo.SMALL_SIZE * Asteroids.UFO_SCORE, game.getScore());
        assertEquals(Asteroids.UFO_INTERVAL, game.getUfoCountdown());
    }

    /**
     * Tests a UFO shot hitting the player ship even when no asteroids are on screen
     * (verifies fix for shot-vs-ship check previously nested inside the asteroid loop).
     */
    @Test
    public void testUfoShotCollidingWithPlayerShipWithoutAsteroids() {
        Asteroids game = new Asteroids(new Random(42L));
        Ship ship = new Ship(Asteroids.SHIP_SCALE, 600.0, 450.0);
        game.setShip(ship);
        int initialLives = game.getLives().size();

        Shot ufoShot = new Shot(600.0, 450.0, new Vector2D(-1.0, 0.0), new Vector2D(0.0, 0.0), 500.0, false);
        game.getShots().add(ufoShot);

        game.collide();

        assertNull(game.getShip());
        assertTrue(game.getShots().isEmpty());
        assertEquals(initialLives - 1, game.getLives().size());
        assertEquals(Asteroids.SPAWN_INTERVAL, game.getSpawnCountdown());
    }

    /**
     * Tests the player ship colliding with a large asteroid: verifies that the player ship is
     * destroyed, a life is lost, and the parent asteroid is removed while splitting into 3 medium asteroids
     * (verifies fix for the bug where the parent asteroid was not removed on ship collision).
     */
    @Test
    public void testPlayerShipCollidingWithAsteroidRemovesParentAsteroid() {
        Asteroids game = new Asteroids(new Random(42L));
        Ship ship = new Ship(Asteroids.SHIP_SCALE, 600.0, 450.0);
        game.setShip(ship);
        int initialLives = game.getLives().size();

        Asteroid largeAsteroid = new Asteroid(Asteroids.ASTEROID_SCALE, 600.0, 450.0);
        game.getAsteroids().add(largeAsteroid);

        game.collide();

        assertNull(game.getShip());
        assertEquals(initialLives - 1, game.getLives().size());
        assertEquals(Asteroids.SPAWN_INTERVAL, game.getSpawnCountdown());
        // Parent large asteroid must be removed, leaving exactly 3 medium child asteroids
        assertFalse(game.getAsteroids().contains(largeAsteroid));
        assertEquals(3, game.getAsteroids().size());
        for (Asteroid rock : game.getAsteroids()) {
            assertEquals(Asteroid.MEDIUM_SIZE, rock.getSize());
        }
    }

    /**
     * Tests direct collision between the player ship and an alien UFO
     * (verifies fix for missing Ship-vs-UFO collision check).
     */
    @Test
    public void testPlayerShipCollidingDirectlyWithUfo() {
        Asteroids game = new Asteroids(new Random(42L));
        Ship ship = new Ship(Asteroids.SHIP_SCALE, 600.0, 450.0);
        Ufo ufo = new Ufo(Asteroids.SHIP_SCALE, 605.0, 450.0, Ufo.LARGE_SIZE, Ufo.EAST);
        game.setShip(ship);
        game.setUfo(ufo);
        game.setUfoCountdown(0);
        int initialLives = game.getLives().size();

        game.collide();

        assertNull(game.getShip());
        assertNull(game.getUfo());
        assertEquals(initialLives - 1, game.getLives().size());
        assertEquals(Asteroids.SPAWN_INTERVAL, game.getSpawnCountdown());
        assertEquals(Asteroids.UFO_INTERVAL, game.getUfoCountdown());
    }

    /**
     * Tests that shots exceeding their maximum travel range are removed during {@link Asteroids#collide()},
     * even when no asteroids are present.
     */
    @Test
    public void testExpiredShotsRemovedDuringCollide() {
        Asteroids game = new Asteroids(new Random(42L));
        Shot expiredShot = new Shot(100.0, 100.0, new Vector2D(1.0, 0.0), new Vector2D(0.0, 0.0), 2.0, true);
        expiredShot.move(Asteroids.SCREEN_WIDTH, Asteroids.SCREEN_HEIGHT);
        assertTrue(expiredShot.isMaxRangeReached());

        Shot activeShot = new Shot(200.0, 200.0, new Vector2D(1.0, 0.0), new Vector2D(0.0, 0.0), 500.0, true);
        activeShot.move(Asteroids.SCREEN_WIDTH, Asteroids.SCREEN_HEIGHT);
        assertFalse(activeShot.isMaxRangeReached());

        game.getShots().add(expiredShot);
        game.getShots().add(activeShot);

        game.collide();

        assertEquals(1, game.getShots().size());
        assertTrue(game.getShots().contains(activeShot));
        assertFalse(game.getShots().contains(expiredShot));
    }

    /**
     * Tests ship, asteroid, and UFO spawning rules and safe-distance clearances.
     */
    @Test
    public void testSpawningRulesAndSafeClearance() {
        Asteroids game = new Asteroids(new Random(42L));

        // 1. Spawn asteroids when ship is null (verifies fix for NoneType crash when ship is unspawned)
        game.spawnAsteroids();
        assertEquals(Asteroids.MAX_ASTEROIDS, game.getAsteroids().size());
        Vector2D screenCenter = new Vector2D(Asteroids.SCREEN_WIDTH / 2.0, Asteroids.SCREEN_HEIGHT / 2.0);
        for (Asteroid asteroid : game.getAsteroids()) {
            assertTrue(asteroid.getPosition().distanceTo(screenCenter) >= Asteroids.MIN_ASTEROID_DISTANCE);
        }

        // 2. Place an asteroid inside MIN_CLEAR_RADIUS of center: spawnShip should wait
        game.getAsteroids().clear();
        game.getAsteroids().add(new Asteroid(Asteroids.ASTEROID_SCALE, 600.0, 450.0));
        game.spawnShip();
        assertNull(game.getShip());

        // 3. Move asteroid far away: spawnShip succeeds
        game.getAsteroids().clear();
        game.getAsteroids().add(new Asteroid(Asteroids.ASTEROID_SCALE, 100.0, 100.0));
        game.spawnShip();
        assertNotNull(game.getShip());
        assertEquals(600.0, game.getShip().getPosition().getX(), EPSILON);
        assertEquals(450.0, game.getShip().getPosition().getY(), EPSILON);

        // 4. Spawn UFO when entry area is clear
        game.getAsteroids().clear();
        game.spawnUfo();
        assertNotNull(game.getUfo());
    }

    /**
     * Tests player keyboard controls (firing, rotating left/right, thrusting) and extra-life awarding
     * at exact 10,000-point intervals via {@link Asteroids#updateFrame()}.
     */
    @Test
    public void testPlayerInputAndUpdateFrameExtraLife() {
        Asteroids game = new Asteroids(new Random(42L));
        game.spawnShip();
        assertNotNull(game.getShip());

        GameKeyListener keys = game.getKeyListener();

        // Simulate pressing Spacebar twice (with release in between) and holding W + Left
        keys.simulateKeyPress(KeyEvent.VK_SPACE, ' ');
        // Holding spacebar without release should not enqueue a second shot
        keys.simulateKeyPress(KeyEvent.VK_SPACE, ' ');
        keys.simulateKeyPress(KeyEvent.VK_W, 'w');
        keys.simulateKeyPress(KeyEvent.VK_LEFT, '\0');

        game.playerInput();
        assertEquals(1, game.getShots().size());
        assertTrue(game.getShip().isThrusting());
        // Ship rotated +3 degrees counter-clockwise from (0, 1)
        assertTrue(game.getShip().getDirection().getX() < 0.0);

        // Release keys
        keys.simulateKeyRelease(KeyEvent.VK_SPACE);
        keys.simulateKeyRelease(KeyEvent.VK_W);
        keys.simulateKeyRelease(KeyEvent.VK_LEFT);

        // Test exact 10,000 score threshold awards an extra life during updateFrame()
        int livesBefore = game.getLives().size();
        game.setScore(Asteroids.EXTRA_LIFE_INTERVAL);
        game.updateFrame();
        assertEquals(livesBefore + 1, game.getLives().size());
        assertEquals(Asteroids.EXTRA_LIFE_INTERVAL * 2, game.getNextNewLife());

        // Test GamePanel headless rendering (both during active gameplay and when gameOver is true)
        GamePanel panel = new GamePanel(game);
        BufferedImage image = new BufferedImage(
                Asteroids.SCREEN_WIDTH, Asteroids.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = image.createGraphics();
        panel.paint(g2d);
        game.setGameOver(true);
        panel.paint(g2d);
        g2d.dispose();
    }
}
