"""
Class to create terminal art of a hangman gallows.
"""
import stdio

class Gallows:
    # Pre-define the character art for each strike level
    strike_0 = """
________
|      |
|         
|        
|      
|   
========
"""
    strike_1 = """
________
|      |
|      O  
|        
|      
|   
========
"""
    strike_2 = """
________
|      |
|      O  
|     /
|      
|   
=======
""" 
    strike_3 = """
________
|      |
|      O  
|     /|
|      
|   
=======
""" 
    strike_4 = """
________
|      |
|      O  
|     /|\ 
|        
|   
=======
"""
    strike_5 = """
________
|      |
|      O  
|     /|\  
|     /   
|        
=======
"""
    
    strike_6 = """
________
|      |
|      O  
|     /|\   
|     / \   
|         
=======
"""
    
    _STRIKES = [strike_0, strike_1, strike_2, strike_3, strike_4, strike_5, strike_6]
    _MAX_STRIKES = len(_STRIKES) - 1
    
    def __init__(self, strikes = 0):
        """
        Creates an instance of Gallows.  If the strikes parameter
        is omitted, the gallows start empty.  If the strikes parameter
        is non-zero, the gallows will start with the requested number
        of body parts displayed.  Valid values are 0 - 6.
        """
        # Make sure the strikes parameter is in the valid range.
        if strikes < 0 or strikes > 6:
            raise ValueError("Invalid value for strikes.")
        self._strikes = strikes
        
    def strikes(self):
        """
        Returns the current number of strikes.
        """
        return self._strikes
    
    def strike(self):
        """
        Increments the strike count.
        If there are already the max amount of strikes, just
        stay at the max.
        """
        if self._strikes < self._MAX_STRIKES:
            self._strikes += 1
        
    def __str__(self):
        """
        Return the pre-defined string associated with the current
        number of strikes.
        """
        return self._STRIKES[self._strikes]
    
def _test():
    """
    Performs unit testing of the Gallows class.
    """
    gallows = Gallows()
    for i in range(gallows._MAX_STRIKES + 1):
        stdio.writeln(gallows)
        gallows.strike()

if __name__ == "__main__":
    _test()