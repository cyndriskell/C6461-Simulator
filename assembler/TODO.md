1. Fix handling of whitespace in source lines. if there is whitespace in a source line assembly of that line fails
2. Add error handling for incorrect assembly syntax, specifc example: if the assembler is given a line with a indirect value > 1 it will still left-shift it and or it. THIS IS WRONG!!!!
