"""
breakout.py, by Phil Grim

Simple implementation of the original arcade game Breakout.
Player controls include:
   s:  Serve the ball
   a:  Move the paddle left
   d:  Move the paddle right
   
Requires the textbook libraries from 'Introduction to Programming in Python: An Interdisciplinary Approach, 1st ed.'
by Sedgewick, Wayne, and Dondero, which can be found on the book's website at 
https://introcs.cs.princeton.edu/python/code/introcs-python.zip

Outstanding issues:
  Collision detection with bricks and paddles can be wonky, and could be optimized better
  Key repeat doesn't work in stddraw
  
Possible improvements:
  Add 'english' to the ball/paddle interaction
  Add sound (stdaudio is kinda bad...)
  
Possible practical uses:
  As a lab assignment/take-home exam for students to implement
  As a lab assignment for students to convert from structured to object oriented 
  
"""

import random
import stdarray
import stddraw
import pygame

# Set up the canvas size
SCREEN_WIDTH = 700
SCREEN_HEIGHT = 980

# Set the size of the playing field sidelines
SIDELINE_SIZE = 20

# Set the size of the paddles
PADDLE_HEIGHT = 20
# Scale paddle width with screen
PADDLE_WIDTH = 25 + SCREEN_WIDTH // 7
# Also set the offset of the paddles from the bottom of the playing field
PADDLE_OFFSET = 30
# Paddle movement speed
PADDLE_DELTA_X = 10

# Number of rows of bricks
BRICK_ROWS = 6
# Number of columns of bricks
BRICK_COLS=8
# Margin between bricks
BRICK_MARGIN = 8
# Set the size of the bricks
BRICK_HEIGHT = 20
# Scale brick width with screen
BRICK_WIDTH = ((SCREEN_WIDTH - (SIDELINE_SIZE * 2)) // BRICK_COLS) - BRICK_MARGIN

# Set the size of the ball
BALL_SIZE = 20

# Set a scaled font size for dialog boxes
DIALOG_FONT_SIZE = SCREEN_HEIGHT // 32
# Set a scaled font size for score output
SCORE_FONT_SIZE = PADDLE_OFFSET - BRICK_MARGIN

# Set the time for each frame in milliseconds
FRAME_LENGTH = 10
# Number of lives for the player
MAX_LIVES = 3
    
def endGameDialog(score):
    """
    Displays a dialog box that shows the game over message, then waits for a keypress.
    """
    stddraw.setFontSize(DIALOG_FONT_SIZE)
    stddraw.rectangle(SCREEN_WIDTH * 0.25, SCREEN_HEIGHT * 0.10, SCREEN_WIDTH * 0.5, SCREEN_HEIGHT * 0.20)
    stddraw.text(SCREEN_WIDTH / 2, SCREEN_HEIGHT * 0.25, 'Game Over')
    stddraw.text(SCREEN_WIDTH / 2, SCREEN_HEIGHT * 0.15, f'Final Score: {score}')
    moreElectricity = True
    while moreElectricity:
        stddraw.show(10)
        if stddraw.hasNextKeyTyped():
            moreElectricity = False  

def randomServe():
    """
    Chooses a random direction for the ball service.
    """
    xvel = random.randint(2,3)
    which = random.randint(1,2)
    if which == 2:
        xvel = -xvel
     
    yvel = random.randint(2,3)
        
    return xvel, yvel

def checkLife(ballY, lives):
    """
    Checks to see if the center of the ball has reached the bottom of the 
    playing field, and if so, deducts a life
    """
    ballRadius = BALL_SIZE // 2
    if ballY + ballRadius  < 0:
        return lives - 1
    else:
        return lives 

def draw(paddle_x, paddle_y, ball_x, ball_y, bricks, lives, score, serve):
    """
    Draws the playing field, ball, and paddles.
    """
    #Set the background and drawing colors
    stddraw.clear(stddraw.BLACK)
    stddraw.setPenColor(stddraw.LIGHT_GRAY)
    
    #Draw the sidelines
    stddraw.filledRectangle(0, SCREEN_HEIGHT - SIDELINE_SIZE, SCREEN_WIDTH, SIDELINE_SIZE)
    stddraw.filledRectangle(0, 0, SIDELINE_SIZE, SCREEN_HEIGHT)
    stddraw.filledRectangle(SCREEN_WIDTH - SIDELINE_SIZE, 0, SIDELINE_SIZE, SCREEN_HEIGHT)
    
    #Draw the paddle
    pllx = paddle_x - PADDLE_WIDTH // 2
    plly = paddle_y - PADDLE_HEIGHT // 2
    stddraw.filledRectangle(pllx, plly, PADDLE_WIDTH, PADDLE_HEIGHT)
    
    # Draw the ball.
    ballRadius = BALL_SIZE // 2
    stddraw.filledCircle(ball_x, ball_y, ballRadius)
    
    # Draw the score
    stddraw.setFontSize(SCORE_FONT_SIZE)
    stddraw.text(SIDELINE_SIZE + SCORE_FONT_SIZE * 2, PADDLE_OFFSET // 2, f'Score: {score}')
    
    lifeOffset = SIDELINE_SIZE + SCORE_FONT_SIZE
    # Draw the lives indicator
    for spot in range(1,MAX_LIVES + 1):
        if lives > spot:
            stddraw.filledCircle(SCREEN_WIDTH - lifeOffset, PADDLE_OFFSET // 2, ballRadius)
        else:
            stddraw.circle(SCREEN_WIDTH - lifeOffset, PADDLE_OFFSET // 2, ballRadius)
        lifeOffset += BALL_SIZE + BRICK_MARGIN
       
       
    # Draw bricks
    row_y_offset = SCREEN_HEIGHT - (SIDELINE_SIZE + PADDLE_OFFSET + BRICK_HEIGHT)
    row_x_offset_left = SIDELINE_SIZE + (BRICK_MARGIN // 2) + (
        (SCREEN_WIDTH - (2 * SIDELINE_SIZE) - (BRICK_COLS * BRICK_WIDTH) - (BRICK_COLS * BRICK_MARGIN)) // 2)
    for row in bricks:
        row_x_offset = row_x_offset_left
        for column in row:
            if (column):
                stddraw.filledRectangle(row_x_offset, row_y_offset, BRICK_WIDTH, BRICK_HEIGHT)
            row_x_offset += BRICK_WIDTH + BRICK_MARGIN
        row_y_offset -= BRICK_HEIGHT + BRICK_MARGIN
    
    # If we're waiting for a serve, draw the dialog
    if serve:
        stddraw.setFontSize(DIALOG_FONT_SIZE)
        stddraw.rectangle(SCREEN_WIDTH * 0.33, SCREEN_HEIGHT * 0.10, SCREEN_WIDTH * 0.33, SCREEN_HEIGHT * 0.10)
        stddraw.text(SCREEN_WIDTH // 2, SCREEN_HEIGHT * 0.15, "Press S to serve.")
    
    #Update screen
    stddraw.show(FRAME_LENGTH)
    
def player_input(paddle_x, ball_x, ball_xvel, ball_yvel):
    """
    Check for key input, and move the paddle if there is any.
    If the ball isn't moving, move it with the paddle.
    Serve if needed
    """
    if stddraw.hasNextKeyTyped():
            key = stddraw.nextKeyTyped().lower()
            
            #
            # Even though we've moved to calling Pygame directly for the direction
            # keys, we're staying with stddraw for the serve function.  We don't
            # need key repeat for this, and it will keep stddraw's key buffer from
            # getting overwhelmed.
            if key == 's' and ball_xvel == 0 and ball_yvel == 0:
                # Serve the ball
                ball_xvel, ball_yvel = randomServe()
            #
            # The following is commented out so that we can use Pygame directly
            # for the direction keys.  This will give us key repeat (player can
            # hold down the key to move continuously.
            #
            
            #if key == 'a':
            #    # Move paddle to the left, but prevent it from going off the screen
            #    paddle_x = paddle_x - PADDLE_DELTA_X
            #    if paddle_x < SIDELINE_SIZE + PADDLE_WIDTH // 2:
            #        paddle_x = SIDELINE_SIZE + PADDLE_WIDTH // 2
            #elif key == 'd':
            #    # Move paddle to the left, but prevent it from going off the screen
            #    paddle_x = paddle_x + PADDLE_DELTA_X
            #    if paddle_x > SCREEN_WIDTH - (SIDELINE_SIZE + PADDLE_WIDTH // 2):
            #        paddle_x = SCREEN_WIDTH - (SIDELINE_SIZE + PADDLE_WIDTH // 2)
            #elif key == 's' and ball_xvel == 0 and ball_yvel == 0:
            #    # Serve the ball
            #    ball_xvel, ball_yvel = randomServe()
            
    # Cheat a little for key repeat - use Pygame directly
    is_key_pressed = pygame.key.get_pressed()
    if is_key_pressed[pygame.K_a]:
        # Move paddle to the left, but prevent it from going off the screen
        paddle_x = paddle_x - PADDLE_DELTA_X
        if paddle_x < SIDELINE_SIZE + PADDLE_WIDTH // 2:
            paddle_x = SIDELINE_SIZE + PADDLE_WIDTH // 2
    elif is_key_pressed[pygame.K_d]:
        # Move paddle to the left, but prevent it from going off the screen
        paddle_x = paddle_x + PADDLE_DELTA_X
        if paddle_x > SCREEN_WIDTH - (SIDELINE_SIZE + PADDLE_WIDTH // 2):
            paddle_x = SCREEN_WIDTH - (SIDELINE_SIZE + PADDLE_WIDTH // 2)
            
    if ball_xvel == 0 and ball_yvel == 0:
        ball_x = paddle_x
            
    return (paddle_x, ball_x, ball_xvel, ball_yvel)

def count(bricks):
    """
    Count remaining bricks.
    """
    sumb = 0
    for row in bricks:
        sumb += sum(row)
    return sumb

def ball(ball_x, ball_y, ball_xvel, ball_yvel, paddle_x, paddle_y, bricks, score):
    """
    Updates the position of the ball based on the current x and y velocities,
    and checks for collisions.
    """
    #Move the ball
    ball_x += ball_xvel
    ball_y += ball_yvel
    
    #Now check for wall and top collision
    newDeltaX = ball_xvel
    newDeltaY = ball_yvel
    ballRadius = BALL_SIZE // 2
    
    # Check top
    if ball_y + ballRadius >= SCREEN_HEIGHT - SIDELINE_SIZE:
        newDeltaY = -newDeltaY
        
    # Check left and right
    if ball_x - ballRadius <= SIDELINE_SIZE or ball_x + ballRadius >= SCREEN_WIDTH - SIDELINE_SIZE:
        newDeltaX = -newDeltaX
        
    # Check for paddle
    if (ball_x - ballRadius >= paddle_x - PADDLE_WIDTH // 2 and 
        ball_x + ballRadius <= paddle_x + PADDLE_WIDTH // 2 and 
        ball_y - ballRadius <= paddle_y + PADDLE_HEIGHT // 2 and 
        ball_y - ballRadius >= paddle_y):
        newDeltaY = -newDeltaY  
    
    #Finally check for brick collision
    #Can probably optimize this
    row_y_offset = SCREEN_HEIGHT - (SIDELINE_SIZE + PADDLE_OFFSET + BRICK_HEIGHT)
    row_x_offset_left = SIDELINE_SIZE + (BRICK_MARGIN // 2) + (
        (SCREEN_WIDTH - (2 * SIDELINE_SIZE) - (BRICK_COLS * BRICK_WIDTH) - (BRICK_COLS * BRICK_MARGIN)) // 2)
    for row in bricks:
        row_x_offset = row_x_offset_left
        for brick in range(len(row)):
            # Skip if missing
            if row[brick]:
                #Top
                if (ball_x - ballRadius >= row_x_offset and 
                    ball_x + ballRadius <= row_x_offset + BRICK_WIDTH and 
                    ball_y - ballRadius <= row_y_offset + BRICK_HEIGHT and 
                    ball_y - ballRadius >= row_y_offset + BRICK_HEIGHT - BRICK_MARGIN):
                    newDeltaY = -newDeltaY
                    row[brick] = False 
                    score += 1
                #Bottom
                elif (ball_x - ballRadius >= row_x_offset and 
                      ball_x + ballRadius <= row_x_offset + BRICK_WIDTH and 
                      ball_y + ballRadius <= row_y_offset + BRICK_MARGIN and 
                      ball_y + ballRadius >= row_y_offset):
                    newDeltaY = -newDeltaY 
                    row[brick] = False 
                    score += 1
                #Left
                elif (ball_x + ballRadius >= row_x_offset and 
                      ball_x + ballRadius <= row_x_offset + BRICK_MARGIN and 
                      ball_y <= row_y_offset + BRICK_HEIGHT and 
                      ball_y >= row_y_offset):
                    newDeltaX = -newDeltaX 
                    row[brick] = False 
                    score += 1    
                #Right
                elif (ball_x - ballRadius <= row_x_offset + BRICK_WIDTH and 
                      ball_x - ballRadius >= row_x_offset + BRICK_WIDTH - BRICK_MARGIN and 
                      ball_y <= row_y_offset + BRICK_HEIGHT and 
                      ball_y >= row_y_offset):
                    newDeltaX = -newDeltaX 
                    row[brick] = False 
                    score += 1    
                
            
            
            row_x_offset += BRICK_WIDTH + BRICK_MARGIN
        row_y_offset -= BRICK_HEIGHT + BRICK_MARGIN

    return (ball_x, ball_y, newDeltaX, newDeltaY, score)

def main():
    """
    Main program loop.  Sets up the initial values, and then loops until the ball escapes the screen.
    """
    
    # Set up the size of the canvas and the drawing scale.  
    # The scale will be set to the same as the size, so that drawing is done in pixels directly.
    stddraw.setCanvasSize(SCREEN_WIDTH, SCREEN_HEIGHT)
    stddraw.setXscale(0, SCREEN_WIDTH)
    stddraw.setYscale(0, SCREEN_HEIGHT)
    
    #Initialize game variables
    #Array to hold bricks
    bricks = stdarray.create2D(BRICK_ROWS, BRICK_COLS, True)
    #Horizontal location of the center of the paddle
    paddle_x = SCREEN_WIDTH // 2
    paddle_y = PADDLE_OFFSET + PADDLE_HEIGHT // 2
    #Location of the ball
    ball_x = SCREEN_WIDTH // 2
    ball_y = PADDLE_OFFSET + PADDLE_HEIGHT + (BALL_SIZE // 2)
    #Velocity of the ball
    ball_xvel = 0
    ball_yvel = 0
    #Lives left
    lives = MAX_LIVES
    score = 0
    
    #Define the brick area for later
    brick_limit = SIDELINE_SIZE + PADDLE_OFFSET + (BRICK_ROWS * (BRICK_HEIGHT + BRICK_MARGIN))
    
    # Draw the screen, then show the serve dialog.
    draw(paddle_x, paddle_y, ball_x, ball_y, bricks, lives, score, True)
    
    # Loop until the ball escapes the screen, which we will signify by returning True from the escape
    # detection function.
    while lives > 0:
        #Draw screen
        draw(paddle_x, paddle_y, ball_x, ball_y, bricks, lives, score, (ball_xvel == 0 and ball_yvel == 0))        
        #Check and move paddle
        paddle_x, ball_x, ball_xvel, ball_yvel = player_input(paddle_x, ball_x, ball_xvel, ball_yvel)
        #Move ball and check collisions
        ball_x, ball_y, ball_xvel, ball_yvel, score = ball(ball_x, ball_y, ball_xvel, ball_yvel, paddle_x, paddle_y, bricks, score)
        #Check to see if all of the bricks are gone, and if they are, and the ball is below the brick area, 
        #make new bricks       
        if count(bricks) == 0 and ball_y < brick_limit:
            bricks = stdarray.create2D(BRICK_ROWS, BRICK_COLS, True)
            # Also speed up the ball a little
            if ball_xvel > 0: ball_xvel += 1
            else: ball_xvel -= 1 
            if ball_yvel > 0: ball_yvel += 1
            else: ball_yvel -= 1
        #Check to see if a life is lost.  If so, reset the ball
        checkedLife = checkLife(ball_y, lives)
        if checkedLife < lives:
            #Reset location of the ball
            ball_x = paddle_x
            ball_y = PADDLE_OFFSET + PADDLE_HEIGHT + (BALL_SIZE // 2)
            #Reset velocity of the ball
            ball_xvel = 0
            ball_yvel = 0
            #Lose the life
            lives = checkedLife

    endGameDialog(score)
    
if __name__ == '__main__':
    main()
    