import java.util.Random;
import java.util.Arrays;
public class Kernel extends Process implements Device{
	private Scheduler scheduler;
	private VFS virtualFileSystem;
	private boolean[] checkPagesInUse = new boolean[1024];//used to check which pages are available in memory

	/**
	 * @return the scheduler from this class
	 */
	public Scheduler getScheduler() {
		return scheduler;
	}
	/**
	 * Constructor for Kernel, initializes scheduler and virtualFileSystem
	 */
	public Kernel() {
		super();
		scheduler = new Scheduler(this);
		virtualFileSystem = new VFS();
	}
	/**
	 * Implemented from Process, checks what currentCall is from OS, handles the function accordingly using Scheduler, starts 
	 * the currently running process, and stops itself
	 */
	public void main() {
		while(true) {
			switch(OS.currentCall) {
			case FREEMEMORY:
				OS.returnValue = FreeMemory((int)OS.parameters.get(0), (int)OS.parameters.get(1));
				break;
			case ALLOCATEMEMORY:
				OS.returnValue = AllocateMemory((int)OS.parameters.get(0));
				break;
			case WAITFORMESSAGE:
				OS.returnValue = WaitForMessage();
				break;
			case SENDMESSAGE:
				if(OS.parameters.size() == 0) break;
				KernelMessage km = (KernelMessage)OS.parameters.get(0);
				SendMessage(km);
				break;
			case OPEN:
				OS.returnValue = Open((String)OS.parameters.get(0));
				break;
			case CLOSE:
				Close((int)OS.parameters.get(0));
				break;
			case READ:
				OS.returnValue = Read((int)OS.parameters.get(0), (int)OS.parameters.get(1));
				break;
			case SEEK:
				Seek((int)OS.parameters.get(0), (int)OS.parameters.get(1));
				break;
			case WRITE:
				OS.returnValue = Write((int)OS.parameters.get(0), (byte[])OS.parameters.get(1));
				break;
			case EXIT:
				Exit();
				break;
			case SLEEP:
				scheduler.sleep((int)OS.parameters.get(0));
				break;
			case SWITCH_PROCESS:
				scheduler.SwitchProcess();
				break;
			case CREATE_PROCESS:
				OS.returnValue = scheduler.CreateProcess((PCB)OS.parameters.get(0));
				break;
			default: 
				break;
			}
			scheduler.currentlyRunning.start();
			try {
				this.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void printCheckPagesInUse() {
		System.out.println(Arrays.toString(checkPagesInUse));
	}
	
	/**
	 * Maps the TLB to the pageNumber given "virtualPageNumber", picks the TLB entry to update at random
	 * Picks a random victim page to write on disk, gives that memory to the currently running process
	 * @param virtualPageNumber
	 */
	public void GetMapping(int virtualPageNumber) {
		Random random = new Random();
		int index = random.nextInt(2);
		Hardware.TLB[0][index] = virtualPageNumber;
		if(scheduler.currentlyRunning.pageTable[virtualPageNumber].physical != -1)
			Hardware.TLB[1][index] = scheduler.currentlyRunning.pageTable[virtualPageNumber].physical;
		else {
			for(int i = 0; i < checkPagesInUse.length; i++) {
				if(!checkPagesInUse[i]) {
					checkPagesInUse[i] = true;
					scheduler.currentlyRunning.pageTable[virtualPageNumber].physical = i;
					Hardware.TLB[1][index] = i;
					return;
				}
			}
			PCB victim = scheduler.getRandomProcess();
			System.out.println("All memory has been used, victim process has pid: " + victim.getPid());
			int victimPage = -1;
			for(int i = 0; i < 100; i++) if(victim.pageTable[i] != null && victim.pageTable[i].physical != -1) victimPage = i;
			byte[] toWrite = new byte[1024];
			for(int i = 0; i < toWrite.length; i++) toWrite[i] = Hardware.Read(victim.pageTable[victimPage].physical * 1024 + i);
			System.out.println("Writing block that starts with " + toWrite[0] + " to disk from victimPage");
			if(victim.pageTable[victimPage].onDisk != -1)
				virtualFileSystem.Seek(OS.swapFileIndex, victim.pageTable[victimPage].onDisk * 1024);
			else {
				virtualFileSystem.Seek(OS.swapFileIndex, OS.currentDiskPageNum * 1024);
				victim.pageTable[victimPage].onDisk = OS.currentDiskPageNum;
				OS.currentDiskPageNum++;
			}
			virtualFileSystem.Write(OS.swapFileIndex, toWrite);
			scheduler.currentlyRunning.pageTable[virtualPageNumber].physical = victim.pageTable[victimPage].physical;
			victim.pageTable[victimPage].physical = -1;
			if(scheduler.currentlyRunning.pageTable[virtualPageNumber].onDisk != -1) {
				virtualFileSystem.Seek(OS.swapFileIndex, scheduler.currentlyRunning.pageTable[virtualPageNumber].onDisk * 1024);
				byte[] toRead = virtualFileSystem.Read(OS.swapFileIndex, 1024);
				for(int i = 0; i < toRead.length; i++) {
					Hardware.Write(scheduler.currentlyRunning.pageTable[virtualPageNumber].physical * 1024 + i, toRead[i]);
				}
			}
			else
				for(int i = 0; i < 1024; i++)
					Hardware.Write(scheduler.currentlyRunning.pageTable[virtualPageNumber].physical * 1024 + i, (byte)0);
		}
	}
	/**
	 * Frees all memory from the currently running process and then calls scheduler.exit
	 */
	public void Exit() {
		FreeMemory(0, scheduler.currentlyRunning.pageTable.length);
		scheduler.exit();
	}
	/**
	 * Frees the memory at a pointer in memory given by "pointer" and the amount to free given by "size"
	 * @param pointer
	 * @param size
	 * @return false on failure, true on success
	 */
	public boolean FreeMemory(int pointer, int size) {
		for(int i = pointer; i < pointer + size; i++) {
			if(scheduler.currentlyRunning.pageTable[i] == null) return false;
			if(scheduler.currentlyRunning.pageTable[i].physical != -1)
				checkPagesInUse[scheduler.currentlyRunning.pageTable[i].physical] = false;
			scheduler.currentlyRunning.pageTable[i] = null;
		}
		return true;
	}
	
	/**
	 * Frees the memory at a pointer in memory given by "pointer" and the amount to free given by "size"
	 * @param pointer
	 * @param size
	 * @return false on failure, true on success
	 */
	public boolean OldFreeMemory(int pointer, int size) {
		if(size%1024 != 0 || pointer%1024 != 0) return false;
		int numOfPages = size/1024;
		int firstIndex = pointer/1024;
		for(int i = firstIndex; i < firstIndex + numOfPages; i++) {
			checkPagesInUse[i] = false;
			for(int j = 0; j < scheduler.currentlyRunning.pageTable.length; j++)
				if(scheduler.currentlyRunning.pageTable[j] != null && scheduler.currentlyRunning.pageTable[j].physical == i) {
					scheduler.currentlyRunning.pageTable[j] = null;
					break;
				}
		}
		return true;
	}
	/**
	 * Helper function to find the start index of the page table given the "numOfPages". Returns -1 on failure
	 * @param numOfPages
	 * @return start index
	 */
	public int findStartIndex(int numOfPages) {
		for(int i = 0; i + numOfPages <= scheduler.currentlyRunning.pageTable.length; i++) {
			for(int j = 0; j < numOfPages; j++) {
				if(scheduler.currentlyRunning.pageTable[i+j] != null) break;
				if(j + 1 == numOfPages) {
					return i;
				}
			}
		}
		return -1;
	}
	/**
	 * Finds the pages in memory to allocate and maps them to as many entries in the currently running PCB's page table, updates checkpagesInUse
	 * @param size
	 * @return the start index in the PCB's page table
	 */
	public int AllocateMemory(int size) {
		if(size%1024 != 0) {
			System.out.println(scheduler.currentlyRunning.getName() + " tried to allocate memory that was not the correct size of a page");
			return -1;
		}
		int numOfPages = size/1024;
		int freePages = 0;
		for(int i = 0; i < scheduler.currentlyRunning.pageTable.length; i++)
			if(scheduler.currentlyRunning.pageTable[i] == null) freePages++;
		int toReturn = findStartIndex(numOfPages);
		if(freePages < numOfPages || toReturn == -1) {
			System.out.println(scheduler.currentlyRunning.getName() + " was killed because it tried to allocate more memory than it was allowed");
			OS.exit();
		}
		for(int i = 0; i < numOfPages; i++) 
			scheduler.currentlyRunning.pageTable[i + toReturn] = new VirtualToPhysicalMapping();
		return toReturn;
	}
	
	
	/**
	 * Finds the pages in memory to allocate and maps them to as many entries in the currently running PCB's page table, updates checkpagesInUse
	 * @param size
	 * @return the start index in the PCB's page table
	 */
	public int OldAllocateMemory(int size) {
		if(size%1024 != 0) {
			System.out.println(scheduler.currentlyRunning.getName() + " tried to allocate memory that was not the correct size of a page");
			return -1;
		}
		int numOfPages = size/1024;
		int freePages = 0;
		for(int i = 0; i < scheduler.currentlyRunning.pageTable.length; i++)
			if(scheduler.currentlyRunning.pageTable[i] == null) freePages++;
		int toReturn = findStartIndex(numOfPages);
		if(freePages < numOfPages || toReturn == -1) {
			System.out.println(scheduler.currentlyRunning.getName() + " was killed because it tried to allocate more memory than it was allowed");
			OS.exit();
		}
		int pagesAllocated = 0;
		for(int i = 0; i < checkPagesInUse.length && pagesAllocated < numOfPages; i++) {
			if(!checkPagesInUse[i]) {
				VirtualToPhysicalMapping vpm = new VirtualToPhysicalMapping();
				vpm.physical = i;
				scheduler.currentlyRunning.pageTable[toReturn + pagesAllocated] = vpm;
				checkPagesInUse[i] = true;
				pagesAllocated++;
			}
		}
		return toReturn;
	}
	/**
	 * Calls WaitForMessage in the Scheduler
	 * @return KernelMessage
	 */
	public KernelMessage WaitForMessage() {
		return scheduler.WaitForMessage();
	}
	/**
	 * Sends a message to the process with the process id "to" and de-schedules it if it is waiting
	 * @param km
	 */
	public void SendMessage(KernelMessage km) {
		KernelMessage kernelMessage = new KernelMessage(km);
		kernelMessage.setFrom(GetPid());
		PCB to = scheduler.getPCBbyPid(kernelMessage.getTo());
		if(to != null) {
			to.getMessages().add(kernelMessage);
			if(scheduler.waiting.containsKey(to.getPid())) {
				scheduler.addToList(scheduler.waiting.remove(to.getPid()));
			}
		}		
	}
	/**
	 * Open call for the OS, only used to open the swap file
	 * @param s
	 * @return index of swap file
	 */
	public int OpenOS(String s) {
		return virtualFileSystem.Open(s);
	}
	
	/**
	 * Calls Open in VFS
	 * @param s
	 * @returns the id of the opened device
	 */
	@Override
	public int Open(String s) {
		int id = virtualFileSystem.Open(s);
		if(id == -1) return -1;
		for(int i = 0; i < scheduler.getCurrentlyRunning().ids.length; i++)
			if(scheduler.getCurrentlyRunning().ids[i] == -1) {
				scheduler.getCurrentlyRunning().ids[i] = id;
				return i;
			}
		return -1;
	}
	/**
	 * Calls GetPidByName in the Scheduler
	 * @param name
	 * @return process id
	 */
	public int GetPidByName(String name) {
		return scheduler.GetPidByName(name);
	}
	/**
	 * Calls GetPid in the Scheduler
	 * @return process id
	 */
	public int GetPid() {
		return scheduler.GetPid();
	}
	/**
	 * Calls Close in VFS
	 * @param id
	 */
	@Override
	public void Close(int id) {
		virtualFileSystem.Close(scheduler.getCurrentlyRunning().ids[id]);
		scheduler.getCurrentlyRunning().ids[id] = -1;	
	}
	/**
	 * Calls Read in VFS
	 * @param id
	 * @param size
	 * @return the data read from the device
	 */
	@Override
	public byte[] Read(int id, int size) {
		return virtualFileSystem.Read(scheduler.getCurrentlyRunning().ids[id], size);
	}
	/**
	 * Calls Seek in VFS
	 * @param id
	 * @param to
	 */
	@Override
	public void Seek(int id, int to) {
		virtualFileSystem.Seek(scheduler.getCurrentlyRunning().ids[id], to);
	}
	/**
	 * Calls Write in VFS
	 * @param id
	 * @param data
	 * @return size of the device after writing
	 */
	@Override
	public int Write(int id, byte[] data) {
		return virtualFileSystem.Write(scheduler.getCurrentlyRunning().ids[id], data);
	}
}