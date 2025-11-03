import stddraw
import stdio
import random

WINNER = None                                   # Your Comment Here
UPPER_PDL_xLIM = 16                             # Your Comment Here
UPPER_PDL_yLIM = 9                              # Your Comment Here
LOWER_PDL_xLIM = 0                              # Your Comment Here
LOWER_PDL_yLIM = 0                              # Your Comment Here
PDL_VEL = 0.5                                   # Your Comment Here 
BALL_xVEL = 0.0032 * random.randrange(-1,2,2)   # Your Comment Here
BALL_yVEL = 0.0018 * random.randrange(-1,2,2)   # Your Comment Here

MULT = 2
CHECK = 0
SPEED = 5

#Setting Up The Canvas
stddraw.setCanvasSize(1280,720)
stddraw.setXscale(LOWER_PDL_xLIM, UPPER_PDL_xLIM)
stddraw.setYscale(LOWER_PDL_yLIM, UPPER_PDL_yLIM)


#Drawing The Paddles and Ball
p1_pdl_x = 1
p1_pdl_y = 3.5

p2_pdl_x = 14.5
p2_pdl_y = 3.5

pdl_w = 0.5
pdl_h = 2.0

ball_x = UPPER_PDL_xLIM/2 
ball_y = UPPER_PDL_yLIM/2

stddraw.filledRectangle(p1_pdl_x, p1_pdl_y, pdl_w, pdl_h)
stddraw.filledRectangle(p2_pdl_x, p2_pdl_y, pdl_w, pdl_h)
stddraw.filledCircle(ball_x,ball_y,.125)

#Constructing The Main Loop
while WINNER is None:
	stddraw.clear()
	if stddraw.hasNextKeyTyped():                                                                   #Taking User Input
		key = stddraw.nextKeyTyped()                                                            #Taking User Input
		if key == 'q' and (p1_pdl_y + pdl_h + 0.5) <= UPPER_PDL_yLIM: p1_pdl_y += PDL_VEL       #Taking User Input
		elif key == 'a' and (p1_pdl_y - 0.5) >= LOWER_PDL_yLIM: p1_pdl_y -= PDL_VEL             #Taking User Input
		elif key == ']' and (p2_pdl_y + pdl_h + 0.5) <= UPPER_PDL_yLIM: p2_pdl_y += PDL_VEL     #Taking User Input
		elif key == '\'' and (p2_pdl_y - 0.5) >= LOWER_PDL_yLIM: p2_pdl_y -= PDL_VEL            #Taking User Input
	stddraw.filledRectangle(p1_pdl_x, p1_pdl_y, pdl_w, pdl_h)
	stddraw.filledRectangle(p2_pdl_x, p2_pdl_y, pdl_w, pdl_h)
	
	
	BALL_PDL1_CHECK = (p1_pdl_x+pdl_w) == round(ball_x,2) and p1_pdl_y <= round(ball_y,2) and (p1_pdl_y+pdl_h) >= round(ball_y,2)
	BALL_PDL2_CHECK = p2_pdl_x == round(ball_x,2) and p2_pdl_y <= round(ball_y,2) and (p2_pdl_y+pdl_h) >= round(ball_y,2)
	
	
	if BALL_PDL1_CHECK or BALL_PDL2_CHECK:
		BALL_xVEL = -BALL_xVEL
		ball_x += BALL_xVEL
		CHECK += 1
		if CHECK >= SPEED:
                        BALL_xVEL *= MULT
                        BALL_yVEL *= MULT
	elif ball_x <= UPPER_PDL_xLIM and ball_x >= LOWER_PDL_xLIM:
		ball_x += BALL_xVEL
	else:
                WINNER = "P1"
                if ball_x < UPPER_PDL_xLIM//2:
                        WINNER = "P2"
		
	if ball_y <= UPPER_PDL_yLIM and ball_y >= LOWER_PDL_yLIM:
		ball_y += BALL_yVEL
	else:
		BALL_yVEL = -BALL_yVEL
		ball_y += BALL_yVEL
	stddraw.filledCircle(ball_x,ball_y,.125)
	stddraw.show(0)

stddraw.setFontSize(32)
stddraw.text(UPPER_PDL_xLIM//2, UPPER_PDL_yLIM//2, WINNER + " Wins")
stddraw.show()
