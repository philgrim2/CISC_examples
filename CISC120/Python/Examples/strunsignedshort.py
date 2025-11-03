class StrUnsignedShort:
    """
    This class demonstrates converting between words that describe integers
    and the integers themselves.
    
    An unsigned short integer is an integer represented by 8 bits 
    and can hold values between 0 and 255 inclusive.
    """
    
    _ones = {0:"zero", 1:"one", 2:"two", 3:"three", 4:"four", 
             5:"five", 6:"six", 7:"seven", 8:"eight", 9:"nine"}
    _tens = {1:"ten", 2:"twenty", 3:"thirty", 4:"forty", 
             5:"fifty", 6:"sixty", 7:"seventy", 8:"eighty", 9:"ninety"}
    _tweens = {11:"eleven", 12:"twelve", 13:"thirteen", 14:"fourteen", 
               15:"fifteen", 16:"sixteen", 17:"seventeen", 18:"eighteen", 
               19:"nineteen"}
    _rOnes = {value:key for key, value in _ones.items()}
    _rTens = {value:key for key, value in _tens.items()}
    _rTweens = {value:key for key, value in _tweens.items()}
    
    def __init__(self, val=0):
        """
        Initializes a new instance of StrUnsignedShort.
        
        val - The initial value of the instance.
              If val is an integer, it must be in the proper range
              If val is a string, it must translate to an integer 
              in the proper range.
        """
        self._value = 0
        if isinstance(val, str):
            # Try to convert the value to an integer using convert function
            self._value = self._convert(val)
        elif isinstance(val, int):
            self._value = val
        else:
            raise TypeError("Invalid conversion to StrUnsignedShort")
        
        # Range check
        if not 0 <= self._value < 256:
            raise ValueError("Value out of range for StrUnsignedShort")
            
    def _convert(self, string):
        """
        Convert from a string value such as "one hundred twenty seven"
        to a number.
        """
        newString = string.replace("-", " ")
        # Try to parse the string into a StrUnsignedShort
        chunks = newString.split()
        if chunks == None or len(chunks) > 5:
            raise ValueError("Invalid literal value for StrUnsignedShort")
        
        number = 0
        for idx in range(len(chunks)):
            chunk = chunks[idx].lower() # make the conversion case-insensitive
            # Chunk could be a number of hundreds, a tween, a ten, or a one
            if chunk in self._rTweens:
                number += self._rTweens[chunk]
            elif chunk in self._rTens:
                number += self._rTens[chunk] * 10
            elif chunk in self._rOnes:
                position = len(chunks) - idx
                if position == 1:
                    number += self._rOnes[chunk]
                elif chunks[idx + 1] == "hundred":
                    number += self._rOnes[chunk] * 100
                elif position == 2:
                    number += self._rOnes[chunk] * 10
                elif position == 3:
                    number += self._rOnes[chunk] * 100
            elif chunk == "hundred":
                # We should have handled this already.
                continue
            elif chunk == "and":
                # We can ignore this.
                continue
            else:
                raise ValueError("Invalid literal value for StrUnsignedShort")
         
        return number
            
    def get_value(self):
        """
        Returns the stored value as an integer.
        """
        return self._value

    # Part 1, Comparison Functions
    #
    # Here we will implement __eq__, __ne__, __lt__, __le__, __gt__, __ge__
    # Keep in mind that recent Python interpreters will infer the rest of the
    # comparators from __eq__ and __lt__, but older ones are not guaranteed to
    # so it's best to implement them fully.
    
    def __eq__(self, other):
        """
        Check equality.
        If other is an instance of StrUnsignedShort, compare the values.
        If other is an int or float, we can also compare them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        """
        if isinstance(other, StrUnsignedShort):
            return self.get_value() == other.get_value()
        elif isinstance(other, (int, float)):
            return self.get_value() == other
        elif isinstance(other, str):
            val = StrUnsignedShort(other)
            return self.get_value() == other.get_value()
        else:
            raise TypeError("Invalid type for comparison to StrUnsignedShort")
    
    def __ne__(self, other):
        """
        Check inequality.
        Just use the equality check and negate the result.
        """
        return not (self == other)
    
    def __lt__(self, other):
        """
        Check less-than condition.
        If other is an instance of StrUnsignedShort, compare the values.
        If other is an int or float, we can also compare them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        """
        if isinstance(other, StrUnsignedShort):
            return self.get_value() < other.get_value()
        elif isinstance(other, (int, float)):
            return self.get_value() < other
        elif isinstance(other, str):
            val = StrUnsignedShort(other)
            return self.get_value() < val.get_value()
        else:
            raise TypeError("Invalid type for comparison to StrUnsignedShort")
    
    def __le__(self, other):
        """
        Check less-than-or-equal condition.
        If other is an instance of StrUnsignedShort, compare the values.
        If other is an int or float, we can also compare them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        """
        if isinstance(other, StrUnsignedShort):
            return self.get_value() <= other.get_value()
        elif isinstance(other, (int, float)):
            return self.get_value() <= other
        elif isinstance(other, str):
            val = StrUnsignedShort(other)
            return self.get_value() <= val.get_value()
        else:
            raise TypeError("Invalid type for comparison to StrUnsignedShort")
    
    def __gt__(self, other):
        """
        Check greater-than condition.
        If other is an instance of StrUnsignedShort, compare the values.
        If other is an int or float, we can also compare them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        """
        if isinstance(other, StrUnsignedShort):
            return self.get_value() > other.get_value()
        elif isinstance(other, (int, float)):
            return self.get_value() > other
        elif isinstance(other, str):
            val = StrUnsignedShort(other)
            return self.get_value() > val.get_value()
        else:
            raise TypeError("Invalid type for comparison to StrUnsignedShort")
    
    def __ge__(self, other):
        """
        Check greater-than-or-equal condition.
        If other is an instance of StrUnsignedShort, compare the values.
        If other is an int or float, we can also compare them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        """
        if isinstance(other, StrUnsignedShort):
            return self.get_value() >= other.get_value()
        elif isinstance(other, (int, float)):
            return self.get_value() >= other
        elif isinstance(other, str):
            val = StrUnsignedShort(other)
            return self.get_value() >= val.get_value()
        else:
            raise TypeError("Invalid type for comparison to StrUnsignedShort")
    
    # Part 2, Math Functions
    #
    # Here we will implement the add, subtract, multiply, and divide functions
    # Each operator has two forms: the form where the StrUnsignedShort object is
    # on the left-hand side of the expression, and the form where the 
    # StrUnsignedShort object is on the right-hand side of the expression.  The
    # Python special functions therefore come in pairs: __add__ and __radd__,
    # __mul__ and __rmul, etc.
    #
    
    def __add__(self, other):
        """
        Implement the + operator.
        If other is an instance of StrUnsignedShort, add the values.
        If other is an int or float, we can also add them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        Return value should be a StrUnsignedShort if it is in range, int otherwise
        """
        retval = 0
        if isinstance(other, StrUnsignedShort):
            retval = self.get_value() + other.get_value()
        elif isinstance(other, (int,float)):
            retval = self.get_value() + other
        elif (isinstance(other, str)):
            retval = self.get_value() + StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
        
        if isinstance(retval, int) and 0 <= retval <= 255:
            return StrUnsignedShort(retval)
        else:
            return retval
        
    def __radd__(self, other):
        """
        Implement the + operator with a StrUnsignedShort on the RHS
        If other is an int or float, we can also add them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        Return value should be whatever other's type was
        """
        retval = other
        if isinstance(other, (int,float)):
            retval = self.get_value() + other
        elif (isinstance(other, str)):
            retval = self.get_value() + StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
        
        return retval
    
    def __sub__(self, other):
        """
        Implement the - operator.
        If other is an instance of StrUnsignedShort, subtract the values.
        If other is an int or float, we can also subtract them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        Return value should be a StrUnsignedShort if it is in range, int otherwise
        """
        retval = 0
        if isinstance(other, StrUnsignedShort):
            retval = self.get_value() - other.get_value()
        elif isinstance(other, (int,float)):
            retval = self.get_value() - other
        elif (isinstance(other, str)):
            retval = self.get_value() - StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
            
        if isinstance(retval, int) and 0 <= retval <= 255:
            return StrUnsignedShort(retval)
        else:
            return retval
        
    def __rsub__(self, other):
        """
        Implement the - operator with a StrUnsignedShort on the RHS
        If other is an int or float, we can also add them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        Return value should be whatever other's type was
        """
        retval = other
        if isinstance(other, (int,float)):
            retval = other - self.get_value()
        elif (isinstance(other, str)):
            retval = StrUnsignedShort(other).get_value() - self.get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
        
        return retval
    
    def __mul__(self, other):
        """
        Implement the * operator.
        If other is an instance of StrUnsignedShort, multiply the values.
        If other is an int or float, we can also multiply them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        Return value should be a StrUnsignedShort if it is in range, int otherwise
        """
        retval = 0
        if isinstance(other, StrUnsignedShort):
            retval = self.get_value() * other.get_value()
        elif isinstance(other, (int,float)):
            retval = self.get_value() * other
        elif (isinstance(other, str)):
            retval = self.get_value() * StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
            
        if isinstance(retval, int) and 0 <= retval <= 255:
            return StrUnsignedShort(retval)
        else:
            return retval
        
    def __rmul__(self, other):
        """
        Implement the * operator with a StrUnsignedShort on the RHS
        If other is an int or float, we can also multiply them.
        If other is a string, we can try to convert it to StrUnsignedShort first.
        Return value should be whatever other's type was
        """
        retval = other
        if isinstance(other, (int,float)):
            retval = self.get_value() * other
        elif (isinstance(other, str)):
            retval = self.get_value() * StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
        
        return retval
    
    def __truediv__(self, other):
        """
        True division (x / y)
        Regardless of the data type of other, true division always
        returns a floating point value.
        """
        if isinstance(other, StrUnsignedShort):
            return self.get_value() / other.get_value()
        elif isinstance(other, (int,float)):
            return self.get_value() / other
        elif (isinstance(other, str)):
            return self.get_value() / StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
            

    def __rtruediv__(self, other):
        """
        True division (x / y)
        Regardless of the data type of other, true division always
        returns a floating point value.
        """
        if isinstance(other, (int,float)):
            return other / self.get_value()
        elif (isinstance(other, str)):
            return self.get_value() / StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
            
    def __floordiv__(self, other):
        """
        Integer division. (x // y)
        Return value should be int.
        """
        if isinstance(other, StrUnsignedShort):
            return self.get_value() // other.get_value()
        elif isinstance(other, int):
            return self.get_value() // other
        elif (isinstance(other, str)):
            return self.get_value() // StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
    
    def __rfloordiv__(self, other):
        """
        Integer division.
        Return value should be int.
        """
        if isinstance(other, int):
            return other // self.get_value()
        elif (isinstance(other, str)):
            return StrUnsignedShort(other).get_value() // self.get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")        
    
    # Part 3 - String representation
    # Python uses two different functions to convert objects to their string 
    # representations: str(), which calls the __str__ operator, and repr()
    # which calls the __repr__ operator.  In our case, they can both do the same
    # thing.  Here I will implement both, using different methods of conversion
    # to demonstrate different ways to solve the problem.
    
    def __str__(self):
        """
        Convert StrUnsignedShort to a string using number words.
        """
        # Get this special case out of the way...
        if self._value == 0:
            return "zero"
        
        # Use a string representation of the digits to determine
        # the output.
        valstr = str(self._value)
        retval = ""
        if len(valstr) == 3:
            # We have hundreds
            h = int(valstr[0])       # Get the number of hundreds
            retval += self._ones[h]  # Look up the word for it and append it
            retval += " hundred "    # Append hundred
            valstr = valstr[1:]      # Strip off the first digit
        if len(valstr) == 2:
            # Tens column, check for a zero (i.e. 104)
            if valstr[0] != "0":
                # Check for a 1 (in case it's a tween)
                if valstr[0] == '1':
                    # Still might be 10
                    if (valstr[1] == '0'):
                        retval += "ten"
                    else:
                        retval += self._tweens[int(valstr)]
                    valstr = ""       # We're done, so clear out the string
                else:
                    if (valstr[1] == '0'):
                        retval += self._tens[int(valstr[0])]
                        valstr = ""   # We're done, so clear out the string
                    else:
                        retval += self._tens[int(valstr[0])] + " "
                        valstr = valstr[1]; # We're not done, move on
            else:
                valstr = valstr[1]; # Move on with the last digit
        if len(valstr) == 1:
            # We should already have taken care of all of the special cases.
            retval += self._ones[int(valstr)]
        
        return retval
    
    def __repr__(self):
        """
        Convert StrUnsignedShort to a string using number words.
        """
        # Get this special case out of the way...
        if self._value == 0:
            return "zero"
        
        retval = ""
        whatsLeft = self._value
        # Use number values instead of strings this time
        h = whatsLeft // 100
        if h > 0:
            retval += self._ones[h]  # Look up the word for it and append it
            retval += " hundred "    # Append hundred
            whatsLeft -= h * 100     # Get rid of the hundreds.
        
        # Handle the tweens, and 10
        if 10 <= whatsLeft <= 19:
            if whatsLeft == 10:
                retval += "ten"
                whatsLeft = 0
            else:
                retval += self._tweens[whatsLeft]
                whatsLeft = 0
                
        # Handle the 10s column
        t = whatsLeft // 10
        if t > 0:
            retval += self._tens[t] + " "
            whatsLeft -= t * 10
            
        # Handle the 1s column
        if (whatsLeft > 0):
            retval += self._ones[whatsLeft]
            
        return retval
            
    
    
    # Part 4 - Bonus operators
    # These are other operators that might be appropriate for the StrUnsignedShort
    # type that weren't included in the requirements.
    
    def __mod__(self, other):
        """
        Modulo. (x % y)
        Return value should be int.
        """
        if isinstance(other, StrUnsignedShort):
            return self.get_value() % other.get_value()
        elif isinstance(other, int):
            return self.get_value() % other
        elif (isinstance(other, str)):
            return self.get_value() % StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
    
    def __rmod__(self, other):
        """
        Reverse Modulo. (y % x)
        Return value should be int.
        """
        if isinstance(other, StrUnsignedShort):
            return other.get_value() % self.get_value()
        elif isinstance(other, int):
            return other % self.get_value() 
        elif (isinstance(other, str)):
            return StrUnsignedShort(other).get_value() % self.get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
    
    def __pow__(self, other):
        """
        Raise to a power (x ** y)
        Return value should be a StrUnsignedShort if it is in range, int otherwise
        """
        retval = 0
        if isinstance(other, StrUnsignedShort):
            retval = self.get_value() ** other.get_value()
        elif isinstance(other, (int,float)):
            retval = self.get_value() ** other
        elif (isinstance(other, str)):
            retval = self.get_value() ** StrUnsignedShort(other).get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
            
        if isinstance(retval, int) and 0 <= retval <= 255:
            return StrUnsignedShort(retval)
        else:
            return retval 
        
    def __rpow__(self, other):
        """
        Reversed raise to a power (y ** x)
        Return value should be whatever other's type was
        """
        retval = other
        if isinstance(other, (int,float)):
            retval = other ** self.get_value()
        elif (isinstance(other, str)):
            retval = StrUnsignedShort(other).get_value() ** self.get_value()
        else:
            raise TypeError("Invalid type for operation with StrUnsignedShort")
        
        return retval
            
        
    def __int__(self):
        """
        Convert to integer.
        """
        return self.get_value()
    
    def __float__(self):
        """
        Convert to floating point value.
        """
        return float(self.get_value())