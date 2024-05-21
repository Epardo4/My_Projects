/*
 * Created by Eli Pardo
 */
public class Word {
	private Bit[] word = new Bit[32]; //contains each bit for this word
	/**
	 * Constructor for the Word class with the word array
	 * @param word
	 */
	public Word(Bit[] word) {
		for(int i = 0; i < 32; i++)
			this.word[i] = new Bit(word[i].getValue());
	}
	/**
	 * Constructor for the Word class with a value to store in the word array
	 */
	public Word(int value) {
		set(value);
	}
	public Word(Word toCopy) {
		this.copy(toCopy);
	}
	public Word() {
		Bit[] toSet = {new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),
				new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),
				new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),
				new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),new Bit(false),
				new Bit(false),new Bit(false),new Bit(false),new Bit(false)};
		word = toSet;
	}
	/**
	 * Accesses the bit at index i and returns a new bit with that value
	 * @param i
	 * @return Bit
	 */
	public Bit getBit(int i) {
		return new Bit(word[i].getValue());
	}
	/**
	 * Mutates the value of the bit at index i to be value
	 * @param i
	 * @param value
	 */
	public void setBit(int i, Bit value) {
		word[i] = new Bit(value.getValue());
	}
	/**
	 * Performs the and operation on every bit for both this and the other words and creates a new one with the result
	 * @param other
	 * @return Word
	 */
	public Word and(Word other) {
		Bit[] result = new Bit[32];
		for(int i = 0; i < word.length; i++) 
			result[i] = word[i].and(other.getBit(i));
		return new Word(result);
	}
	/**
	 * Performs the or operation on every bit for both this and the other words and creates a new one with the result
	 * @param other
	 * @return Word
	 */
	public Word or(Word other) {
		Bit[] result = new Bit[32];
		for(int i = 0; i < word.length; i++)
			result[i] = word[i].or(other.getBit(i));
		return new Word(result);
	}
	/**
	 * Performs the xor operation on every bit for both this and the other words and creates a new one with the result
	 * @param other
	 * @return Word
	 */
	public Word xor(Word other) {
		Bit[] result = new Bit[32];
		for(int i = 0; i < word.length; i++)
			result[i] = word[i].xor(other.getBit(i));
		return new Word(result);
	}
	/**
	 * Performs the not operation on every bit for this word and creates a new one with the result
	 * @return Word
	 */
	public Word not() {
		Bit[] result = new Bit[32];
		for(int i = 0; i < word.length; i++)
			result[i] = word[i].not();
		return new Word(result);
	}
	/**
	 * Shifts each bit value in word the specified amount right
	 * @param amount
	 * @return Word
	 */
	public Word rightShift(int amount) {
		Bit[] result = new Bit[32];
		//move each value to the right
		for(int i = 0; i < word.length - amount; i++) {
			result[i] = new Bit(this.getBit(i + amount).getValue());
		}
		//set each other value to be false
		for(int i = word.length - amount; i < word.length; i++)
			result[i] = new Bit(false);
		return new Word(result);
	}
	/**
	 * Shifts each bit value in word the specified amount left
	 * @param amount
	 * @return Word
	 */
	public Word leftShift(int amount) {
		Bit[] result = new Bit[32];
		//move each value to the left
		for(int i = 0; i < word.length - amount; i++)
			result[i + amount] = new Bit(word[i].getValue());
		//set everything else to be false
		for(int i = 0; i < amount; i++)
			result[i] = new Bit(false);
		return new Word(result);
	}
	/***
	 * Creates a String to display the word
	 * @return String
	 */
	public String toString() {
		String result = "";
		for(int i = word.length - 1; i >= 0; i--)
			result += word[i].toString();
		return result;
	}
	/**
	 * Converts the binary word value into a long value
	 * @return long
	 */
	public long getUnsigned() {
		long result = 0;
		for(int i = 0; i < word.length; i++)
			if(word[i].getValue()) result += Math.pow(2, i);
		return result;
	}
	/**
	 * Converts the binary word value into an integer value
	 * @return int
	 */
	public int getSigned() {
		int result = 0;
		boolean isNegative = false;
		//if the last bit is true, the value must be negative
		Word test = new Word(this);
		Bit[] check = test.word;
		if(check[31].getValue()) {
			isNegative = true;
			test = test.not();
			check = test.increment().word;
		}
		for(int i = 0; i < check.length; i++)
			if(check[i].getValue()) result += Math.pow(2, i);
		if(isNegative) return result * -1;
		return result;
	}
	/**
	 * Copies each bit in other into this word
	 * @param other
	 */
	public void copy(Word other) {
		for(int i = 0; i < word.length; i++)
			word[i] = new Bit(other.getBit(i).getValue());
	}
	/**
	 * Sets the value of the bit array to be binary for value
	 * @param value
	 */
	public void set(int value) {
		int check = 0;
		boolean isNegative = false;
		if(value < 0) {
			isNegative = true;
			value *= -1;
		}
		//set the entire word to be false
		for(int i = 0; i < word.length; i++)
			word[i] = new Bit(false);
		//keep adding bits to the array as instructed until value is either 1 or 0
		while(value > 1) {
			//check which bit will be bigger than value and then set the bit before that to true
			//subtract this amount from value to get a new number to make binary
			for(int i = 0; i < word.length; i++) {
				check = (int)Math.pow(2, i);
				if(check > value) {
					word[i-1] = new Bit(true);
					value -= check/2;
					i = word.length;
				}
			}
		}
		if(value == 1) word[0] = new Bit(true);
		//not the word and increment it so that way it is considered negative
		if(isNegative) {
			word = this.not().word;
			word = increment().word;
		}
	}
	/**
	 * Increments the current word and returns its result
	 * @return this incremented
	 */
	public Word increment() {
		//copy this word
		Word toReturn = new Word(word);
		Bit check = new Bit(true);
		int i = 0;
		//continue to set the each bit to it's opposite until, it is set to false, or there are no more bits in the word
		while(check.getValue() && i < 32) {
			toReturn.setBit(i, word[i].xor(check));
			check = word[i].and(check);
			i++;
		}
		return toReturn;
	}
	public Word decrement() {
		//copy this word
		Word toReturn = new Word(word);
		Bit check = new Bit(true);
		int i = 0;
		//continue to set the each bit to it's opposite until, it is set to false, or there are no more bits in the word
		while(check.getValue() && i < 32) {
			toReturn.setBit(i, word[i].xor(check));
			check = word[i].xor(check);
			i++;
		}
		return toReturn;
	}
}
