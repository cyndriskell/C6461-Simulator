package C6461AssemblerExceptions;

public class UnknownLabelException extends Exception {
    public String label;

    public UnknownLabelException(String label) {
        this.label = label;
    }
}
