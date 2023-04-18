import card
from card import Card
import random 
import stdio

class Deck:
    "A class for holding a deck of playing cards"
    
    def __init__(self, card_list):
        """Constructor for the Deck class.  Creates an instance variable 
        that is a clone of the list of cards."""
        
        self._cards = [c.clone() for c in card_list]


    def shuffle(self):
        """
        Randomize the order of the deck of cards.
        """
        random.shuffle(self._cards)


    def draw(self):
        """
        Returns the top card on the deck.  Removes the card.
        """
        return self._cards.pop(0)


    def __getitem__(self, idx):
        """
        Override operator [].  Allows indexing the Deck object as a list.
        """
        return self._cards[idx]


    def __len__(self):
        """
        Returns the current length of the deck of cards.
        """
        return len(self._cards)
    
if __name__ == '__main__':
    french_ranks = [key for key, value in card.default_rank_dict.items()] 
    french_suits = [key for key, value in card.default_suit_dict.items()]

    stdio.writeln(french_ranks)
    stdio.writeln(french_suits)
    
    card_lst = []

    for suit in french_suits:
        for rank in french_ranks:
            card_lst.append(Card(rank, suit))
            
    myDeck = Deck(card_lst)

    for card in myDeck:
        stdio.writeln(card)

    stdio.writeln("The eighth card in my deck is the " + str(myDeck[7]))
    
    stdio.writeln('<---------------->')
    myDeck.shuffle()
    for card in myDeck:
        stdio.writeln(card)
    stdio.writeln("The eighth card in my deck is the " + str(myDeck[7]))
