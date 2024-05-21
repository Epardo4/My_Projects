/*
 * Created by Eli Pardo
 */
public class ALU {
	public Word op1;
	public Word op2;
	public Word result;
	public Bit boolResult;
	public int clockCycles = 0;
	/**
	 * Constructor for the ALU class
	 * @param op1
	 * @param op2
	 */
	public ALU(Word op1, Word op2) {
		this.op1 = op1;
		this.op2 = op2;
	}
	/**
	 * Decides what operation to do based on the operation parameter and 
	 * assigns result to the operation given by the operation parameter
	 * @param operation
	 */
	public void doOperation(Bit[] operation) {
		//and if operation = [1000]
		if(operation[0].and(operation[1].not()).and(operation[2].not()).and(operation[3].not()).getValue())
			result = op1.and(op2);
		//or if operation = [1001]
		if(operation[0].and(operation[1].not()).and(operation[2].not()).and(operation[3]).getValue())
			result = op1.or(op2);
		//xor if operation = [1010]
		if(operation[0].and(operation[1].not()).and(operation[2]).and(operation[3].not()).getValue())
			result = op1.xor(op2);
		//not if operation = [1011]
		if(operation[0].and(operation[1].not()).and(operation[2]).and(operation[3]).getValue())
			result = op1.not();
		//left shift if oepration = [1100]
		if(operation[0].and(operation[1]).and(operation[2].not()).and(operation[3].not()).getValue()) {
			//only obtain the first 5 bits of op2 so it does not left shift to many times
			Word toShift = new Word();
			toShift.setBit(0, new Bit(true));
			toShift.setBit(1, new Bit(true));
			toShift.setBit(2, new Bit(true));
			toShift.setBit(3, new Bit(true));
			toShift.setBit(4, new Bit(true));
			result = op1.leftShift(op2.and(toShift).getSigned());
		}
		//right shift if operation = [1101]
		if(operation[0].and(operation[1]).and(operation[2].not()).and(operation[3]).getValue()) {
			//only obtain the first 5 bits of op2 so it does not right shift to many times
			Word toShift = new Word();
			toShift.setBit(0, new Bit(true));
			toShift.setBit(1, new Bit(true));
			toShift.setBit(2, new Bit(true));
			toShift.setBit(3, new Bit(true));
			toShift.setBit(4, new Bit(true));
			result = op1.rightShift(op2.and(toShift).getSigned());
		}
		//add if operation = [1110]
		if(operation[0].and(operation[1]).and(operation[2]).and(operation[3].not()).getValue())
			result = add(op1, op2);
		//subtract if operation = [1111]
		if(operation[0].and(operation[1]).and(operation[2]).and(operation[3]).getValue())
			result = subtract(op1, op2);
		//multiply if operation = [0111]
		if(operation[0].not().and(operation[1]).and(operation[2]).and(operation[3]).getValue()) {
			result = multiply(op1, op2);
			clockCycles = 10;
		}
		else clockCycles = 2;
		//equals if operation = [0000]
		if(operation[0].not().and(operation[1].not()).and(operation[2].not()).and(operation[3].not()).getValue()) {
			boolResult = equals(op1, op2);
		}
		//not equals if operation = [0001]
		if(operation[0].not().and(operation[1].not()).and(operation[2].not()).and(operation[3]).getValue()) {
			boolResult = notEquals(op1, op2);
		}
		//less than if operation = [0010]
		if(operation[0].not().and(operation[1].not()).and(operation[2]).and(operation[3].not()).getValue()) {
			boolResult = lessThan(op1, op2);
		}
		//greater than or equal if operation = [0011]
		if(operation[0].not().and(operation[1].not()).and(operation[2]).and(operation[3]).getValue()) {
			boolResult = greaterThanOrEquals(op1, op2);
		}
		//greater than if operation = [0100]
		if(operation[0].not().and(operation[1]).and(operation[2].not()).and(operation[3].not()).getValue()) {
			boolResult = greaterThan(op1, op2);
		}
		//less than or equal if operation = [0101]
		if(operation[0].not().and(operation[1]).and(operation[2].not()).and(operation[3]).getValue()) {
			boolResult = lessThanOrEquals(op1, op2);
		}
	}
	/**
	 * Checks if operand 1 is equal to operand2
	 * @param operand1
	 * @param operand2
	 * @return Word
	 */
	public Bit equals(Word operand1, Word operand2) {
		Word check = subtract(operand1, operand2);
		for(int i = 0; i < 32; i++)
			if(check.getBit(i).getValue())
				return new Bit(false);
		return new Bit(true);
	}
	/**
	 * Checks if operand 1 is not equal to operand2
	 * @param operand1
	 * @param operand2
	 * @return Word
	 */
	public Bit notEquals(Word operand1, Word operand2) {
		Bit check = equals(operand1, operand2);
		if(check.getValue())
			return new Bit(false);
		return new Bit(true);
	}
	/**
	 * Checks if operand 1 is less than operand2
	 * @param operand1
	 * @param operand2
	 * @return Word
	 */
	public Bit lessThan(Word operand1, Word operand2) {
		Word check = subtract(operand1, operand2);
		if(!check.getBit(31).getValue())
			return new Bit(false);
		return new Bit(true);
	}
	/**
	 * Checks if operand 1 is greater than operand2
	 * @param operand1
	 * @param operand2
	 * @return Word
	 */
	public Bit greaterThan(Word operand1, Word operand2) {
		Bit checkL = lessThan(operand1, operand2);
		Bit checkE = equals(operand1, operand2);
		if(checkL.getValue() || checkE.getValue())
			return new Bit(false);
		return new Bit(true);
	}
	/**
	 * Checks if operand 1 is greater than or equal to operand2
	 * @param operand1
	 * @param operand2
	 * @return Word
	 */
	public Bit greaterThanOrEquals(Word operand1, Word operand2) {
		Bit checkG = greaterThan(operand1, operand2);
		Bit checkE = equals(operand1, operand2);
		if(checkG.or(checkE).not().getValue())
			return new Bit(false);
		return new Bit(true);
	}
	/**
	 * Checks if operand 1 is less than or equal to operand2
	 * @param operand1
	 * @param operand2
	 * @return Word
	 */
	public Bit lessThanOrEquals(Word operand1, Word operand2) {
		Bit checkL = lessThan(operand1, operand2);
		Bit checkE = equals(operand1, operand2);
		if(checkL.or(checkE).not().getValue())
			return new Bit(false);
		return new Bit(true);
	}
	
	/**
	 * Makes operand2 negative and then adds operand1 and operand2 together
	 * @param operand1
	 * @param operand2
	 * @return the value to be assigned to result
	 */
	public Word subtract(Word operand1, Word operand2) {
		//make oeprand2 negative
		operand2 = operand2.not();
		Word add1 = new Word();
		add1.setBit(0, new Bit(true));
		operand2 = add(add1, operand2);
		return add(operand1, operand2);
	}
	
	/**
	 * Used to add operand1 and operand2 together to get the result
	 * @param operand1
	 * @param operand2
	 * @return the value to be assigned to result
	 */
	public Word add(Word operand1, Word operand2) {
		return add2(operand1, operand2);
	}
	/**
	 * Adds 2 words together
	 * @param toAdd
	 * @return the sum of the 2 words
	 */
	private Word add2(Word input1, Word input2) {
		Word toReturn = new Word();
		Bit[] input = new Bit[3];
		//Holds what the carry for the next operation will be
		Bit carry = new Bit(false);
		for(int i = 0; i < 32; i++) {
			//assign the input to be added
			input[0] = input1.getBit(i);
			input[1] = input2.getBit(i);
			input[2] = carry;
			//becomes the sum for toReturn and carry for next iteration
			carry = input[0].and(input[1]).or(input[0].xor(input[1]).and(input[2]));
			toReturn.setBit(i, input[0].xor(input[1]).xor(input[2]));
		}
		return toReturn;
	}
	/**
	 * Adds 4 words together to get a sum to be used in multiply
	 * @param input1
	 * @param input2
	 * @param input3
	 * @param input4
	 * @return the sum of the four inputs
	 */
	private Word add4(Word input1, Word input2, Word input3, Word input4) {
		Word toReturn = new Word();
		Bit[] input = new Bit[6];
		//used to determine what the carries should be based on the output of add 4
		Bit[] output = {new Bit(false), new Bit(false), new Bit(false)}; 
		Bit carry = new Bit(false);
		//goes through each bit in the inputs and adds them together into one bit with the respective carries
		for(int i = 0; i < 32; i++) {
			//assign what to add together and put this in input which will then be added
			input[0] = input1.getBit(i);
			input[1] = input2.getBit(i);
			input[2] = input3.getBit(i);
			input[3] = input4.getBit(i);
			input[4] = new Bit(output[1].getValue());
			input[5] = carry;
			carry = new Bit(output[2].getValue());
			//will become the number used in toReturn
			output[0] = input[0].xor(input[1]).xor(input[2].xor(input[3])).xor(input[4].xor(input[5]));
			//will become the second carry represented by the carry variable
			output[2] = input[0].and(input[1]).and(input[2].and(input[3]).or(input[4].and(input[5]).or(
					input[2].xor(input[3]).and(input[4].xor(input[5]))))).or(
					input[2].and(input[3]).and(input[0].and(input[1]).or(input[4].and(input[5]).or(
							input[0].xor(input[1]).and(input[4].xor(input[5])))))).or(
					input[4].and(input[5]).and(input[2].and(input[3]).or(input[0].and(input[1]).or(
							input[2].xor(input[3]).and(input[0].xor(input[1]))))));
			//will become the first carry
			output[1] = input[0].xor(input[1]).and(input[2].xor(input[3])).and(input[4].xor(input[5])).or(
						input[0].and(input[1]).and(input[2].and(input[3]).and(input[4]).and(input[5]).or(
						input[2].or(input[3]).or(input[4]).or(input[5]).not()).or(
						input[2].xor(input[3]).and(input[4].or(input[5]).not())).or(
						input[4].xor(input[5]).and(input[2].or(input[3]).not())))).or(
						input[2].and(input[3]).and(input[0].or(input[1]).or(input[4]).or(input[5]).not().or(
						input[0].xor(input[1]).and(input[4].or(input[5]).not())).or(
						input[4].xor(input[5]).and(input[0].or(input[1]).not())))).or(
						input[4].and(input[5]).and(input[0].or(input[1]).or(input[2]).or(input[3]).not().or(
						input[0].xor(input[1]).and(input[2].or(input[3]).not())).or(
						input[2].xor(input[3]).and(input[0].or(input[1]).not())))).or(
						input[0].xor(input[1]).and(input[2].xor(input[3])).and(input[4].or(input[5]).not())).or(
						input[0].xor(input[1]).and(input[4].xor(input[5])).and(input[2].or(input[3]).not())).or(
						input[2].xor(input[3]).and(input[4].xor(input[5])).and(input[0].or(input[1]).not()));
			toReturn.setBit(i, new Bit(output[0].getValue()));
		}	
		return toReturn;
	}
	/**
	 * Multiplies operand1 and operand2 together
	 * @param operand1
	 * @param operand2
	 * @return the value to be assigned to result
	 */
	public Word multiply(Word operand1, Word operand2) {
		//Handle negative numbers here
		Word toMultiply1 = new Word(operand1);
		Word toMultiply2 = new Word(operand2);
		//if both numbers are negative, their product should be positive
		if(operand1.getBit(31).and(operand2.getBit(31)).getValue()) {
			toMultiply1 = toMultiply1.not().increment();
			toMultiply2 = toMultiply2.not().increment();
		}
		else if(operand1.getBit(31).getValue()) {
			toMultiply1 = new Word(operand2);
			toMultiply2 = new Word(operand1);
		}
		//This array will contain each of the words from multiplying the two words listed
		Word[] multiplied = new Word[32];
		for(int i = 0; i < 32; i++) {
			//copy operand1 and left shift by this bit's position in operand2 if this bit is true
			//or copy a word that equals 0 if this bit is false
			//put it in the array
			if(toMultiply2.getBit(i).getValue())
				multiplied[i] = toMultiply1.leftShift(i);
			else multiplied[i] = new Word();
		}
		//use add4 to add everything in the multiplied array
		Word[] result = new Word[8];
		for(int i = 0; i < 8; i++) {
			result[i] = add4(multiplied[i*4], multiplied[i*4 + 1], multiplied[i*4 + 2], multiplied[i*4 + 3]);
		}
		//once there are only 8 words to add, call add4 for the first 4 and the last 4 and then add their sums together
		return add(add4(result[0],result[1],result[2],result[3]), add4(result[4],result[5],result[6],result[7]));
	}
}