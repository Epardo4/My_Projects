/*
 * Created by Eli Pardo
 */
import java.util.LinkedList;
public class Lexer {
	public String[] input;
	/**
	 * Constructor for the Lexer class
	 * @param input
	 */
	public Lexer(String input) {
		this.input = input.split("\n");
	}
	/**
	 * Goes through input and creates tokens for each possible assembly instruction
	 * @return an array containing tokens for each line of assembly code
	 */
	public LinkedList<Token>[] lex() throws Exception{
		LinkedList<Token>[] lines = new LinkedList[input.length];
		String[] line;
		LinkedList<Token> tokens;
		//splits the lines into individual words, then creates tokens out of them
		for(int j = 0 ; j < lines.length; j++) {
			line = input[j].split(" ");
			tokens = new LinkedList<Token>();
			//figures out what the next token in the line is
			for(String i : line) {
				if(i.charAt(i.length() - 1) + 0 == 13)
					i = i.substring(0, i.length() - 1);
				if(i.equals("MATH"))
					tokens.add(new Token(Token.token.math));
				else if(i.equals("AND"))
					tokens.add(new Token(Token.token.and));
				else if(i.equals("OR"))
					tokens.add(new Token(Token.token.or));
				else if(i.equals("XOR"))
					tokens.add(new Token(Token.token.xor));
				else if(i.equals("NOT"))
					tokens.add(new Token(Token.token.not));
				else if(i.equals("LEFT"))
					tokens.add(new Token(Token.token.left));
				else if(i.equals("RIGHT"))
					tokens.add(new Token(Token.token.right));
				else if(i.equals("SHIFT"))
					tokens.add(new Token(Token.token.shift));
				else if(i.equals("ADD"))
					tokens.add(new Token(Token.token.add));
				else if(i.equals("MULT"))
					tokens.add(new Token(Token.token.multiply));
				else if(i.equals("SUBTRACT"))
					tokens.add(new Token(Token.token.subtract));
				else if(i.equals("HALT"))
					tokens.add(new Token(Token.token.halt));
				else if(i.equals("DESTONLY"))
					tokens.add(new Token(Token.token.copy));
				else if(i.equals("BRANCH"))
					tokens.add(new Token(Token.token.branch));
				else if(i.equals("CALL"))
					tokens.add(new Token(Token.token.call));
				else if(i.equals("PUSH"))
					tokens.add(new Token(Token.token.push));
				else if(i.equals("LOAD"))
					tokens.add(new Token(Token.token.load));
				else if(i.equals("STORE"))
					tokens.add(new Token(Token.token.store));
				else if(i.equals("POP"))
					tokens.add(new Token(Token.token.pop));
				else if(i.equals("PEEK"))
					tokens.add(new Token(Token.token.peek));
				else if(i.equals("EQUALS"))
					tokens.add(new Token(Token.token.equals));
				else if(i.equals("NOTEQUALS"))
					tokens.add(new Token(Token.token.notEquals));
				else if(i.equals("LESSTHAN"))
					tokens.add(new Token(Token.token.lessThan));
				else if(i.equals("GREATEROREQUAL"))
					tokens.add(new Token(Token.token.greaterOrEqual));
				else if(i.equals("GREATERTHAN"))
					tokens.add(new Token(Token.token.greaterThan));
				else if(i.equals("LESSOREQUAL"))
					tokens.add(new Token(Token.token.lessOrEqual));
				else if(i.equals("RETURN"))
					tokens.add(new Token(Token.token.Return));
				else if(i.equals("CALLIF"))
					tokens.add(new Token(Token.token.callIf));
				else if(i.equals("JUMP"))
					tokens.add(new Token(Token.token.jump));
				else if(i.length() > 0 && i.charAt(0) == 'R') 
					tokens.add(new Token(Token.token.register, tryInt(i.substring(1))));
				else if(i.length() > 0)
					tokens.add(new Token(Token.token.immediate, tryInt(i)));
			}
			lines[j] = tokens;
		}
		return lines;
	}
	/**
	 * Tries to turn the check parameter into an integer, throws an exception if not able to
	 * @param check
	 * @return the converted int
	 */
	public int tryInt(String check) throws Exception{
		if(check.charAt(check.length() - 1) + 0 == 13) {
			check = check.substring(0, check.length() - 1);
		}
		try {
			return Integer.parseInt(check);
		}
		catch(Exception e) {
			throw new Exception("The input: " + check + " is either not valid or does not contain a correct integer");
		}
	}
	/**
	 * toString for testing the tokens
	 * @return String
	 */
	public String toString(LinkedList<Token>[] toPrint) {
		String toReturn = "";
		if(toPrint.length > 0 && toPrint[0].size() > 0)
			for(var list : toPrint) {
				for(var token : list)
					toReturn += token;
				toReturn = toReturn.substring(0, toReturn.length() - 1) + "\n";
			}
		return toReturn;
	}
}