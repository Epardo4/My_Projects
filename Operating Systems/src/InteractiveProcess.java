
public class InteractiveProcess extends UserlandProcess{
	/**
	 * Implemented from Process, prints that it is the interactive process, sleeps for 50 milliseconds, and calls cooperate
	 */
	@Override
	public void main() {
		while(true) {
			System.out.println("This is the interactive process        " + getPriority());
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
