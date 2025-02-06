import java.util.HashMap;
import java.util.Map;

public class C6461AssemblerOpcodes {
    public static final Map<String, String> OPCODES = new HashMap<>();

    static {
        OPCODES.put("LDR", "000001"); // Load Register
        OPCODES.put("STR", "000010"); // Store Register
        OPCODES.put("LDA", "000011"); // Load Address
        OPCODES.put("LDX", "100001"); // Load Index Register
        OPCODES.put("STX", "100010"); // Store Index Register
    }

}
