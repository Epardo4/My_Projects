/*
 * Created by Eli Pardo
 */
public class Processor {
	public Word PC;
	public Word SP;
	public Word currentInstruction;
	public int currentClockCycle = 0;
	public Word[] registers = new Word[32];
	public Bit halted = new Bit(false);
	public Word R1;
	public Word R2;
	public ALU math = new ALU(R1, R2);
	public Word opcode;
	public Word result;
	public int destinationIndex = -1;
	public int numOfReg;
	public Cache cache = new Cache();
	public boolean testWithCache = true;
	/**
	 * Constructor for the processor class and sets PC and SP
	 */
	public Processor() {
		PC = new Word();
		SP = new Word();
		SP.setBit(10, new Bit(true));
		for(int i = 0; i < 32; i++) 
			registers[i] = new Word();
	}
	/*
	 * Decodes currentInstruction based off of the SIA32 documentation (explained in part below)
	 */
	public void decode() {
		R1 = R2 = null;
		Word test = new Word();
		//figure out what to do based off of the first five bits (opcode) of current instruction
		test.setBit(0, new Bit(true));
		test.setBit(1, new Bit(true));
		test.setBit(2, new Bit(true));
		test.setBit(3, new Bit(true));
		test.setBit(4, new Bit(true));
		opcode = currentInstruction.and(test);
		test = new Word();
		//if opcode is ***10
		if(!opcode.getBit(0).getValue() && opcode.getBit(1).getValue()) {
			numOfReg = 3;
			//get the value of the register specified by this index
			R2 = registers[getRegisterIndex(currentInstruction.getBit(14), currentInstruction.getBit(15), 
					currentInstruction.getBit(16), currentInstruction.getBit(17), currentInstruction.getBit(18))];
			//get the value of the register specified by this index
			R1 = registers[getRegisterIndex(currentInstruction.getBit(19), currentInstruction.getBit(20), 
					currentInstruction.getBit(21), currentInstruction.getBit(22), currentInstruction.getBit(23))];
			destinationIndex = getRegisterIndex(currentInstruction.getBit(5), currentInstruction.getBit(6), 
					currentInstruction.getBit(7), currentInstruction.getBit(8), currentInstruction.getBit(9));
		}
		//if opcode is ***01
		else if(opcode.getBit(0).getValue() && !opcode.getBit(1).getValue()) {
			numOfReg = 1;
			//get the value stored in the last 18 bits of currentInstruction
			R1 = currentInstruction.rightShift(14);
			destinationIndex = getRegisterIndex(currentInstruction.getBit(5), currentInstruction.getBit(6), 
					currentInstruction.getBit(7), currentInstruction.getBit(8), currentInstruction.getBit(9));
			R2 = null;
		}
		//if opcode is ***11
		else if(opcode.getBit(0).getValue() && opcode.getBit(1).getValue()) {
			numOfReg = 2;
			//get the value of the register specified by this index
			R1 = registers[getRegisterIndex(currentInstruction.getBit(14), currentInstruction.getBit(15), 
					currentInstruction.getBit(16), currentInstruction.getBit(17), currentInstruction.getBit(18))];
			//get the value stored in the last 13 bits of currentInstruction
			R2 = currentInstruction.rightShift(19);
			destinationIndex = getRegisterIndex(currentInstruction.getBit(5), currentInstruction.getBit(6), 
					currentInstruction.getBit(7), currentInstruction.getBit(8), currentInstruction.getBit(9));
		}
		else numOfReg = 0;
	}
	/**
	 * Gets the index of the array based on the bits parameter which is passed in during decode
	 * @param bits
	 * @return integer index for register
	 */
	public int getRegisterIndex(Bit ... bits) {
		if(bits[4].not().and(bits[3].not()).and(bits[2].not()).and(bits[1].not()).and(bits[0].not()).getValue()) return 0;
		if(bits[4].not().and(bits[3].not()).and(bits[2].not()).and(bits[1].not()).and(bits[0]).getValue()) return 1;
		if(bits[4].not().and(bits[3].not()).and(bits[2].not()).and(bits[1]).and(bits[0].not()).getValue()) return 2;
		if(bits[4].not().and(bits[3].not()).and(bits[2].not()).and(bits[1]).and(bits[0]).getValue()) return 3;
		if(bits[4].not().and(bits[3].not()).and(bits[2]).and(bits[1].not()).and(bits[0].not()).getValue()) return 4;
		if(bits[4].not().and(bits[3].not()).and(bits[2]).and(bits[1].not()).and(bits[0]).getValue()) return 5;
		if(bits[4].not().and(bits[3].not()).and(bits[2]).and(bits[1]).and(bits[0].not()).getValue()) return 6;
		if(bits[4].not().and(bits[3].not()).and(bits[2]).and(bits[1]).and(bits[0]).getValue()) return 7;
		if(bits[4].not().and(bits[3]).and(bits[2].not()).and(bits[1].not()).and(bits[0].not()).getValue()) return 8;
		if(bits[4].not().and(bits[3]).and(bits[2].not()).and(bits[1].not()).and(bits[0]).getValue()) return 9;
		if(bits[4].not().and(bits[3]).and(bits[2].not()).and(bits[1]).and(bits[0].not()).getValue()) return 10;
		if(bits[4].not().and(bits[3]).and(bits[2].not()).and(bits[1]).and(bits[0]).getValue()) return 11;
		if(bits[4].not().and(bits[3]).and(bits[2]).and(bits[1].not()).and(bits[0].not()).getValue()) return 12;
		if(bits[4].not().and(bits[3]).and(bits[2]).and(bits[1].not()).and(bits[0]).getValue()) return 13;
		if(bits[4].not().and(bits[3]).and(bits[2]).and(bits[1]).and(bits[0].not()).getValue()) return 14;
		if(bits[4].not().and(bits[3]).and(bits[2]).and(bits[1]).and(bits[0]).getValue()) return 15;
		if(bits[4].and(bits[3].not()).and(bits[2].not()).and(bits[1].not()).and(bits[0].not()).getValue()) return 16;
		if(bits[4].and(bits[3].not()).and(bits[2].not()).and(bits[1].not()).and(bits[0]).getValue()) return 17;
		if(bits[4].and(bits[3].not()).and(bits[2].not()).and(bits[1]).and(bits[0].not()).getValue()) return 18;
		if(bits[4].and(bits[3].not()).and(bits[2].not()).and(bits[1]).and(bits[0]).getValue()) return 19;
		if(bits[4].and(bits[3].not()).and(bits[2]).and(bits[1].not()).and(bits[0].not()).getValue()) return 20;
		if(bits[4].and(bits[3].not()).and(bits[2]).and(bits[1].not()).and(bits[0]).getValue()) return 21;
		if(bits[4].and(bits[3].not()).and(bits[2]).and(bits[1]).and(bits[0].not()).getValue()) return 22;
		if(bits[4].and(bits[3].not()).and(bits[2]).and(bits[1]).and(bits[0]).getValue()) return 23;
		if(bits[4].and(bits[3]).and(bits[2].not()).and(bits[1].not()).and(bits[0].not()).getValue()) return 24;
		if(bits[4].and(bits[3]).and(bits[2].not()).and(bits[1].not()).and(bits[0]).getValue()) return 25;
		if(bits[4].and(bits[3]).and(bits[2].not()).and(bits[1]).and(bits[0].not()).getValue()) return 26;
		if(bits[4].and(bits[3]).and(bits[2].not()).and(bits[1]).and(bits[0]).getValue()) return 27;
		if(bits[4].and(bits[3]).and(bits[2]).and(bits[1].not()).and(bits[0].not()).getValue()) return 28;
		if(bits[4].and(bits[3]).and(bits[2]).and(bits[1].not()).and(bits[0]).getValue()) return 29;
		if(bits[4].and(bits[3]).and(bits[2]).and(bits[1]).and(bits[0].not()).getValue()) return 30;
		else return 31;
	}
	/**
	 * Executes the function specified by the bits from 10-13 of currentInstruction and saves the value in result
	 */
	public void execute() {
		//if opcode is 000** then currentInstruction needs to execute a math function using its ALU
		if(!opcode.getBit(2).getValue() && !opcode.getBit(3).getValue() && !opcode.getBit(4).getValue()){
			//if opcode is ***00, then the processor knows to halt and handles that here
			if(!opcode.getBit(0).getValue() && !opcode.getBit(1).getValue())
				halted = halted.not();
			else if(opcode.getBit(0).getValue() && !opcode.getBit(1).getValue()) {
				result = new Word(R1);
			}
			else {
				//set op1, op2, and the function to execute, then execute it and store it in result
				math.op1 = new Word(R1);
				math.op2 = new Word(R2);
				Bit[] function = {currentInstruction.getBit(13), currentInstruction.getBit(12), 
						currentInstruction.getBit(11), currentInstruction.getBit(10)};
				doOperation(function);
				result = math.result;
			}
		}
		//if opcode is 001** then currentInstruction is a branch instruction
		else if(opcode.getBit(2).getValue() && !opcode.getBit(3).getValue() && !opcode.getBit(4).getValue()) {
			//for jump instructions
			if(numOfReg == 0) {
				PC = currentInstruction.rightShift(5);
			}
			else if(numOfReg == 1) {
				math.op1 = PC;
				math.op2 = currentInstruction.rightShift(14);
				Bit[] function = {new Bit(true), new Bit(true), new Bit(true), new Bit(false)};
				doOperation(function);
				PC = new Word(math.result);
				result = registers[destinationIndex];
			}
			//for branching instructions, evaluate the boolean operation first, then evaluate the instruction
			else {
				Word immediateValue = null;
				if(numOfReg == 2) {
					math.op1 = new Word(R1);
					math.op2 = new Word(registers[destinationIndex]);
					immediateValue = currentInstruction.rightShift(19);
				}
				else if(numOfReg == 3) {
					math.op1 = new Word(R1);
					math.op2 = new Word(R2);
					immediateValue = currentInstruction.rightShift(24);
				}
				Bit[] function = {currentInstruction.getBit(13), currentInstruction.getBit(12), 
						currentInstruction.getBit(11), currentInstruction.getBit(10)};
				doOperation(function);
				if(math.boolResult.getValue()) {
					math.op1 = new Word(PC);
					math.op2 = immediateValue;
					Bit[] fxn = {new Bit(true), new Bit(true), new Bit(true), new Bit(false)};
					doOperation(fxn);
					PC = new Word(math.result);
				}
				result = registers[destinationIndex];
			}
		}
		//if opcode is 010** then currentInstruction is a call instruction
		else if(!opcode.getBit(2).getValue() && opcode.getBit(3).getValue() && !opcode.getBit(4).getValue()) {
			//for jump instructions
			if(numOfReg == 0 || numOfReg == 1) {
				SP = SP.decrement();
				write(SP, PC);
				if(numOfReg == 0) PC = currentInstruction.rightShift(5);
				else {
					math.op1 = new Word(R1);
					math.op2 = new Word(registers[destinationIndex]);
					Bit[] function = {new Bit(true), new Bit(true), new Bit(true), new Bit(false)};
					doOperation(function);
					PC = new Word(math.result);
					result = registers[destinationIndex];
				}
			}
			//for callIf instructions, evaluate the boolean operation first, then evaluate the instruction
			else {
				Word check1 = new Word(), check2 = new Word(), val1 = new Word(), val2 = new Word();
				if(numOfReg == 2) {
					check1 = R1;
					check2 = registers[destinationIndex];
					val1 = PC;
					val2 = R2;
				}
				else {
					check1 = R1;
					check2 = R2;
					val1 = currentInstruction.rightShift(24);
					val2 = registers[destinationIndex];
				}
				Bit[] function = {currentInstruction.getBit(13), currentInstruction.getBit(12), 
						currentInstruction.getBit(11), currentInstruction.getBit(10)};
				math.op1 = new Word(check1);
				math.op2 = new Word(check2);
				doOperation(function);
				Bit check = math.boolResult;
				if(check.getValue()) {
					SP = SP.decrement();
					write(SP, PC);
					math.op1 = new Word(val1);
					math.op2 = new Word(val2);
					Bit[] fxn = {new Bit(true), new Bit(true), new Bit(true), new Bit(false)};
					doOperation(fxn);
					PC = new Word(math.result);
				}
				result = registers[destinationIndex];
			}
		}
		//if opcode is 011** then currentInstruction is a push instruction
		else if(opcode.getBit(2).getValue() && opcode.getBit(3).getValue() && !opcode.getBit(4).getValue()) {
			SP = SP.decrement();
			if(numOfReg == 1 || numOfReg == 2) {
				math.op1 = new Word(R1);
				math.op2 = new Word(registers[destinationIndex]);
			}
			else if(numOfReg == 3) {
				math.op1 = new Word(R1);
				math.op2 = new Word(R2);
			}
			Bit[] function = {currentInstruction.getBit(13), currentInstruction.getBit(12), 
					currentInstruction.getBit(11), currentInstruction.getBit(10)};
			doOperation(function);
			write(SP, new Word(math.result));
			result = new Word(registers[destinationIndex]);
		}
		//if opcode is 100** then currentInstruction is a load instruction
		else if(!opcode.getBit(2).getValue() && !opcode.getBit(3).getValue() && opcode.getBit(4).getValue()) {
			if(numOfReg == 0) {
				PC = read(SP);
				SP = SP.increment();
				result = new Word(registers[destinationIndex]);
			}
			else {
				if(numOfReg == 1) {
					math.op1 = new Word(R1);
					math.op2 = new Word(registers[destinationIndex]);
				}
				if(numOfReg == 2 || numOfReg == 3) {
					math.op1 = new Word(R1);
					math.op2 = new Word(R2);
				}
				Bit[] function = {new Bit(true), new Bit(true), new Bit(true), new Bit(false)};
				doOperation(function);
				result = read(math.result);
			}
		}
		//if opcode is 101** then currentInstruciton is a store instruction
		else if(opcode.getBit(2).getValue() && !opcode.getBit(3).getValue() && opcode.getBit(4).getValue()) {
			Word value = new Word();
			if(numOfReg == 1) {
				value = new Word(R1);
				result = new Word(registers[destinationIndex]);
			}
			else {
				if(numOfReg == 2) {
					math.op1 = new Word(R2);
					math.op2 = new Word(registers[destinationIndex]);
					value = new Word(R1);
				}
				if(numOfReg == 3) {
					value = new Word(R2);
					math.op1 = new Word(R1);
					math.op2 = new Word(registers[destinationIndex]);
				}
				Bit[] function = {new Bit(true), new Bit(true), new Bit(true), new Bit(false)};
				doOperation(function);
				result = new Word(math.result);
			}
			write(result, value);
			result = registers[destinationIndex];
		}
		//if opcode is 110 then currentInstruction is a pop instruction
		else if(!opcode.getBit(2).getValue() && opcode.getBit(3).getValue() && opcode.getBit(4).getValue()) {
			if(numOfReg == 1) {
				result = read(SP);
				SP = SP.increment();
			}
			else {
				math.op1 = new Word(R1);
				math.op2 = new Word(R2);
				Bit[] add = {new Bit(true), new Bit(true), new Bit(true), new Bit(false)};
				doOperation(add);
				result = new Word(math.result);
				Bit[] subtract = {new Bit(true), new Bit(true), new Bit(true), new Bit(true)};
				math.op1 = new Word(SP);
				math.op2 = new Word(result);
				doOperation(subtract);
				result = read(new Word(math.result));
			}
		}
	}
	/**
	 * Stores the result from execute in the register specified by the bits from 5-9 of currentInstruction
	 */
	public void store() {
		//figure out which register to store result in and then stores it there
		if(destinationIndex > 0)
			registers[destinationIndex] = new Word(result);
	}
	/**
	 * Runs the program from memory
	 * Stops the program if halted is true
	 */
	public void run() {
		halted = new Bit(false);
		while(true) {
			fetch();
			decode();
			execute();
			if(halted.getValue()) {
				System.out.println(currentClockCycle);
				break;
			}
			store();
		}
	}
	/**
	 * Does the desired operation using the ALU and updates currentClockCycle accordingly
	 * @param operation
	 */
	public void doOperation(Bit[] operation) {
		math.doOperation(operation);
		currentClockCycle += math.clockCycles;
	}
	/**
	 * Writes into the cache at the desired index the desired value value and updates currentClockCycle accordingly if testWithCache is true
	 * Otherwise does the same, but with MainMemory
	 * @param index
	 * @param value
	 */
	public void write(Word index, Word value) {
		if(testWithCache) {
			cache.write(index, value);
			currentClockCycle += cache.getClockCycles();
		}
		else {
			MainMemory.write(index, value);
			currentClockCycle += 300;
		}
	}
	/**
	 * Reads from the cache at the desired index and updates currentClockCycle accordingly if testWithCache is true
	 * Otherwise does the same, but with MainMemory
	 * @param index
	 * @param value
	 * @return Word at index
	 */
	public Word read(Word index) {
		Word toReturn;
		if(testWithCache) {
			toReturn = cache.read(index);
			currentClockCycle += cache.getClockCycles();
		}
		else {
			toReturn = MainMemory.read(index);
			currentClockCycle += 300;
		}
		return toReturn;
	}
	/**
	 * Fetches the current instruction from the static MainMemory class at the address PC
	 */
	public void fetch() {
		currentInstruction = read(PC);
		PC = PC.increment();
	}
}