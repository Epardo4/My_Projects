import java.util.ArrayList;
public class OS {	
	private static Kernel kernel; //The Kernel for the Operating System
	public static byte[] bytesRead; //The bytes read to cross between processes
	//The enum for the function call types
	public enum CallType {CREATE_PROCESS, SWITCH_PROCESS, SLEEP, EXIT, OPEN, CLOSE, READ, WRITE, SEEK, SENDMESSAGE, WAITFORMESSAGE, ALLOCATEMEMORY,
		FREEMEMORY, GETMAPPING}; 
	public static CallType currentCall; //the function the Kernel has to execute next
	public static ArrayList<Object> parameters = new ArrayList<>(); //The array list containing the parameters for currentCall
	public static Object returnValue; //What currentCall returned
	public static int swapFileIndex; //the index for the swap file in VFS
	public static int currentDiskPageNum = 0; //the current page number in the disk
	/**
	 * Calls FreeMemory in the Kernel
	 * @return KernelMessage
	 * @throws Exception
	 */
	public static boolean FreeMemory(int pointer, int size) throws Exception{
		parameters.clear();
		parameters.add(pointer);
		parameters.add(size);
		currentCall = CallType.FREEMEMORY;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
		return (boolean)returnValue;
	}
	/**
	 * Calls AllocateMemory in the Kernel
	 * @return KernelMessage
	 * @throws Exception
	 */
	public static int AllocateMemory(int size) throws Exception{
		parameters.clear();
		parameters.add(size);
		currentCall = CallType.ALLOCATEMEMORY;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
		return (int)returnValue;
	}
	/**
	 * Calls GetMapping in the Kernel
	 * @return KernelMessage
	 * @throws Exception
	 */
	public static void GetMapping(int virtualPageNumber) throws Exception{
		kernel.GetMapping(virtualPageNumber);
	}
	/**
	 * Calls WaitForMessage in the Kernel
	 * @return KernelMessage
	 * @throws Exception
	 */
	public static KernelMessage WaitForMessage() throws Exception{
		parameters.clear();
		currentCall = CallType.WAITFORMESSAGE;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
		return (KernelMessage)returnValue;
	}
	/**
	 * Calls SendMessage in the Kernel
	 * @param km
	 * @throws Exception
	 */
	public static void SendMessage(KernelMessage km) throws Exception{
		parameters.clear();
		parameters.add(km);
		currentCall = CallType.SENDMESSAGE;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
	}
	/**
	 * Calls GetPidByName in the Kernel
	 * @param name
	 * @return process id
	 */
	public static int GetPidByName(String name) {
		return kernel.GetPidByName(name);
	}
	/**
	 * Calls GetPid in the Kernel
	 * @return process id
	 */
	public static int GetPid() {
		return kernel.GetPid();
	}
	/**
	 * Calls Open in the Kernel
	 * @param s
	 * @returns the id of the opened device
	 */
	public static int Open(String s) throws Exception {
		parameters.clear();
		parameters.add(s);
		currentCall = CallType.OPEN;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
		return (int)returnValue;
	}
	/**
	 * Calls Close in the Kernel
	 * @param id
	 */
	public static void Close(int id) throws Exception{
		parameters.clear();
		parameters.add(id);
		currentCall = CallType.CLOSE;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
	}
	/**
	 * Calls Read in the Kernel
	 * @param id
	 * @param size
	 * @return the data read from the device
	 */
	public static byte[] Read(int id, int size) throws Exception{
		parameters.clear();
		parameters.add(id);
		parameters.add(size);
		currentCall = CallType.READ;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
		return (byte[])returnValue;
	}
	/**
	 * Calls Seek in the Kernel
	 * @param id
	 * @param to
	 */
	public static void Seek(int id, int to) throws Exception{
		parameters.clear();
		parameters.add(id);
		parameters.add(to);
		currentCall = CallType.SEEK;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
	}
	/**
	 * Calls Write in the Kernel
	 * @param id
	 * @param data
	 * @return size of the device after writing
	 */
	public static int Write(int id, byte[] data) throws Exception{
		parameters.clear();
		parameters.add(id);
		parameters.add(data);
		currentCall = CallType.WRITE;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
		return (int)returnValue;
	}
	/**
	 * Clears the parameters, sets currentCall to exit, closes all of the opened devices in the PCB and starts the kernel
	 */
	public static void exit() {
		for(int i = 0; i < kernel.getScheduler().currentlyRunning.ids.length; i++) {
			if(kernel.getScheduler().currentlyRunning.ids[i] != -1) {
				try {
					Close(kernel.getScheduler().currentlyRunning.ids[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				kernel.getScheduler().currentlyRunning.ids[i] = -1;
			}
		}
		parameters.clear();
		currentCall = CallType.EXIT;
		kernel.start();
	}
	/**
	 * Clears the parameters, adds milliseconds to the parameters, sets currentCall to to sleep, starts the kernel, and stops the currently running process
	 * @param milliseconds
	 * @throws Exception
	 */
	public static void sleep(int milliseconds) throws Exception{
		parameters.clear();
		parameters.add(milliseconds);
		currentCall = CallType.SLEEP;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
	}
	/**
	 * Clears the parameters, calls switch process, starts the kernel, and stops the currently running process
	 * @throws Exception
	 */
	public static void SwitchProcess() throws Exception{
		parameters.clear();
		currentCall = CallType.SWITCH_PROCESS;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
	}
	/**
	 * Clears the parameters, adds up and the priority to the the parameters, calls switch process, starts the kernel, 
	 * and stops the currently running process
	 * @param up
	 * @param priority
	 * @return the id of the currently running process
	 * @throws Exception
	 * 
	 */
	public static int CreateProcess(UserlandProcess up, PCB.Priority priority) throws Exception {
		PCB toCreate = new PCB(up, priority);
		parameters.clear();
		parameters.add(toCreate);
		currentCall = CallType.CREATE_PROCESS;
		kernel.start();
		kernel.getScheduler().currentlyRunning.stop();
		return up.getPid();
	}
	/**
	 * Clears the parameters, adds up to the the parameters and assumes this is a real time process, calls switch process, 
	 * starts the kernel, and stops the currently running process
	 * @param up
	 * @return the id of the currently running process
	 * @throws Exception 
	 */
	public static int CreateProcess(UserlandProcess up) throws Exception {
		PCB toCreate = new PCB(up, PCB.Priority.REAL_TIME);
		parameters.clear();
		parameters.add(toCreate);
		currentCall = CallType.CREATE_PROCESS;
		kernel.start();
		if(kernel.getScheduler().currentlyRunning == null)
			while(kernel.getScheduler().currentlyRunning == null) {
				Thread.sleep(10);
			}
		else kernel.getScheduler().currentlyRunning.stop();
		return up.getPid();
	}
	/**
	 * Creates the Kernel, creates and starts init
	 * @param init
	 * @throws Exception
	 */
	public static void StartUp(UserlandProcess init) throws Exception {
		kernel = new Kernel();
		swapFileIndex = kernel.OpenOS("file SwapFile.txt");
		CreateProcess(init);
	}
}
