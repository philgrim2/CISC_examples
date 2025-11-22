import stdio
import sys
from element import Element
from instream import InStream

class PeriodicTable:
    """
    Container class for a collection of Elements.
    """
    
    def __init__(self, file = None):
        # If there's a filename, treat it as a properly-formatted
        # csv file.  If not, create an empty instance.
        
        # Dictionary to hold the elements and index them by symbol
        self._elements = dict()
        if file != None:
            inputstream = InStream(file)
            # Assume the file has a header as the first line.
            # We can ignore the header.
            if inputstream.hasNextLine(): inputstream.readLine()
            # Read the file one line at a time, parsing each line.
            while inputstream.hasNextLine():
                line = inputstream.readLine()
                fields = line.split(sep = ',')
                # Field order: atomic_number, name, symbol, group, period,
                # element_category, atomic_weight, phase
                atomicNumber = int(fields[0].strip())
                name = fields[1].strip()
                symbol= fields[2].strip()
                gstr = fields[3].strip()
                try:
                    group = int(gstr)
                except:
                    group = None
                pstr = fields[4].strip()
                try:
                    period = int(pstr)
                except:
                    period = None
                category = fields[5].strip()
                atomicWeight = float(fields[6].strip())
                phase = fields[6].strip()
    
                e = Element(name, symbol, atomicNumber, atomicWeight, group, period,  
                            category, phase)
                self._elements[symbol] = e
    
    def __len__(self):
        return len(self._elements)
    
    def __getitem__(self, key):
        if isinstance(key, str):
            if key in self._elements:
                return self._elements[key]
            else:
                for k, v in self._elements.items():
                    if v.name() == key:
                        return v
        elif isinstance(key, float):
            for k, v in self._elements.items():
                if v.atomicWeight() == key:
                    return v
        elif isinstance(key, int):
            for k, v in self._elements.items():
                if v.atomicNumber() == key:
                    return v

        return None
    
    def run(self):
        """
        Presents a terminal interface that allows the user to enter
        a chemical formula and receive the molecular weight of that formula.
        """
        stdio.writeln("Formula: ")
        running = True
        while running and not stdio.isEmpty():            
            formula = stdio.readString()
            if formula.lower() in ['quit','exit','stop']:
                running = False
                break
            try:    
                weight = self._parse(formula)
                stdio.writef("Molecular weight: %.6f\n", weight)
            except Exception as e:
                stdio.writeln(str(e))
            stdio.writeln("Formula: ")
    def _parse(self, formula):
        """
        Parses a chemical formula, looks up the weight of each element,
        and totals the molecular weight.
        """
        weight = 0.0
        cur = 0.0
        i = 0
        while i < len(formula):
            symbol = formula[i]
            if symbol.isdigit():
                while i + 1 < len(formula) and formula[i+1].isdigit():
                    i += 1
                    symbol += formula[i]
                mult = int(symbol)
                cur *= mult
                weight += cur
                cur = 0.0
            elif symbol.isupper():
                weight += cur
                cur = 0.0
                if i + 1 < len(formula) and formula[i+1].islower():
                    i += 1
                    symbol += formula[i]
                elem = self[symbol]
                if elem == None:
                    raise ValueError(f'No element for symbol {symbol}')
                cur = elem.atomicWeight()
            else:
                raise ValueError(f'Invalid input {symbol}')
            i += 1
            weight += cur
        return weight
    
def _test():
    # Make a periodic table from the default file
    pertab = PeriodicTable('periodic_table.csv')
    # Find an element by name
    e = pertab["Hydrogen"]
    stdio.writeln(str(e))
    # Find an element by symbol
    e = pertab["He"]
    stdio.writeln(str(e))
    # Find an element by atomic weight
    e = pertab[10.81]
    stdio.writeln(str(e))
     # Find an element by atomic number
    e = pertab[3]
    stdio.writeln(str(e))

def _main():
    filename = sys.argv[1]
    
    pertab = PeriodicTable(filename)
    pertab.run()

if __name__ == '__main__':
    if '--test' in sys.argv:
        _test()
    else:
        _main()