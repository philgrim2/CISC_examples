import stddraw
import stdio
import random
import sys

AREA_SIZE = 500

n = int(sys.argv[1])

# Draw experiment
stddraw.setCanvasSize(AREA_SIZE, AREA_SIZE)
stddraw.setXscale(-n,n)
stddraw.setYscale(-n,n)

stddraw.rectangle(-n, -n, 2 * n, 2 * n)
stddraw.line(n,0,-n,0)
stddraw.line(0,-n,0,n)

h = -n
while h < n:
    w = -n
    while w < n:
        stddraw.point(w, h)
        w = w + 1
    h = h + 1

stddraw.show(1000)
stddraw.setPenColor(stddraw.RED)
stddraw.setPenRadius(0.025)
# Initialize position and count
x = 0
y = 0

oldx = 0
oldy = 0

c = 0

# Loop while the walker has not reached a boundary
while -n < x < n and -n < y < n:
    # Decide direction:
    # 0 = North
    # 1 = East
    # 2 = South
    # 3 = West
    r = random.randint(0, 3)
    
    # Move the walker's position
    if r == 0:
        y = y + 1
    elif r == 1:
        x = x + 1
    elif r == 2:
        y = y - 1
    elif r == 3:
        x = x - 1
    
    # Update the count
    c = c + 1
    
    # Draw the line
    stddraw.line(oldx, oldy, x, y)
    oldx = x
    oldy = y
    stddraw.show(1000)
     

stdio.write('The walker took ')
stdio.write(c)
stdio.writeln(' steps.')
stddraw.show()