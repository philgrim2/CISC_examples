#-----------------------------------------------------------------------
# flip.py
#-----------------------------------------------------------------------

import stddraw
import sys
from picture import Picture

#-----------------------------------------------------------------------

# Accept the name of a JPG or PNG image file, read an image from the
# file, and display the image flipped horizontally.

fileName = sys.argv[1]


source = Picture(fileName)
w = source.width()
h = source.height()
target = Picture(w, h)

for tCol in range(w):
    for tRow in range(h):
        sCol = tCol 
        sRow = (h - 1) - tRow 
        target.set(tCol, tRow, source.get(sCol, sRow))

stddraw.setCanvasSize(w, h)
stddraw.picture(target)
stddraw.show()

#-----------------------------------------------------------------------

# python flip.py kitty.jpeg



