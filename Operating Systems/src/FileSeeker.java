
public class FileSeeker extends UserlandProcess{
	private int index = -1;
	/**
	 * The main function for the FileSeeker class. Opens a file. Seeks, reads, and sleeps a few times. Closes the file, and exits
	 */
	public void main() {
		while(true) {
			try {
				Thread.sleep(100);
				if(getTimesStarted() == 1) OS.Open("file testFile.txt");
				else if(getTimesStarted() == 2) index = (int)OS.returnValue;
//				else if(getTimesStarted() == 20) {
//					System.out.println("Closing FakeFileSystem from FileSeeker");
//					OS.Close(index);
//				}
				else if(getTimesStarted() >= 21) {
					System.out.println("Exiting FileSeeker");
					OS.exit();
				}
				if(getTimesStarted()%3 == 2) OS.Seek(index, 3);
				else if(getTimesStarted()%3 == 0) OS.Read(index, 2);
				else {
					System.out.println("FileSeeker read: " + getArrayString((byte[])OS.returnValue) + "   from FakeFileSystem at index " + index);
					OS.sleep(50);
				}			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
