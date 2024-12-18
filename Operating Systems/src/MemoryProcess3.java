import java.util.Random;
public class MemoryProcess3 extends UserlandProcess{
	int amountToWrite = 5120; //Denotes the number of pages to write each time, in this case 5
	int timesRan = 0;
	/**
	 * Reads from and writes random bytes to memory along with the other 3 memory processes. Runs 10 times and then exits
	 */
	public void main() {
		while(true) {
			if(timesRan == 10) {
				System.out.println("MemoryProcess3 exiting and freeing memory");
				OS.exit();
			}
			try {
				if(getTimesStarted()%4 == 1) OS.AllocateMemory(1024);
				else if(getTimesStarted()%4 == 2) {
					timesRan++;
					Thread.sleep(100);
					System.out.println();
					int readIndex = (int)OS.returnValue;
					byte[] bytesRead = new byte[20];
					for(int i = 0 ; i < bytesRead.length; i++)
						bytesRead[i] = Read(readIndex*1024 + i);
					System.out.println("MemoryProcess3 read this from memory:" + getArrayString(bytesRead));
					OS.AllocateMemory(amountToWrite);
				}
				else if(getTimesStarted()%4 == 3) {
					int freeingPageNumber = (int)OS.returnValue;
					int freeingIndex = (freeingPageNumber + 2) * 1024;
					Random random = new Random();
					byte[] toWrite = new byte[amountToWrite];
					random.nextBytes(toWrite);
					for(int i = 0; i < amountToWrite; i++) Write(freeingPageNumber*1024 + i, toWrite[i]);
					int pageNumber = Hardware.getPhysicalAddress(freeingIndex)/1024;
					System.out.println("MemoryProcess3 wrote 5 pages to memory and is now freeing the page starting with " + 
							Read(freeingIndex) + " at physical page number " + pageNumber + " so the next process can read it");
					System.out.println("Page Number " + pageNumber + " in memory is: " + Hardware.memoryToString(pageNumber*1024, 1024));
					OS.FreeMemory(Hardware.getPhysicalAddress(freeingIndex), 1024);
				}
				else OS.SwitchProcess();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
