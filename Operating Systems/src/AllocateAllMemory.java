import java.util.Random;
public class AllocateAllMemory extends UserlandProcess{
	/**
	 * This is the main function for the AllocateAllMemory class, it allocates all 100 blocks of memory for this process, writes and reads
	 */
	public void main() {
		try {
			if(getTimesStarted() == 5) {
				System.out.println("Freeing and exiting from " + getPid());
				OS.exit();
			}
			if(getTimesStarted() == 1)
				OS.AllocateMemory(102400);
			else if(getTimesStarted() == 4){
				byte[] toRead = new byte[5];
				int index = 0;
				for(int i = 0; i < 5; i++) {
					toRead[i] = Read(index);
					index += 5120;
				}
				System.out.println("Read " + getArrayString(toRead) + " from memory");
				OS.SwitchProcess();
			}
			else {
				Random random = new Random();
				byte[] bytes = new byte[100];
				random.nextBytes(bytes);
				int index = 0;
				for(byte b : bytes) {
					Write(index, b);
					index += 1024;
				}
				Thread.sleep(100);
				System.out.println("\nSwitching Processes from " + getPid() + "\n");
				OS.SwitchProcess();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
