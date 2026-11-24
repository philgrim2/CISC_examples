# Asteroids

A Java implementation of the classic **Asteroids** arcade game, migrated from the original Python implementation (`src/main/python/asteroids.py`). It uses standard JDK Swing/AWT libraries with zero external runtime dependencies and includes a headless JUnit 5 test suite.

---

## Requirements

* **Java**: JDK 17 or newer (tested with OpenJDK 21)
* **Build Tool**: Apache Maven 3.6+

---

## Building and Running

### 1. Build the Project and Run Tests

Compile the source code and run the full JUnit 5 test suite (configured to run in headless mode automatically):

```bash
mvn test
```

Package the executable JAR:

```bash
mvn package
```

### 2. Run the Game

You can launch the game using the packaged JAR:

```bash
java -jar target/asteroids-1.0-SNAPSHOT.jar
```

Or run the main class directly via `java`:

```bash
java -cp target/classes com.inlt.example.asteroids.Asteroids
```

### 3. Generate Javadoc Documentation

Generate HTML Javadocs for all classes and methods:

```bash
mvn javadoc:javadoc
```

The generated documentation will be placed in `target/site/apidocs/index.html`.

---

## Player Controls & Gameplay

| Key(s) | Action |
| :--- | :--- |
| **Up Arrow** or **W** | Fire thrusters (accelerate in current heading) |
| **Left Arrow** or **A** | Rotate ship counter-clockwise |
| **Right Arrow** or **D** | Rotate ship clockwise |
| **Space Bar** | Fire mass-driver shot (one shot per key press) |
| **Any Key** (at Game Over) | Dismiss the Game Over dialog and exit |

### Scoring & Rules

* **Starting Lives**: 3 (maximum of 10 lives)
* **Extra Life**: Awarded every **10,000 points**
* **Asteroids**:
  * Large Asteroid (`size = 1`): **15 points** (splits into 3 medium asteroids)
  * Medium Asteroid (`size = 2`): **30 points** (splits into 3 small asteroids)
  * Small Asteroid (`size = 3`): **45 points** (destroyed completely)
* **UFOs**:
  * Large UFO (`size = 1`): **50 points**
  * Small UFO (`size = 2`): **100 points**

---

## Project Layout

```text
.
├── pom.xml
├── README.md
└── src
    ├── main
    │   ├── java/com/inlt/example/asteroids
    │   │   ├── Asteroid.java          # On-screen asteroid entity (sizes 1, 2, 3)
    │   │   ├── Asteroids.java         # Main game controller, state, and entry point
    │   │   ├── GameKeyListener.java   # Keyboard input handler
    │   │   ├── GamePanel.java         # Swing rendering panel
    │   │   ├── Ship.java              # Player ship entity
    │   │   ├── Shot.java              # Mass-driver projectile entity
    │   │   ├── Ufo.java               # Alien UFO entity (sizes 1, 2)
    │   │   ├── Vector2D.java          # 2D vector math and coordinate wrapping
    │   │   └── VectorObject.java      # Base polygon game object class
    │   ├── python
    │   │   └── asteroids.py           # Original Python implementation
    │   └── resources/sounds           # Classic game sound assets
    └── test
        └── java/com/inlt/example/asteroids
            ├── AsteroidsCollisionAndGameTest.java
            ├── GameObjectsTest.java
            ├── Vector2DTest.java
            └── VectorObjectTest.java
```

---

## Bugs Fixed from the Original Python Implementation

During the analysis of `src/main/python/asteroids.py`, several bugs were identified and fixed in the Java implementation:

1. **Parent Asteroid Not Removed on Player Ship Collision (`asteroids.py` lines 766–779)**
   * **Issue**: In `_collide()`, when the player's ship collided with an asteroid, the player ship was destroyed and 3 smaller child asteroids were spawned (for sizes 1 and 2), but `self._asteroids.remove(asteroid)` was omitted. The original parent asteroid remained on screen alongside its 3 newly spawned child fragments.
   * **Fix**: `Asteroids.collide()` explicitly removes the parent asteroid from `this.asteroids` before splitting it into smaller fragments.

2. **Missing Player Ship vs. UFO Collision Detection (`asteroids.py` lines 700–780)**
   * **Issue**: `_collide()` checked for UFO-vs-asteroid, shot-vs-asteroid, player-shot-vs-UFO, UFO-shot-vs-ship, and ship-vs-asteroid collisions, but never checked for a direct collision between the player's `Ship` and the `UFO`. The player ship and UFO could pass directly through one another.
   * **Fix**: Added a direct ship-vs-UFO collision check in `Asteroids.collide()` that destroys both the player ship (consuming a life and starting the respawn timer) and the UFO (resetting the UFO spawn countdown).

3. **Shot-vs-UFO, Shot-vs-Ship, and Shot Range Expiration Nested Inside the Asteroid Loop (`asteroids.py` lines 710–764)**
   * **Issue**: In `_collide()`, the loop checking whether shots hit the UFO, whether UFO shots hit the player ship, and whether shots exceeded `max_range()` was nested *inside* `for asteroid in temp_asteroids:`. This caused those checks to run redundantly once per asteroid on every frame, and prevented shots from hitting the UFO or player ship (or expiring at max range) whenever no asteroids were currently on screen. Additionally, the `break` statement when a UFO shot hit the player ship only broke out of the inner shot loop rather than halting checks for the destroyed ship.
   * **Fix**: Moved the shot-vs-UFO, UFO-shot-vs-ship, and shot range expiration checks into a separate pass over active shots outside the asteroid loop in `Asteroids.collide()`.

4. **Crash in `_spawn_asteroids()` When the Player Ship Is Unspawned (`asteroids.py` lines 568–573)**
   * **Issue**: `_spawn_asteroids()` called `self._ship.center().distanceTo(x, y)` without checking if `self._ship` was `None`. If the last asteroid on screen was destroyed while the player ship was destroyed and waiting to respawn (`self._ship is None`), `_spawn_asteroids()` crashed with `AttributeError: 'NoneType' object has no attribute 'center'`.
   * **Fix**: `Asteroids.spawnAsteroids()` checks whether `this.ship != null`; if the ship is not currently spawned, it measures clearance from the screen center `(SCREEN_WIDTH / 2.0, SCREEN_HEIGHT / 2.0)` where the ship will respawn.

5. **Asymmetric UFO Base Velocity and Inverted `EAST` / `WEST` Constants (`asteroids.py` lines 237–263)**
   * **Issue**: `UFO.__BASE_VELOCITY` was hardcoded as `Vector2D(0.15, 0.0)` (always pointing in `+X`), and `UFO.__init__` added `self._direction * size` to it. Consequently, an eastbound (`+X`) size-1 UFO moved at speed `1.15` (`0.15 + 1.0`), whereas a westbound (`-X`) size-1 UFO moved at speed `0.85` (`0.15 - 1.0`). In addition, `UFO.EAST` (`(-1.0, 0.0)`) and `UFO.WEST` (`(1.0, 0.0)`) were inverted relative to the coordinate system used in `_spawn_ufo()`.
   * **Fix**: Defined `Ufo.EAST` as `(1.0, 0.0)` and `Ufo.WEST` as `(-1.0, 0.0)`, and computed the UFO's velocity along its travel direction vector via `this.direction.multiply(BASE_SPEED + size)` so eastbound and westbound UFOs of the same size travel at identical speeds.

6. **`UFO.rotate()` Desynchronized Direction and Velocity (`asteroids.py` lines 321–326)**
   * **Issue**: `UFO.rotate(angle)` rotated `self._direction` but did not rotate `self._velocity`, causing the UFO's heading and velocity vectors to diverge whenever `rotate()` was called.
   * **Fix**: `Ufo.rotate(double angle)` rotates both `this.direction` and `this.velocity`.

7. **`Shot` Subclassed `VectorObject` Without Initializing Base Class State (`asteroids.py` lines 393–410)**
   * **Issue**: `Shot` inherited from `VectorObject` without calling `super().__init__()`, leaving `self._points` uninitialized so calling inherited `VectorObject` methods (`rotate`, `translate`, `scale`) would raise an `AttributeError`.
   * **Fix**: `Shot` properly initializes its `VectorObject` base class and safely implements `center()`, `radius()`, `rotate()`, `translate()`, and `draw()`.

8. **Extra Life Threshold Off-By-One and Unenforced `MAX_LIVES` (`asteroids.py` lines 476, 533–542, 827–829)**
   * **Issue**: Extra lives are earned every 10,000 points up to `MAX_LIVES = 10`, but `run()` checked `if self._score > self._next_new_life:` (failing to award a life when the score reached *exactly* 10,000) and `_add_life()` never enforced `MAX_LIVES`.
   * **Fix**: `Asteroids.updateFrame()` checks `this.score >= this.nextNewLife`, and `Asteroids.addLife()` caps the life reserve at `MAX_LIVES`.

9. **Shared Mutable Class Attributes in `Asteroids` (`asteroids.py` lines 500–515)**
   * **Issue**: `_lives = []`, `_asteroids = []`, and `_shots = []` were defined as mutable class-level attributes rather than instance attributes in `__init__`, causing state to be shared across multiple `Asteroids` instances.
   * **Fix**: All state collections (`lives`, `asteroids`, `shots`) are initialized per instance in the `Asteroids` constructor.

10. **Coordinate Modulo Wrapping for Negative Coordinates in Java (`Vector2D.wrap`)**
    * **Issue**: Python's `%` operator performs mathematical modulo (returning `[0, limit)` for negative numbers), whereas Java's `%` operator returns a negative remainder for negative dividends (e.g., `-5.0 % 1200.0 == -5.0`).
    * **Fix**: `Vector2D.wrap(double limitX, double limitY)` computes `((coord % limit) + limit) % limit` so negative coordinates wrap cleanly to the opposite edge of the screen.
