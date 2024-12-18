import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
public class FakeFileSystem implements Device{
	RandomAccessFile[] files = new RandomAccessFile[10]; //holds all the RandomAccessFile objects for this device
	/**
	 * Creates a new File with seed s and puts it at the first available index in the array
	 * @param s
	 * @return id of new File
	 */
	public int Open(String s) {
		try {
			if(s == null || s.equals(""))
				throw new Exception("File name is empty or null");
		}catch(Exception e) {
			e.printStackTrace();
		}
		RandomAccessFile file = null;
		try {
			file = new RandomAccessFile(s, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int id = 0; id < files.length; id++) {
			if(files[id] == null) {
				files[id] = file;
				return id;
			}
		}
		return -1;
	}
	/**
	 * Sets to null and closes the file at id to null
	 * @param id
	 */
	public void Close(int id) {
		if(files[id] != null) {
			try {
				files[id].close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			files[id] = null;
		}
	}
	/**
	 * Reads data of size "size" from the file at id
	 * @param id
	 * @param size
	 * @return the data read from the file at id
	 */
	public byte[] Read(int id, int size) {
		byte[] toReturn = new byte[size];
		try {
			files[id].read(toReturn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toReturn;
	}
	/**
	 * Reads from the file at id, but does not return the data
	 * @param id
	 * @param to
	 */
	public void Seek(int id, int to) {
		try {
			files[id].read(new byte[to]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Writes data to the file at id
	 * @param id
	 * @param data
	 * @return the size of the file after writing to it
	 */
	public int Write(int id, byte[] data) {
		try {
			files[id].write(data);
			return (int)files[id].length();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1;
	}		
}
