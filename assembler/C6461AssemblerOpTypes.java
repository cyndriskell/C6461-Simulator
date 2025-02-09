import java.util.HashMap;
import java.util.Map;

public class C6461AssemblerOpTypes {
    public static final Map<String, C6461AssemblerOpType> TYPES = new HashMap<>();

    static {
        TYPES.put("HLT"  , C6461AssemblerOpType.HALT);
        TYPES.put("LDR"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("STR"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("LDA"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("LDX"  , C6461AssemblerOpType.LOAD_STORE_IDX);
        TYPES.put("STX"  , C6461AssemblerOpType.LOAD_STORE_IDX);
        TYPES.put("JZ"   , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("JNE"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("JCC"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("JMA"  , C6461AssemblerOpType.LOAD_STORE_IDX);
        TYPES.put("JSR"  , C6461AssemblerOpType.LOAD_STORE_IDX);
        TYPES.put("RFS"  , C6461AssemblerOpType.IMMEDIATE);
        TYPES.put("SOB"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("JGE"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("AMR"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("SMR"  , C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("AIR"  , C6461AssemblerOpType.REG_IMM);
        TYPES.put("SIR"  , C6461AssemblerOpType.REG_IMM);
        TYPES.put("MLT"  , C6461AssemblerOpType.REG_REG);
        TYPES.put("DVD"  , C6461AssemblerOpType.REG_REG);
        TYPES.put("TRR"  , C6461AssemblerOpType.REG_REG);
        TYPES.put("AND"  , C6461AssemblerOpType.REG_REG);
        TYPES.put("ORR"  , C6461AssemblerOpType.REG_REG);
        TYPES.put("NOT"  , C6461AssemblerOpType.REG);
        TYPES.put("SRC"  , C6461AssemblerOpType.SHIFT_ROTATE);
        TYPES.put("RRC"  , C6461AssemblerOpType.SHIFT_ROTATE);
        TYPES.put("IN"   , C6461AssemblerOpType.REG_IMM);
        TYPES.put("OUT"  , C6461AssemblerOpType.REG_IMM);
        TYPES.put("CHK"  , C6461AssemblerOpType.REG_IMM);
        TYPES.put("FADD" , C6461AssemblerOpType.FLOATING);
        TYPES.put("FSUB" , C6461AssemblerOpType.FLOATING);
        TYPES.put("VADD" , C6461AssemblerOpType.FLOATING);
        TYPES.put("VSUB" , C6461AssemblerOpType.FLOATING);
        TYPES.put("CNVRT", C6461AssemblerOpType.LOAD_STORE);
        TYPES.put("LDFR" , C6461AssemblerOpType.FLOATING);
        TYPES.put("STFR" , C6461AssemblerOpType.FLOATING);
    }
    
}
