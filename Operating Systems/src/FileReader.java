
public class FileReader extends UserlandProcess{
	private int index = -1;
	/**
	 * The main function for the FileReader class. Opens a file, reads from it, and sleeps
	 */
	public void main() {
		while(true) {
			try {
				Thread.sleep(100);
				if(getTimesStarted() == 1) OS.Open("file testFile.txt");
				else if(getTimesStarted() == 2) index = (int)OS.returnValue;
				if(getTimesStarted()%2 == 0)
					OS.Read(index, 5);
				else {
					System.out.println("FileReader read: " + getArrayString((byte[])OS.returnValue) + "   from FakeFileSystem at index " + index);
					OS.sleep(50);
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
