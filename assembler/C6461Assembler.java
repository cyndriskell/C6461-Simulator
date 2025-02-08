import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO! Implement better error handling
class C6461Assembler {
	static final String VERSION = "v0.0.1-dev";

	public static void main(String args[]) throws Exception {
		//System.out.println("C6461Assembler " + VERSION);
		C6461AssemblerOptions options = C6461AssemblerOptions.from_args(args);
 
		if(options.help) {
			System.out.println("C6461Assembler [-hvVl] [-f format] [-o out_file] source_file\n");
			System.out.println("-h --help: Prints usage information");
			System.out.println("-V --version: Prints version information");
			System.out.println("-v --verbose: Gives more debugging information during the assembly process");
			System.out.println("-f --format: Specify output format, by default the format will be `binary`");
			System.out.println("\toctal: Outputs a file in the format of:");
			System.out.println("\t\t\"Address\\tMachine Code\\tSymbolic Representation\" for each mnemonic");
			System.out.println("\thexadecimal: Same as `octal` but using hexadecimal instead");
			System.out.println("\tbinary: Outputs a flat machine code binary for use with the C6461 Simulator");
			System.out.println("\tloadfile: Outputs in the loadfile format specified in the C6461 docs");
			System.out.println("-l --listing: Generate a listing file in the `octal` format");
			System.out.println("-o --output: Specifies output filename, by default the name will be `a.out`");

			System.exit(0);
		}
		if(options.version) {
			System.out.println("C6461Assembler "+VERSION);
			System.exit(0);
		}
		if(options.input == null) {
			System.err.println("An input file must be provided.\nC6461Assembler --help");
			System.exit(1);
		}
		
		FileReader input_file;
		int bufSize;
		try {
			File input = new File(options.input);
			// I would be very suprised if we encounter an assembly source file larger than 2GiB, so this cast is OK
			bufSize = (int)input.length();
			input_file = new FileReader(input);
		} catch (FileNotFoundException e) {
			System.err.println("File '"+options.input+"' not found.");
			System.exit(1);
			// This lets javac know that if this catch is reached all code after is unreachable, prevents "Variable may not be initalized" nonsense.
			return;
		}

		char[] buf = new char[bufSize];
		input_file.read(buf);
		input_file.close();

		String input_string = String.valueOf(buf);

		Vector<C6461AssemblerCode> code = new Vector<>();
		HashMap<String, Short> labels = new HashMap<>();
		Short current_address = 0;
		int line_num = 0;

		// Pass 1
		for (String line: input_string.split("\n")) {
			line_num++;
			current_address = pass1_parse_labels(line, labels, current_address, line_num);
			System.err.println(current_address);
		}

		System.err.println(labels);
		current_address = 0;
		line_num = 0;

		// Pass 2
		for (String line: input_string.split("\n")) {
			pass2_assemble(line, code, labels, current_address, line_num);
		}

		// Emit
		emit(code, options.format, options.listing, options.output);
	}

	// Check if an instruction/directive mnemonic is valid
	static String check_instruction(String mnemonic) {
		// Trim whitespace
        mnemonic = mnemonic.trim();
        if (mnemonic.isEmpty()) {
            return "Error: Empty line detected, skipping";
        }

        // **Fix mnemonic splitting to properly handle spaces and commas**
        String[] parts = mnemonic.replace(",", " ").split("\\s+");

        // Check if the mnemonic has exactly 4 parts
        if (parts.length != 4) {
            return "Error: Instruction format incorrect -> " + mnemonic;
        }

        String opcode = C6461AssemblerOpcodes.OPCODES.get(parts[0]); // Get Opcode
        if (opcode == null) {
            return "Error: Invalid mnemonic " + parts[0];
        }

        try {
            int regNum = Integer.parseInt(parts[1]);  // R register
            int ixNum = Integer.parseInt(parts[2]);   // IX index register
            int addressNum = Integer.parseInt(parts[3]); // Target address

            // **Validate number ranges**
            if (regNum < 0 || regNum > 3) return " : R register out of range (0-3) -> " + mnemonic;
            if (ixNum < 0 || ixNum > 3) return "Error: IX register out of range (0-3) -> " + mnemonic;
            if (addressNum < 0 || addressNum > 31) return "Error: Address out of range (0-31) -> " + mnemonic;

            // Convert to binary format
            String r = String.format("%2s", Integer.toBinaryString(regNum)).replace(' ', '0'); // 2-bit R
            String ix = String.format("%2s", Integer.toBinaryString(ixNum)).replace(' ', '0'); // 2-bit IX
            String i = "0"; // Immediate addressing (I-bit), default is 0
            String address = String.format("%5s", Integer.toBinaryString(addressNum)).replace(' ', '0'); // 5-bit address

            // Combine to form 16-bit machine code
            String machineCode = opcode + r + ix + i + address;

            // Convert to hexadecimal
            int hexValue = Integer.parseInt(machineCode, 2);
            String hexString = String.format("0x%04X", hexValue);

            return mnemonic + " -> " + machineCode + " -> " + hexString;
        } catch (NumberFormatException e) {
            return "Error: Invalid number format -> " + mnemonic;
        }
		//TODO! check_instruction
		// return true;
	}

	static void error(String line, String message, int line_num) {
		System.err.println(String.format("Error: %s", message));
		System.err.println(String.format("on line %d", line_num));
		System.err.println(String.format("\t%s", line));
		System.exit(1);
	}

	static boolean is_directive(String mnemonic) {
		return mnemonic != null && (mnemonic.equals("DATA") || mnemonic.equals("LOC"));
	}

	static short handle_directive_address(String line, String mnemonic, String args, Short address, int line_num) {
		String[] argsvs; 
		short[] argsv;

		switch (mnemonic) {
			case "LOC":
				if (args == null) {
					error(line, "the LOC directive requires an argument", line_num);
				}

				argsvs = args.split(",");
				argsv = new short[argsvs.length];
				for (int i = 0; i < argsvs.length; i++) {
					try {
						argsv[i] = Short.parseShort(argsvs[i]);
					} catch (NumberFormatException e) {
						error(line, String.format("Invalid argument \"%s\" for LOC directive", argsvs[i]), line_num);
					}
				}
				
				if (argsv.length > 1) {
					error(line, String.format("the LOC directive only takes one argument (%d provided)", argsv.length), line_num);
				}

				address = argsv[0];
				break;
			case "DATA":
				address = (short)(address+2);
				break;
			default:
				break;
		}

		return address;
	}

	// Pass 1, basically assemble the program without actually doing codegen, will increment address as appropriate
	// 		This will allow us to identify any labels needed and look for any obvious errors
	static short pass1_parse_labels(String line, HashMap<String, Short> labels, Short address, int line_num) {
		// parts of a line of assembly
		// label: mnemonic args ;comment
		// labels can only be used with instruction mnemonics not assembler directives

		// regex which matches a line of assembly (dark magic!)
		Pattern line_pattern = Pattern.compile("(?:(?:(?<label>[A-z]+[0-9]*):\\h*)?(?:(?<mnemonic>(?:[A-Z]|[a-z])+)((?:\\h+)(?<args>(?:(?:,?[0-9]|[A-z])+)+))?\\h*)?)(?:;\\h*(?<comment>.*))?");
		Matcher line_match = line_pattern.matcher(line);

		if (line_match.matches()) {
			String label = line_match.group("label");
			String mnemonic = line_match.group("mnemonic");
			mnemonic = mnemonic != null ? mnemonic.toUpperCase() : null;
			String args = line_match.group("args");
			String comment = line_match.group("comment");

			System.err.println(String.format("label: %s, mnemonic: %s, args: %s, comment: %s",
				label,
				mnemonic,
				args,
				comment
			));

			if (is_directive(mnemonic)) {
				if (label != null) {
					error(line, "Labels cannot be used with assembler directives.", line_num);
				}
				
				address = handle_directive_address(line, mnemonic, args, address, line_num);
				return address;
			} else if (mnemonic != null) {
				if (label != null) {
					labels.put(label, address);
				}
				address = (short)(address+2);
			}
		}		

		return address;	
	}

	// Pass 2, assemble the program for real this time, will push each chunk of generated code to the `code` vector which will be used in the emit stage
	static boolean pass2_assemble(String line, Vector<C6461AssemblerCode> code, HashMap<String, Short> labels, Short address, int line_num) {
		//TODO! assemble
		return true;
	}

	// Emit stage, use the generated code, desired output format, whether or not we want a listing file generated, and finally the output file name
	static boolean emit(Vector<C6461AssemblerCode> code, C6461AssemblerFormat format, boolean listing, String filename) throws Exception {
		File outfile = new File(filename);
		File listfile = new File(filename+".list");
		boolean result = false;
		switch (format) {
			case C6461AssemblerFormat.BINARY:
				result = emit_binary(code, outfile);
				break;
			case C6461AssemblerFormat.LOADFILE:
				result = emit_loadfile(code, outfile);
				break;
			case C6461AssemblerFormat.OCTAL:
				return emit_listing(code, format, listfile);
			case C6461AssemblerFormat.HEXADECIMAL:
				return emit_listing(code, format, listfile);
			default:
				System.err.println("default reached in C6461Assembler::emit! this should not happen!");
				System.exit(1);
				break;
		}

		if(listing) {
			emit_listing(code, C6461AssemblerFormat.OCTAL, listfile);
		}

		return result;
	}

	// Emit flat binary to outfile
	static boolean emit_binary(Vector<C6461AssemblerCode> code, File outfile) throws Exception {
		FileOutputStream outstream = new FileOutputStream(outfile);
		short max_addr = code.stream().filter((a) -> {return !a.comment_only;}).<Short>reduce((short)0, (max, code_chunk) -> {if(code_chunk.address > max) {max = code_chunk.address;} return max;}, (a, b) -> {return 0;});
		ByteBuffer buffer = ByteBuffer.allocate(max_addr+2);
	    // We assume that the C6461 architecture is little-endian
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		for (C6461AssemblerCode code_chunk : code) {
			if (code_chunk.comment_only) {
				continue;
			}

			buffer.putShort(code_chunk.address, code_chunk.code);
		}
		
		outstream.write(buffer.array());
		outstream.close();
		return true;
	}

	// Emit 'loadfile' binary format
	static boolean emit_loadfile(Vector<C6461AssemblerCode> code, File outfile) throws Exception {
		FileWriter outwriter = new FileWriter(outfile);
		for (C6461AssemblerCode code_chunk : code) {
			if (code_chunk.comment_only) {
				continue;
			}
			outwriter.write(String.format("%06o    %06o\n", code_chunk.address, code_chunk.code));
		}
		outwriter.close();
		return true;
	}

	// Emit listing file
	static boolean emit_listing(Vector<C6461AssemblerCode> code, C6461AssemblerFormat format, File listfile) throws Exception {
		FileWriter outwriter = new FileWriter(listfile);
		for (C6461AssemblerCode code_chunk : code) {
			// Output format is:
			// address    code label   mnemonic  args        comment
			// 000000    000000 Lab:   LDA  0,0,0            ;test
			//
			StringBuilder argsstrb;
			if (code_chunk.args == null) {
				argsstrb = null;
			} else {
				argsstrb = new StringBuilder();
				for (String arg : code_chunk.args) {
					argsstrb.append(arg);
					argsstrb.append(",");
				}

				argsstrb.deleteCharAt(argsstrb.length()-1);
			}



			outwriter.write(String.format("%6s    %6s %6s   %4s  %-12s        %s\n", 
				code_chunk.address != null ? format == C6461AssemblerFormat.HEXADECIMAL ? String.format("%06x", code_chunk.address) : String.format("%06o", code_chunk.address) : "      ",
				code_chunk.code != null ? format == C6461AssemblerFormat.HEXADECIMAL ? String.format("%06x", code_chunk.code) : String.format("%06o", code_chunk.code) : "      ",
				code_chunk.label != null ? code_chunk.label+":" : "",
				code_chunk.mnemonic != null ? code_chunk.mnemonic : "",
				code_chunk.args != null ? argsstrb : "",
				code_chunk.comment != null ? ";"+code_chunk.comment : ""
			));
		}

		outwriter.close();
		return true;
	}
}
