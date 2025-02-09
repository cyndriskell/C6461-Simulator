import java.util.HashMap;
import java.util.Map;

public class C6461AssemblerOpcodes {
    public static final Map<String, Short> OPCODES = new HashMap<>();

    static {
        OPCODES.put("HLT"  , (short)0b000000); // Halt
        OPCODES.put("LDR"  , (short)0b000001); // Load Register
        OPCODES.put("STR"  , (short)0b000010); // Store Register
        OPCODES.put("LDA"  , (short)0b000011); // Load Address
        OPCODES.put("LDX"  , (short)0b100001); // Load Index Register
        OPCODES.put("STX"  , (short)0b100010); // Store Index Register
        OPCODES.put("JZ"   , (short)0b001000); // Jump If Zero
        OPCODES.put("JNE"  , (short)0b001001); // Jump If Not Equal
        OPCODES.put("JCC"  , (short)0b001010); // Jump If Condition Code
        OPCODES.put("JMA"  , (short)0b001011); // Unconditional Jump
        OPCODES.put("JSR"  , (short)0b001100); // Jump To Subroutine
        OPCODES.put("RFS"  , (short)0b001101); // Return From Subroutine
        OPCODES.put("SOB"  , (short)0b001110); // Subtract One And Branch
        OPCODES.put("JGE"  , (short)0b001111); // Jump Greater Then or Equal To
        OPCODES.put("AMR"  , (short)0b000100); // Add Memory To Register
        OPCODES.put("SMR"  , (short)0b000101); // Subtract Memory From Register
        OPCODES.put("AIR"  , (short)0b000110); // Add Immediate To Register
        OPCODES.put("SIR"  , (short)0b000111); // Subtract Immediate From Register
        OPCODES.put("MLT"  , (short)0b111000); // Multiply Register by Register
        OPCODES.put("DVD"  , (short)0b111001); // Divide Register by Register
        OPCODES.put("TRR"  , (short)0b111010); // Test the Equality of Register and Register
        OPCODES.put("AND"  , (short)0b111011); // Logical And of Register and Register
        OPCODES.put("ORR"  , (short)0b111100); // Logical Or of Register and Register
        OPCODES.put("NOT"  , (short)0b111101); // Logical Not of Register To Register
        OPCODES.put("SRC"  , (short)0b011001); // Shift Register by Count
        OPCODES.put("RRC"  , (short)0b011010); // Rotate Register by Count
        OPCODES.put("IN"   , (short)0b110001); // Input Character To Register from Device
        OPCODES.put("OUT"  , (short)0b110010); // Output Character to Device from Register
        OPCODES.put("CHK"  , (short)0b110011); // Check Device Status to Register
        OPCODES.put("FADD" , (short)0b011011); // Floating Add Memory To Register
        OPCODES.put("FSUB" , (short)0b011100); // Floating Subtract Memory From Register
        OPCODES.put("VADD" , (short)0b011101); // Vector Add
        OPCODES.put("VSUB" , (short)0b011110); // Vector Subtract
        OPCODES.put("CNVRT", (short)0b011111); // Convert to Fixed/FloatingPoint
        OPCODES.put("LDFR" , (short)0b101000); // Load Floating Register From Memory
        OPCODES.put("STFR" , (short)0b101001); // Store Floating Register To Memory
    }

}
