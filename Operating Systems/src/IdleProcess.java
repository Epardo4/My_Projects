
public class IdleProcess extends UserlandProcess {
	public void main() {
		/**
		 * Implemented from Process, sleep for 50 milliseconds, and calls cooperate
		 */
		while(true) {
			try {
				cooperate();
				Thread.sleep(50);
//				cooperate();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
