import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.time.temporal.ChronoField;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.Random;
import java.util.TimerTask;

public class Scheduler {
	class Interrupt extends TimerTask{
		/**
		 * interrupts when the timer goes off. Demotes currentlyRunning since it has now used up its quantum, calls requestStop on currentlyRunning
		 */
		public void run() {
			if(currentlyRunning != null) {
				currentlyRunning.timeOutCount++;
				if(currentlyRunning.timeOutCount == 5) {
					currentlyRunning.timeOutCount = 0;
					switch(currentlyRunning.priority) {
						case REAL_TIME:
							currentlyRunning.priority = PCB.Priority.INTERACTIVE;
							break;
						case INTERACTIVE:
							currentlyRunning.priority = PCB.Priority.BACKGROUND;
							break;
						default: break;
					}
				}
				currentlyRunning.requestStop();
			}
		}
	}

	private ArrayList<PCB> realTime = new ArrayList<>(); //Contains the real time processes that the scheduler is switching over
	private ArrayList<PCB> interactive = new ArrayList<>(); //Contains the interactive processes that the scheduler is switching over
	private ArrayList<PCB> background = new ArrayList<>(); //Contains the background processes that the scheduler is switching over
	private ArrayList<PCB> sleeping = new ArrayList<>(); ////Contains the sleeping processes that the scheduler is switching over
	private Timer timer; //The timer to figure out when to switch the currently running process
	public PCB currentlyRunning; //The currently running process
	public Kernel kernel; //A reference to the Kernel that this scheduler is used in
	public HashMap<Integer, PCB> waiting = new HashMap<>(); //Hashmap that records the waiting PCBs
	/**
	 * Constructor for scheduler, initializes the interrupt, timer, and schedules the timer
	 */
	public Scheduler(Kernel kernel){
		Interrupt interrupt = new Interrupt();
		timer = new Timer();
		timer.scheduleAtFixedRate(interrupt, 0, 250);
		this.kernel = kernel;
	}
	/**
	 * Gets a random process from a list that is not the currently running process
	 * @param list
	 * @return PCB
	 */
	public PCB searchList(ArrayList<PCB> list) {
		ArrayList<Integer> checked = new ArrayList<>();
		Random random = new Random();
		while(checked.size() < list.size()) {
			int index = random.nextInt(list.size());
			if(list.get(index).getPid() == currentlyRunning.getPid()) {
				if(!checked.contains(currentlyRunning.getPid())) checked.add(currentlyRunning.getPid());
				continue;
			}
			if(checked.contains(list.get(index).getPid())) continue;
			for(int i = 0; i < 100; i++) {
				if(list.get(index).pageTable[i] != null && list.get(index).pageTable[i].physical != -1)
					return list.get(index);
			}
			checked.add(list.get(index).getPid());
		}
		return null;
	}
	/**
	 * Gets a random process that is not currentlyRunning
	 * @return PCB
	 */
	public PCB getRandomProcess() {
		ArrayList<Integer> checked = new ArrayList<>();
		Random random = new Random();
		PCB toReturn;
		while(checked.size() < 4) {
			int list = random.nextInt(4);
			if(list == 0 && !checked.contains(0)) {
				toReturn = searchList(realTime);
				if(toReturn != null) return toReturn;
				checked.add(0);
			}
			if(list == 1 && !checked.contains(1)) {
				toReturn = searchList(interactive);
				if(toReturn != null) return toReturn;
				checked.add(1);
			}
			if(list == 2 && !checked.contains(2)) {
				toReturn = searchList(background);
				if(toReturn != null) return toReturn;
				checked.add(2);
			}
			if(list == 3 && !checked.contains(3)) {
				toReturn = searchList(sleeping);
				if(toReturn != null) return toReturn;
				checked.add(3);
			}
		}
		var values = waiting.values();
		for(var v : values) {
			for(int i = 0; i < 100; i++) {
				if(v.pageTable[i] != null && v.pageTable[i].physical != -1) return v;
			}
		}
		return null;
	}
	
	/**
	 * Gets the currently running PCB
	 * @return PCB
	 */
	public PCB getCurrentlyRunning() {
		return currentlyRunning;
	}
	/**
	 * Adds toAdd to sleeping
	 * @param toAdd
	 */
	public void addSleeping(PCB toAdd) {
		if(sleeping.size() == 0) sleeping.add(toAdd);
		else {
			int i = 0;
			while(sleeping.get(i).getWakeUpTime() < toAdd.getWakeUpTime() && i < sleeping.size()) i++;
			sleeping.add(i, toAdd);
		}
	}
	/**
	 * sets the wakeUpTime in currentlyRunning to the current time plus milliseconds, adds to sleeping, and calls switch process
	 */
	public void sleep(int milliseconds) {
		currentlyRunning.setWakeUpTime(ZonedDateTime.now().get(ChronoField.MILLI_OF_DAY));
		addSleeping(currentlyRunning);
		currentlyRunning = null;
		SwitchProcess();
	}
	/**
	 * Creates up and switches the process if there is no currently running process
	 * @param up
	 * @return the id of up
	 */
	public int CreateProcess(PCB up) {
		addToList(up);
		if(currentlyRunning == null) {
			SwitchProcess();
		}
		return up.getPid();
	}
	/**
	 * wake up any processes that are sleeping and the current time has passed their wakeUpTime
	 * Gets a random number and decides which priority process to run based off of this number and what processes are available to run
	 * Clears the TLB in Hardware
	 */
	public void SwitchProcess() {
		int[][] newTLB = {{-1, -1}, {-1, -1}};
		if(currentlyRunning != null && currentlyRunning.getName().contains("MemoryProcess"))
			System.out.print("\nBefore Process Switch: TLB is " + Hardware.TLBtoString());
		Hardware.TLB = newTLB;
		if(currentlyRunning != null && currentlyRunning.getName().contains("MemoryProcess"))
			System.out.print("   After Process Switch: TLB is " + Hardware.TLBtoString() + "\n");
		while(sleeping.size() > 0 && sleeping.get(0).getWakeUpTime() <= ZonedDateTime.now().get(ChronoField.MILLI_OF_DAY)) {
			addToList(sleeping.remove(0));
		}
		Random rand = new Random();
		int num = rand.nextInt(20);
		if(realTime.size() != 0) {
			if(num <= 11) switching(realTime);
			else if(num <= 17 && interactive.size() != 0) switching(interactive);
			else if(background.size() != 0) switching(background);
			else switching(realTime);
		}
		else if(interactive.size() != 0) {
			if(num <= 14 || background.size() == 0) switching(interactive);
			else switching(background);
		}
		else if(background.size() != 0) switching(background);
		else if(sleeping.size() != 0) SwitchProcess();
		else System.exit(0);
	}
	/**
	 * Puts the currently running process on the waiting queue if there's no message sent to it, respond to the message otherwise
	 * @return KernelMessage
	 */
	public KernelMessage WaitForMessage() {
		if(currentlyRunning.getMessages().size() > 0) {
			return currentlyRunning.getMessages().removeFirst();
		}
		int waitingPid = GetPid();
		waiting.put(waitingPid, currentlyRunning);
		currentlyRunning = null;
		SwitchProcess();
		return null;
	}
	/**
	 * Gets the PCB with process id pid
	 * @param pid
	 * @return PCB
	 */
	public PCB getPCBbyPid(int pid) {
		for(PCB p : realTime) if(pid == p.getPid()) return p;
		for(PCB p : interactive) if(pid == p.getPid()) return p;
		for(PCB p : background) if(pid == p.getPid()) return p;
		for(PCB p : sleeping) if(pid == p.getPid()) return p;
		var keys = waiting.keySet();
		for(int key : keys) if(pid == key) return waiting.get(key);
		return null;
	}
	/**
	 * Gets the process id of a process with this name
	 * @param name
	 * @return process id
	 */
	public int GetPidByName(String name) {
		for(PCB p : realTime) if(p.getName().equals(name)) return p.getPid();
		for(PCB p : interactive) if(p.getName().equals(name)) return p.getPid();
		for(PCB p : background) if(p.getName().equals(name)) return p.getPid();
		for(PCB p : sleeping) if(p.getName().equals(name)) return p.getPid();
		var keys = waiting.keySet();
		for(int key : keys) if(waiting.get(key).getName().equals(name)) return waiting.get(key).getPid();
		return -1;
	}
	/**
	 * Gets the process id of the currently running process
	 * @return process id
	 */
	public int GetPid() {
		return currentlyRunning.getPid();
	}
	/**
	 * if there currentlyRunning is null or done, set currentlyRunning to first item in list, otherwise call addToList with currentlyRunning and then 
	 * set currentlyRunning to the next item of this list
	 * @param list
	 */
	public void switching(ArrayList<PCB> list) {
		if(currentlyRunning == null) {
			currentlyRunning = list.remove(0);
		}
		else if(currentlyRunning.isDone()) {
			for(int id : currentlyRunning.ids) {
				if(id != -1)
					kernel.Close(id);
			}
			currentlyRunning = list.remove(0);
		}
		else {
			addToList(currentlyRunning);
			currentlyRunning = list.remove(0);
		}
	}
	/**
	 * Adds up to the correct list
	 * @param up
	 */
	public void addToList(PCB up) {
		switch (up.priority){
			case REAL_TIME:
				realTime.add(up);
				break;
			case INTERACTIVE:
				interactive.add(up);
				break;
			case BACKGROUND:
				background.add(up);
				break;
			default: break;
		}
	}
	/**
	 * Exits currentlyRunning, closes all devices, and calls SwitchProcess
	 */
	public void exit() {
		currentlyRunning.exit();
		SwitchProcess();
	}
}
