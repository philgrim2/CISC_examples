import stdio
import random
import sys

n = int(sys.argv[1])
m = int(sys.argv[2]) # the number of simulations to average over

i = 0 # iterator for our counting loop
total_steps = 0 # the total number of steps over all runs

while i < m: # run the simulation m times
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
    
        # Update the count
        c = c + 1 

    stdio.write('The walker took ')
    stdio.write(c)
    stdio.writeln(' steps.')
    total_steps += c # add this simulation to the running total

    i += 1 # Increment the iterator

average_steps = total_steps / m
# Calculate the average
stdio.write('The average number of steps was:\t')
stdio.writeln(average_steps)