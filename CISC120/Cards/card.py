# Default French deck card ranks
default_rank_dict = {'ace':1, '2':2, '3':3, '4':4, '5':5, '6':6, '7':7, '8':8,
                         '9':9, '10':10, 'jack':11, 'queen':12, 'king':13}

# Default French deck suits
default_suit_dict = {'spades':4, 'hearts':3, 'clubs':2, 'diamonds':1}

def _reverse_dict(dictionary):
    """
    Returns the given dictionary of key:value pairs as a new dictionary
    with the pairs swapped (value:key)
    """
    return {val:key for key, val in dictionary.items()}

class Card:
    """
    A class to represent a playing card.  Unless otherwise specified, the card will be one
    of the 52 cards of a standard French deck.
    """
    def __init__(self, rank, suit, rank_dict=default_rank_dict, suit_dict=default_suit_dict):
        """
        Create a new instance of a Card.  Store the encoding dictionaries and their reverse
        representations.  Use these encodings to ensure that the correct
        numeric representation of the rank and suit are stored.
        """
        self._rank_encoding = rank_dict
        self._suit_encoding = suit_dict

        self._rank_decoding = _reverse_dict(self._rank_encoding)
        self._suit_decoding = _reverse_dict(self._suit_encoding)

        self._rank = self._rank_encoding[rank]
        self._suit = self._suit_encoding[suit]

    def get_rank(self):
        """
        Returns the string representation of the card's rank.
        """
        return self._rank_decoding[self._rank]

    def get_suit(self):
        """
        Returns the string representation of the card's suit.
        """
        return self._suit_decoding[self._suit]
    
    def get_value(self):
        """
        Returns the numeric value of the card's rank.
        """
        return self._rank
    
    def get_color(self):
        """
        For now, assumes french deck.  Need a way to specify for alternate decks.
        """
        if self._suit_decoding[self._suit] in ['hearts', 'diamonds']: return 'red'
        else: return 'black'

    def __str__(self):
        """
        Returns a string representation of the card's rank and suit.
        """
        return self.get_rank() + ' of ' + self.get_suit()

    def __repr__(self):
        """
        Returns the canonical string representation of the card.  Equivalent to __str__()
        """
        return self.get_rank() + ' of ' + self.get_suit()

    def clone(self):
        """
        Returns an exact copy of the card.
        """
        return Card(self.get_rank(), self.get_suit(), self._rank_encoding,
                    self._suit_encoding)

    def __eq__(self, other):
        """
        Override operator == to check for equality of card objects.
        Cards are equal if both the rank and suit are equal.
        """
        if other is None:
            return False
        return self._rank == other._rank and self._suit == other._suit

    def __lt__(self, other):
        """
        Override operator <.  Card ordinality is determined first by rank, then by suit.
        Value of ranks and suits are provided by the encoding dictionaries passed to the 
        constructor.
        """
        if self._rank == other._rank:
            return self._suit < other._suit

        return self._rank < other._rank

    def __le__(self, other):
        """
        Override operator <=.  Card ordinality is determined first by rank, then by suit.
        Value of ranks and suits are provided by the encoding dictionaries passed to the 
        constructor.
        """
        return (self._suit <= other._suit) if (self._rank == other._rank) else (self._rank <= other._rank)


    def __gt__(self, other):
        """
        Override operator >.  Card ordinality is determined first by rank, then by suit.
        Value of ranks and suits are provided by the encoding dictionaries passed to the 
        constructor.
        """
        if self._rank == other._rank:
            return self._suit > other._suit

        return self._rank > other._rank


    def __ge__(self, other):
        """
        Override operator >=.  Card ordinality is determined first by rank, then by suit.
        Value of ranks and suits are provided by the encoding dictionaries passed to the 
        constructor.
        """
        return (self._rank == other._rank and self._suit >= other._suit) or (self._rank != other._rank and self._suit >= other._suit)