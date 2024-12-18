
public class NoAllocation extends UserlandProcess{
	/**
	 * Demonstrates that a process will be killed if it tries to read or write in memory without allocating first
	 */
	public void main() {
		while(true) {
			try {
				System.out.println(Read(0));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
