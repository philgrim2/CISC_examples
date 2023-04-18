import stddraw

class Button:

    """A button is a labeled rectangle in a window.
    It is activated or deactivated with the activate()
    and deactivate() methods. The clicked(p) method
    returns true if the button is active and p is inside it."""

    def __init__(self, x, y, width, height, label):
        """ Creates a rectangular button, eg:
        qb = Button(myWin, centerPoint, width, height, 'Quit') """ 

        w,h = width/2.0, height/2.0
        self._x = x
        self._y = y
        self._xmax, self._xmin = x+w, x-w
        self._ymax, self._ymin = y+h, y-h
        self._width = width
        self._height = height
        self._label = label
        self._active = True

    def clicked(self, x, y):
        "Returns true if button active and p is inside"
        return (self._active and
                self._xmin <= x <= self._xmax and
                self._ymin <= y <= self._ymax)

    def draw(self):
        if self._active:
            stddraw.setPenColor(stddraw.LIGHT_GRAY)
            stddraw.filledRectangle(self._xmin, self._ymin, self._width, self._height)
            stddraw.setPenColor(stddraw.BLACK)
            stddraw.setPenRadius(0.01)
            stddraw.rectangle(self._xmin, self._ymin, self._width, self._height)
            stddraw.setPenRadius()
        else:
            stddraw.setPenColor(stddraw.DARK_GRAY)
            stddraw.filledRectangle(self._xmin, self._ymin, self._width, self._height)
            stddraw.setPenColor(stddraw.BLACK)
            stddraw.rectangle(self._xmin, self._ymin, self._width, self._height)
            
        stddraw.text(self._x, self._y, self._label)
    
    def activate(self):
        "Sets this button to 'active'."
        self._active = True

    def deactivate(self):
        "Sets this button to 'inactive'."
        self._active = False
