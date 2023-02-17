import stdio

class Element:
    """
    Class to represent the attributes of an element in the periodic table.
    """
    
    def __init__(self, name, symbol, atomicNumber, atomicWeight, group, period, category, phase):
        """
        Initializes an instance of Element.
        """
        self._name = name
        self._symbol = symbol
        self._atomicNumber = atomicNumber
        self._atomicWeight = atomicWeight
        self._group = group
        self._period = period
        self._category = category
        self._phase = phase
        
    def name(self):
        """
        Accessor for the element's full name, e.g. Hydrogen
        """
        return self._name
    
    def symbol(self):
        """
        Accessor for the element's symbol, e.g. H
        """
        return self._symbol
    
    def atomicNumber(self):
        """
        Accessor for the element's atomic number, e.g. 1
        """
        return self._atomicNumber
    
    def atomicWeight(self):
        """
        Accessor for the element's atomic weight in amu (atomic mass units), e.g. 1.00082
        """
        return self._atomicWeight
    
    def group(self):
        """
        Accessor for the element's group number (column in the periodic table), e.g. 1
        Some elements will not have a group (represented by None)
        """
        return self._group
    
    def period(self):
        """
        Accessor for the element's period number (row in the periodic table), e.g. 1
        """
        return self._period
    
    def category(self):
        """
        Accessor for the element's broad classification, e.g. diatomic_nonmetal
        """
        return self._category
    
    def phase(self):
        """
        Accessor for the element's phase, e.g. gas
        """
        return self._phase
    
    def __str__(self):
        return f'{self._name} ({self._symbol}), Atomic number: {self._atomicNumber}, Atomic weight: {self._atomicWeight}'
    
    
def main():
    hydrogen = Element('Hydrogen', 'H', 1, 1.008, 1, 1, 'diatomic_nonmetal', 'gas')
    stdio.writeln(str(hydrogen))
    stdio.writef('%s is a %s, found as a %s\n', hydrogen.name(), hydrogen.category(), hydrogen.phase())
    
if __name__ == '__main__':
    main()