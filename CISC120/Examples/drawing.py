import stddraw

x = 320
y = 250

dy = 2
    
stddraw.setCanvasSize(640,480)
stddraw.setXscale(0,640)
stddraw.setYscale(0,480)
stddraw.setPenColor(stddraw.WHITE)
while True:
    stddraw.clear(stddraw.LIGHT_GRAY)
    stddraw.line(0,0,320,400)
    stddraw.line(320,400,640,0)
    stddraw.line(640,0,0,0)

    y += dy
    if y >= 400:
        dy = -dy
    elif y <= 0:
        dy = -dy
    
    stddraw.point(x, y)

    stddraw.show(10)

