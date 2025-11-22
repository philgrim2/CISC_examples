from instream import InStream
from outstream import OutStream

ins = InStream("8ints.txt")
out = OutStream("example_out.txt")

while ins.hasNextLine():
    line = ins.readLine()
    out.writeln(line)

