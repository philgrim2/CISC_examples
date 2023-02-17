import math
import stdio
import stddraw
import sys
import color
from picture import Picture

def scale(source, factor):
    """
    Takes a Picture object containing source image, and a floating point scaling factor.
    Returns a new Picture object which is scaled to the new size.
    """
    hT = math.ceil(source.height() * factor)
    wT = math.ceil(source.width() * factor)
    target = Picture(wT, hT)
    
    for colT in range(wT):
        for rowT in range(hT):
            colS = colT * source.width() // wT
            rowS = rowT * source.height() // hT
            target.set(colT, rowT, source.get(colS, rowS))
    return target

def extract(source, width, height, centerXpos, centerYpos):
    """
    Extracts a portion of the source image with the specified width, centered
    at the given x and y coordinates, and returns it as a new Picture.
    """
    centerx = math.ceil(source.width() * centerXpos)
    centery = math.ceil(source.height() * (1 - centerYpos))
    
    xoffset = centerx - width // 2
    yoffset = centery - height // 2
    
    target = Picture(width, height)
    
    for colT in range(width):
        for rowT in range(height):
            colS = colT + xoffset
            rowS = rowT + yoffset
            if colS not in range(source.width()) or rowS not in range(source.height()):
                target.set(colT, rowT, color.BLACK)
            else:
                target.set(colT, rowT, source.get(colS, rowS))
            
    return target
    
def main():
    """
    Accepts four command line arguments - a string containing 
    the name of a picture file, floating point x and y percentages
    for the center point to zoom on, and a floating point
    number indicating a zoom factor to apply to the image.
    """

    fil = sys.argv[1]
    centerXpos = float(sys.argv[2])
    centerYpos = float(sys.argv[3])
    factor = float(sys.argv[4])
    source = Picture(fil)

    target = scale(source, factor)
    target = extract(target, source.width(), source.height(), centerXpos, centerYpos)   
    stddraw.setCanvasSize(target.width(), target.height())
    stddraw.picture(target)
    
    
    stddraw.show()
    
if __name__ == '__main__':
    main()