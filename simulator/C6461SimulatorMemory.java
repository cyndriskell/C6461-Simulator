public class C6461SimulatorMemory {
	private short[] inner;
	private static final short MEM_SIZE = 2048;

	public C6461SimulatorMemory() {
        	this.inner = new short[MEM_SIZE];
        }

	public void clear() {
		this.inner = new short[MEM_SIZE];
	}

	public void load(short[] rom) {
		if (rom.length > this.inner.length) {
			throw new IllegalArgumentException("ROM file too large for memory");
		}

		this.clear();
		System.arraycopy(rom, 0, this.inner, 0, rom.length);
	}

	public void store(short address, short value) {
        	if (isValidAddress(address)) {
            		this.inner[address] = value;
        	} else {
            		throw new IllegalArgumentException("Invalid memory address");
        	}
    	}

    	public short fetch(short address) {
        	if (isValidAddress(address)) {
            		return this.inner[address];
		}
        	throw new IllegalArgumentException("Invalid memory address");
    	}

	private boolean isValidAddress(short address) {
        	return address >= 0 && address < MEM_SIZE;
    	}
}
