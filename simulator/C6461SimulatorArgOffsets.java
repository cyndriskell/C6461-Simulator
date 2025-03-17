// This maps assembly mnemonics to an array of "offset pairs" into an assembled instruction of a specific
// type, which will be used by getArgs to extract the arguments of the instruction
// The pairs are ranges in the form (]
public class C6461SimulatorArgOffsets {
    public static final short[] OPCODE    = {16, 10};
    public static final short[] REGISTER  = {10, 8};
    public static final short[] FLOATREG  = REGISTER;
    public static final short[] REGX      = REGISTER;
    public static final short[] INDEXREG  = {8, 6};
    public static final short[] REGY      = INDEXREG;
    public static final short[] INDIRECT  = {6, 5};
    public static final short[] ADDRESS   = {5, 0};
    public static final short[] ARITHLOG  = {8, 7};
    public static final short[] LEFTRIGHT = {7, 6};
    public static final short[] COUNT     = {4, 0};
    public static final short[] DEVID     = COUNT;
    public static final short[] SIGN      = {16, 15};
    public static final short[] EXPONENT  = {15, 8};
    public static final short[] MANTISSA  = {8, 0};

    public static final short[][] HLT     = {OPCODE};
    public static final short[][] LDR     = {OPCODE, REGISTER, INDEXREG, INDIRECT, ADDRESS};
    public static final short[][] STR     = LDR;
    public static final short[][] LDA     = LDR;
    public static final short[][] LDX     = LDR;
    public static final short[][] STX     = LDR; 
    public static final short[][] JZ      = LDR;
    public static final short[][] JNE     = LDR;
    // REGISTER = Condition Code
    public static final short[][] JCC     = LDR;
    public static final short[][] JMA     = LDR;
    public static final short[][] JSR     = LDR;
    // Immediate Ins. ADDR = IMMEDIATE
    public static final short[][] RFS     = LDR;
    public static final short[][] SOB     = LDR;
    public static final short[][] JGE     = LDR;
    public static final short[][] AMR     = LDR;
    public static final short[][] SMR     = LDR;
    // Immediate Ins. ADDR = IMMEDIATE
    public static final short[][] AIR     = LDR;
    // Immediate Ins. ADDR = IMMEDIATE
    public static final short[][] SIR     = LDR;
    public static final short[][] MLT     = {OPCODE, REGX, REGY};
    public static final short[][] DVD     = MLT;
    public static final short[][] TRR     = MLT;
    public static final short[][] AND     = MLT;
    public static final short[][] ORR     = MLT;
    public static final short[][] NOT     = MLT;
    public static final short[][] SRC     = {OPCODE, REGISTER, ARITHLOG, LEFTRIGHT, COUNT};
    public static final short[][] RRC     = SRC;
    public static final short[][] IN      = {OPCODE, REGISTER, DEVID};
    public static final short[][] OUT     = IN;
    public static final short[][] CHK     = IN;
    public static final short[][] FADD    = {OPCODE, FLOATREG, INDEXREG, INDIRECT, ADDRESS};
    public static final short[][] FSUB    = FADD;
    public static final short[][] VADD    = FADD;
    public static final short[][] VSUB    = FADD;
    public static final short[][] CNVRT   = LDR;
    public static final short[][] LDFR    = FADD;
    public static final short[][] STFR    = FADD;
}
