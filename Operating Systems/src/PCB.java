import java.util.LinkedList;
public class PCB {
	public VirtualToPhysicalMapping[] pageTable = new VirtualToPhysicalMapping[100]; //100 page table entries that correspond to pages in memory or on disk
	public int[] ids; //ids for all the devices in this function
	public enum Priority {REAL_TIME, INTERACTIVE, BACKGROUND}; //enum for the possible priorities of a PCB
	public Priority priority; //this PCB's priority
	private static int nextPid; //not used yet
	public int timeOutCount = 0; //determines how many times this PCB's process has reached its quantum
	private int pid; //the process ID of up
	public UserlandProcess up; //the process for this PCB
	private int wakeUpTime; //determines what time this process is allowed to wake up
	private String name; //the name of up
	private LinkedList<KernelMessage> messages = new LinkedList<>(); //the messages for this PCB
	
	/**
	 * Constructor for the PCB class
	 * @param up
	 * @param p
	 */
	public PCB(UserlandProcess up, Priority p) {
		ids = new int[10];
		for(int i = 0; i < ids.length; i++) ids[i] = -1;
		this.up = up;
		pid = up.getPid();
		priority = p;
		up.setPriority(priority);
		name = up.getClass().getSimpleName();
	}
	/**
	 * Gets the messages for this PCB
	 * @return messages
	 */
	public LinkedList<KernelMessage> getMessages(){
		return messages;
	}
	/**
	 * Gets the name for up
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Accesses the time for this process to be able to wake up
	 * @return int
	 */
	public int getWakeUpTime() {
		return wakeUpTime;
	}
	/**
	 * Sets the time for this process to wake up
	 * @param WUT
	 */
	public void setWakeUpTime(int WUT) {
		wakeUpTime = WUT;
	}
	/**
	 * Calls stop on up and waits until this is done
	 */
	public void stop() throws Exception{
		up.stop();
		while(up.isStopped()) {
			Thread.sleep(50);
		}
	}
	/**
	 * Checks if up is done
	 * @return boolean
	 */
	public boolean isDone() {
		return up.isDone();
	}
	/**
	 * Calls start on up
	 */
	public void start() {
		up.start();
	}
	/**
	 * Accesses up's process ID
	 * @return int
	 */
	public int getPid() {
		return pid;
	}
	/**
	 * Calls requestStop on up
	 */
	public void requestStop() {
		up.requestStop();
	}
	/**
	 * Closes all open files and calls exit on up
	 */
	public void exit() {
		up.exit();
	}
}
