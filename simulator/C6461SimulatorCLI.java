import java.util.Scanner;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class C6461SimulatorCLI {
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		C6461SimulatorMachine machine = new C6461SimulatorMachine();

		main: while (true) {
			System.out.println("\nC6461 Simulator");
            System.out.println("1. Load ROM (binary) into machine memory");
		    System.out.println("2. Peek memory");
			System.out.println("3. Reset machine");
			System.out.println("4. Set Register");
			System.out.println("5. Step");
		    System.out.println("6. Display register values");
		    System.out.println("7. Exit");

			int choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {
				case 1:
					doLoad(machine, scanner);
					break;
				case 2:
					doPeek(machine, scanner);
					break;
				case 3:
					machine.init();
					break;
				case 4:
					doSetReg(machine, scanner);
					break;
				case 5:
					machine.step();
					break;
				case 6:
					doRegDisplay(machine);
					break;
				case 7:
					break main;
				default:
					break;

			}
		}
	}

	public static void doLoad(C6461SimulatorMachine machine, Scanner scanner) throws IOException {
		System.out.print("Binary path? ");

		String path = scanner.nextLine();
		byte[] byte_contents = Files.readAllBytes(Paths.get(path));
		short[] rom = new short[byte_contents.length/2];
		ByteBuffer.wrap(byte_contents).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(rom);
		machine.loadRom(rom);
	}

	public static void doPeek(C6461SimulatorMachine machine, Scanner scanner) {
		System.out.print("Address? ");
		short addr = (short)scanner.nextInt();
		scanner.nextLine();

		short val = machine.memory.fetch(addr);
		System.out.printf("%04d: %05d\n", addr, val);
	}

	public static void doSetReg(C6461SimulatorMachine machine, Scanner scanner) {
		System.out.print("Register? ");
		String reg = scanner.nextLine();
		System.out.print("Value? ");
		short val = (short)scanner.nextInt();
		scanner.nextLine();

		switch (reg.toLowerCase()) {
			case "gpr0":
				machine.gprs[0] = val;
				break;
			case "gpr1":
				machine.gprs[1] = val;
				break;
			case "gpr2":
				machine.gprs[2] = val;
				break;
			case "gpr3":
				machine.gprs[3] = val;
				break;
			case "idx1":
				machine.idxs[0] = val;
				break;
			case "idx2":
				machine.idxs[1] = val;
				break;
			case "idx3":
				machine.idxs[2] = val;
				break;
			case "pc":
				machine.pc = val;
				break;
			case "cc":
				machine.cc = (byte)val;
				break;
			case "ir":
				machine.ir = val;
				break;
			case "mar":
				machine.mar = val;
				break;
			case "mbr":
				machine.mbr = val;
				break;
			case "mfr":
				machine.mfr = val;
				break;
		}
	}

	public static void doRegDisplay(C6461SimulatorMachine machine) {
		System.out.printf("GPRS:\t\t PC: %d\n\t0: %d\t CC: %d\n\t1: %d\t IR: %d\n\t2: %d\tMAR: %d\n\t3: %d\tMBR: %d\n", machine.pc, machine.gprs[0], machine.cc, machine.gprs[1], machine.ir, machine.gprs[2], machine.mar, machine.gprs[3], machine.mbr);
		System.out.printf("IDXS:\t\tMFR: %d\n\t1: %d\n\t2: %d\n\t3: %d\n", machine.mfr, machine.idxs[0], machine.idxs[1], machine.idxs[2]);
	}
}
