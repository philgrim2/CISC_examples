"""
asteroids.py, by Phil Grim

Simple implementation of the original arcade game Asteroids.
Player controls include:
   Up arrow    or w:  Thrusters
   Left arrow  or a:  Rotate left
   Right Arrow or d:  Rotate right
   Space Bar:         Fire mass driver
   
Extra life earned every 10,000 points
   
Single file for easier distribution, although multiple files would be cleaner.

Requires the textbook libraries from 'Introduction to Programming in Python: An Interdisciplinary Approach, 1st ed.'
by Sedgewick, Wayne, and Dondero, which can be found on the book's website at 
https://introcs.cs.princeton.edu/python/code/introcs-python.zip

Outstanding issues:
  Key repeat doesn't work in stddraw, so using pygame for key repeat.
  Object inheritance still needs some refactoring.
  
Possible improvements:
  Add sound (stdaudio is kinda bad...)
  
Possible practical uses:
  As a lab assignment/take-home exam for students to implement. 
  
References:
  Sounds from https://www.classicgaming.cc/classics/asteroids/
  Vector math from https://stackoverflow.com/questions/28458145/rotate-2d-polygon-without-changing-its-position
  Key repeat reference and inspiration from https://realpython.com/asteroids-game-python/
  
"""
import math
import random
import stddraw
import pygame

class Vector2D:
    """
    Implement a two-dimensional vector (since we're trying to minimize use of pygame directly)
    """
    def __init__(self, x=0.0, y=0.0):
        self.x, self.y = x, y

    def rotate(self, angle):
        """
        Rotate the vector to a specified angle in degrees.
        """
        angle = math.radians(angle)
        sin = math.sin(angle)
        cos = math.cos(angle)
        x = self.x
        y = self.y
        self.x = x * cos - y * sin
        self.y = x * sin + y * cos
        
    def wrap(self, limit_x, limit_y):
        self.x = self.x % limit_x
        self.y = self.y % limit_y
        
        
    def distanceTo(self, *args):
        """
        Calculate distance to another Vector2D (if one arg)
        or to an x,y coordinate (if two args)
        """
        other = None
        if len(args) == 1:
            other = args[0]
        else:
            other = Vector2D(args[0], args[1])
        return math.sqrt((self.x - other.x) ** 2 + (self.y - other.y) ** 2)
                  
        
    def __add__(self, other):
        """
        Adds this vector to the other vector
        """
        if isinstance(other, Vector2D):
            return Vector2D(self.x + other.x, self.y + other.y)
        else:
            return Vector2D(self.x + other, self.y + other)
    
    def __mul__(self, other):
        """
        Multiplies this vector by another vector
        """
        if isinstance(other, Vector2D):
            return Vector2D(self.x * other.x, self.y * other.y)
        else:
            return Vector2D(self.x * other, self.y * other)
        
    def __repr__(self):
        return f'({self.x},{self.y})'

class VectorObject:
    """
    Base class for the game objects.  A VectorObject is defined by a list of Vector2D points.
    """
    def __init__(self, points, scale=1.0):
        self._points = [Vector2D(*point) for point in points]
        self.scale(scale)

    def rotate(self, angle):
        """
        Rotate the object around its center to the specified angle (in degrees)
        """
        center = self.center()
        for point in self._points:
            point.x -= center.x
            point.y -= center.y
            point.rotate(angle)
            point.x += center.x
            point.y += center.y
            
    def translate(self, position):
        """
        Warp the object to a new position
        """
        center = self.center()
        for point in range(0, len(self._points)):
            self._points[point].x -= center.x
            self._points[point].y -= center.y
            self._points[point] = self._points[point] + position    

    def center(self):
        """
        Find the center of the polygon.
        """
        totalX = totalY = 0.0
        for i in self._points:
            totalX += i.x
            totalY += i.y

        len_points = len(self._points)

        return Vector2D(totalX / len_points, totalY / len_points)
    
    def radius(self):
        center = self.center()
        dist = 0
        for point in self._points:
            this_dist = center.distanceTo(point)
            if this_dist > dist: dist = this_dist
        return dist
    
    def scale(self, scale):
        """
        Scale the reference points to the scale factor
        """
        self._scale = scale
        center = self.center()
        for point in self._points:
            offx = point.x - center.x
            offy = point.y - center.y
            offx *= self._scale
            offy *= self._scale
            point.x = center.x + offx
            point.y = center.y + offy

            
    def draw(self):
        xcoords = []
        ycoords = []
        for point in self._points:
            xcoords.append(point.x)
            ycoords.append(point.y)
        stddraw.polygon(xcoords, ycoords)

class Ship(VectorObject):
    """
    Class to represent the player ship.
    """
            
    # Define the canonical ship polygon, which will get scaled based on the screen size
    __SHIP_POINTS = [(0,2), (1,-1), (0,0), (-1,-1)]
    # Define a ship polygon with a thrust flame
    __THRUST_POINTS = [(0,2), (1,-1), (0,0), (-0.4, -0.4), (0,-0.7), (0.4,-0.4), (0,0),(-1,-1)]
    
    #Ship acceleration per frame
    __ACCELERATION = 0.15
    #Ship rotational speed
    SHIP_ROTATE_DEGREES = 3
    
    def __init__(self, scale, x, y):
        """
        Creates an instance of the ship at the specified scale factor.
        """
        super().__init__(self.__SHIP_POINTS, scale)
        self._position = Vector2D(x, y)
        self.translate(self._position)
        self._velocity = Vector2D(0,0)
        self._direction = Vector2D(0,1)
        self._thrust = False
        self._thrusting = VectorObject(self.__THRUST_POINTS, scale)
        self._thrusting.translate(self._position)

    def draw(self):       
        if (self._thrust):
            self._thrusting.draw()
        else:
            super().draw()
        
    def rotate(self, angle):
        super().rotate(angle)
        self._thrusting.rotate(angle)
        self._direction.rotate(angle)
        
    def thrust(self, thrusting):
        self._thrust = thrusting
        
    def move(self, limit_x, limit_y):
        if (self._thrust):
            self._velocity += self._direction * self.__ACCELERATION
        self._position += self._velocity
        self._position.wrap(limit_x, limit_y)
        self.translate(self._position)
        self._thrusting.translate(self._position)
        #print(f'Direction: {self._direction}, Velocity: {self._velocity}, Position: {self._position}')
        
    def position(self):
        return Vector2D(self._position.x, self._position.y)
    
    def velocity(self):
        return Vector2D(self._velocity.x, self._velocity.y)
    
    def direction(self):
        return Vector2D(self._direction.x, self._direction.y)

        
class UFO(VectorObject):
    """
    Class to represent the alien ships.
    """
    EAST = Vector2D(-1.0,0.0)
    WEST = Vector2D(1.0, 0.0)
    # UFO will shoot when it has traveled at least this percent of the limit area
    SHOT_FREQUENCY = 0.05

            
    # Define the canonical ship polygon, which will get scaled based on the screen size
    __SHIP_POINTS = [(0,1), (2,1), (2.5,0), (2,-1), (-2, -1), (-2.5, 0), (-2, 1), (0, 1), (-1, 1), (-0.6, 1.75), (0.6, 1.75), (1, 1), (0.0, 1)]
    
    #Ship base velocity
    __BASE_VELOCITY = Vector2D(0.15,0.0)

    
    def __init__(self, scale, x, y, size = 1, direction = WEST):
        """
        Creates an instance of the ship at the specified scale factor.
        """
        self._size = size
        if (size == 2): scale *= 0.5
        super().__init__(self.__SHIP_POINTS, scale)
        self._position = Vector2D(x, y)
        self.translate(self._position)
        self._velocity = Vector2D(self.__BASE_VELOCITY.x, self.__BASE_VELOCITY.y)
        self._direction = Vector2D(direction.x, direction.y)
        # Small ship moves faster
        self._velocity += self._direction * size
        self._since_shot = 0
        self._since_turn = 0    
        
    def move(self, limit_x, limit_y):
        """
        Updates the UFO's position.  Will randomly change direction occasionally.
        Returns False if the UFO has left the limit area, True otherwise
        """
        # If the UFO has traveled at least 20% of the limit area since the last turn, it can turn
        if random.randint(1,4) == 1 and self._since_turn > limit_x * 0.2:
            # Going to turn.  Can only move either straight east-west, or 
            # 45 degrees north/south.
            rot = 45
            if 0.9 < abs(self._direction.x) < 1.1:
                # this is straight, so bend 45 degrees up or down
                if random.randint(1,2) == 1: rot *= -1
            elif self._direction.y > 0.0:
                if self._direction.x > 0.0: rot *= -1
            else:
                if self._direction.x < 0.0: rot *= -1   
        
            self._direction.rotate(rot)
            self._velocity.rotate(rot)
            self._since_turn = 0
            
        at = Vector2D(self._position.x, self._position.y)
        self._position += self._velocity
        self._since_shot += at.distanceTo(self._position)
        self._since_turn += at.distanceTo(self._position)
        self.translate(self._position)
        return not (self.position().x - self.radius() > limit_x or
                    self.position().x + self.radius() < 0 or 
                    self.position().y - self.radius() > limit_y or
                    self.position().y + self.radius() < 0)
        
    def shoot(self, limit_x, limit_y):
        """
        Fires a shot from the UFO's mass driver in a random direction.
        """
        # If the UFO has traveled at least the required distance, fire a random shot
        if self._since_shot > limit_x * self.SHOT_FREQUENCY:
            direction = Vector2D(self._direction.x, self._direction.y)
            direction.rotate(random.randrange(360))
            muzzle = self._position + (direction * self.radius())
            shot = Shot(muzzle.x, muzzle.y, direction, self._velocity, limit_x * 0.75, False)
            self._since_shot = 0
            return shot
            
        
    def position(self):
        return Vector2D(self._position.x, self._position.y)
    
    def velocity(self):
        return Vector2D(self._velocity.x, self._velocity.y)
    
    def direction(self):
        return Vector2D(self._direction.x, self._direction.y)

    def rotate(self, angle):
        """
        Change direction but not orientation
        """
        self._direction.rotate(angle)
        
    def size(self):
        return self._size

class Asteroid(VectorObject):
    """
    Class to represent an on-screen asteroid.  Can be scaled to various sizes.
    """
     
    # Define  the canonical polygon for an asteroid
    __ASTEROID_POINTS = [(0.0,4.0), (1.0,3.5), (1.2, 3.0), (2.0, 2.0), (2.5, 3.2), (3.6, -0.6), (3.25, -2.6), (2.25, -1.7), (1.25, -3.2),
                         (.75, -2.6), (-1.0, -3.2), (-2.24, -2.67), (-3.5, -1.15), (-2.4, 2.6), (-1.8, 2.25), (-0.5, 3.0)]
    
    # Velocity step (5 speeds possible)
    __VELOCITY_STEP = 0.25

     
    def __init__(self, scale, x, y, size = 1, velocity = None, boost = False, direction = None, deflect = None):
        self._size = size
        if (size == 2): scale *= 0.5
        elif (size == 3): scale *= 0.2
        super().__init__(self.__ASTEROID_POINTS, scale)
        self._position = Vector2D(x, y)
        self.translate(self._position)
        # Random rotation so they look different. 
        self.rotate(random.randrange(0,360))
                
        self._velocity = Vector2D(0,0)
        if direction == None:
            self._direction = Vector2D(0,1)
            # Rotate the direction vector randomly
            self._direction.rotate(random.randrange(0,360))
        else:
            self._direction = Vector2D(direction.x, direction.y)
        if deflect != None:
            self._direction.rotate(deflect)
            
        if velocity == None: 
            # Add in some velocity - either 1 or 2 times the velocity step
            self._velocity += self._direction * (self.__VELOCITY_STEP * random.randint(1,2))
        else:
            self._velocity = Vector2D(velocity.x, velocity.y)
            
        if boost:
            # Add in some velocity - either 1 or 2 times the velocity step
            self._velocity += self._direction * (self.__VELOCITY_STEP * random.randint(1,2))
       
    
    def move(self, limit_x, limit_y): 
        self._position += self._velocity
        self._position.wrap(limit_x, limit_y)
        self.translate(self._position)  
        
    def position(self):
        return Vector2D(self._position.x, self._position.y)
        
    def size(self):
        return self._size
    
    def velocity(self):
        return Vector2D(self._velocity.x, self._velocity.y)
    
    def direction(self):
        return Vector2D(self._direction.x, self._direction.y)

            
        
class Shot(VectorObject):
    """
    Class to represent a mass-driver shot.
    """
    RADIUS = 3
    
    def __init__(self, x, y, direction, velocity, max_range, player=True):
        """
        Shots are already moving in the direction the ship is facing,
        so we take a direction and initial velocity to start with
        """
        self._position = Vector2D(x, y)
        self._traveled = 0            
        self._velocity = velocity + (direction * 3.5)
        self._direction = Vector2D(direction.x, direction.y)
        self._range = max_range
        self._player = player
       
    def center(self):
        """
        Override the vector object center function - our center is just (x, y)
        """
        return Vector2D(self._position.x, self._position.y)
    
    def move(self, limit_x, limit_y): 
        at = Vector2D(self._position.x, self._position.y)
        self._position += self._velocity
        self._traveled += self._position.distanceTo(at)
        self._position.wrap(limit_x, limit_y)
        
    def max_range(self):
        return self._traveled > self._range
        
    def draw(self):
        stddraw.filledCircle(self._position.x, self._position.y, self.RADIUS)
        
    def radius(self):
        return self.RADIUS
    
    def position(self):
        return self._position
    
    def playerShot(self):
        """
        Returns True if the player ship was the source of the shot, False otherwise
        """
        return self._player
        
class Asteroids:
    """
    Implementation class for the game itself.  Holds all of the constants required
    for setting up the game.
    """
    # Canvas size
    SCREEN_WIDTH = 1200
    SCREEN_HEIGHT = 900

    # Scaled font size for dialog boxes
    DIALOG_FONT_SIZE = SCREEN_HEIGHT // 32
    # Scaled font size for score output
    SCORE_FONT_SIZE = SCREEN_HEIGHT // 48
    
    # Scale for player ship
    SHIP_SCALE = SCREEN_WIDTH / 120
    
    # Scale for largest asteroid size
    ASTEROID_SCALE = SCREEN_WIDTH / 80
    
    # Base score for destroying an asteroid
    ASTEROID_SCORE = 15
    
    # Base score for destroying a UFO
    UFO_SCORE = 50
    
    # Horizontal margin for score and lives display
    MARGIN_H = 40
    # Vertical margin for score and lives display
    MARGIN_V = 25
    
    # Time for each frame in milliseconds
    FRAME_LENGTH = 10

    # Number of lives for the player
    MAX_LIVES = 10
    
    # Player starting lives
    STARTING_LIVES = 3
    
    # Get an extra life every so often
    EXTRA_LIFE_INTERVAL = 10000
    
    # Number of asteroids per spawn
    MAX_ASTEROIDS = 6
    
    # Minimum distance that an asteroid can spawn from the player's ship
    MIN_ASTEROID_DISTANCE = 150
    
    # Clear area radius for spawning a new ship
    MIN_CLEAR_RADIUS = 100
    
    # Spawn delay after death (in ms)
    SPAWN_INTERVAL = 2000
    
    # Point interval for spawning UFO
    UFO_INTERVAL = 1500
    
    # Player starting lives
    _lives = []
    
    # Player score
    _score = 0
    
    # Ship
    _ship = None
    
    # List of current asteroids
    _asteroids = []
    
    # List of current mass driver shots
    _shots = []
    
    # UFO, if there is one.
    _ufo = None

    def __init__(self):
        # Set up the size of the canvas and the drawing scale.  
        # The scale will be set to the same as the size, so that drawing is done in pixels directly.
        stddraw.setCanvasSize(self.SCREEN_WIDTH, self.SCREEN_HEIGHT)
        stddraw.setXscale(0, self.SCREEN_WIDTH)
        stddraw.setYscale(0, self.SCREEN_HEIGHT)
                
        for _ in range(self.STARTING_LIVES):
            self._add_life()        
            
        self._next_new_life = self.EXTRA_LIFE_INTERVAL
                
        self._spawn_countdown = 0
        
        self._ufo_countdown = self.UFO_INTERVAL
    
    def _add_life(self):
        new_life = Ship(self.SHIP_SCALE/2,0,0)
        lifeOffset = self.MARGIN_H
        for _ in range(len(self._lives)):
            lifeOffset += (new_life.radius() * 2)
            
        new_pos = Vector2D(self.SCREEN_WIDTH - lifeOffset, self.SCREEN_HEIGHT - self.MARGIN_V)
        new_life.translate(new_pos)
        self._lives.append(new_life)
        
        
    
    def _endGameDialog(self):
        """
        Displays a dialog box that shows the game over message, then waits for a keypress.
        """
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.filledRectangle(self.SCREEN_WIDTH * 0.25, self.SCREEN_HEIGHT * 0.4, self.SCREEN_WIDTH * 0.5, self.SCREEN_HEIGHT * 0.20)
        stddraw.setPenColor(stddraw.LIGHT_GRAY)
        
        stddraw.setFontSize(self.DIALOG_FONT_SIZE)
        stddraw.rectangle(self.SCREEN_WIDTH * 0.25, self.SCREEN_HEIGHT * 0.4, self.SCREEN_WIDTH * 0.5, self.SCREEN_HEIGHT * 0.20)
        stddraw.text(self.SCREEN_WIDTH / 2, self.SCREEN_HEIGHT * 0.55, 'Game Over')
        stddraw.text(self.SCREEN_WIDTH / 2, self.SCREEN_HEIGHT * 0.45, f'Final Score: {self._score}')
        #Clear key buffer
        stddraw.show(1000)
        while stddraw.hasNextKeyTyped():
            stddraw.nextKeyTyped()
        stddraw.show(1000)
        moreElectricity = True
        while moreElectricity:
            stddraw.show(10)
            if stddraw.hasNextKeyTyped():
                moreElectricity = False  

    def _spawn_asteroids(self):
        while len(self._asteroids) < self.MAX_ASTEROIDS:
            x = random.randrange(self.SCREEN_WIDTH)
            y = random.randrange(self.SCREEN_HEIGHT)
            if self._ship.center().distanceTo(x, y) >= self.MIN_ASTEROID_DISTANCE:
                self._asteroids.append(Asteroid(self.ASTEROID_SCALE, x, y))
                
    def _spawn_ship(self):
        """
        Spawns the player's ship.  Makes sure there's a minimum clear area first
        """
        center = Vector2D(self.SCREEN_WIDTH / 2, self.SCREEN_HEIGHT/2)
        closest = self.SCREEN_WIDTH
        for asteroid in self._asteroids:
            dist = asteroid.position().distanceTo(center)
            if dist < closest:
                closest = dist
        if closest > self.MIN_CLEAR_RADIUS:
            self._ship = Ship(self.SHIP_SCALE, self.SCREEN_WIDTH // 2, self.SCREEN_HEIGHT // 2)   
                
    def _spawn_ufo(self):
        """
        Spawns a UFO of one of two random sizes at one of four random entry points
        """ 
        # Pick random size and position
        eastwest = random.randint(1,2)
        northsouth = random.randint(1,2)
        which = random.randint(1,10)
        
        if eastwest == 1:
            x = 0
            dirx = 1.0
        else:
            x = self.SCREEN_WIDTH
            dirx = -1.0
            
        if northsouth == 1:
            y = self.SCREEN_HEIGHT // 4
        else:
            y = self.SCREEN_HEIGHT - (self.SCREEN_HEIGHT // 4)
            
        if which > 6:
            size = 2
        else:
            size = 1    
             
        direction = Vector2D(dirx, 0.0)

        center = Vector2D(x, y)
        closest = self.SCREEN_WIDTH
        for asteroid in self._asteroids:
            dist = asteroid.position().distanceTo(center)
            if dist < closest:
                closest = dist
        if closest > self.MIN_CLEAR_RADIUS:
            self._ufo = UFO(self.SHIP_SCALE, x, y, size, direction)
        

    def _draw(self):
        """
        Draws the ship, asteroids, shots, and score.
        """
        #Set the background and drawing colors
        stddraw.clear(stddraw.BLACK)
        stddraw.setPenColor(stddraw.LIGHT_GRAY)
      
        # Draw the score
        stddraw.setFontFamily('Courier')
        stddraw.setFontSize(self.SCORE_FONT_SIZE)
        stddraw.text(self.MARGIN_H, self.SCREEN_HEIGHT - self.MARGIN_V // 2, f'{self._score}')
    
        # Draw the lives
        stddraw.setPenRadius(0)
        for life in self._lives:
            life.draw()
        stddraw.setPenRadius()
        
        # Draw the ship, if it's spawned
        if self._ship != None:
            self._ship.draw()
            
        # Draw the UFO if there is one.
        if self._ufo != None:
            self._ufo.draw()
        
        # Draw the asteroids
        for asteroid in self._asteroids:
            asteroid.draw()
    
        # Draw the shots
        for shot in self._shots:
            shot.draw()
    
        #Update screen
        stddraw.show(self.FRAME_LENGTH)
    
    def _player_input(self):
        """
        Check for key input, and update the ship accordingly
        """
        if self._ship == None:
            # Clear out key buffer
            while stddraw.hasNextKeyTyped():
                stddraw.nextKeyTyped()
        else:
            if stddraw.hasNextKeyTyped():
                key = stddraw.nextKeyTyped().lower()
            
                #
                # Even though we've moved to calling Pygame directly for the direction
                # keys, we're staying with stddraw for the shoot function, making one
                # keypress equal one shot.  We don't need key repeat for this, and it 
                # will keep stddraw's key buffer from getting overwhelmed.
                if key == ' ':
                    muzzle = self._ship.position() + (self._ship.direction() * (self.SHIP_SCALE))
                    self._shots.append(Shot(muzzle.x, muzzle.y, self._ship.direction(),
                                            self._ship.velocity(), self.SCREEN_HEIGHT * 0.8))
            
            # Cheat a little for key repeat - use Pygame directly
            is_key_pressed = pygame.key.get_pressed()
            if is_key_pressed[pygame.K_a] or is_key_pressed[pygame.K_LEFT]:
                # Rotate ship counterclockwise
                self._ship.rotate(self._ship.SHIP_ROTATE_DEGREES)
            elif is_key_pressed[pygame.K_d] or is_key_pressed[pygame.K_RIGHT]:
                # Rotate ship clockwise
                self._ship.rotate(-self._ship.SHIP_ROTATE_DEGREES)
            if is_key_pressed[pygame.K_w] or is_key_pressed[pygame.K_UP]:
                # Fire thrusters
                self._ship.thrust(True)
            else:
                self._ship.thrust(False)
           
    def _collide(self):
        """
        Check for collisions, and remove shots that have reached max range
        Asteroids don't collide with each other
        """
        # First collide shots with asteroids
        # Create copies of the lists to iterate over.  Hitting an asteroid 
        # consumes the shot, and the asteroid is either split or destroyed.        
        temp_shots = [shot for shot in self._shots]
        temp_asteroids = [asteroid for asteroid in self._asteroids]
        for asteroid in temp_asteroids:
            # If there's an active UFO, check for it hitting an asteroid
            if self._ufo != None:
                dist = self._ufo.position().distanceTo(asteroid.position())
                if dist < self._ufo.radius() + asteroid.radius():
                    self._asteroids.remove(asteroid)
                    # If the asteroid was size 1 or 2, spawn 3 smaller ones
                    if asteroid.size() < 3:
                        for _ in range(3):
                            rock = Asteroid(self.ASTEROID_SCALE, asteroid.position().x, asteroid.position().y,
                                            asteroid.size()+1, asteroid.velocity(), True, asteroid.direction(), 
                                            random.randrange(-30, 31))
                            self._asteroids.append(rock)
                    self._ufo = None
                    self._ufo_countdown = self.UFO_INTERVAL
            # Check for shots hitting asteroids or a UFO
            for shot in temp_shots:
                # Make sure we're not checking something that's already been removed
                if shot in self._shots and asteroid in self._asteroids:
                    dist = shot.position().distanceTo(asteroid.position())
                    if dist < shot.radius() + asteroid.radius():
                        self._shots.remove(shot)
                        self._asteroids.remove(asteroid)
                        if shot.playerShot():
                            self._score += asteroid.size() * self.ASTEROID_SCORE
                            self._ufo_countdown -= asteroid.size() * self.ASTEROID_SCORE
                        # If the asteroid was size 1 or 2, spawn 3 smaller ones
                        if asteroid.size() < 3:
                            for _ in range(3):
                                rock = Asteroid(self.ASTEROID_SCALE, asteroid.position().x, asteroid.position().y,
                                                asteroid.size()+1, asteroid.velocity(), True, asteroid.direction(), 
                                                random.randrange(-30, 31))
                                self._asteroids.append(rock)
                # If the UFO is active, check for a shot hitting it.
                if shot in self._shots and self._ufo != None and shot.playerShot():
                    dist = shot.position().distanceTo(self._ufo.position())
                    if dist < shot.radius() + self._ufo.radius():
                        self._shots.remove(shot)
                        self._score += self._ufo.size() * self.UFO_SCORE
                        self._ufo_countdown = self.UFO_INTERVAL
                        self._ufo = None
                elif shot in self._shots and self._ship != None and not shot.playerShot():
                    # See if a UFO shot hits the player
                    dist = shot.position().distanceTo(self._ship.position())  
                    if dist < self._ship.radius() + shot.radius():
                        self._shots.remove(shot)
                        self._ship = None
                        self._lives.pop(-1)
                        self._spawn_countdown = self.SPAWN_INTERVAL
                        break
                
                # Check and remove shots that are out of range
                if shot in self._shots and shot.max_range():
                    self._shots.remove(shot)   
                    
            # Then do player ship with asteroids
            if self._ship != None and asteroid in self._asteroids:
                dist = self._ship.position().distanceTo(asteroid.position())
                if dist < self._ship.radius() + asteroid.radius():
                    self._ship = None
                    self._lives.pop(-1)
                    self._spawn_countdown = self.SPAWN_INTERVAL
                    # If the asteroid was size 1 or 2, spawn 3 smaller ones
                    if asteroid.size() < 3:
                        for _ in range(3):
                            rock = Asteroid(self.ASTEROID_SCALE, asteroid.position().x, asteroid.position().y,
                                            asteroid.size()+1, asteroid.velocity(), True, asteroid.direction(), 
                                            random.randrange(-30, 31))
                            self._asteroids.append(rock)
                    break
                
    def run(self):
        # Loop until the player is out of lives
        while len(self._lives) > 0:
            # If there's no ship, spawn one at the end of the countdown
            if self._ship == None:
                if self._spawn_countdown <= 0:
                    self._spawn_ship()
                else:
                    self._spawn_countdown -= self.FRAME_LENGTH
            
            # If it's time to spawn a new UFO, spawn it
            if self._ufo == None and self._ufo_countdown <= 0:
                self._spawn_ufo()
            
            # Draw the next frame
            self._draw()
            # Check for keyboard input
            self._player_input();
            # Move stuff
                    
            if len(self._asteroids) == 0:
                # Spawn some asteroids
                self._spawn_asteroids()
                
            for asteroid in self._asteroids:
                asteroid.move(self.SCREEN_WIDTH, self.SCREEN_HEIGHT)
                
            for shot in self._shots:
                shot.move(self.SCREEN_WIDTH, self.SCREEN_HEIGHT)
             
            if self._ship != None:         
                self._ship.move(self.SCREEN_WIDTH, self.SCREEN_HEIGHT)
                
            if self._ufo != None:         
                onscreen = self._ufo.move(self.SCREEN_WIDTH, self.SCREEN_HEIGHT)
                # Let the UFO shoot if it's ready
                shot = self._ufo.shoot(self.SCREEN_WIDTH, self.SCREEN_HEIGHT)
                if shot != None:
                    self._shots.append(shot)
                # Remove the UFO if it has left the screen
                if not onscreen:
                    self._ufo = None
                    self._ufo_countdown = self.UFO_INTERVAL
                    
            self._collide()
            
            if self._score > self._next_new_life:
                self._add_life()
                self._next_new_life += self.EXTRA_LIFE_INTERVAL
        
        # Show the end game dialog 
        self._draw()   
        self._endGameDialog()        

def main():
    """
    Main program entry point.  Creates the game object and runs it.
    """
    game = Asteroids()
    game.run()    
    
if __name__ == '__main__':
    main()
    