#-----------------------------------------------------------------------
# alberssquares.py
#-----------------------------------------------------------------------

import sys
import stddraw
from color import Color

#-----------------------------------------------------------------------

# Modified for exercise 3.1.3:  Accept nine parameters, and display six
# squares in all combinations of colors.

r1 = int(sys.argv[1])
g1 = int(sys.argv[2])
b1 = int(sys.argv[3])
c1 = Color(r1, g1, b1)

r2 = int(sys.argv[4])
g2 = int(sys.argv[5])
b2 = int(sys.argv[6])
c2 = Color(r2, g2, b2)

r3 = int(sys.argv[7])
g3 = int(sys.argv[8])
b3 = int(sys.argv[9])
c3 = Color(r3, g3, b3)

stddraw.setCanvasSize(768, 512)
stddraw.setYscale(.2, .8)

# Drawing large squares left to right
stddraw.setPenColor(c1)
stddraw.filledSquare(.25, .33, .1)
stddraw.filledSquare(.25, .66, .1)

stddraw.setPenColor(c2)
stddraw.filledSquare(.5, .33, .1)
stddraw.filledSquare(.5, .66, .1)

stddraw.setPenColor(c3)
stddraw.filledSquare(.75, .33, .1)
stddraw.filledSquare(.75, .66, .1)

#Drawing small squares from left to right
stddraw.setPenColor(c2)
stddraw.filledSquare(.25, .33, .05)
stddraw.setPenColor(c3)
stddraw.filledSquare(.25, .66, .05)

stddraw.setPenColor(c1)
stddraw.filledSquare(.5, .33, .05)
stddraw.setPenColor(c3)
stddraw.filledSquare(.5, .66, .05)

stddraw.setPenColor(c1)
stddraw.filledSquare(.75, .33, .05)
stddraw.setPenColor(c2)
stddraw.filledSquare(.75, .66, .05)

stddraw.show()

#python .\alberssquares.py 123 3 234 76 35 76 222 3 222

