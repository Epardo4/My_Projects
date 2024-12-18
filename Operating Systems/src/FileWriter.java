
public class FileWriter extends UserlandProcess{
	private int index = -1;
	/**
	 * The main function for the FileWriter class. Opens a file, writes to it, and sleeps
	 */
	public void main() {
		while(true) {
			try {
				Thread.sleep(100);
				if(getTimesStarted() == 1) OS.Open("file testFile.txt");
				else if(getTimesStarted() == 2) index = (int)OS.returnValue;
				if(getTimesStarted()%2 == 0) {
					System.out.println("Writing");
					OS.Write(index, OS.bytesRead);
				}
				else
					OS.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
