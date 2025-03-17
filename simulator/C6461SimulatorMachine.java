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

	// World
	public C6461SimulatorGUI world;

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

	public void connectWorld(C6461SimulatorGUI world) {
		this.world = world;
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
			case C6461SimulatorOpcodes.HLT:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.HLT);
				this.halt(args[0]);
				break;
			case C6461SimulatorOpcodes.LDR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.LDR);
				this.loadRegister(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.LDA:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.LDA);
				this.loadAddress(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.LDX:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.LDX);
				this.loadIndex(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.STR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.STR);
				this.storeRegister(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.STX:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.STX);
				this.storeIndex(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.JZ:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.JZ);
				this.jumpZero(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.JNE:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.JNE);
				this.jumpNotEqual(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.JCC:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.JCC);
				this.jumpConditionCode(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.JMA:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.JMA);
				this.jumpUnconditional(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.JSR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.JSR);
				this.jumpSubroutine(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.RFS:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.RFS);
				this.returnSubroutine(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.SOB:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.SOB);
				this.subtractOneBranch(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.JGE:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.JGE);
				this.jumpGreaterOrEqual(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.AMR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.AMR);
				this.addMemoryRegister(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.SMR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.SMR);
				this.subtractMemoryRegister(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.AIR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.AIR);
				this.addImmediateRegister(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.SIR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.SIR);
				this.subtractImmediateRegister(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.MLT:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.MLT);
				this.multiplyReg(args[0], args[1], args[2]);
				break;
			case C6461SimulatorOpcodes.DVD:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.DVD);
				this.divideReg(args[0], args[1], args[2]);
				break;
			case C6461SimulatorOpcodes.TRR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.TRR);
				this.equalityReg(args[0], args[1], args[2]);
				break;
			case C6461SimulatorOpcodes.AND:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.AND);
				this.bitwiseAndReg(args[0], args[1], args[2]);
				break;
			case C6461SimulatorOpcodes.ORR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.ORR);
				this.bitwiseOrReg(args[0], args[1], args[2]);
				break;
			case C6461SimulatorOpcodes.NOT:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.NOT);
				this.bitwiseNotReg(args[0], args[1], args[2]);
				break;
			case C6461SimulatorOpcodes.SRC:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.SRC);
				this.shiftReg(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.RRC:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.RRC);
				this.rotateReg(args[0], args[1], args[2], args[3], args[4]);
				break;
			case C6461SimulatorOpcodes.IN:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.IN);
				this.inputCharRegDev(args[0], args[1], args[2]);
				break;
			case C6461SimulatorOpcodes.OUT:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.OUT);
				this.outputCharRegDev(args[0], args[1], args[2]);
				break;
			/* case C6461SimulatorOpcodes.CHK:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.CHK);
				this.funcName();
				break;
			case C6461SimulatorOpcodes.FADD:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.FADD);
				this.funcName();
				break;
			case C6461SimulatorOpcodes.FSUB:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.FSUB);
				this.funcName();
				break;
			case C6461SimulatorOpcodes.VADD:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.VADD);
				this.funcName();
				break;
			case C6461SimulatorOpcodes.VSUB:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.VSUB);
				this.funcName();
				break;
			case C6461SimulatorOpcodes.CNVRT:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.CNVRT);
				this.funcName();
				break;
			case C6461SimulatorOpcodes.LDFR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.LDFR);
				this.funcName();
				break;
			case C6461SimulatorOpcodes.STFR:
				args = C6461SimulatorMachine.getArgs(instruction, C6461SimulatorArgOffsets.STFR);
				this.funcName();
				break; */
			default:
				this.mfr = C6461SimulatorMFRFlags.ILGLOP;
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
	
	public void halt(short _opcode) {
		this.mfr = C6461SimulatorMFRFlags.ILGLOP;
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

	public void jumpZero(short _opcode, short register, short index, short indirect, short address) {
		if (this.gprs[register] == 0) {
			this.pc = this.effectiveAddress(index, indirect, address);
		}
	}
	
	public void jumpNotEqual(short _opcode, short register, short index, short indirect, short address) {
		if (this.gprs[register] != 0) {
			this.pc = this.effectiveAddress(index, indirect, address);
		}
	}
	
	public void jumpConditionCode(short _opcode, short condition_code, short index, short indirect, short address) {
		if ((this.cc & (1 << condition_code)) != 0) {
			this.pc = this.effectiveAddress(index, indirect, address);
		}
	}
	
	public void jumpUnconditional(short _opcode, short register, short index, short indirect, short address) {
		this.pc = this.effectiveAddress(index, indirect, address);
	}
	
	public void jumpSubroutine(short _opcode, short register, short index, short indirect, short address) {
		this.gprs[3] = (short)(this.pc + 1);
		this.pc = this.effectiveAddress(index, indirect, address);
	}
	
	public void returnSubroutine(short _opcode, short register, short index, short indirect, short immediate) {
		this.gprs[0] = immediate;
		this.pc = this.gprs[3];
	}
	
	public void subtractOneBranch(short _opcode, short register, short index, short indirect, short address) {
		this.gprs[register] = (short)(this.gprs[register] - 1);
		if (this.gprs[register] > 0) {
			this.pc = this.effectiveAddress(index, indirect, address);
		}
	}
	
	public void jumpGreaterOrEqual(short _opcode, short register, short index, short indirect, short address) {
		if (this.gprs[register] >= 0) {
			this.pc = this.effectiveAddress(index, indirect, address);
		}
	}
	
	public void addMemoryRegister(short _opcode, short register, short index, short indirect, short address) {
		this.gprs[register] = (short)(this.gprs[register] + this.memory.fetch(this.effectiveAddress(index, indirect, address)));
	}

	public void subtractMemoryRegister(short _opcode, short register, short index, short indirect, short address) {
		this.gprs[register] = (short)(this.gprs[register] - this.memory.fetch(this.effectiveAddress(index, indirect, address)));
	}	
	
	public void addImmediateRegister(short _opcode, short register, short index, short indirect, short immediate) {
		this.gprs[register] = (short)(this.gprs[register] + immediate);
	}	

	public void subtractImmediateRegister(short _opcode, short register, short index, short indirect, short immediate) {
		this.gprs[register] = (short)(this.gprs[register] - immediate);
	}	

	public void multiplyReg(short _opcode, short register_x, short register_y) {
		if (!((register_x == 0 || register_x == 2) && (register_y == 0 || register_y == 2))) {
			this.mfr = C6461SimulatorMFRFlags.ILGLOP;
			return;
		}

		int val = this.gprs[register_x]*this.gprs[register_y];
		short high = (short)(val >> 16);
		short low  = (short)(val & 0xFFFF);

		this.gprs[register_x] = high;
		this.gprs[register_y] = low;
		// Check for overflow
	}

	public void divideReg(short _opcode, short register_x, short register_y) {
		if (!((register_x == 0 || register_x == 2) && (register_y == 0 || register_y == 2))) {
			this.mfr = C6461SimulatorMFRFlags.ILGLOP;
			return;		
		}

		int val = 0;
		try {
			val = this.gprs[register_x]/this.gprs[register_y];
		} catch (ArithmeticException e) {
			this.cc = C6461SimulatorCCFlags.DIVDZERO;
			// maybe dont fault here
			this.mfr = C6461SimulatorMFRFlags.ILGLOP; 
		}

		short high = (short)(val >> 16);
		short low  = (short)(val & 0xFFFF);

		this.gprs[register_x] = high;
		this.gprs[register_y] = low;
		// Check for overflow
	}
	
	public void equalityReg(short _opcode, short register_x, short register_y) {
		if (this.gprs[register_x] == this.gprs[register_y]) {
			this.cc = 1;
		} else {
			this.cc = 0;
		}
	} 	
	
	public void bitwiseAndReg(short _opcode, short register_x, short register_y) {
		this.gprs[register_x] = (short)((this.gprs[register_x] & this.gprs[register_y]) & 0xFFFF);
	} 	
	
	public void bitwiseOrReg(short _opcode, short register_x, short register_y) {
		this.gprs[register_x] = (short)((this.gprs[register_x] | this.gprs[register_y]) & 0xFFFF);
	} 	
	
	public void bitwiseNotReg(short _opcode, short register_x, short register_y) {
		this.gprs[register_x] = (short)((~this.gprs[register_x]) & 0xFFFF);
	}
	
	public void shiftReg(short _opcode, short register, short arith_log, short left_right, short count) {
		int val = this.gprs[register];

		if (left_right == 1) {
			val = val << count;
		} else {
			if (arith_log == 1) {
				val = val >>> count;
			} else {
				val = val >> count;	
			}
		}

		this.gprs[register] = (short)(val & 0xFFFF);
	}
	
	public void rotateReg(short _opcode, short register, short arith_log, short left_right, short count) {
		int val = this.gprs[register];
		
		if (left_right == 1) {
			val = (val << count) | (val >>> (16 - count));
		} else {
			val = (val >>> count) | (val << (16 - count));
		}

		this.gprs[register] = (short)(val & 0xFFFF);
	}

	public void inputCharRegDev(short _opcode, short register, short devid) {
		char val = 0;	
		switch (devid) {
			case C6461SimulatorDevIDs.KEYBOARD:
				val = this.world.waitForCharInput();	
				break;
			default:
				this.mfr = C6461SimulatorMFRFlags.ILGLOP;
				break;
		}
		
		this.gprs[register] = (short)val;
	}
	
	public void outputCharRegDev(short _opcode, short register, short devid) {
		char val = (char)(this.gprs[register] & 0xFF);
		switch (devid) {
			case C6461SimulatorDevIDs.PRINTER:
				this.world.printToPrinter(val);
				break;
			default:
				this.mfr = C6461SimulatorMFRFlags.ILGLOP;
		}
	}

	public static short[] getArgs(short instruction, short[][] offsets) {
		short[] out = new short[offsets.length];

		for (int i = 0; i < offsets.length; i++) {
			short start = offsets[i][0];
			short end = offsets[i][1];
			out[i] = (short) (((instruction & 0xFFFF) >>> end) & ((1 << (start - end)) - 1));
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
