public class RandomReader extends UserlandProcess{
	private int index = -1;
	/**
	 * The main function for RandomReader. Opens a random device, reads from it, and sleeps
	 */
	public void main() {
		while(true) {
			try {
				Thread.sleep(100);
				if(getTimesStarted() == 1) OS.Open("random 100");
				else if(getTimesStarted() == 2) index = (int)OS.returnValue;
				if(getTimesStarted()%2 == 0) OS.Read(index, 5);
				else {
					OS.bytesRead = (byte[])OS.returnValue;
					System.out.println("RandomReader read: " + getArrayString(OS.bytesRead) + "   from RandomDevice at index " + index);
					OS.sleep(50);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
