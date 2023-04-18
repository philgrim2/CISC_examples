import stddraw
from picture import Picture
from card import Card

class GraphicalCard(Card):
    "A class for displaying graphical playing cards"
    
    _rank_to_name = {'ace':'A', '1':'1', '2':'2', '3':'3', '4':'4', '5':'5', '6':'6',
                    '7':'7', '8':'8', '9':'9', '10':'T', 'jack':'J', 'queen':'Q', 'king':'K'}
    _suit_to_name  ={'spades':'S','hearts':'H','clubs':'C','diamonds':'D'}
    _image_dir = './cards/'
    
    __xloc = -1
    __yloc = -1
    
    
    def __init__(self, rank, suit, back_name='Deck1'):
        # Initialize the superclass.  Let it use the default rank and suit dictionaries.
        super().__init__(rank, suit)
        # Load the card based on the rank and suit.
        file_name = self._image_dir + self._suit_to_name[suit] + self._rank_to_name[str(rank)] + ".gif"
        self._back_name = back_name
        self._image = Picture(file_name)
        self._back = Picture(self._image_dir + back_name + ".gif")
        self._highlight = False
        self._face = False
        
    def draw(self, x, y):
        """
        Draw the card on the screen at the given point.
        """
        self.__xloc = x
        self.__yloc = y
        if (self._face):
            stddraw.picture(self._image, x, y)
        else:
            stddraw.picture(self._back, x, y)
        if self._highlight:
            xmin = self.__xloc - self._image.width() / 2
            ymin = self.__yloc - self._image.height() / 2

            stddraw.setPenColor(stddraw.YELLOW)
            stddraw.rectangle(xmin,ymin,self._image.width(), self._image.height())

    def width(self):
        if self._image != None:
            return self._image.width()
        else:
            return 0
        
    def height(self):
        if self._image != None:
            return self._image.height()
        else:
            return 0
        
    def position(self):
        return (self.__xloc, self.__yloc)
    
    def clicked(self, x, y, obstruction_y = 0):
        xmin = self.__xloc - self._image.width() / 2
        xmax = self.__xloc + self._image.width() / 2
        ymin = self.__yloc - self._image.height() / 2
        if obstruction_y > 0:
            ymin += self._image.height() - obstruction_y
        ymax = self.__yloc + self._image.height() / 2

        return (xmin <= x <= xmax and
                ymin <= y <= ymax)
    
    def highlight(self, b = None):
        if b != None:
            self._highlight = b
        return self._highlight
    
    def face(self, b = None):
        if b != None:
            self._face = b
        return self._face
                  
    def clone(self):
        return GraphicalCard(self.get_rank(), self.get_suit(), self._back_name)