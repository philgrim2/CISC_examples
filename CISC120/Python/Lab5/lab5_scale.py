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

def main():
    """
    Accepts two command line arguments - a string containing the name of a picture file, and a floating point
    number indicating a scale factor to apply to the image.
    """

    fil = sys.argv[1]
    factor = float(sys.argv[2])
    source = Picture(fil)

    target = scale(source, factor)
        
    stddraw.setCanvasSize(target.width(), target.height())
    stddraw.picture(target)
    stddraw.show()
    
if __name__ == '__main__':
    main()