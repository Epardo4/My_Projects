
public class VFS implements Device{
	Pointer[] pointers; //pointers to all devices in the VFS
	RandomDevice rand; //the random device for the VFS
	FakeFileSystem ffs; //the fake file system device for the VFS
	/*Sub Class to make the pointers to the devices*/
	public class Pointer{
		Device device;
		int index;
		/**
		 * Constructor for the Pointer class
		 * @param id
		 * @param device
		 */
		public Pointer(int id, Device device) {
			index = id;
			this.device = device;
		}
	}
	/**
	 * Constructor for the VFS class
	 */
	public VFS() {
		pointers = new Pointer[20];
	}
	/**
	 * Opens the device at the index listed first in s with the seed listed second in s
	 * @param s
	 * @return the ID for the pointer of this device
	 */
	public int Open(String s) {
		String[] input = s.split(" ");
		Device device;
		switch(input[0]) {
		case "random":
			if(rand == null)
				rand = new RandomDevice();
			device = rand;
			break;
		case "file":
			if(ffs == null)
				ffs = new FakeFileSystem();
			device = ffs;
			break;
		default:
			device = null;
			return -1;
		}
		int id = 0;
		int index = device.Open(input[1]);
		if(index == -1) return -1;
		for(int i = 0; i < pointers.length; i++) {
			if(pointers[i] == null) {
				pointers[i] = new Pointer(index, device);
				id = i;
				break;
			}
		}
		System.out.println("Opened " + pointers[id].device + " at index " + pointers[id].index);
		return id;
	}
	/**
	 * Closes the device at id
	 * @param id
	 */
	public void Close(int id) {
		if(id >= 0 && pointers[id] != null) {
			pointers[id].device.Close(pointers[id].index);
			System.out.println("Closed " + pointers[id].device + " at index " + pointers[id].index);
		}
		pointers[id] = null;
	}
	/**
	 * Reads from the device at id with size specified by "size"
	 * @param id
	 * @param size
	 * @return the data read from the device
	 */
	public byte[] Read(int id, int size) {
		return pointers[id].device.Read(pointers[id].index, size);
	}
	/**
	 * Seeks from the device at id to the amount specified by "to"
	 * @param id
	 * @param to
	 */
	public void Seek(int id, int to) {
		pointers[id].device.Seek(pointers[id].index, to);
	}
	/**
	 * Writes to the device at id with the data specified by "data"
	 * @param id
	 * @param data
	 * @return the size of the device after writing
	 */
	public int Write(int id, byte[] data) {
		return pointers[id].device.Write(pointers[id].index, data);
	}
}
