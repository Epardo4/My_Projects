/*
 * Created by Eli Pardo
 */
public class MainMemory {
	public static Word[] memory = new Word[1024];
	/**
	 * Reads from memory at the address specified by parameter address
	 * @parameter address
	 * @return what was read from memory
	 */
	public static Word read(Word address) {
		if(memory[0] == null) {
			for(int i = 0; i < memory.length; i++) {
				memory[i] = new Word();
			}
		}
		return memory[(int)address.getUnsigned()];
	}
	/**
	 * Writes to memory at the address specified by parameter address
	 * @parameter address
	 * @parameter value
	 */
	public static void write(Word address, Word value) {
		if(memory[0] == null) {
			for(int i = 0; i < memory.length; i++) {
				memory[i] = new Word();
			}
		}
		memory[(int)address.getUnsigned()] = new Word(value);
	}
	/**
	 * Loads all of the strings from data into memory after converting each 0 or 1 into a bit containing false or true respectively
	 * @param data
	 */
	public static void load(String[] data) {
		for(int i = 0; i < data.length; i++) {
			memory[i] = new Word();
			for(int j = 0; j < data[i].length(); j++) {
				if(data[i].charAt(j) == '0') memory[i].setBit(31 - j, new Bit(false));
				else memory[i].setBit(31 - j, new Bit(true));
			}
		}
	}
	public static void clear() {
		for(int i = 0; i < memory.length; i++) {
			memory[i] = new Word();
		}
	}
}
