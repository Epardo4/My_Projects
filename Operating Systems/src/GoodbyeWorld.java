
public class GoodbyeWorld extends UserlandProcess{
	KernelMessage km;
	/**
	 * The main function for the GoodbyeWorld
	 *  class. Waits for a message, sends a message, and repeats that process
	 */
	public void main() {
		while(true) {
			try {
				Thread.sleep(75);
				int i = getTimesStarted()%3;
				if(getTimesStarted() == 1) OS.sleep(10);
				if(i != 0) OS.WaitForMessage();
				else {
					km = (KernelMessage)OS.returnValue;
					System.out.println("GoodbyeWorld " + km.toString());
					km.setTo(OS.GetPidByName("HelloWorld"));
					OS.SendMessage(km);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}