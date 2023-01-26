import stdio

def complementWC(dna_string):
    """
    Generates the Watson-Crick complement of a DNA string.
    """
    complement = ""
    for char in dna_string:
        if   char == 'A':  complement += 'T'
        elif char == 'T':  complement += 'A'
        elif char == 'C':  complement += 'G'
        elif char == 'G':  complement += 'C'
    return complement

def main():
    """
    Test the complementWC() function.
    """
    dna = 'ATGCCTACTG'
    stdio.writef('DNA String: %s, Watson-Crick complement: %s', dna, complementWC(dna))
    
if __name__ == '__main__':
    main()