#!/usr/bin/env python3
# Dissasembles a C6461 loadfile
from sys import argv

class OPCODES:
    HLT   =  0b000000 # Halt
    LDR   =  0b000001 # Load Register
    STR   =  0b000010 # Store Register
    LDA   =  0b000011 # Load Address
    LDX   =  0b100001 # Load Index Register
    STX   =  0b100010 # Store Index Register
    JZ    =  0b001000 # Jump If Zero
    JNE   =  0b001001 # Jump If Not Equal
    JCC   =  0b001010 # Jump If Condition Code
    JMA   =  0b001011 # Unconditional Jump
    JSR   =  0b001100 # Jump To Subroutine
    RFS   =  0b001101 # Return From Subroutine
    SOB   =  0b001110 # Subtract One And Branch
    JGE   =  0b001111 # Jump Greater Then or Equal To
    AMR   =  0b000100 # Add Memory To Register
    SMR   =  0b000101 # Subtract Memory From Register
    AIR   =  0b000110 # Add Immediate To Register
    SIR   =  0b000111 # Subtract Immediate From Register
    MLT   =  0b111000 # Multiply Register by Register
    DVD   =  0b111001 # Divide Register by Register
    TRR   =  0b111010 # Test the Equality of Register and Register
    AND   =  0b111011 # Logical And of Register and Register
    ORR   =  0b111100 # Logical Or of Register and Register
    NOT   =  0b111101 # Logical Not of Register To Register
    SRC   =  0b011001 # Shift Register by Count
    RRC   =  0b011010 # Rotate Register by Count
    IN    =  0b110001 # Input Character To Register from Device
    OUT   =  0b110010 # Output Character to Device from Register
    CHK   =  0b110011 # Check Device Status to Register
    FADD  =  0b011011 # Floating Add Memory To Register
    FSUB  =  0b011100 # Floating Subtract Memory From Register
    VADD  =  0b011101 # Vector Add
    VSUB  =  0b011110 # Vector Subtract
    CNVRT =  0b011111 # Convert to Fixed/FloatingPoint
    LDFR  =  0b101000 # Load Floating Register From Memory
    STFR  =  0b101001 # Store Floating Register To Memory

OP_TO_MNEM = {
    0b000000: "HLT",
    0b000001: "LDR",
    0b000010: "STR",
    0b000011: "LDA",
    0b100001: "LDX",
    0b100010: "STX",
    0b001000: "JZ",
    0b001001: "JNE",
    0b001010: "JCC",
    0b001011: "JMA",
    0b001100: "JSR",
    0b001101: "RFS",
    0b001110: "SOB",
    0b001111: "JGE",
    0b000100: "AMR",
    0b000101: "SMR",
    0b000110: "AIR",
    0b000111: "SIR",
    0b111000: "MLT",
    0b111001: "DVD",
    0b111010: "TRR",
    0b111011: "AND",
    0b111100: "ORR",
    0b111101: "NOT",
    0b011001: "SRC",
    0b011010: "RRC",
    0b110001: "IN",
    0b110010: "OUT",
    0b110011: "CHK",
    0b011011: "FADD",
    0b011100: "FSUB",
    0b011101: "VADD",
    0b011110: "VSUB",
    0b011111: "CNVRT",
    0b101000: "LDFR",
    0b101001: "STFR",
}

class OFFSETS:
    OPCODE    = [16, 10]
    REGISTER  = [10, 8]
    FLOATREG  = REGISTER
    REGX      = REGISTER
    INDEXREG  = [8, 6]
    REGY      = INDEXREG
    INDIRECT  = [6, 5]
    ADDRESS   = [5, 0]
    ARITHLOG  = [8, 7]
    LEFTRIGHT = [7, 6]
    COUNT     = [4, 0]
    DEVID     = COUNT
    SIGN      = [16, 15]
    EXPONENT  = [15, 8]
    MANTISSA  = [8, 0]
    HLT     = [OPCODE]
    LDR     = [OPCODE, REGISTER, INDEXREG, INDIRECT, ADDRESS]
    STR     = LDR
    LDA     = LDR
    LDX     = LDR
    STX     = LDR 
    JZ      = LDR
    JNE     = LDR
    # REGISTER = Condition Code
    JCC     = LDR
    JMA     = LDR
    JSR     = LDR
    # Immediate Ins. ADDR = IMMEDIATE
    RFS     = LDR
    SOB     = LDR
    JGE     = LDR
    AMR     = LDR
    SMR     = LDR
    # Immediate Ins. ADDR = IMMEDIATE
    AIR     = LDR
    # Immediate Ins. ADDR = IMMEDIATE
    SIR     = LDR
    MLT     = [OPCODE, REGX, REGY]
    DVD     = MLT
    TRR     = MLT
    AND     = MLT
    ORR     = MLT
    NOT     = MLT
    SRC     = [OPCODE, REGISTER, ARITHLOG, LEFTRIGHT, COUNT]
    RRC     = SRC
    IN      = [OPCODE, REGISTER, DEVID]
    OUT     = IN
    CHK     = IN
    FADD    = [OPCODE, FLOATREG, INDEXREG, INDIRECT, ADDRESS]
    FSUB    = FADD
    VADD    = FADD
    VSUB    = FADD
    CNVRT   = LDR
    LDFR    = FADD
    STFR    = FADD

def halt(opcode):
    print('HLT')

def loadRegister(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def loadAddress(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def loadIndex(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def storeRegister(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def storeIndex(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def jumpZero(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def jumpNotEqual(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def jumpConditionCode(opcode, condition_code, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(condition_code)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def jumpUnconditional(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def jumpSubroutine(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def returnSubroutine(opcode, register, index, indirect, immediate):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(immediate)

    print(out)

def subtractOneBranch(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def jumpGreaterOrEqual(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def addMemoryRegister(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def subtractMemoryRegister(opcode, register, index, indirect, address):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(index)
    out += ','
    out += str(address)
    if indirect != 0:
        out += ','
        out += str(indirect)
    
    print(out)

def addImmediateRegister(opcode, register, index, indirect, immediate):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(immediate)
    
    print(out)

def subtractImmediateRegister(opcode, register, index, indirect, immediate):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(immediate)
    
    print(out)

def multiplyReg(opcode, register_x, register_y):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register_x)
    out += ','
    out += str(register_y)
    
    print(out)

def divideReg(opcode, register_x, register_y):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register_x)
    out += ','
    out += str(register_y)
    
    print(out)

def equalityReg(opcode, register_x, register_y):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register_x)
    out += ','
    out += str(register_y)
    
    print(out)

def bitwiseAndReg(opcode, register_x, register_y):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register_x)
    out += ','
    out += str(register_y)
    
    print(out)

def bitwiseOrReg(opcode, register_x, register_y):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register_x)
    out += ','
    out += str(register_y)
    
    print(out)

def bitwiseNotReg(opcode, register_x, register_y):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register_x)
    
    print(out)

def shiftReg(opcode, register, arith_log, left_right, count):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(count)
    out += ','
    out += str(left_right)
    out += ','
    out += str(arith_log)

    print(out)

def rotateReg(opcode, register, arith_log, left_right, count):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(count)
    out += ','
    out += str(left_right)
    out += ','
    out += str(arith_log)

    print(out)

def inputCharRegDev(opcode, register, devid):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(devid)

    print(out)

def outputCharRegDev(opcode, register, devid):
    out = ''
    out += OP_TO_MNEM[opcode]
    out += ' '
    out += str(register)
    out += ','
    out += str(devid)

    print(out)

def get_args(code, offsets):
    out = []
    i = 0
    while i < len(offsets):
        start = offsets[i][0]
        end = offsets[i][1]
        out.append((code >> end) & ((1 << (start - end)) - 1))
        i += 1

    return out

loadfile_path = argv[1]
data_segment_start = int(argv[2])

with open(loadfile_path) as loadfile:
    line = loadfile.readline()
    expected_addr = 0
    while (line != ""):
        parts = line.split()
        addr = int(parts[0], base=8)
        code = int(parts[1], base=8)
        if addr != expected_addr:
            print(f'LOC {addr}')
            expected_addr = addr+1
        else:
            expected_addr = expected_addr+1
        
        if addr < data_segment_start:
            code = f'DATA {code}'
            print(f'{code}')
            line = loadfile.readline()
            continue

        opcode = (code  >> 10)

        match opcode:
            case OPCODES.HLT:
                args = get_args(code, OFFSETS.HLT)
                halt(args[0])
                
            case OPCODES.LDR:
                args = get_args(code, OFFSETS.LDR)
                loadRegister(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.LDA:
                args = get_args(code, OFFSETS.LDA)
                loadAddress(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.LDX:
                args = get_args(code, OFFSETS.LDX)
                loadIndex(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.STR:
                args = get_args(code, OFFSETS.STR)
                storeRegister(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.STX:
                args = get_args(code, OFFSETS.STX)
                storeIndex(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.JZ:
                args = get_args(code, OFFSETS.JZ)
                jumpZero(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.JNE:
                args = get_args(code, OFFSETS.JNE)
                jumpNotEqual(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.JCC:
                args = get_args(code, OFFSETS.JCC)
                jumpConditionCode(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.JMA:
                args = get_args(code, OFFSETS.JMA)
                jumpUnconditional(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.JSR:
                args = get_args(code, OFFSETS.JSR)
                jumpSubroutine(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.RFS:
                args = get_args(code, OFFSETS.RFS)
                returnSubroutine(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.SOB:
                args = get_args(code, OFFSETS.SOB)
                subtractOneBranch(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.JGE:
                args = get_args(code, OFFSETS.JGE)
                jumpGreaterOrEqual(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.AMR:
                args = get_args(code, OFFSETS.AMR)
                addMemoryRegister(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.SMR:
                args = get_args(code, OFFSETS.SMR)
                subtractMemoryRegister(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.AIR:
                args = get_args(code, OFFSETS.AIR)
                addImmediateRegister(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.SIR:
                args = get_args(code, OFFSETS.SIR)
                subtractImmediateRegister(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.MLT:
                args = get_args(code, OFFSETS.MLT)
                multiplyReg(args[0], args[1], args[2])
                
            case OPCODES.DVD:
                args = get_args(code, OFFSETS.DVD)
                divideReg(args[0], args[1], args[2])
                
            case OPCODES.TRR:
                args = get_args(code, OFFSETS.TRR)
                equalityReg(args[0], args[1], args[2])
                
            case OPCODES.AND:
                args = get_args(code, OFFSETS.AND)
                bitwiseAndReg(args[0], args[1], args[2])
                
            case OPCODES.ORR:
                args = get_args(code, OFFSETS.ORR)
                bitwiseOrReg(args[0], args[1], args[2])
                
            case OPCODES.NOT:
                args = get_args(code, OFFSETS.NOT)
                bitwiseNotReg(args[0], args[1], args[2])
                
            case OPCODES.SRC:
                args = get_args(code, OFFSETS.SRC)
                shiftReg(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.RRC:
                args = get_args(code, OFFSETS.RRC)
                rotateReg(args[0], args[1], args[2], args[3], args[4])
                
            case OPCODES.IN:
                args = get_args(code, OFFSETS.IN)
                inputCharRegDev(args[0], args[1], args[2])
                
            case OPCODES.OUT:
                args = get_args(code, OFFSETS.OUT)
                outputCharRegDev(args[0], args[1], args[2])
                
            case _:
                ...
        
        line = loadfile.readline()

