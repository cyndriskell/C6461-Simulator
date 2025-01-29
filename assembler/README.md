# C6461Assembler

## Synopsis
C6461Assembler [-hvVl] [-f _format_] [-o _out_file_] _source_file_

## Options
`-h --help`: Prints usage information

`-V --version`: Prints version information

`-v --verbose`: Gives more debugging information during the assembly process

`-f --format`: Specify output format, by default the format will be `binary`

&nbsp&nbsp`octal`: Outputs a file in the format of:

&nbsp&nbsp&nbsp&nbsp`Address\tMachine Code\tSymbolic Representation` for each instruction

&nbsp&nbsp`hexadecimal`: Same as `octal` but using hexadecimal instead

&nbsp&nbsp`binary`: Outputs a flat machine code binary for use with the C6461 Simulator

`-l --listing`: Generate a listing file in the `octal` format

`-o --output`: Specifies output filename, by default the name will be `a.out`

