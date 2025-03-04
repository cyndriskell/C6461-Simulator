public class C6461SimulatorMachine {
	// General Purpose Registers (16 bits each)
	public short gprs[];
	// Index Registers (16 bits each; 1-3; 0 indicates no indexing!)
	public short idxs[];
	// Program Counter (12 bits)
	public short pc;
	// Condition Code (4 bits)
	public byte cc;
	// Instruction Register (16 bits)
	public short ir;
	// Memory Address Register (12 bits)
	public short mar;
	// Memory Buffer Register (16 bits)
	public short mbr;
	// Machine Fault Register (4 bits)
	public short mfr;

	// Memory
	public C6461SimulatorMemory memory;

	// ROM? I'm not sure if we are supposed to have a dedicated ROM object atm, will look further into it

	public C6461SimulatorMachine() {
		this.memory = new C6461SimulatorMemory();
		this.init();
	}

	public static C6461SimulatorMachine new_with_memory(C6461SimulatorMemory memory) {
		C6461SimulatorMachine self = new C6461SimulatorMachine();
		self.memory = memory;
		self.init();
		return self;
	}

	public void loadRom(short[] rom) {
		this.memory.load(rom);
	}

	public void step() {
		short instruction = this.fetch();
		this.execute(instruction);
	}

	public short fetch() {
		return this.memory.fetch(this.pc++);
	}

	public void execute(short instruction) {
		short[] args;
		// What instruction are we talking about here
		short opcode = (short)((instruction & 0xFFFF) >> 10);
		switch (opcode) {
			case C6461SimulatorOpcodes.LDR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.OFFSETS.get("LDR"));
				this.loadRegister(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.LDA:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.OFFSETS.get("LDA"));
				this.loadAddress(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.LDX:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.OFFSETS.get("LDX"));
				this.loadIndex(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.STR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.OFFSETS.get("STR"));
				this.storeRegister(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.STX:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.OFFSETS.get("STX"));
				this.storeIndex(args[0], args[1], args[2], args[3], args[4]);
				break;
			default:
				this.mfr = 4;
				System.err.printf("WARN: Unhandled opcode: %d, code: %d\n", opcode, instruction & 0xffff);
				break;
		}
	}

	public short effectiveAddress(short index, short indirect, short address) {
		short addr;
		if (index == 0) {
			addr = address;
		} else if (index >= 1 && index <= 3) {
			addr = (short)(this.idxs[index-1] + address);
		} else {
			System.err.println("Index register not in range 0-3!");
			System.exit(-1);
			// Unreachable
			return 0;
			//throw new C6461IllegalOperandException("Index register not in range 0-3!");
		}

		if (indirect == 1) {
			addr = this.memory.fetch(addr);
		}

		return addr;
	}

	public void loadRegister(short _opcode, short register, short index, short indirect, short address) {
		this.gprs[register] = this.memory.fetch(this.effectiveAddress(index, indirect, address));
	}

	public void loadAddress(short _opcode, short register, short index, short indirect, short address) {
		this.gprs[register] = this.effectiveAddress(index, indirect, address);
	}

	public void loadIndex(short _opcode, short register, short index, short indirect, short address) {
		this.idxs[index-1] = this.memory.fetch(this.effectiveAddress(index, indirect, address));
	}

	public void storeRegister(short _opcode, short register, short index, short indirect, short address) {
		this.memory.store(this.effectiveAddress(index, indirect, address), this.gprs[register]);
	}

	public void storeIndex(short _opcode, short register, short index, short indirect, short address) {
		this.memory.store(this.effectiveAddress(index, indirect, address), this.idxs[index-1]);
	}

        public static short[] getArgs(short instruction, short[] offsets) {
            short[] out = new short[offsets.length - 1];
            short start = offsets[0];
            short end = offsets[1];

            for (int i = 0; i < offsets.length - 1; i++) {
                out[i] = (short) (((instruction & 0xFFFF) >>> end) & ((1 << (start - end)) - 1));
                start = end;
                if (i + 2 < offsets.length) {
                        end = offsets[i + 2];
                }
            }
        
            return out;
        }	

	public void init() {
		this.gprs = new short[4];
		this.idxs = new short[3];
		this.pc = 0;
		this.cc = 0;
		this.ir = 0;
		this.mar = 0;
		this.mbr = 0;
		this.mfr = 0;
		this.memory.clear();
	}
}

