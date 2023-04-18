import stdarray
import stddraw
import stdio
import card
from card import Card
from deck import Deck
from graphicalcard import GraphicalCard
from picture import Picture

class Solitaire:

    _back_image = "Deck2"
    # Set up the canvas size
    _SCREEN_WIDTH = 1024
    _SCREEN_HEIGHT = 768
    # Set up offsets and margins
    _BORDER_OFFSET = _SCREEN_HEIGHT // 14
    _PILE_MARGIN = _SCREEN_HEIGHT // 16

    _PILES = 7

    # Set a scaled font size
    _FONT_SIZE = _SCREEN_HEIGHT // 32

    def __init__(self):
        # Set up the empty lists that will hold the various piles
        self._piles = []
        for i in range(self._PILES):
            pile = []
            self._piles.append(pile);
        self._home = []
        for suit in card.default_suit_dict.keys():
            pile = []
            self._home.append(pile)

        # Make the deck
        french_ranks = [key for key, value in card.default_rank_dict.items()] 
        french_suits = [key for key, value in card.default_suit_dict.items()]
    
        card_lst = []

        for suit in french_suits:
            for rank in french_ranks:
                card_lst.append(GraphicalCard(rank, suit, self._back_image))

        self._deck = Deck(card_lst)
        self._deck.shuffle();

        self._discard = []
        self._highlighted = []

        # Set up the play area
        stddraw.setCanvasSize(self._SCREEN_WIDTH, self._SCREEN_HEIGHT)
        stddraw.setXscale(0, self._SCREEN_WIDTH)
        stddraw.setYscale(0, self._SCREEN_HEIGHT)
        stddraw.setFontSize(self._FONT_SIZE)

        # Load the deck image
        self._deck_image = Picture("./cards/" + self._back_image + ".gif")

    def _draw(self):
        stddraw.clear(stddraw.DARK_GREEN)
        
        # Use the width and height of the deck image for reference
        # We could scale up and down with screen size later
        card_width = self._deck_image.width()
        card_height = self._deck_image.height()

        # Draw the home row
        home_row_y = self._SCREEN_HEIGHT - (card_height + self._BORDER_OFFSET)
        home_row_x = self._BORDER_OFFSET

        stddraw.setPenColor(stddraw.LIGHT_GRAY)
        for suit in self._home:
            # Draw empty stack rectangle
            stddraw.rectangle(home_row_x, home_row_y, card_width, card_height)
            # If there's a card on top, draw it
            if len(suit) > 0:
                card = suit[len(suit) - 1]
                card.draw(home_row_x + card_width // 2, home_row_y + card_height // 2)
            home_row_x += card_width + self._PILE_MARGIN
            
        # Draw the deck and discard pile
        stddraw.setPenColor(stddraw.LIGHT_GRAY)
        deck_x = self._SCREEN_WIDTH - (self._BORDER_OFFSET + (2 * card_width) + self._PILE_MARGIN)
        stddraw.rectangle(deck_x, home_row_y, card_width, card_height)
        if len(self._deck) > 0:
            stddraw.picture(self._deck_image, deck_x + (card_width // 2), home_row_y + (card_height // 2))

        discard_x = deck_x + self._PILE_MARGIN + card_width
        stddraw.setPenColor(stddraw.LIGHT_GRAY)
        stddraw.rectangle(discard_x, home_row_y, card_width, card_height)
        if len(self._discard) > 0:
            card = self._discard[len(self._discard) - 1]
            card.draw(discard_x + (card_width // 2), home_row_y + (card_height // 2))
            
        # Draw the piles
        pile_row_x = self._BORDER_OFFSET
        pile_row_y =  self._SCREEN_HEIGHT - (2 * card_height + self._BORDER_OFFSET + self._PILE_MARGIN)
        for i in range(len(self._piles)):
            # Draw empty stack rectangle
            stddraw.setPenColor(stddraw.LIGHT_GRAY)
            stddraw.rectangle(pile_row_x, pile_row_y, card_width, card_height)
            # Draw any cards in the pile
            pile = self._piles[i]
            card_top_offset = 0
            for i in range(len(pile)):
                card = pile[i]
                card.draw(pile_row_x + (card_width // 2), pile_row_y + (card_height // 2) - card_top_offset)
                card_top_offset += card_height // 5
            
            pile_row_x += card_width + self._PILE_MARGIN      

        # Show
        stddraw.show(0)

    def _deal(self):
        for pile in range(self._PILES):
            pilemax = pile + 1
            while len(self._piles[pile]) < pilemax:
                c = self._deck.draw();
                face = (len(self._piles[pile]) == pilemax - 1)
                c.face(face)
                self._piles[pile].append(c);


    def _handleMouseClick(self, x : int, y : int):
        # Need to figure out if anything was clicked
        card_width = self._deck_image.width()
        card_height = self._deck_image.height()

        # First see if the deck was clicked (if it's there)
        home_row_y = self._SCREEN_HEIGHT - (card_height + self._BORDER_OFFSET)
        deck_x = self._SCREEN_WIDTH - (self._BORDER_OFFSET + (2 * card_width) + self._PILE_MARGIN)
        if deck_x < x < deck_x + card_width and home_row_y < y < home_row_y + card_height:
            self._draw_from_deck()
            self._clear_highlights()
            return
        
        # Check to see if the top card on the discard pile is clicked, and highlight it if it is
        discard_x = self._SCREEN_WIDTH - (self._BORDER_OFFSET + card_width)
        if discard_x < x < discard_x + card_width and home_row_y < y < home_row_y + card_height:
            self._clear_highlights()
            if len(self._discard) > 0:
                card = self._discard[len(self._discard) - 1]
                card.highlight(not card.highlight())
                self._highlighted = [card]
            return
        # Check to see if a card in the piles is clicked.
        # If there is a card highlighted right now, then one being clicked here
        # is an attempted move completion.  If one isn't highlighted, it's an 
        # attempted move start.
        piles_min_x = self._BORDER_OFFSET
        piles_max_x = piles_min_x + (7 * card_width) + (6 * self._PILE_MARGIN)
        piles_max_y = home_row_y - self._PILE_MARGIN
        piles_min_y = piles_max_y - (card_height + (18 * (card_height // 5)))
        if piles_min_x < x < piles_max_x + card_width and piles_min_y < y < piles_max_y:
            # If there's a highlighted card, and a pile's being clicked, that's
            # an attempted move completion. Otherwise might be a move start.
            if len(self._highlighted ) > 0:
                for pile in self._piles:
                    # Check to see if there's a top card and then if it's a valid move
                    if len(pile) > 0:
                        card = pile[len(pile) - 1]
                        if card.clicked(x, y):
                            if card.face() and self._valid_move(self._highlighted[0], card, False):
                                self._move(pile)
                                return
                    else:
                        if self._valid_move(self._highlighted[0], None, False):
                            self._move(pile)
                            return
            else:
                # Nothing highlighted, so let's highlight
                for pile in self._piles:
                    # First check for multi
                    i = 0
                    while i < len(pile) - 1:
                        card = pile[i]
                        if card.clicked(x, y, card_height//5) and card.face():
                            #This is a multi-click
                            while i < len(pile):
                                card = pile[i]
                                card.highlight(True)
                                self._highlighted += [card]
                                i += 1
                            return
                        else:
                            i+=1
                    
                    # Now check the top card
                    if len(pile) > 0:
                        card = pile[len(pile) - 1]
                        if card.clicked(x, y):
                            if card.face():
                                card.highlight(True)
                                self._highlighted = [card]
                                return
                            else:
                                card.face(True)
                                return

        # Check to see if the home row is clicked.  If a card is highlighted, it's an
        # attempted move completion.  If not, it's an attempted move start.
        home_row_min_x = self._BORDER_OFFSET
        home_row_max_x = home_row_min_x + (4 * card_width) + (3 * self._PILE_MARGIN)
        if home_row_min_x < x < home_row_max_x and home_row_y < y < home_row_y + card_height:
            if len(self._highlighted ) > 0:
                for pile in self._home:
                    # Check to see if there's a top card and then if it's a valid move
                    if len(pile) > 0:
                        card = pile[len(pile) - 1]
                        if card.clicked(x, y):
                            if self._valid_move(self._highlighted[0], card, True):
                                self._move(pile)
                                return
                    else:
                        if self._valid_move(self._highlighted[0], None, True):
                            self._move(pile)
                            return
            else:
                for pile in self._home:
                    # Check to see if there's a top card and then if it's a valid move
                    if len(pile) > 0:
                        card = pile[len(pile) - 1]
                        if card.clicked(x, y):
                            card.highlight(True)
                            self._highlighted = [card]
                            return

        # If none of the other things above happened, and there's a highlight, clear it.
        self._clear_highlights()

    def _clear_highlights(self):
        for card in self._highlighted:
            card.highlight(False)
        self._highlighted = []
    
    def _valid_move(self, source : GraphicalCard, dest : GraphicalCard, is_home : bool) -> bool:
        if source == None:
            return False
        if is_home:
            # Home row requires the card to be the same suit and one higher than the destination
            # beginning with the ace if the pile is empty
            if   dest == None: return source.get_rank() == 'ace'
            elif source.get_suit() == dest.get_suit(): return source.get_value() - dest.get_value() == 1
            else: return False
        else:
            # if destination is empty, card must be a king, otherwise source must be one less than dest
            # and the opposite color
            if   dest == None: return source.get_rank() == 'king'
            elif source.get_color() != dest.get_color(): return dest.get_value() - source.get_value() == 1
            else: return False

    def _move(self, target : list):
        for card in self._highlighted:
            self._remove(card)
            target.append(card)
            card.highlight(False)
            
        self._highlighted = []

    def _remove(self, card : GraphicalCard):
        # Find the card and remove it from the list it's in
        if card in self._discard:
            self._discard.remove(card)
            return
        for pile in self._home:
                if card in pile:
                    pile.remove(card)
                    return
        for pile in self._piles:
            for t in pile:
                if card in pile:
                    pile.remove(card)
                    return


    def _draw_from_deck(self):
        if len(self._deck) > 0:
            # Draw up to 3 cards from the deck
            i = 0
            while i < 3 and len(self._deck) > 0:
                card = self._deck.draw()
                card.face(True)
                self._discard.append(card)
                i += 1
        else:
            # Flip the discard back over
            self._deck = Deck(self._discard)
            self._discard = []


    def run(self):
        self._deal()

        done = False;
        while not done:
            if stddraw.mousePressed():
                self._handleMouseClick(stddraw.mouseX(), stddraw.mouseY())
            self._draw()
            home = 0 
            for pile in self._home:
                home += len(pile)
            done = home >= 52 

 

if __name__ == '__main__':
    game = Solitaire()
    game.run()