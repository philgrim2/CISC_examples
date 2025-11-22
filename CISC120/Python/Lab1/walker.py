import stdio
import random
import sys

n = int(sys.argv[1])

# Initialize position and count
x = 0
y = 0
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
        
    match r:
        case 0:
            y = y + 1
        case 1:
            x = x + 1
        case 2:
            y = y - 1
        case 3:
            x = x - 1
            
    # Update the count
    c = c + 1    
        
stdio.write('The walker took ')
stdio.write(c)
stdio.writeln(' steps.')