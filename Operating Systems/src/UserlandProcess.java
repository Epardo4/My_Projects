
public abstract class UserlandProcess extends Process{
	/**
	 * Empty constructor for the UserlandProcess class
	 */
	public UserlandProcess() {}
	/**
	 * Reads the address from the given "address". Format address as follows: (Virtual Page Number)*(1024) + (page offset)E exits on failure
	 * @param address
	 * @return byte from memory at physicalAddress
	 */
	public byte Read(int address) {
		int physicalAddress = Hardware.getPhysicalAddress(address);
		if(physicalAddress == -1) {
			System.out.println(this.getClass().getSimpleName() + " was killed because it tried to access memory that does not belong to it");
			OS.exit();
		}
		return Hardware.Read(physicalAddress);
	}
	/**
	 * Write value to memory at the address from the given "address". See read for address formating. Exits on failure
	 * @param address
	 * @param value
	 */
	public void Write(int address, byte value) {
		int physicalAddress = Hardware.getPhysicalAddress(address);
		if(physicalAddress == -1) {
			System.out.println(this.getClass().getSimpleName() + " was killed because it tried to access memory that does not belong to it");
			OS.exit();
		}
		Hardware.Write(physicalAddress, value);
	}
	
	
	
}
