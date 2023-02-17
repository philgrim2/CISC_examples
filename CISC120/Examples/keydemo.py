import stddraw
import stdio

nrows = 6
ncols = 6

stddraw.setXscale(0,nrows)
stddraw.setYscale(0,ncols)

keys = [['A','B','C','D','E','F'],
        ['G','H','I','J','K','L'],
        ['M','N','O','P','Q','R'],
        ['S','T','U','V','W','X'],
        ['Y','Z','0','1','2','3'],
        ['4','5','6','7','8','9']]

keysize = 0.95

while True:
  stddraw.clear()
  keypress = ''
  mouseX = 0
  mouseY = 0
  if stddraw.hasNextKeyTyped():
    keypress = stddraw.nextKeyTyped().upper()
    stdio.writeln("KeyPressed: " + keypress)
  if stddraw.mousePressed():
    mouseX = stddraw.mouseX()
    mouseY = stddraw.mouseY()
    stdio.writef("Mouse pressed:(%.4f, %.4f)\n", mouseX, mouseY)
  for row in range(nrows):
    for col in range(ncols):
      if keypress == keys[row][col]:
        stddraw.setPenColor(stddraw.CYAN)
        stddraw.filledSquare(col + 0.5, row + 0.5, keysize/2)       
      if (col + 0.025) < mouseX < (col + 0.975) and  (row + 0.025) < mouseY < (row + 0.975):
        stddraw.setPenColor(stddraw.YELLOW)
        stddraw.filledSquare(col + 0.5, row + 0.5, keysize/2)

      stddraw.setPenColor(stddraw.BLACK)
      
      stddraw.square(col + 0.5, row + 0.5, keysize/2)
      stddraw.text(col + 0.5, row + 0.5,keys[row][col])
      

  stddraw.show(20)