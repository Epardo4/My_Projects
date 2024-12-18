import java.util.Random;
public class RandomDevice implements Device{
	Random[] randoms = new Random[10]; //holds all the Random objects for this device
	/**
	 * Creates a new Random with seed s and puts it at the first available index in the array
	 * @param s
	 * @return id of new Random
	 */
	public int Open(String s) {
		Random rand = new Random(Integer.parseInt(s));
		for(int id = 0; id < randoms.length; id++) {
			if(randoms[id] == null) {
				randoms[id] = rand;
				return id;
			}
		}
		return -1;
	}
	/**
	 * Sets the Random at id to null
	 * @param id
	 */
	public void Close(int id) {
		randoms[id] = null;
	}
	/**
	 * Reads data of size "size" from the Random at id
	 * @param id
	 * @param size
	 * @return the data read from the Random at id
	 */
	public byte[] Read(int id, int size) {
		if(randoms[id] == null) return null;
		byte[] toReturn = new byte[size];
		randoms[id].nextBytes(toReturn);
		return toReturn;
	}
	/**
	 * Reads from the Random at id, but does not return the data
	 * @param id
	 * @param to
	 */
	public void Seek(int id, int to) {
		byte[] toSeek = new byte[to];
		randoms[id].nextBytes(toSeek);
	}
	/**
	 * Just an empty function, doesn't do anything
	 * @param id
	 * @param data
	 * @return 0
	 */
	public int Write(int id, byte[] data) {
		return 0;
	}
}
