public class C6461SimulatorMemory {
	private short[] inner;
	private static final short MEM_SIZE = 2048;
	private Cache cache;

	// Cache implementation
	private class Cache {
		private static final int CACHE_LINES = 16;  // 16 cache lines
		private static final int LINE_SIZE = 4;     // 4 words per line
		private CacheLine[] lines;
		private int hits;
		private int misses;

		private class CacheLine {
			boolean valid;
			int tag;
			short[] data;
			boolean dirty;
			int lastUsed;  // For LRU replacement

			CacheLine() {
				valid = false;
				tag = 0;
				data = new short[LINE_SIZE];
				dirty = false;
				lastUsed = 0;
			}
		}

		public Cache() {
			lines = new CacheLine[CACHE_LINES];
			for (int i = 0; i < CACHE_LINES; i++) {
				lines[i] = new CacheLine();
			}
			hits = 0;
			misses = 0;
		}

		// Get cache line index from address
		private int getIndex(short address) {
			return (address / LINE_SIZE) % CACHE_LINES;
		}

		// Get tag from address
		private int getTag(short address) {
			return address / (LINE_SIZE * CACHE_LINES);
		}

		// Get offset within cache line
		private int getOffset(short address) {
			return address % LINE_SIZE;
		}

		// Read data from cache
		public short read(short address) {
			int index = getIndex(address);
			int tag = getTag(address);
			int offset = getOffset(address);

			CacheLine line = lines[index];
			if (line.valid && line.tag == tag) {
				// Cache hit
				hits++;
				line.lastUsed = hits + misses;
				return line.data[offset];
			}

			// Cache miss
			misses++;
			if (line.valid && line.dirty) {
				// Write back dirty line
				writeBack(index);
			}

			// Load new line from memory
			loadLine(index, address);
			return lines[index].data[offset];
		}

		// Write data to cache
		public void write(short address, short value) {
			int index = getIndex(address);
			int tag = getTag(address);
			int offset = getOffset(address);

			CacheLine line = lines[index];
			if (line.valid && line.tag == tag) {
				// Cache hit
				hits++;
			} else {
				// Cache miss
				misses++;
				if (line.valid && line.dirty) {
					writeBack(index);
				}
				loadLine(index, address);
			}

			line.data[offset] = value;
			line.dirty = true;
			line.lastUsed = hits + misses;
		}

		// Load a cache line from memory
		private void loadLine(int index, short address) {
			short baseAddr = (short)(address - getOffset(address));
			CacheLine line = lines[index];
			
			for (int i = 0; i < LINE_SIZE; i++) {
				line.data[i] = inner[baseAddr + i];
			}
			
			line.valid = true;
			line.tag = getTag(address);
			line.dirty = false;
		}

		// Write back a dirty cache line to memory
		private void writeBack(int index) {
			// CacheLine line = lines[index];
			// short baseAddr = (short)((line.tag * CACHE_LINES + index) * LINE_SIZE);
			
			// for (int i = 0; i < LINE_SIZE; i++) {
			// 	inner[baseAddr + i] = line.data[i];
			// }
			return;
		}

		// Get cache statistics
		public int getHits() { return hits; }
		public int getMisses() { return misses; }
		public double getHitRate() {
			return (hits + misses == 0) ? 0 : (double)hits / (hits + misses);
		}
		public short[][] getLines() {
			short[][] val = new short[CACHE_LINES][LINE_SIZE];
			int idx = 0;
			for (CacheLine line : lines) {
				val[idx++] = line.data;
			}

			return val;
		}
	}

	public C6461SimulatorMemory() {
		this.inner = new short[MEM_SIZE];
		this.cache = new Cache();
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
			cache.write(address, value);
		} else {
			throw new IllegalArgumentException("Invalid memory address");
		}
	}

	public short fetch(short address) {
		if (isValidAddress(address)) {
			return this.inner[address];
			// return cache.read(address);
		}
		throw new IllegalArgumentException("Invalid memory address");
	}

	private boolean isValidAddress(short address) {
		return address >= 0 && address < MEM_SIZE;
	}

	// Get cache statistics
	public int getCacheHits() { return cache.getHits(); }
	public int getCacheMisses() { return cache.getMisses(); }
	public double getCacheHitRate() { return cache.getHitRate(); }
	public short[][] getCacheLines() { return cache.getLines(); }
}