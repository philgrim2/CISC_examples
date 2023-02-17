import math
import stdio
import stddraw
import sys
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

def draw_rect(target, width, height, centerXpos, centerYpos):
    """
    Draws a rectangle that is the size of the desired zoom area, with a dot 
    showing the center point. 
    """
    centerx = math.ceil(target.width() * centerXpos)
    centery = math.ceil(target.height() * centerYpos)
    
    cornerx = centerx - width // 2
    cornery = centery - height // 2
    stddraw.rectangle(cornerx, cornery, width, height)
    stddraw.filledCircle(centerx, centery, 2)

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
        
    stddraw.setCanvasSize(target.width(), target.height())
    stddraw.setXscale(0, target.width())
    stddraw.setYscale(0, target.height())
    stddraw.picture(target)
    
    draw_rect(target, source.width(), source.height(), centerXpos, centerYpos)
    
    stddraw.show()
    
if __name__ == '__main__':
    main()