/*
 * Created by Eli Pardo
 */
public class Bit {
	private boolean value;
	/**
	 * Constructor for Bit
	 * @param value
	 */
	public Bit(boolean value) {
		this.value = value;
	}
	/**
	 * Mutates the value of the bit to be value
	 * @param value
	 */
	public void set(boolean value) {
		this.value = value;
	}
	/**
	 * Mutates the value of the bit to be true
	 */
	public void set() {
		value = true;
	}
	/**
	 * Mutates the value of the bit to be false
	 */
	public void clear(){
		value = false;
	}
	/**
	 * Mutates the value of the bit to be opposite of what it currently is
	 */
	public void toggle() {
		if(value) value = false;
		else value = true;
	}
	/**
	 * Accesses the value of the bit
	 */
	public boolean getValue() {
		return value;
	}
	/**
	 * If both the value of other and this bit are true, then return a new true bit, otherwise make it false
	 * @param other
	 * @return Bit
	 */
	public Bit and(Bit other) {
		if(value) 
			if(other.getValue()) return new Bit(true);
		return new Bit(false);
	}
	/**
	 * If either of the values of other and this bit are true, then return a new true bit, otherwise make it false
	 * @param other
	 * @return Bit
	 */
	public Bit or(Bit other) {
		if(value) return new Bit(true);
		if(other.getValue()) return new Bit(true);
		return new Bit(false);
	}
	/**
	 * If the values of other and this bit are different, then return a new true bit, otherwise make it false
	 * @param other
	 * @return Bit
	 */
	public Bit xor(Bit other) {
		if(value) {
			if(other.getValue()) return new Bit(false);
		}
		else if(other.getValue() == false) return new Bit(false);
		return new Bit(true);
	}
	/**
	 * Mutates the value of the bit to be opposite of what it currently is
	 */
	public Bit not() {
		if(value) return new Bit(false);
		return new Bit(true);
	}
	/**
	 * Displays a true bit as "t" and a false bit as "f"
	 * @return String
	 */
	public String toString() {
		if(value) return "t";
		return "f";
	}
}
