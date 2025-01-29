package C6461AssemblerExceptions;

public class InvalidInstructionException extends Exception {
    public String instruction;

    public InvalidInstructionException(String instruction) {
        this.instruction = instruction;
    }
};
