/*
 * Created by Eli Pardo
 */
public class Token{
	//list of all possible tokens
	enum token {immediate, register, copy, math, and, or, xor, not, left, right, shift, add, subtract, multiply, halt,
		branch, call, push, load, store, pop, equals, notEquals, lessThan, greaterOrEqual, greaterThan, lessOrEqual, Return, callIf, jump, peek};
	public token t = null;
	int immediate = 0;
	int register = 0;
	/**
	 * Constructor for the Token class
	 * @param t
	 */
	public Token(token t) {
		this.t = t;
	}
	/**
	 * Constructor for the Token class
	 */
	public Token(token t, int value) {
		this.t = t;
		if(t.name().equals("immediate")) immediate = value;
		if(t.name().equals("register")) register = value;
	}
	/**
	 * toString for testing the tokens
	 * @return String
	 */
	public String toString() {
		if(t.name().equals("immediate")) return ("(immediate, " + immediate + ") ");
		else if(t.name().equals("register")) return ("(register, " + register + ") ");
		else return "(" + t.name() + ") ";
	}
}
