
public class RTPSleeping extends UserlandProcess{
	/**
	 * Prints that this is the real time process that does sleep, then calls OS.sleep, should not get demoted
	 */
	@Override
	public void main() {
		for(int i = 0; i < 3; i++) {
			System.out.println("This is the Real Time Process that does sleep");
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			OS.sleep(50);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
