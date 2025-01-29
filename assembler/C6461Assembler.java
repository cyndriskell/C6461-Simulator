import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;

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
			System.out.println("\t\t\"Address\\tMachine Code\\tSymbolic Representation\" for each instruction");
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

		String input_string = String.valueOf(buf);

		Vector<C6461AssemblerCode> code = new Vector<>();
		HashMap<String, Integer> labels = new HashMap<>();
		int current_address = 0;

		// Pass 1
		for (String line: input_string.split("\n")) {
			
		}
	}

	// Check if an instruciton/directive mnemonic is valid
	static boolean check_instruction(String mnemonic) {
		//TODO! check_instruction
		return true;
	}

	// Pass 1, basically assemble the program without actually doing codegen, will increment address as appropriate
	// 		This will allow us to identify any labels needed and look for any obvious errors
	static boolean parse_labels(String line, HashMap<String, Integer> labels, Integer address) {
		//TODO! parse_labels
		return true;	
	}

	// Pass 2, assemble the program for real this time, will push each chunk of generated code to the `code` vector which will be used in the emit stage
	static boolean assemble(String line, Vector<C6461AssemblerCode> code, HashMap<String, Integer> labels, Integer address) {
		//TODO! assemble
		return true;
	}

	// Emit stage, use the generated code, desired output format, whether or not we want a listing file generated, and finally the output file name
	static boolean emit(Vector<C6461AssemblerCode> code, C6461AssemblerFormat format, boolean listing, String filename) {
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
	static boolean emit_binary(Vector<C6461AssemblerCode> code, File outfile) {
		//TODO! emit_binary
		return true;
	}

	// Emit 'loadfile' binary format
	static boolean emit_loadfile(Vector<C6461AssemblerCode> code, File outfile) {
		//TODO! emit_loadfile
		return true;
	}

	// Emit listing file
	static boolean emit_listing(Vector<C6461AssemblerCode> code, C6461AssemblerFormat format, File listfile) {
		//TODO! emit_listing
		return true;
	}
}
