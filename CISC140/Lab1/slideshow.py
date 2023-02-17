import stddraw
import stdio
import stdrandom
import stopwatch
import sys
import time
from color import Color
from picture import Picture

DELAY = 1  # Delay in seconds

def blend(c1, c2, alpha):
    r = (1-alpha)*c1.getRed()   + alpha*c2.getRed()
    g = (1-alpha)*c1.getGreen() + alpha*c2.getGreen()
    b = (1-alpha)*c1.getBlue()  + alpha*c2.getBlue()
    return Color(int(r), int(g), int(b))

def effect(source):
    """
    Apply an effect to the source picture.
    """
    timer = time.time()
    width = source.width()
    height = source.height()
    target = Picture(width, height)
    
    for col in range(width):
        for row in range(height):
            cc = (width  + col + stdrandom.uniformInt(-5, 6)) % width
            rr = (height + row + stdrandom.uniformInt(-5, 6)) % height
            c = source.get(cc, rr)
            target.set(col, row, c)
    stdio.writef('Effect took %.4f seconds.\n', time.time() - timer)
    return target

def transition(before, after):
    """
    Apply a transition from the before pic to the after pic.
    """
    steps = 5
    width = max(before.width(), after.width())
    height = max(before.height(), after.height())
    before_margin_horiz = abs(width - before.width()) // 2
    before_margin_vert = abs(height - before.height()) // 2
    after_margin_horiz = abs(width - after.width()) // 2
    after_margin_vert = abs(height - after.height()) // 2
    
    pic = Picture(width, height)
    for t in range(steps + 1):
        for col in range(width):
            for row in range(height):
                # Need to get a before pixel and an after pixel
                # Pictures might be different sizes - if there isn't
                # a valid pixel for either the before or after,
                # just use black
                beforePixel = None
                afterPixel = None
                
                adjCol = col - before_margin_horiz
                adjRow = row - before_margin_vert
                if (0 <= adjCol < before.width() and
                    0 <= adjRow < before.height()):
                    beforePixel = before.get(adjCol, adjRow)
                else:
                    # we're off the edge of before
                    beforePixel = stddraw.BLACK
                    
                adjCol = col - after_margin_horiz
                adjRow = row - after_margin_vert
                if (0 <= adjCol < after.width() and
                    0 <= adjRow < after.height()):
                    afterPixel = after.get(adjCol, adjRow)
                else:
                    # we're off the edge of after
                    afterPixel = stddraw.BLACK
                
                alpha = float(t) / float(steps)
                c = blend(beforePixel, afterPixel, alpha)
                pic.set(col, row, c)
        stddraw.picture(pic)
        stddraw.show(0)

def slideshow(slides):
    index = -1
    while True:
        stddraw.clear(stddraw.BLACK) 
        if index < 0:
            # First slide.
            stddraw.picture(slides[index])
        else:
            now = index - 1
            if now < 0: now = len(slides) - 1
            transition(slides[now], slides[index])
        stddraw.show(0) # No need for delay.  It's already painfully slow
        index += 1
        if index == len(slides): index = 0
            

def main():
    """
    Main program loads each picture specified on the command line,
    sets up the drawing area, then starts the slideshow.
    """
    slides = []
    screenWidth = 0;
    screenHeight = 0;
    for filename in sys.argv[1:]:
        # Load the picture.  Preprocess the effect because it's slow
        stdio.writeln("Loading " + filename)
        slide = effect(Picture(filename))
        
        # Find the largest height and width
        screenWidth = max(screenWidth, slide.width());
        screenHeight = max(screenHeight, slide.height());
        
        # Add the slide to the list                   
        slides.append(slide)
    
    stddraw.setCanvasSize(screenWidth, screenHeight)
    slideshow(slides)

if __name__ == '__main__':
    main()