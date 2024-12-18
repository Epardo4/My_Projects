
public class VirtualToPhysicalMapping {
	public int physical; //physical page number
	public int onDisk; //on disk page number
	/**
	 * Constructor for the VirtualToPhysicalMapping class
	 */
	public VirtualToPhysicalMapping() {
		physical = -1;
		onDisk = -1;
	}
}
