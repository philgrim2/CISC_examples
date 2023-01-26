import os
os.environ['PYGAME_HIDE_SUPPORT_PROMPT'] = "hide"
import stdio
import stddraw
import sys
from picture import Picture
from color import Color

def read():
    """
    Reads raw pixel information from standard input and creates a Picture object
    from those pixels.  The expected format is:
    
    w h
    r g b r g b r g b r g b
    r g b r g b r g b r g b
    r g b r g b r g b r g b
    r g b r g b r g b r g b
    
    where w, h, r, g, and b are integer values.
    
    """
    width = stdio.readInt()
    height = stdio.readInt()
    
    picture = Picture(width, height)
    
    for row in range(height):
        for column in range(width):
            r = stdio.readInt()
            g = stdio.readInt()
            b = stdio.readInt()
            pixel = Color(r, g, b)
            picture.set(column, row, pixel)
            
    return picture

def write(picture):
    """
    Reads raw pixel information from a Picture object and writes the data to standard 
    output.  The format is:
    
    w h
    r g b r g b r g b r g b
    r g b r g b r g b r g b
    r g b r g b r g b r g b
    r g b r g b r g b r g b
    
    where w, h, r, g, and b are integer values.
    
    """
    stdio.writef("%d %d\n", picture.width(), picture.height())
    
    for row in range(picture.height()):
        for column in range(picture.width()):
            pixel = picture.get(column, row)
            stdio.writef("%d %d %d ", pixel.getRed(), pixel.getGreen(), pixel.getBlue())
        stdio.writeln()
        
def _testRead():
    picture = read()
    stddraw.setCanvasSize(picture.width(), picture.height())
    stddraw.picture(picture)
    stddraw.show()
    
def _testWrite():
    
    picture = Picture(sys.argv[2])
    write(picture)
    
def usage():
    stdio.writeln("Usage:  python rawpicture.py [ read  | write <filename> ] ")
    stdio.writeln()
    stdio.writeln("  Tests the rawpicture module functionality")
                  
def main():
    """
    Test driver
    """
    if len(sys.argv) == 2 and sys.argv[1] == '--read':
        _testRead()
    elif len(sys.argv) == 3 and sys.argv[1] == '--write':
        _testWrite()
    else:
        usage()
        
    
if __name__ == '__main__':
    main()
