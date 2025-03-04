import java.util.HashMap;
import java.util.Map;

// This maps assembly mnemonics to an array of "offset pairs" into an assembled instruction of a specific
// type, which will be used by getArgs to extract the arguments of the instruction
public class C6461SimulatorArgOffsets {
    public static final Map<String, short[]> OFFSETS = new HashMap<>();

    static {
        OFFSETS.put("HLT"  , new short[] {}); // Halt
        // Load/Store instructions, offsets: (OPCODE, REG, IDX, IND, ADDR)
        OFFSETS.put("LDR"  , new short[] {16, 10, 8, 6, 5, 0}); // Load Register
        OFFSETS.put("STR"  , new short[] {16, 10, 8, 6, 5, 0}); // Store Register
        OFFSETS.put("LDA"  , new short[] {16, 10, 8, 6, 5, 0}); // Load Address
        OFFSETS.put("LDX"  , new short[] {16, 10, 8, 6, 5, 0}); // Load Index Register
        OFFSETS.put("STX"  , new short[] {16, 10, 8, 6, 5, 0}); // Store Index Register
        OFFSETS.put("JZ"   , new short[] {}); // Jump If Zero
        OFFSETS.put("JNE"  , new short[] {}); // Jump If Not Equal
        OFFSETS.put("JCC"  , new short[] {}); // Jump If Condition Code
        OFFSETS.put("JMA"  , new short[] {}); // Unconditional Jump
        OFFSETS.put("JSR"  , new short[] {}); // Jump To Subroutine
        OFFSETS.put("RFS"  , new short[] {}); // Return From Subroutine
        OFFSETS.put("SOB"  , new short[] {}); // Subtract One And Branch
        OFFSETS.put("JGE"  , new short[] {}); // Jump Greater Then or Equal To
        OFFSETS.put("AMR"  , new short[] {}); // Add Memory To Register
        OFFSETS.put("SMR"  , new short[] {}); // Subtract Memory From Register
        OFFSETS.put("AIR"  , new short[] {}); // Add Immediate To Register
        OFFSETS.put("SIR"  , new short[] {}); // Subtract Immediate From Register
        OFFSETS.put("MLT"  , new short[] {}); // Multiply Register by Register
        OFFSETS.put("DVD"  , new short[] {}); // Divide Register by Register
        OFFSETS.put("TRR"  , new short[] {}); // Test the Equality of Register and Register
        OFFSETS.put("AND"  , new short[] {}); // Logical And of Register and Register
        OFFSETS.put("ORR"  , new short[] {}); // Logical Or of Register and Register
        OFFSETS.put("NOT"  , new short[] {}); // Logical Not of Register To Register
        OFFSETS.put("SRC"  , new short[] {}); // Shift Register by Count
        OFFSETS.put("RRC"  , new short[] {}); // Rotate Register by Count
        OFFSETS.put("IN"   , new short[] {}); // Input Character To Register from Device
        OFFSETS.put("OUT"  , new short[] {}); // Output Character to Device from Register
        OFFSETS.put("CHK"  , new short[] {}); // Check Device Status to Register
        OFFSETS.put("FADD" , new short[] {}); // Floating Add Memory To Register
        OFFSETS.put("FSUB" , new short[] {}); // Floating Subtract Memory From Register
        OFFSETS.put("VADD" , new short[] {}); // Vector Add
        OFFSETS.put("VSUB" , new short[] {}); // Vector Subtract
        OFFSETS.put("CNVRT", new short[] {}); // Convert to Fixed/FloatingPoint
        OFFSETS.put("LDFR" , new short[] {}); // Load Floating Register From Memory
        OFFSETS.put("STFR" , new short[] {}); // Store Floating Register To Memory
    }

}
