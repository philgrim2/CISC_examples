import stddraw
import card
from card import Card
from button import Button
from deck import Deck
from graphicalcard import GraphicalCard
from picture import Picture

class CardView:
    """
    A class to graphically display a card deck.  The user is able to cycle through the cards
    in the deck by pressing the Draw button, or quit the application by pressing the Quit button.
    """
    _image_file = "./cards/Deck1.gif"
    # Set up the canvas size
    _SCREEN_WIDTH = 640
    _SCREEN_HEIGHT = 480
    # Set a scaled font size
    _FONT_SIZE = _SCREEN_HEIGHT // 32
    
    def __init__(self):
        """
        Create an instance of the CardView application.  Initialize the graphical resources. 
        Create the card deck and shuffle it.
        """
        french_ranks = [key for key, value in card.default_rank_dict.items()] 
        french_suits = [key for key, value in card.default_suit_dict.items()]
    
        card_lst = []

        for suit in french_suits:
            for rank in french_ranks:
                card_lst.append(GraphicalCard(rank, suit))

        self._deck = Deck(card_lst)
        self._deck.shuffle();

        stddraw.setCanvasSize(self._SCREEN_WIDTH, self._SCREEN_HEIGHT)
        stddraw.setXscale(0, self._SCREEN_WIDTH)
        stddraw.setYscale(0, self._SCREEN_HEIGHT)
        stddraw.setFontSize(self._FONT_SIZE)
        
        self._quitButton = Button(320, 400, 80, 30, "Quit")
        self._drawButton = Button(320, 450, 80, 30, "Draw")

        self._deck_image = Picture(self._image_file)
        
    def run(self):
        """
        Run the application main loop
        """
        card = None
        running = True;
        while (running):
            stddraw.clear()
            self._quitButton.draw()
            self._drawButton.draw()
            if len(self._deck) > 0:
                stddraw.picture(self._deck_image, 100, 240)
            if stddraw.mousePressed():
                mX = stddraw.mouseX()
                mY = stddraw.mouseY()
                if self._quitButton.clicked(mX, mY):
                    running = False
                elif self._drawButton.clicked(mX,mY):
                    card = self._deck.draw()
                    # If we are out of cards, we should deactivate the draw button to avoid
                    # an error when the user clicks it again.
                    if len(self._deck) == 0:
                        self._drawButton.deactivate()
            if isinstance(card, Card):
                card.draw(250, 240)
            stddraw.show(10)    
            
if __name__ == '__main__':
    cardViewApp = CardView()
    cardViewApp.run()
                