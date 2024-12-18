
public class Ping extends UserlandProcess{
	KernelMessage km;
	/**
	 * The main function for the Ping class. Sends the first message, then waits for a message and repeats that process
	 */
	public void main() {
		while(true) {
			try {
				int i = getTimesStarted()%3;
				if(getTimesStarted() == 1) {
					km = new KernelMessage(0, null, OS.GetPidByName("Pong"));
					OS.SendMessage(km);
				}
				Thread.sleep(75);
				if(i == 1) {
					km = (KernelMessage)OS.returnValue;
					System.out.println("PING " + km.toString());
					km.setTo(OS.GetPidByName("Pong"));
					km.setWhat(km.getWhat() + 1);
					OS.SendMessage(km);
				}
				else {
					OS.WaitForMessage();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
