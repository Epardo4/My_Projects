import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable{
	private boolean quantumExpired = false; //The boolean that lets the process know to cooperate
	private Thread thread; //thread to run this process
	private boolean isAlive = true; //shows if this process is still alive
	private int timesStarted = 0; //shows how many times this process has been started
	private Semaphore semaphore; //this process's semaphore
	private PCB.Priority priority;
	/**
	 * Constructor for Process, initializes thread and semaphore
	 */
	public Process() {
		thread = new Thread(this);
		semaphore = new Semaphore(0);
		thread.start();
	}
	/**
	 * Gets the toString of the array specified by "array"
	 * @param array
	 * @return a toString for the array
	 */
	public String getArrayString(byte[] array) {
		String toReturn = "[";
		for(byte b : array) {
			toReturn += b + ", ";
		}
		return toReturn.substring(0, toReturn.length() - 2) + "]";
	}
	/**
	 * Runs the process by acquiring the semaphore and calling main
	 */
	@Override
	public void run() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		main();
	}
	/**
	 * Accesses the priority of this process's PCB
	 * @return PCB.Priority
	 */
	public PCB.Priority getPriority() {
		return priority;
	}
	/**
	 * Mutates the priority of this process's PCB
	 * @param p
	 */
	public void setPriority(PCB.Priority p) {
		priority = p;
	}
	/**
	 * Accesses the number of times this process has been started
	 * @return timesStarted
	 */
	public int getTimesStarted() {
		return timesStarted;
	}
	/**
	 * @return the number of permits available from the semaphore
	 */
	public int permitsAvailable() {
		return semaphore.availablePermits();
	}
	/**
	 * Sets isAlive to the not of isAlive in order to exit this process
	 */
	public void exit() {
		isAlive = false;
	}
	/**
	 * Sets quantumExpired to true for cooperate
	 */
	public void requestStop() {
		quantumExpired = true;
	}
	/**
	 * The main class to be called in run (not implemented in this class)
	 */
	public abstract void main();
	/**
	 * If there are zero permits available, this function returns true
	 * @return if there are permits not available
	 */
	public boolean isStopped() {
		if(semaphore.availablePermits() == 0) return true;
		return false;
	}
	/**
	 * @return the id of this thread
	 */
	public int getPid() {
		return (int)thread.getId();
	}
	/**
	 * @returns if this thread is still alive
	 */
	public boolean isDone() {
		if(thread.isAlive() && isAlive) return false;
		return true;
	}
	/**
	 * Starts the thread, handles some corner cases
	 */
	public void start() {
//		System.out.println("started    " + this);
		timesStarted++;
		semaphore.release();
		if(timesStarted != 1 && !(this instanceof Kernel) || (this instanceof Kernel && timesStarted >= 3)){
			run();
		}
	}
	/**
	 * Acquires the semaphore
	 */
	public void stop() throws InterruptedException {
		semaphore.acquire();
	}
	/**
	 * If quantumExpired is true, this function calls the SwitchProcess function from the OS class
	 */
	public void cooperate() throws Exception {
		if(quantumExpired) {
			quantumExpired = false;
			OS.SwitchProcess();
		}
	}
}
