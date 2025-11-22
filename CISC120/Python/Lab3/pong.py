import stddraw
import stdio
import random

UPPER_PDL_xLIM = 16                             # Your Comment Here
UPPER_PDL_yLIM = 9                              # Your Comment Here
LOWER_PDL_xLIM = 0                              # Your Comment Here
LOWER_PDL_yLIM = 0                              # Your Comment Here

P1_PDL_X = 1
P2_PDL_X = 14.5
PDL_W = 0.5
PDL_H = 2.0
BALL_RADIUS = .125

PDL_VEL = 0.5                                   # Your Comment Here 


MULT = 2
SPEED = 5

def draw(p1_pdl_y, p2_pdl_y, ball_x, ball_y):
    """
    Draws The Paddles and Ball
    """
    stddraw.clear()
    stddraw.filledRectangle(p1_pdl_x, p1_pdl_y, pdl_w, pdl_h)
    stddraw.filledRectangle(p2_pdl_x, p2_pdl_y, pdl_w, pdl_h)
    stddraw.filledCircle(ball_x,ball_y,.125)
    
def paddle_input(p1_pdl_y, p2_pdl_y):
    """
    Check for player input and move paddles accordingly.
    """
    if stddraw.hasNextKeyTyped():                                                                 
        key = stddraw.nextKeyTyped()                                                           
        if key == 'q' and (p1_pdl_y + PDL_H + 0.5) <= UPPER_PDL_yLIM: p1_pdl_y += PDL_VEL       
        elif key == 'a' and (p1_pdl_y - 0.5) >= LOWER_PDL_yLIM: p1_pdl_y -= PDL_VEL             
        elif key == ']' and (p2_pdl_y + PDL_H + 0.5) <= UPPER_PDL_yLIM: p2_pdl_y += PDL_VEL     
        elif key == '\'' and (p2_pdl_y - 0.5) >= LOWER_PDL_yLIM: p2_pdl_y -= PDL_VEL            
      
    return (p1_pdl_y, p2_pdl_y)

def check_collide(ball_x, ball_y, p1_pdl_y, p2_pdl_y, ball_xvel, ball_yvel, check):
    """
    Check for ball colliding with top and bottom of screen, or with paddles, and
    reverse direction when that happens.
    """
    if ball_y >= UPPER_PDL_yLIM or ball_y <= LOWER_PDL_yLIM:
        ball_yvel = -ball_yvel
        
    p1check = (P1_PDL_X+PDL_W) == round(ball_x,2) and p1_pdl_y <= round(ball_y,2) and (p1_pdl_y+PDL_H) >= round(ball_y,2)
    p2check = P2_PDL_X == round(ball_x,2) and p2_pdl_y <= round(ball_y,2) and (p2_pdl_y+PDL_H) >= round(ball_y,2)

    if p1check or p2check:
        ball_xvel = -ball_xvel
        check += 1 
    return (ball_xvel, ball_yvel, check)

def move_ball(ball_x, ball_y, ball_xvel, ball_yvel, check):
    if check >= SPEED:
        ball_xvel *= MULT
        ball_yvel *= MULT
    ball_x += ball_xvel
    ball_y += ball_yvel
    
    return (ball_x, ball_y, ball_xvel, ball_yvel)
    
def check_winner(ball_x):
    if ball_x <= LOWER_PDL_xLIM:
        return "P2"
    elif ball_x >= UPPER_PDL_xLIM:
        return "P1"
    else:
        return None
    
def main():
    #Setting Up The Canvas
    stddraw.setCanvasSize(1280,720)
    stddraw.setXscale(LOWER_PDL_xLIM, UPPER_PDL_xLIM)
    stddraw.setYscale(LOWER_PDL_yLIM, UPPER_PDL_yLIM)
    
    # Set initial ball velocity randomly
    ball_xvel = 0.0032 * random.randrange(-1,2,2)   
    ball_yvel = 0.0018 * random.randrange(-1,2,2)   
    check = 0
    
    # Set paddles to center
    p1_pdl_y = 3.5
    p2_pdl_y = 3.5

    # Set ball to center
    ball_x = UPPER_PDL_xLIM/2 
    ball_y = UPPER_PDL_yLIM/2
    
    # Draw initial screen
    draw(p1_pdl_y, p2_pdl_y, ball_x, ball_y)

    winner = None  
    #Constructing The Main Loop
    while winner is None:
        # Check for player input and move paddles
        p1_pdl_y, p2_pdl_y = paddle_input(p1_pdl_y, p2_pdl_y)
        
        # Move ball
        ball_x, ball_y, ball_xvel, ball_yvel = move_ball(ball_x, ball_y, ball_xvel, ball_yvel, check)
        
        # Check for ball collisions
        ball_xvel, ball_yvel, check = check_collision(ball_x, ball_y, p1_pdl_y, 
                                                      p2_pdl_y, ball_xvel, ball_yvel, check)

        # Check for ball reaching edge
        winner = check_winner(ballx)
        
        draw(p1_pdl_y, p2_pdl_y, ball_x, ball_y)
        stddraw.show(0)

    stddraw.setFontSize(32)
    stddraw.text(UPPER_PDL_xLIM//2, UPPER_PDL_yLIM//2, WINNER + " Wins")
    stddraw.show()

if __name__ == '__main__':
    main()





