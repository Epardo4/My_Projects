public class KernelMessage {
	private int what; //what to do with this message
	private int from; //which process is this message from
	private int to; //which process is this message to
	private byte[] data; //this message's data
	/**
	 * Constructor for the KernelMessage Class
	 * @param what
	 * @param data
	 * @param to
	 */
	public KernelMessage(int what, byte[] data, int to) {
		this.what = what;
		this.data = data;
		this.to = to;
	}
	/**
	 * Copy Constructor for the KernelMessage
	 * @param toCopy
	 */
	public KernelMessage(KernelMessage toCopy) {
		what = toCopy.getWhat();
		data = toCopy.getData();
		from = toCopy.getFrom();
		to = toCopy.getTo();
	}
	/**
	 * Creates a String to represent this KernelMessage
	 * @return String
	 */
	public String toString() {
		return "from: " + from + " to: " + to + " what: " + what;
	}
	/**
	 * Sets the "to" member of this class
	 * @param to
	 */
	public void setTo(int to) {
		this.to = to;
	}
	/**
	 * Sets the "what" member of this class
	 * @param what
	 */
	public void setWhat(int what) {
		this.what = what;
	}
	/**
	 * Sets the "from" member of this class
	 * @param from
	 */
	public void setFrom(int from) {
		this.from = from;
	}
	/**
	 * Gets the "from" member of this class
	 * @return from
	 */
	public int getFrom() {
		return from;
	}
	/**
	 * Gets the "to" member of this class
	 * @return to
	 */
	public int getTo() {
		return to;
	}
	/**
	 * Gets the "what" member of this class
	 * @return what
	 */
	public int getWhat() {
		return what;
	}
	/**
	 * Gets the "data" member of this class
	 * @return data
	 */
	public byte[] getData() {
		return data;
	}
}
