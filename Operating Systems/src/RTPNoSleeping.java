
public class RTPNoSleeping extends UserlandProcess{
	/**
	 * Implemented from Process, prints that it is the real time process that doesn't call OS.sleep, sleeps for 50 milliseconds, and calls cooperate
	 */
	@Override
	public void main() {
		while(true) {
			System.out.println("This is the Real Time Process that does not sleep     " + getPriority());
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				cooperate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
