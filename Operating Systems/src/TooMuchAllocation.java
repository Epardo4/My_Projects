
public class TooMuchAllocation extends UserlandProcess{
	/**
	 * Demonstrates that a process will be killed if it tries to allocate too much memory
	 */
	public void main() {
	while(true) {
			try {
				OS.AllocateMemory(112640);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
