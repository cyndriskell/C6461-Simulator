public class C6461AssemblerOptions {
	public boolean help = false;
	public boolean version = false;
	public boolean verbose = false;
	public C6461AssemblerFormat format = C6461AssemblerFormat.BINARY;
	public boolean listing = false;
	public String output = "a.out";
	public String input = null;

	public static C6461AssemblerOptions from_args(String[] args) {
		C6461AssemblerOptions options = new C6461AssemblerOptions();

		for(int i = 0; i < args.length; i++) {
			String arg = args[i];
			if(arg.startsWith("-")) {
				if(arg.startsWith("--")) {
					switch (arg.substring(2)) {
						case "help":
							options.help = true;
							break;
						case "version":
							options.version = true;
							break;
						case "verbose":
							options.verbose = true;
							break;
						case "format":
							if(i+1 >= args.length) {
								System.err.println("format requires an argument" + "\nC6461Assembler --help");
								System.exit(1);
							}
							i++;
							String formatarg = args[i];
							switch (formatarg) {
								case "octal":
									options.format = C6461AssemblerFormat.OCTAL;
									break;
								case "hexadecimal":
									options.format = C6461AssemblerFormat.HEXADECIMAL;
									break;
								case "binary":
									options.format = C6461AssemblerFormat.BINARY;
									break;
								case "text":
									options.format = C6461AssemblerFormat.LOADFILE;
								default:
									System.err.println("Unknown format: " + formatarg + "\nC6461Assembler --help");
									System.exit(1);
							}
							break;
						case "listing":
							options.listing = true;
							break;
						case "output":
							if(i+1 >= args.length) {
								System.err.println("output requires an argument" + "\nC6461Assembler --help");
								System.exit(1);
							}
							i++;
							options.output = args[i];
							break;
						default:
							System.err.println("Unknown option: " + arg.substring(2) + "\nC6461Assembler --help");
							System.exit(1);
					}
				} else {
					switch (arg.substring(1)) {
						case "h":
							options.help = true;
							break;
						case "V":
							options.version = true;
							break;
						case "v":
							options.verbose = true;
							break;
						case "f":
							if(i+1 >= args.length) {
								System.err.println("format requires an argument" + "\nC6461Assembler --help");
								System.exit(1);
							}
							i++;
							String formatarg = args[i];
							switch (formatarg) {
								case "octal":
									options.format = C6461AssemblerFormat.OCTAL;
									break;
								case "hexadecimal":
									options.format = C6461AssemblerFormat.HEXADECIMAL;
									break;
								case "binary":
									options.format = C6461AssemblerFormat.BINARY;
									break;
								default:
									System.err.println("Unknown format: " + formatarg + "\nC6461Assembler --help");
									System.exit(1);
							}
							break;
						case "l":
							options.listing = true;
							break;
						case "o":
							if(i+1 >= args.length) {
								System.err.println("output requires an argument" + "\nC6461Assembler --help");
								System.exit(1);
							}
							i++;
							options.output = args[i];
							break;
						default:
							System.err.println("Unknown option: " + arg.substring(1) + "\nC6461Assembler --help");
							System.exit(1);
						}
				}
			} else {
				options.input = args[i];
			}
		}

		return options;
	}
}
