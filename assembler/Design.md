## General Flow
`Stage 1`: go through whole file incrementing address count as needed (including LOC directive), noting any labels as we go along

`Stage 2`: use `C6461AssemblerOpcodes`, `C6461AssemblerOpTypes`, and the labels `HashMap` to send the mnemonic and arguments for each line to the appropriate handler function which generates a `C6461AssemblerCode` object. All the code objects are pushed onto a `Vector`

`Emit Stage`: use the code `Vector` and the desired output format (and if a listing file is desired) to output the final result of the assembler

## Classes
`C6461Assembler`: contains the main method, as well as option handling, and the actual assembly.

`C6461AssemblerOptions`: contains a class representing the options for the assembler as well as argument parsing

`C6461AssemblerCode`: represents a sort of intermediate representation, allows for all aspects of emitted code to be in one place

`C6461AssemblerFormat`: enum representing the options for the output format of the assembler

`C6461AssemblerOpcodes`: contains a `Map<String, Short>` which maps mnemonics to opcode values

`C6461AssemblerOpType`: enum representing the different `OpType`s (see [OpTypes](#OpTypes))

`C6461AssemblerOpTypes`: contains a `Map<String, C6461AssemblerOpType>` which maps mnemonics to optypes

## OpTypes
While looking at the ISA documentation it was noted that many instructions shared argument and machine code layout styles. For this reason, each intruction is given a OpType which is then used to pass it in the assembly stage to the handler function for its type