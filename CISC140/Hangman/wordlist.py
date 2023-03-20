"""
Implements a random word list for playing Hangman.
Words are loaded from a text file called wordlist.txt
in the same directory as the module.  Words in the
word list acquired from https://www.hangmanwords.com/words
"""
import stdio
from instream import InStream
from random import Random

class WordList:
    
    def __init__(self, seed = None):
        """
        Construct a new WordList object.
        If the seed parameter is None, the random generator
        will be initialized with a seed from the current time.
        Otherwise the provided seed will be used, which can be
        helpful for testing purposes.
        Words are loaded from wordlist.txt
        """
        self._rand = Random(seed)
        inputstream = InStream('wordlist.txt')         
        self._words = inputstream.readAllStrings()
        
    def select(self):
        """
        Returns a randomly-selected word from the internal word list.
        """
        return self._rand.choice(self._words)
    
    def __len__(self):
        """
        Returns the number of words in the list.
        """
        return len(self._words)
    
    
def _test():
    """
    Unit testing for WordList
    """
    stdio.writeln("Testing.")
    # Construct a word list with a random seed.  Make sure it loads the list.
    wlist = WordList()
    assert len(wlist) > 0
    stdio.writef("Word list contains %d words, one of which is %s\n", 
                 len(wlist), wlist.select())
        
    # Construct two word lists with a deterministic seed
    copy1 = WordList(1024)
    copy2 = WordList(1024)
    
    # Get two words from each list.  They should be the same.
    word1a = copy1.select()
    word1b = copy1.select()
    
    word2a = copy2.select()
    word2b = copy2.select()
    
    stdio.writeln("Checking deterministic seed.")
    assert (word1a, word1b) == (word2a, word2b)
    stdio.writeln("Passed.")
    
        
if __name__ == "__main__":
    _test()