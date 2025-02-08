/**
 *  This is our "intermediate representation" of the program,
 *  we can use the IR to output in any different number of formats
 *  with different amounts of information included.
 */
public class C6461AssemblerCode {
    /** address where this machine code will go */
    public Short address;
    /** 16 bit binary representation of the machine code */
    public Short code;
    /** just the opcode portion of `code` */
    public Short opcode;
    /** assembly mnemonic for this chunk of machine code */
    public String mnemonic;
    /** assembly parameters for this chunk of machine code */
    public String[] args;
    /** label associated with this chunk of code */
    public String label;
    /** comment associated with this line of source */
    public String comment;
    /** marks if this chunk is only a comment<br><br>
     *  if this is true the other fields will be null
     */
    public boolean comment_only;
}
