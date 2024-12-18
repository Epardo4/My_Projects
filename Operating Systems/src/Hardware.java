import java.util.Arrays;
public class Hardware {
	private static byte[] memory = new byte[1048576];
	public static int[][] TLB = {{-1, -1},{-1, -1}};
	/**
	 * Gets the page number for the currently running process given a virtual page number, calls OS.GetMapping if needed
	 * @param virtualPageNumber
	 * @return page number
	 */
	private static int getPageNumber(int virtualPageNumber) {
		for(int i = 0; i < TLB[0].length; i++)
			if(TLB[0][i] == virtualPageNumber)
				return Hardware.TLB[1][i];
		try {
			OS.GetMapping(virtualPageNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * Gets the physical address in memory provided the virtual address specified by variable "address", returns -1 on failure
	 * @param address
	 * @return physical address
	 */
	public static int getPhysicalAddress(int address) {
		int virtualPageNumber = address/1024;
		int physicalPageNumber = getPageNumber(virtualPageNumber);
		if(physicalPageNumber == -1) {
			physicalPageNumber = getPageNumber(virtualPageNumber);
			if(physicalPageNumber == -1) return -1;
		}
		int physicalAddress = (physicalPageNumber * 1024) + (address % 1024);
		return physicalAddress;
	}
	/**
	 * Reads from memory at the address given
	 * @param address
	 * @return memory[address]
	 */
	public static byte Read(int address) {
		return memory[address];
	}
	/**
	 * Writes to the memory at address "address" with the data given in "value"
	 * @param address
	 * @param value
	 */
	public static void Write(int address, byte value) {
		memory[address] = value;
	}
	/**
	 * toString for memory starting at the start address and ending at start + size
	 * @param start
	 * @param size
	 * @return String
	 */
	public static String memoryToString(int start, int size) {
		String toReturn = "[";
		int end = start + size;
		while(start < end) {
			toReturn += memory[start] + ",";
			start++;
		}
		return toReturn.substring(0, toReturn.length()-1) + "]";
	}
	/**
	 * toString for the TLB
	 * @return String
	 */
	public static String TLBtoString() {
		return Arrays.toString(TLB[0]) + " -> " + Arrays.toString(TLB[1]);
	}
	
	
	
	
	
}
