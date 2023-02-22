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

if __name__ == '__main__':
    if '--test' in sys.argv:
        _test()
    else:
        _main()