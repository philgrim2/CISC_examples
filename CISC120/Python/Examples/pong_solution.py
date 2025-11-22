import random
import stddraw

# Set up the canvas size
SCREEN_WIDTH = 1024
SCREEN_HEIGHT = 768

# Set the size of the playing field sidelines
SIDELINE_HEIGHT = 20

# Set the size of the paddles
PADDLE_WIDTH = 20
# Scale paddle height with screen
PADDLE_HEIGHT = 25 + SCREEN_HEIGHT // 7
# Also set the offset of the paddles from the edges of the playing field
PADDLE_OFFSET = 30
# Paddle movement speed
PADDLE_DELTA_Y = 30

# Set the size of the ball
BALL_SIZE = 20

# Set a scaled font size
FONT_SIZE = SCREEN_HEIGHT // 32

score = 0

def pressAnyKey():
    moreElectricity = True
    while moreElectricity:
        stddraw.show(10)
        if stddraw.hasNextKeyTyped():
            moreElectricity = False

def serveDialog():
    """
    Displays a dialog box that prompts for a keypress to start the game.  Allows players to 
    prepare rather than starting immediately.
    """
    stddraw.rectangle(SCREEN_WIDTH * 0.33, SCREEN_HEIGHT * 0.10, SCREEN_WIDTH * 0.33, SCREEN_HEIGHT * 0.10)
    stddraw.text(SCREEN_WIDTH / 2, SCREEN_HEIGHT * 0.15, "Press a key to serve.")
    pressAnyKey()

def endGameDialog(winner):
    """
    Displays a dialog box that shows the winner of the game.
    """
    stddraw.rectangle(SCREEN_WIDTH * 0.33, SCREEN_HEIGHT * 0.10, SCREEN_WIDTH * 0.33, SCREEN_HEIGHT * 0.10)
    stddraw.text(SCREEN_WIDTH / 2, SCREEN_HEIGHT * 0.15, winner + " Wins!")
    pressAnyKey()

def randomServe():
    """
    Chooses a random direction for the ball service.
    """
    xvel = random.randint(2,3)
    dir = random.randint(1,2)
    if dir == 2:
        xvel = -xvel
     
    yvel = random.randint(2,3)
    dir = random.randint(1,2)
    if dir == 2:
        yvel = -yvel
        
    return xvel, yvel


def draw(p1x, p1y, p2x, p2y, ballX, ballY):
    """
    Draws the playing field, ball, and paddles.
    """
 
    # Make the background black and the screen elements light grey,
    # to approximate the old Pong machines on a black and white TV.
    stddraw.clear(stddraw.BLACK)
    stddraw.setPenColor(stddraw.LIGHT_GRAY)
    
    # Draw the upper and lower boundaries
    stddraw.filledRectangle(0, 0, SCREEN_WIDTH, SIDELINE_HEIGHT)
    stddraw.filledRectangle(0, SCREEN_HEIGHT - SIDELINE_HEIGHT, SCREEN_WIDTH, SIDELINE_HEIGHT)
    
    # Draw the paddles
    p1lly = p1y - PADDLE_HEIGHT/2
    stddraw.filledRectangle(p1x, p1lly, PADDLE_WIDTH, PADDLE_HEIGHT)
    p2lly = p2y - PADDLE_HEIGHT/2
    p2llx = p2x - PADDLE_WIDTH
    stddraw.filledRectangle(p2llx, p2lly, PADDLE_WIDTH, PADDLE_HEIGHT)
    
    # Draw the ball.  Using a rectangle because that's what the old Pong looked like
    ballRadius = BALL_SIZE/2
    stddraw.filledRectangle(ballX - ballRadius, ballY - ballRadius, BALL_SIZE, BALL_SIZE)
    
    stddraw.show(10)

def moveBall(ballX, ballY, ballDeltaX, ballDeltaY):
    """
    Updates the position of the ball based on the current x and y velocities
    """
    return ballX + ballDeltaX, ballY + ballDeltaY

def setPaddlePosition(paddleY, delta):
    """
    Updates the position of a paddle, checking to be sure that the paddle doesn't go
    outside the playing field boundaries.  
    """
    pos = paddleY + delta
    if pos + PADDLE_HEIGHT/2 > SCREEN_HEIGHT - SIDELINE_HEIGHT:
        pos = SCREEN_HEIGHT - SIDELINE_HEIGHT - PADDLE_HEIGHT / 2
    elif pos < SIDELINE_HEIGHT + PADDLE_HEIGHT/2:
        pos = SIDELINE_HEIGHT + PADDLE_HEIGHT/2
    return pos

def rebound(ballX, ballY, p1Y, p2Y, ballDeltaX, ballDeltaY):
    """
    Determines if the ball has impacted the sidelines or paddles and
    updates the velocity accordingly.
    """
    global score
    newDeltaX = ballDeltaX
    newDeltaY = ballDeltaY
    
    ballRadius = BALL_SIZE/2
    
    # Check the bottom and top boundary
    if ballY - ballRadius <= SIDELINE_HEIGHT:
        newDeltaY = -newDeltaY
    elif ballY + ballRadius >= SCREEN_HEIGHT - SIDELINE_HEIGHT:
        newDeltaY = -newDeltaY
        
    # Check for hitting the paddle faces
    p1by = p1Y - PADDLE_HEIGHT/2
    p1ty = p1by + PADDLE_HEIGHT
    p1x = PADDLE_OFFSET + PADDLE_WIDTH
    if ballX - ballRadius <= p1x and ballY >= p1by and ballY <= p1ty:
        score += 1
        newDeltaX = -newDeltaX
        
    p2by = p2Y - PADDLE_HEIGHT/2
    p2ty = p2by + PADDLE_HEIGHT
    p2x = SCREEN_WIDTH - (PADDLE_OFFSET + PADDLE_WIDTH)
    if ballX + ballRadius >= p2x and ballY >= p2by and ballY <= p2ty:    
        score += 1
        newDeltaX = -newDeltaX
        
    return newDeltaX, newDeltaY

def gameOver(ballX):
    """
    Checks to see if the center of the ball has reached either edge of the 
    playing field
    """
    ballRadius = BALL_SIZE/2
    return ballX + ballRadius  < 0 or ballX - ballRadius > SCREEN_WIDTH


def main():
    """
    Main program loop.  Sets up the initial values, and then loops until the ball escapes the screen.
    """
    # Set up the size of the canvas and the drawing scale.  
    # The scale will be set to the same as the size, so that drawing is done in pixels directly.
    stddraw.setCanvasSize(SCREEN_WIDTH, SCREEN_HEIGHT)
    stddraw.setXscale(0, SCREEN_WIDTH)
    stddraw.setYscale(0, SCREEN_HEIGHT)
    stddraw.setFontSize(FONT_SIZE)
    
    # Set up variables to hold the locations of the game objects and initialize to the starting values:
    #   Ball should start in the center of the screen. 
    #   Ball initial velocity is random
    #   Paddles start in the center of their travel.
    ballX = SCREEN_WIDTH / 2
    ballY = SCREEN_HEIGHT / 2
    ballDeltaX, ballDeltaY = randomServe()
    p1x = PADDLE_OFFSET
    p1y = SCREEN_HEIGHT / 2
    p2x = SCREEN_WIDTH - PADDLE_OFFSET
    p2y = SCREEN_HEIGHT / 2
    
    # Draw the screen, then show the serve dialog.
    draw(p1x, p1y, p2x, p2y, ballX, ballY)
    serveDialog()
    
    # Loop until the ball escapes the screen, which we will signify by returning True from the escape
    # detection function.
    while not gameOver(ballX):
        # Draw the current state of the board
        draw(p1x, p1y, p2x, p2y, ballX, ballY)
        # Calculate the new position of the ball
        ballX, ballY = moveBall(ballX, ballY, ballDeltaX, ballDeltaY)
        # Check for control inputs and apply movement to paddles
        if stddraw.hasNextKeyTyped():
            key = stddraw.nextKeyTyped()
            if key == 'w':
                p1y = setPaddlePosition(p1y, PADDLE_DELTA_Y)
            elif key == 's':
                p1y = setPaddlePosition(p1y, -PADDLE_DELTA_Y)
            elif key == 'i':
                p2y = setPaddlePosition(p2y, PADDLE_DELTA_Y)
            elif key == 'k':
                p2y = setPaddlePosition(p2y, -PADDLE_DELTA_Y)
                
        # Check for ball rebounds
        ballDeltaX, ballDeltaY = rebound(ballX, ballY, p1y, p2y, ballDeltaX, ballDeltaY)
    
    # Game has ended.  Print out the winner
    winner = "Player 1"
    if ballX < SCREEN_WIDTH:
        winner = "Player 2"
    endGameDialog(winner)
    
    
if __name__ == "__main__":
    main()