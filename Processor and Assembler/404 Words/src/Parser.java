/*
 * Created by Eli Pardo
 */
import java.util.LinkedList;
import java.util.Arrays;
public class Parser {
	public LinkedList<Token>[] input;
	int index = 0;
	/**
	 * Constructor for the Parser class
	 * @param input
	 */
	public Parser(LinkedList<Token>[] input) {
		this.input = input;
	}
	/**
	 * Parses each linkedlist of input into binary instructions
	 * @return a String array of binary instructions
	 */
	public String[] parse() throws Exception{
		String instructionType = "";
		String instruction = "";
		String[] values = new String[3];
		String[] fromFunction;
		String[] output = new String[input.length];
		for(index = 0; index < input.length; index++) {
			if(input[index].getFirst().t.name().equals("halt")) {
				output[index] = "00000000000000000000000000000000";
				continue;
			}
			else if(input[index].getFirst().t.name().equals("Return")) {
				output[index] = "00000000000000000000000000010000";
				continue;
			}
			Token nextInstruction = input[index].removeFirst();			
			//handles math instruction types here
			if(nextInstruction.t.name().equals("math")) {
				instructionType = "000";
				fromFunction = getMathInstruction();
				
			}
			else if(nextInstruction.t.name().equals("push")) {
				instructionType = "011";
				fromFunction = getMathInstruction();
			}
			//handles boolean instruction types here
			else if(nextInstruction.t.name().equals("branch")) {
				instructionType = "001";
				fromFunction = getBoolInstruction();
			}
			else if(nextInstruction.t.name().equals("callIf")) {
				instructionType = "010";
				fromFunction = getBoolInstruction();
			}
			//these are all other instruction types (not math and not boolean)
			else {
				if(nextInstruction.t.name().equals("jump")) instructionType = "001";
				else if(nextInstruction.t.name().equals("call")) instructionType = "010";
				else if(nextInstruction.t.name().equals("load")) instructionType = "100";
				else if(nextInstruction.t.name().equals("store")) instructionType = "101";
				else if(nextInstruction.t.name().equals("pop") || nextInstruction.t.name().equals("peek")) instructionType = "110";
				else throw new Exception("There is an invalid instruction on line: " + (index + 1));
				fromFunction = getNoInstruction();
			}
			//put everything that's been parsed in it's correct place to complete each binary instruction
			instruction = fromFunction[0];
			values[0] = fromFunction[1];
			values[1] = fromFunction[2];
			values[2] = fromFunction[3];
			output[index] = values[0] + instruction + values[1] + instructionType + values[2];
		}
		return output;
	}
	/**
	 * Since the instruction type being parsed contains no instruction, this function parses the rest of the current line
	 * and the registers it uses
	 * @param i
	 * @return String[] of values
	 */
	public String[] getNoInstruction() throws Exception{
		String instruction;
		String[] values = getValues();
		if(values[2].equals("00")) instruction = "";
		else instruction = "0110";
		String[] toReturn = {instruction, values[0], values[1], values[2]};
		return toReturn;
	}
	/**
	 * Since the instruction type being parsed contains a math instruction, this function parses what that instruction is
	 * and the registers it uses
	 * @param i
	 * @return String[] of values
	 */
	public String[] getMathInstruction() throws Exception {
		Token nextInstruction = input[index].removeFirst();
		String instruction;
		String[] values;
		if(nextInstruction.t.name().equals("copy")) instruction = "1110";
		else if(nextInstruction.t.name().equals("and")) instruction = "1000";
		else if(nextInstruction.t.name().equals("or")) instruction = "1001";
		else if(nextInstruction.t.name().equals("xor")) instruction = "1010";
		else if(nextInstruction.t.name().equals("not")) instruction = "1011";
		else if(nextInstruction.t.name().equals("left")) {
			if(!input[index].removeFirst().t.name().equals("shift"))
				throw new Exception("There is a left shift function which is not valid on line: " + (index + 1));
			instruction = "1100";
		}
		else if(nextInstruction.t.name().equals("right")) {
			if(!input[index].removeFirst().t.name().equals("shift"))
				throw new Exception("There is a right shift function which is not valid on line: " + (index + 1));
			instruction = "1101";
		}
		else if(nextInstruction.t.name().equals("shift")) {
			if((nextInstruction = input[index].removeFirst()).t.name().equals("left")) instruction = "1100";
			else if(nextInstruction.t.name().equals("right")) instruction = "1101";
			else throw new Exception("There is a shift function which is not valid on line: " + (index + 1));
		}
		else if(nextInstruction.t.name().equals("add")) instruction = "1110";
		else if(nextInstruction.t.name().equals("subtract")) instruction = "1111";
		else if(nextInstruction.t.name().equals("multiply")) instruction = "0111";
		else throw new Exception("There is a math function which is not valid on line: " + (index + 1));
		values = getValues();
		String[] toReturn = {instruction, values[0], values[1], values[2]};
		return toReturn;
	}
	/**
	 * Since the instruction type being parsed contains a boolean instruction, this function parses what that instruction is
	 * and the registers it uses
	 * @param i
	 * @return String[] of values
	 */
	public String[] getBoolInstruction() throws Exception {
		String instruction;
		String[] values;
		Token nextInstruction = input[index].removeFirst();
		if(nextInstruction.t.name().equals("equals")) instruction = "0000";
		else if(nextInstruction.t.name().equals("notEquals")) instruction = "0001";
		else if(nextInstruction.t.name().equals("lessThan")) instruction = "0010";
		else if(nextInstruction.t.name().equals("greaterOrEqual")) instruction = "0011";
		else if(nextInstruction.t.name().equals("greaterThan")) instruction = "0100";
		else if(nextInstruction.t.name().equals("lessOrEqual")) instruction = "0101";
		else throw new Exception("There is a boolean function which is not valid on line: " + (index + 1) + "   " + nextInstruction.t.name());
		values = getValues();
		String[] toReturn = {instruction, values[0], values[1], values[2]};
		return toReturn;
	}
	/**
	 * Gets the values for a math instruction in the registers, immediate value, and opcode
	 * @param values
	 * @param destination
	 * @return a string array indicating what to do with the registers and immediate value
	 */
	public String[] getValues() throws Exception{
		var values = input[index];
		String[] toReturn = new String[3];
		String immediate = null;
		toReturn[0] = "";
		Token token;
		int count = 0;
		//while there's more things in the assembly instruction continue to delete the head from this linkedlist
		while(values.size() > 0) {
			token = values.removeFirst();
			if(token.t.name().equals("register") && values.size() == 0) {
				toReturn[1] = new Word(token.register).toString().substring(27);
				toReturn[1] = toReturn[1].replace('f', '0');
				toReturn[1] = toReturn[1].replace('t', '1');
				count++;
			}
			else if(token.t.name().equals("register")) {
				toReturn[0] += new Word(token.register).toString().substring(27);
				toReturn[0] = toReturn[0].replace('f', '0');
				toReturn[0] = toReturn[0].replace('t', '1');
				count++;
			}
			else if(token.t.name().equals("immediate")) {
				immediate = new Word(token.immediate).toString();
				immediate = immediate.replace('f', '0');
				immediate = immediate.replace('t', '1');
			}
			else throw new Exception("Not a complete instruction on line: " + (index + 1));
		}
		//figures out what to do according to what type of function this instruction is, the number of
		//registers in the instruction, and the immediate value
		//needs to handle all number of registers and whether immediate is null or not here
		if(count == 1 && immediate != null) {
			toReturn[0] = immediate.substring(14);
		}
		else if(count == 1 && immediate == null) {
			immediate = new Word().toString();
			immediate = immediate.replace('f', '0');
			toReturn[0] = immediate.substring(14);
		}
		if(count == 2 && immediate != null)
			toReturn[0] = immediate.substring(19) + toReturn[0];
		else if(count == 2 && immediate == null) {
			toReturn[0] = "00000000" + toReturn[0] + toReturn[1];
			count = 3;
		}
		else if(count == 3 && immediate == null) {
			immediate = new Word().toString();
			immediate = immediate.replace('f', '0');
			toReturn[0] = immediate.substring(24) + toReturn[0];
		}
		else if(count == 3 && immediate != null) {
			toReturn[0] = immediate.substring(24) + toReturn[0];
		}
		else if(count == 0) {
			toReturn[1] = "";
			toReturn[0] = immediate.substring(5);
		}
		//set opcode
		if(count == 0) toReturn[2] = "00";
		if(count == 1) toReturn[2] = "01";
		if(count == 2) toReturn[2] = "11";
		if(count == 3) toReturn[2] = "10";
		//if there was no destination index, fill in that portion of the binary instruction here
		if(toReturn[1] == null) {
			int removeIndex = toReturn[0].length() - 5;
			toReturn[1] = toReturn[0].substring(removeIndex);
			toReturn[0] = toReturn[0].substring(0, removeIndex);
		}
		return toReturn;
	}
	/**
	 * Creates a String to represent this class
	 * @return String
	 */
	public String toString() {
		String[] output = null;
		try {
			output = parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Arrays.toString(output);
	}	
}
