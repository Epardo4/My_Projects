/*
 * Created by Eli Pardo
 */
public class Cache {
	private Word[] instructionCache = new Word[9];
	private Word[][] L2Cache = new Word[4][9];
	private int clockCycles = 0;
	private ALU checkIndex;
	private Bit[] subtract = {new Bit(true), new Bit(true), new Bit(true), new Bit(true)};
	/**
	 * Constructor for the Cache class
	 */
	public Cache() {
		checkIndex = new ALU(null, null);
	}
	/**
	 * Reads from instructionCache and if that is not successful, reads from L2Cache
	 * @param index
	 * @return Word stored at index
	 */
	public Word read(Word index) {
		//if instructionCache has not yet been initialized
		if(instructionCache[0] == null){
			updateL2Cache(index);
			clockCycles = 350;
			return MainMemory.read(index);
		}
		int check = checkIndexFromCache(index, instructionCache[0]);
		//if index is in instructionCache
		if(check >= 0 && check < 8) {
			clockCycles = 10;
			return instructionCache[check + 1];
		}
		//Go to L2Cache since index is not in instructionCache
		return readL2(index);
	}
	/**
	 * Accesses the clockCycles member
	 * @return clockCycles
	 */
	public int getClockCycles() {
		return clockCycles;
	}
	/**
	 * Updates the instructionCache with new Words at index from the MainMemory
	 * @param index
	 */
	public void updateInstructionCache(Word index) {
		Word toIncrement;
		//if index is at the very end of the MainMemory array (call function for example)
		if(index.getSigned() > 1016) toIncrement = new Word(1016);
		else toIncrement = new Word(index);
		instructionCache[0] = toIncrement;
		for(int i = 1; i <= 8; i++) {
			instructionCache[i] = MainMemory.read(toIncrement);
			toIncrement = toIncrement.increment();
		}
	}
	/**
	 * Writes value into the instructionCache if the index is in instrucitonCache, tries to write to L2Cache otherwise
	 * @param index
	 * @param value
	 */
	public void write(Word index, Word value) {
		//if instructionCache has not yet been initialized
		if(instructionCache[0] == null){
			updateL2Cache(index);
			int indexToPut = index.getSigned() - instructionCache[0].getSigned();
			instructionCache[indexToPut + 1] = value;
			clockCycles = 350;
			return;
		}
		int check = checkIndexFromCache(index, instructionCache[0]);
		//if index is in instructionCache
		if(check >= 0 && check < 8) {
			clockCycles = 10;
			instructionCache[check + 1] =  value;
		}
		//Go to L2Cache since index is not in instructionCache
		writeL2(index, value);
	}
	/**
	 * Reads from L2 Cache, if index not here, read from MainMemory and update all caches
	 * @param index
	 * @return Word at desired index
	 */
	public Word readL2(Word index) {
		//if L2Cache has not yet been initialized
		if(L2Cache[0][0] == null) {
			updateL2Cache(index);
			clockCycles = 350;
			return MainMemory.read(index);
		}
		int validIndex;
		Word toReturn = null;
		Word[] check;
		//if L2Cache[0] has been initialized, check if index is here
		if((check = L2Cache[0])[0] != null) {
			validIndex = checkIndexFromCache(index, check[0]);
			if(validIndex >= 0 && validIndex < 8)  {
				swap(check, 0);
				toReturn = check[validIndex + 1];
			}
		}
		//if L2Cache[1] has been initialized, check if index is here
		if((check = L2Cache[1])[0] != null) {
			validIndex = checkIndexFromCache(index, check[0]);
			if(validIndex >= 0 && validIndex < 8) {
				swap(check, 1);
				toReturn = check[validIndex + 1];
			}
		}
		//if L2Cache[2] has been initialized, check if index is here
		if((check = L2Cache[2])[0] != null) {
			validIndex = checkIndexFromCache(index, check[0]);
			if(validIndex >= 0 && validIndex < 8) {
				swap(check, 2);
				toReturn = check[validIndex + 1];
			}
		}
		//if L2Cache[3] has been initialized, check if index is here
		if((check = L2Cache[3])[0] != null) {
			validIndex = checkIndexFromCache(index, check[0]);
			if(validIndex >= 0 && validIndex < 8) {
				swap(check, 3);
				toReturn = check[validIndex + 1];
			}
		}
		//if any of the above code has produced any results
		if(toReturn != null) {
			clockCycles = 50;
			return toReturn;
		}
		//otherwise pull from MainMemory
		updateL2Cache(index);
		clockCycles = 350;
		return MainMemory.read(index);
	}
	/**
	 * Updates the L2Cache with old Words from instructionCache and calls updateInstructionCache(index)
	 * @param index
	 */
	public void updateL2Cache(Word index) {
		//Word[] to contain old Words from instructionCache
		Word[] toPut = new Word[9];
		for(int i = 0; i < 9; i++)
			toPut[i] = instructionCache[i];
		updateInstructionCache(index);
		//if L2Cache[0] has not yet been initialized, put toPut here
		if(L2Cache[0][0] == null) L2Cache[0] = toPut;
		else if(L2Cache[1][0] == null) {
			L2Cache[1] = L2Cache[0];
			L2Cache[0] = toPut;
		}
		//if L2Cache[1] has not yet been initialized, put toPut here
		else if(L2Cache[2][0] == null) {
			L2Cache[2] = L2Cache[1];
			L2Cache[1] = L2Cache[0];
			L2Cache[0] = toPut;
		}
		//if L2Cache[2] has not yet been initialized, put toPut here
		else if(L2Cache[3][0] == null) {
			L2Cache[3] = L2Cache[2];
			L2Cache[2] = L2Cache[1];
			L2Cache[1] = L2Cache[0];
			L2Cache[0] = toPut;
		}
		//Since L2Cache is full, write everything in L2Cache to MainMemory, move all Caches down a level and replenish instructionCache
		else {
			Word toIncrement = L2Cache[3][0];
			for(int i = 1; i <= 8; i++) {
				MainMemory.write(toIncrement, L2Cache[3][i]);
				toIncrement = toIncrement.increment();
			}
			L2Cache[3] = L2Cache[2];
			L2Cache[2] = L2Cache[1];
			L2Cache[1] = L2Cache[0];
			L2Cache[0] = toPut;
		}
	}
	/**
	 * Writes value into the L2Cache if the index is in instrucitonCache, updates all Caches otherwise and writes to instructionCache
	 * @param index
	 * @param value
	 */
	public void writeL2(Word index, Word value) {
		//if L2Cache has not yet been initialized
		if(L2Cache[0][0] == null) {
			updateL2Cache(index);
			clockCycles = 350;
			int indexToPut = index.getSigned() - instructionCache[0].getSigned();
			instructionCache[indexToPut + 1] = value;
		}
		int validIndex;
		Word[] check;
		//if L2Cache[0] has been initialized, check if index is here
		if((check = L2Cache[0])[0] != null) {
			validIndex = checkIndexFromCache(index, check[0]);
			if(validIndex >= 0 && validIndex < 8) {
				check[validIndex + 1] = value;
				swap(check, 0);
				clockCycles = 50;
				return;
			}
		}
		//if L2Cache[1] has been initialized, check if index is here
		if((check = L2Cache[1])[0] != null) {
			validIndex = checkIndexFromCache(index, check[0]);
			if(validIndex >= 0 && validIndex < 8) {
				check[validIndex + 1] = value;
				swap(check, 1);
				clockCycles = 50;
				return;			}
		}
		//if L2Cache[2] has been initialized, check if index is here
		if((check = L2Cache[2])[0] != null) {
			validIndex = checkIndexFromCache(index, check[0]);
			if(validIndex >= 0 && validIndex < 8) {
				check[validIndex + 1] = value;
				swap(check, 2);
				clockCycles = 50;
				return;			}
		}
		//if L2Cache[3] has been initialized, check if index is here
		if((check = L2Cache[3])[0] != null) {
			validIndex = checkIndexFromCache(index, check[0]);
			if(validIndex >= 0 && validIndex < 8) {
				check[validIndex + 1] = value;
				swap(check, 3);
				clockCycles = 50;
				return;			
			}
		}
		//otherwise, update all caches and write to instructionCache
		updateL2Cache(index);
		clockCycles = 350;
		int indexToPut = index.getSigned() - instructionCache[0].getSigned();
		instructionCache[indexToPut + 1] = value;
	}
	/**
	 * Swaps the cache at level index from L2Cache with instructionCache and swaps instructionCache with check
	 * @param check
	 * @param index
	 */
	public void swap(Word[] check, int index) {
		L2Cache[index] = instructionCache;
		instructionCache = check;
	}
	/**
	 * Creates a String to display the Word array arr as an array of the getSigned() values in the array
	 * @param arr
	 * @return a way to display the array
	 */
	public String arrayToString(Word[] arr) {
		String toReturn = "[";
		for(int i = 0; i < arr.length; i++) {
			toReturn += arr[i].getSigned() + ", ";
		}
		toReturn = toReturn.substring(0, toReturn.length() - 2);
		return toReturn + "]";
	}
	/**
	 * Creates a String to display all caches
	 * @return String
	 */
	public String toString() {
		String toReturn = "instructionCache:\n" + arrayToString(instructionCache);
		for(int i = 0; i < 4; i++) {
			toReturn += "\nL2Cache[" + i + "]\n" + arrayToString(L2Cache[i]);
		}
		return toReturn;
	}
	/**
	 * Checks if the desired cache contains the index
	 * @param index
	 * @param cacheRange
	 * @return an integer to represent how far index is from cacheRange
	 */
	public int checkIndexFromCache(Word index, Word cacheRange) {
		checkIndex.op1 = new Word(index);
		checkIndex.op2 = new Word(cacheRange);
		checkIndex.doOperation(subtract);
		return new Word(checkIndex.result).getSigned();
	}
}
