/*
 * Created by Eli Pardo
 */
import static org.junit.Assert.*;
import org.junit.Test;

public class UnitTest {
	@Test
	public void testBits() {
		Bit example = new Bit(true);
		Bit other = new Bit(false);
		assertEquals("t", example.toString());
		assertEquals("f", other.toString());
		assertEquals("f", example.and(other).toString());
		assertEquals("t", example.or(other).toString());
		assertEquals("t", example.xor(other).toString());
		other.toggle();
		assertEquals("t", other.toString());
		other.clear();
		assertEquals("f", other.toString());
	}
	@Test
	public void testWords() {
		Word example = new Word(2157);
		assertEquals("fffffffffffffffffffftffffttfttft", example.toString());
		assertEquals(2157, example.getSigned());
		Word other = new Word(0);
		other.copy(example);
		assertEquals(2157, other.getSigned());
		example.set(123);
		assertEquals(123, example.getUnsigned());
		assertEquals("fffffffffffffffffffffffffttftfft", example.and(other).toString());
		assertEquals("fffffffffffffffffffftffffttttttt", example.or(other).toString());
		assertEquals("fffffffffffffffffffftfffffftfttf", example.xor(other).toString());
		assertEquals(984, example.leftShift(3).getUnsigned());
		assertEquals("ttttttttttttttttttttfttttfftfftf", other.not().toString());
		assertEquals("fffffffffffffffffffffftffffttftt", other.rightShift(2).toString());
		assertEquals("f", example.getBit(10).toString());
		example.setBit(10, new Bit(true));
		assertEquals("t", example.getBit(10).toString());
		example.set(-123);
		assertEquals("tttttttttttttttttttttttttfffftft",example.toString());
		other.set(-2157);
		assertEquals(-2157, other.getSigned());
	}
	Bit[] and = {new Bit(true), new Bit(false), new Bit(false), new Bit(false)};
	Bit[] or = {new Bit(true), new Bit(false), new Bit(false), new Bit(true)};
	Bit[] xor = {new Bit(true), new Bit(false), new Bit(true), new Bit(false)};
	Bit[] not = {new Bit(true), new Bit(false), new Bit(true), new Bit(true)};
	Bit[] leftShift = {new Bit(true), new Bit(true), new Bit(false), new Bit(false)};
	Bit[] rightShift = {new Bit(true), new Bit(true), new Bit(false), new Bit(true)};
	Bit[] add = {new Bit(true), new Bit(true), new Bit(true), new Bit(false)};
	Bit[] subtract = {new Bit(true), new Bit(true), new Bit(true), new Bit(true)};
	Bit[] multiply = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
	ALU alu = new ALU(new Word(25), new Word(15));
	@Test
	public void testLogic() {
		alu.doOperation(and);
		assertEquals(9, alu.result.getSigned());
		alu.doOperation(or);
		assertEquals(31, alu.result.getSigned());
		alu.doOperation(xor);
		assertEquals(22, alu.result.getSigned());
		alu.doOperation(not);
		assertEquals(-26, alu.result.getSigned());
		alu.op2 = new Word(3);
		alu.doOperation(leftShift);
		assertEquals(200, alu.result.getSigned());
		alu.doOperation(rightShift);
		assertEquals(3, alu.result.getSigned());
	}
	@Test
	public void testAdd() {
		alu.op2 = new Word(15);
		alu.doOperation(add);
		assertEquals(40, alu.result.getSigned());
		alu.op2 = new Word();
		alu.doOperation(add);
		assertEquals(25, alu.result.getSigned());
	}
	@Test
	public void testSubtract() {
		alu.op2 = alu.op1;
		alu.op1 = new Word(10);
		alu.doOperation(subtract);
		assertEquals(-15, alu.result.getSigned());
		alu.op2 = new Word(-15);
		alu.doOperation(subtract);
		assertEquals(25, alu.result.getSigned());
		alu.op1 = new Word(-5);
		alu.op2 = new Word(15);
		alu.doOperation(subtract);
		assertEquals(-20, alu.result.getSigned());
		alu.op2 = new Word(5);
		alu.op1 = new Word(10);
		alu.doOperation(subtract);
		assertEquals(5, alu.result.getSigned());
	}
	@Test
	public void testMultiply() {
		alu.op2 = new Word();
		alu.doOperation(multiply);
		assertEquals(0, alu.result.getSigned());
		alu.op1 = new Word(3);
		alu.op2 = new Word(4);
		alu.doOperation(multiply);
		assertEquals(12, alu.result.getSigned());
		alu.op1 = new Word(-10);
		alu.op2 = new Word(-10);
		alu.doOperation(multiply);
		assertEquals(100, alu.result.getSigned());
		alu.op2 = new Word(10);
		alu.doOperation(multiply);
		assertEquals(-100, alu.result.getSigned());
		alu.op1 = new Word(10);
		alu.doOperation(multiply);
		assertEquals(100, alu.result.getSigned());
		alu.op1 = new Word(150);
		alu.op2 = new Word(-123);
		alu.doOperation(multiply);
		assertEquals(-18450, alu.result.getSigned());
	}
	@Test
	public void testIncrement() {
		Word test = new Word(23);
		assertEquals(24, test.increment().getSigned());
		test = new Word(0);
		assertEquals(1, test.increment().getSigned());
		test = new Word(-1);
		assertEquals(0, test.increment().getSigned());
		test = new Word(-567);
		assertEquals(-566, test.increment().getSigned());
		test = new Word(1);
		assertEquals(2, test.increment().getSigned());
	}
	@Test
	public void testReadAndWrite() {
		for(int i = 0; i < 1024; i++) {
			MainMemory.write(new Word(i), new Word());
		}
		assertEquals(0, MainMemory.read(new Word()).getSigned());
		Word address = new Word(12);
		Word value = new Word(123);
		MainMemory.write(new Word(), value);
		assertEquals(123, MainMemory.read(new Word()).getSigned());
		value = new Word(24);
		MainMemory.write(address, value);
		assertEquals(24, MainMemory.read(address).getSigned());
		value = new Word(-24);
		address = new Word(1023);
		MainMemory.write(address, value);
		assertEquals(-24, MainMemory.read(address).getSigned());
		for(int i = 0; i < 1024; i++) {
			MainMemory.write(new Word(i), new Word());
		}
	}
	@Test
	public void testLoad() {
		String[] testData = new String[1024];
		String toAdd = "";
		int toLoad;
		for(int i = 0; i < 1024; i++) {
			toLoad = i * 3;
			if(i % 4 == 0) toLoad *= -1;
			toAdd = new Word(toLoad).toString();
			toAdd = toAdd.replaceAll("f", "0");
			toAdd = toAdd.replaceAll("t", "1");
			testData[i] = toAdd;
		}
		MainMemory.load(testData);
		for(int i = 0; i < 1024; i++) {
			toLoad = i * 3;
			if(i % 4 == 0) toLoad *= -1;
			assertEquals(toLoad, MainMemory.read(new Word(i)).getSigned());
		}
		for(int i = 0; i < 1024; i++) {
			MainMemory.write(new Word(i), new Word());
		}
		for(int i = 0; i < 10; i++) {
			MainMemory.write(new Word(i), new Word(i));
		}
		for(int i = 0; i < 10; i++) {
			assertEquals(i, MainMemory.read(new Word(i)).getSigned());
		}
		for(int i = 0; i < 1024; i++) {
			MainMemory.write(new Word(i), new Word());
		}
	}
	@Test
	public void testLexerAndToken() throws Exception{
		Lexer lexer;
		String allTokens = "10 20 30 R1 R2 R3 DESTONLY MATH AND OR XOR NOT LEFT RIGHT SHIFT ADD SUBTRACT MULT HALT "
				+ "BRANCH CALL PUSH LOAD STORE POP EQUALS NOTEQUALS LESSTHAN GREATEROREQUAL GREATERTHAN "
				+ "LESSOREQUAL RETURN CALLIF JUMP PEEK";
		lexer = new Lexer(allTokens);
		assertEquals(lexer.toString(lexer.lex()), "(immediate, 10) (immediate, 20) (immediate, 30) (register, 1) (register, 2) (register, 3) "
				+ "(copy) (math) (and) (or) (xor) (not) (left) (right) (shift) (add) (subtract) (multiply) (halt) "
				+ "(branch) (call) (push) (load) (store) (pop) (equals) (notEquals) (lessThan) (greaterOrEqual) (greaterThan) (lessOrEqual) (Return) "
				+ "(callIf) (jump) (peek)\n");
		lexer = new Lexer("MATH DESTONLY 5 R1");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 5) (register, 1)\n");
		lexer = new Lexer("MATH MULT R2 R2 R3");
		assertEquals(lexer.toString(lexer.lex()), "(math) (multiply) (register, 2) (register, 2) (register, 3)\n");
		lexer = new Lexer("MATH NOT 89 R2");
		assertEquals(lexer.toString(lexer.lex()), "(math) (not) (immediate, 89) (register, 2)\n");
		lexer = new Lexer("MATH SUBTRACT 21 R1 R2");
		assertEquals(lexer.toString(lexer.lex()), "(math) (subtract) (immediate, 21) (register, 1) (register, 2)\n");
		lexer = new Lexer("MATH SHIFT LEFT R2 R1 R2");
		assertEquals(lexer.toString(lexer.lex()), "(math) (shift) (left) (register, 2) (register, 1) (register, 2)\n");
		lexer = new Lexer("MATH RIGHT SHIFT R2 R20 R31");
		assertEquals(lexer.toString(lexer.lex()), "(math) (right) (shift) (register, 2) (register, 20) (register, 31)\n");
		lexer = new Lexer("MATH XOR R2 R3 R4");
		assertEquals(lexer.toString(lexer.lex()), "(math) (xor) (register, 2) (register, 3) (register, 4)\n");
		lexer = new Lexer("MATH AND 22 R17");
		assertEquals(lexer.toString(lexer.lex()), "(math) (and) (immediate, 22) (register, 17)\n");
		lexer = new Lexer("MATH OR 12 R3 R4");
		assertEquals(lexer.toString(lexer.lex()), "(math) (or) (immediate, 12) (register, 3) (register, 4)\n");
		lexer = new Lexer("MATH DESTONLY 5 R1\nMATH ADD R1 R1 R2\nMATH ADD R2 R2\nMATH ADD R2 R1 R3\nHALT");
		String check = "(math) (copy) (immediate, 5) (register, 1)\n"
				+ "(math) (add) (register, 1) (register, 1) (register, 2)\n"
				+ "(math) (add) (register, 2) (register, 2)\n"
				+ "(math) (add) (register, 2) (register, 1) (register, 3)\n"
				+ "(halt)\n";
		assertEquals(lexer.toString(lexer.lex()), check);
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 1 R2\nMATH DESTONLY 1 R3\nBRANCH LESSOREQUAL R1 R3 R0 3\nMATH MULT R1 R2\n" + 
				"MATH SUBTRACT R1 1 R1\nJUMP 2\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 10) (register, 1)\n(math) (copy) (immediate, 1) (register, 2)\n"
				+ "(math) (copy) (immediate, 1) (register, 3)\n(branch) (lessOrEqual) (register, 1) (register, 3) (register, 0) (immediate, 3)"
				+ "\n(math) (multiply) (register, 1) (register, 2)\n(math) (subtract) (register, 1) (immediate, 1) (register, 1)\n(jump)"
				+ " (immediate, 2)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 15 R2\nBRANCH GREATERTHAN R2 R1 R7 1\nMATH DESTONLY 20 R3\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 10) (register, 1)\n(math) (copy) (immediate, 15) "
				+ "(register, 2)\n(branch) (greaterThan) (register, 2) (register, 1) (register, 7) (immediate, 1)\n(math) (copy) ("
				+ "immediate, 20) (register, 3)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 12 R1\nMATH ADD R0 R1 R2\nCALLIF EQUALS R1 R2 R0 4\nHALT\nMATH MULT R1 R2 R3\nRETURN");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 12) (register, 1)\n(math) (add) (register, 0)"
				+ " (register, 1) (register, 2)\n(callIf) (equals) (register, 1) (register, 2) (register, 0) (immediate, 4)\n("
				+ "halt)\n(math) (multiply) (register, 1) (register, 2) (register, 3)\n(Return)\n");
		lexer = new Lexer("MATH DESTONLY 1 R1\nCALLIF NOTEQUALS R0 R1 R7 4\nMATH ADD R1 R2 R3\nHALT\nMATH DESTONLY 25 R2\nRETURN");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 1) (register, 1)\n(callIf) (notEquals) (register,"
				+ " 0) (register, 1) (register, 7) (immediate, 4)\n(math) (add) (register, 1) (register, 2) (register, 3)\n(hal"
				+ "t)\n(math) (copy) (immediate, 25) (register, 2)\n(Return)\n");
		lexer = new Lexer("MATH DESTONLY 2 R2\nPUSH SUBTRACT R0 R2 R0\nPOP R1\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 2) (register, 2)\n(push) (subtract) ("
				+ "register, 0) (register, 2) (register, 0)\n(pop) (register, 1)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 2 R2\nPUSH MULT R1 R2 R7\nPOP R3\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 10) (register, 1)\n(math) (copy) "
				+ "(immediate, 2) (register, 2)\n(push) (multiply) (register, 1) (register, 2) (register, 7)\n(pop) (register, 3)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 1010 R1\nMATH DESTONLY 20 R3\nSTORE R1 R3 R0\nLOAD R1 R0 R2\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 1010) (register, 1)\n(math) (copy) (im"
				+ "mediate, 20) (register, 3)\n(store) (register, 1) (register, 3) (register, 0)\n(load) (register, 1) "
				+ "(register, 0) (register, 2)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 560 R1\nMATH DESTONLY 25 R2\nMATH DESTONLY 40 R4\nSTORE R4 R2 R1\nMATH DESTONLY 575 R5\nLOAD R5 R2 R3\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 560) (register, 1)\n(math) (copy) "
				+ "(immediate, 25) (register, 2)\n(math) (copy) (immediate, 40) (register, 4)\n(store) (register, 4) "
				+ "(register, 2) (register, 1)\n(math) (copy) (immediate, 575) (register, 5)\n(load) (register, 5) (register, 2) (register, 3)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 1000 R1\nMATH DESTONLY 24 R2\nSTORE R0 R2 R1\nPEEK R2 R0 R3\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 1000) (register, 1)\n(math) (copy) "
				+ "(immediate, 24) (register, 2)\n(store) (register, 0) (register, 2) (register, 1)\n(peek) (register, 2) "
				+ "(register, 0) (register, 3)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 995 R1\nMATH DESTONLY 5 R2\nMATH DESTONLY 19 R3\nSTORE R2 R3 R1\nPEEK R3 R2 R4\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 995) (register, 1)\n(math) (copy) (immediate, 5) "
				+ "(register, 2)\n(math) (copy) (immediate, 19) (register, 3)\n(store) (register, 2) (register, 3) (register, 1)\n(peek) "
				+ "(register, 3) (register, 2) (register, 4)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 12 R0\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 12) (register, 0)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 5 R1\nMATH ADD R1 R1 R2\nMATH ADD R2 R2\nMATH ADD R2 R1 R3\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 5) (register, 1)\n(math) (add) (register, 1) (register, 1) "
				+ "(register, 2)\n(math) (add) (register, 2) (register, 2)\n(math) (add) (register, 2) (register, 1) (register, 3)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 3 R1\nMATH NOT R1 R2\nMATH AND R1 R2 R3\nMATH OR R1 R2 R4\nMATH XOR R4 R0\nMATH XOR R3 R4 R5\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 3) (register, 1)\n(math) (not) (register, 1) (register, 2)\n(math) "
				+ "(and) (register, 1) (register, 2) (register, 3)\n(math) (or) (register, 1) (register, 2) (register, 4)\n(math) (xor) (register, 4) "
				+ "(register, 0)\n(math) (xor) (register, 3) (register, 4) (register, 5)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 1 R1\nMATH DESTONLY 2 R2\nMATH AND R1 R2 R3\nMATH XOR R1 R2 R4\nMATH OR R1 R2 R5\nMATH NOT R4 R6\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 1) (register, 1)\n(math) (copy) (immediate, 2) (register, 2)\n(math) "
				+ "(and) (register, 1) (register, 2) (register, 3)\n(math) (xor) (register, 1) (register, 2) (register, 4)\n(math) (or) (register, 1) "
				+ "(register, 2) (register, 5)\n(math) (not) (register, 4) (register, 6)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 2 R2\nMATH ADD R1 R2 R3\nMATH MULT R1 R2 R4\nMATH SUBTRACT R2 R1 R5\nMATH SUBTRACT "
		        + "R1 R2 R6\nMATH LEFT SHIFT R1 R2 R7\nMATH RIGHT SHIFT R2 R1 R8\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 10) (register, 1)\n(math) (copy) (immediate, 2) (register, 2)\n(math) "
				+ "(add) (register, 1) (register, 2) (register, 3)\n(math) (multiply) (register, 1) (register, 2) (register, 4)\n(math) (subtract) "
				+ "(register, 2) (register, 1) (register, 5)\n(math) (subtract) (register, 1) (register, 2) (register, 6)\n(math) (left) (shift) "
				+ "(register, 1) (register, 2) (register, 7)\n(math) (right) (shift) (register, 2) (register, 1) (register, 8)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 5 R10\nMATH ADD R0 R10 R20\nMATH MULT R10 R20 R30\nMATH MULT R10 R30 R1\nMATH SUBTRACT R1 R30 R17\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 5) (register, 10)\n(math) (add) (register, 0) (register, 10) "
				+ "(register, 20)\n(math) (multiply) (register, 10) (register, 20) (register, 30)\n(math) (multiply) (register, 10) "
				+ "(register, 30) (register, 1)\n(math) (subtract) (register, 1) (register, 30) (register, 17)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 1 R1\nMATH DESTONLY 2 R2\nMATH SHIFT LEFT R2 R1 R3\nMATH SHIFT RIGHT R2 R1 R4\nMATH OR R3 R4 R5\nMATH OR R2 R5 R6\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 1) (register, 1)\n(math) (copy) (immediate, 2) (register, 2)\n(math) "
				+ "(shift) (left) (register, 2) (register, 1) (register, 3)\n(math) (shift) (right) (register, 2) (register, 1) (register, 4)\n(math) "
				+ "(or) (register, 3) (register, 4) (register, 5)\n(math) (or) (register, 2) (register, 5) (register, 6)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 0 R1\nMATH DESTONLY 0 R2\nBRANCH EQUALS R1 R2 1\nBRANCH NOTEQUALS R1 R2\nBRANCH LESSTHAN R1 R2 1\n"
				+ "CALLIF GREATERTHAN R1 R2 1\nCALLIF GREATEROREQUAL R1 R2 1\nCALLIF LESSOREQUAL R1 R2 1\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 0) (register, 1)\n(math) (copy) (immediate, 0) (register, 2)\n(branch) "
				+ "(equals) (register, 1) (register, 2) (immediate, 1)\n(branch) (notEquals) (register, 1) (register, 2)\n(branch) (lessThan) "
				+ "(register, 1) (register, 2) (immediate, 1)\n(callIf) (greaterThan) (register, 1) (register, 2) (immediate, 1)\n(callIf) "
				+ "(greaterOrEqual) (register, 1) (register, 2) (immediate, 1)\n(callIf) (lessOrEqual) (register, 1) (register, 2) "
				+ "(immediate, 1)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 12 R1\nMATH DESTONLY 25 R2\nBRANCH EQUALS R1 R2 1\nBRANCH NOTEQUALS R1 R2\nBRANCH LESSTHAN R1 R2 1\n"
				+ "CALLIF GREATERTHAN R1 R2 1\nCALLIF GREATEROREQUAL R1 R2 1\nCALLIF LESSOREQUAL R1 R2 1\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 12) (register, 1)\n(math) (copy) (immediate, 25) (register, 2)\n(branch) "
				+ "(equals) (register, 1) (register, 2) (immediate, 1)\n(branch) (notEquals) (register, 1) (register, 2)\n(branch) (lessThan) "
				+ "(register, 1) (register, 2) (immediate, 1)\n(callIf) (greaterThan) (register, 1) (register, 2) (immediate, 1)\n(callIf) "
				+ "(greaterOrEqual) (register, 1) (register, 2) (immediate, 1)\n(callIf) (lessOrEqual) (register, 1) (register, 2) "
				+ "(immediate, 1)\n(halt)\n");
		lexer = new Lexer("MATH DESTONLY 25 R1\nMATH DESTONLY 12 R2\nBRANCH EQUALS R1 R2 1\nBRANCH NOTEQUALS R1 R2\nBRANCH LESSTHAN R1 R2 1\n"
				+ "CALLIF GREATERTHAN R1 R2 1\nCALLIF GREATEROREQUAL R1 R2 1\nCALLIF LESSOREQUAL R1 R2 1\nHALT");
		assertEquals(lexer.toString(lexer.lex()), "(math) (copy) (immediate, 25) (register, 1)\n(math) (copy) (immediate, 12) "
				+ "(register, 2)\n(branch) (equals) (register, 1) (register, 2) (immediate, 1)\n(branch) (notEquals) "
				+ "(register, 1) (register, 2)\n(branch) (lessThan) (register, 1) (register, 2) (immediate, 1)\n(callIf) "
				+ "(greaterThan) (register, 1) (register, 2) (immediate, 1)\n(callIf) (greaterOrEqual) (register, 1) (register, 2) "
				+ "(immediate, 1)\n(callIf) (lessOrEqual) (register, 1) (register, 2) (immediate, 1)\n(halt)\n");
	}
	@Test
	public void testParser() throws Exception{
		Lexer lexer = new Lexer("HALT");
		Parser parser = new Parser(lexer.lex());
		String[] check = parser.parse();
		assertEquals(check[0], "00000000000000000000000000000000");
		lexer = new Lexer("MATH DESTONLY 5 R1");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000000010111100000100001");
		lexer = new Lexer("MATH MULT R2 R2 R3");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000100001001110001100010");
		lexer = new Lexer("MATH NOT 89 R2");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000101100110110001000001");
		lexer = new Lexer("MATH SUBTRACT 21 R1 R2");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000101010000111110001000011");
		lexer = new Lexer("MATH LEFT SHIFT R2 R1 R2");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000100000111000001000010");
		lexer = new Lexer("MATH RIGHT SHIFT R2 R20 R31");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000101010011011111100010");
		lexer = new Lexer("MATH XOR R2 R3 R4");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000100001110100010000010");
		lexer = new Lexer("MATH AND 22 R17");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000001011010001000100001");
		lexer = new Lexer("MATH OR 12 R3 R4");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000011000001110010010000011");
		lexer = new Lexer("MATH DESTONLY 5 R1\nMATH ADD R1 R1 R2\nMATH ADD R2 R2\nMATH ADD R2 R1 R3\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000000010111100000100001");
		assertEquals(check[1], "00000000000010000111100001000010");
		assertEquals(check[2], "00000000000100001011100001000010");
		assertEquals(check[3], "00000000000100000111100001100010");
		assertEquals(check[4], "00000000000000000000000000000000");
		lexer = new Lexer("MATH XOR R1 R2\nMATH NOT R2 R1\nMATH MULT 12 R2 R15");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000010001010100001000010");
		assertEquals(check[1], "00000000000100000110110000100010");
		assertEquals(check[2], "00000000011000001001110111100011");
		lexer = new Lexer("MATH SUBTRACT 20 R24 R15\nMATH MULT R15 R2 R30\nMATH AND R1 R30 R15");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000101001100011110111100011");
		assertEquals(check[1], "00000000011110001001111111000010");
		assertEquals(check[2], "00000000000011111010000111100010");
		lexer = new Lexer("MATH ADD R0 R0 R1\nMATH ADD R1 R4\nMATH MULT 20 R4 R17");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000000000011100000100010");
		assertEquals(check[1], "00000000000010010011100010000010");
		assertEquals(check[2], "00000000101000010001111000100011");
		lexer = new Lexer("MATH SHIFT LEFT R1 R2 R3\nMATH SHIFT RIGHT 24 R3 R4");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		assertEquals(check[0], "00000000000010001011000001100010");
		assertEquals(check[1], "00000000110000001111010010000011");
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 1 R2\nMATH DESTONLY 1 R3\nBRANCH LESSOREQUAL R1 R3 R0 3\nMATH MULT R1 R2\n" + 
				"MATH SUBTRACT R1 1 R1\nJUMP 2\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000101011100000100001, 00000000000000000111100001000001, 00000000000000000111100001100001, "
				+ "00000011000010001101010000000110, 00000000000010001001110001000010, 00000000000010000111110000100011, "
				+ "00000000000000000000000001000100, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 15 R2\nBRANCH GREATERTHAN R2 R1 R7 1\nMATH DESTONLY 20 R3\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000101011100000100001, 00000000000000111111100001000001, 00000001000100000101000011100110, "
				+ "00000000000001010011100001100001, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 12 R1\nMATH ADD R0 R1 R2\nCALLIF EQUALS R1 R2 R0 4\nHALT\nMATH MULT R1 R2 R3\nRETURN");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000110011100000100001, 00000000000000000111100001000010, 00000100000010001000000000001010, "
				+ "00000000000000000000000000000000, 00000000000010001001110001100010, 00000000000000000000000000010000]");
		lexer = new Lexer("MATH DESTONLY 1 R1\nCALLIF NOTEQUALS R0 R1 R7 4\nMATH ADD R1 R2 R3\nHALT\nMATH DESTONLY 25 R2\nRETURN");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000000111100000100001, 00000100000000000100010011101010, 00000000000010001011100001100010, "
				+ "00000000000000000000000000000000, 00000000000001100111100001000001, 00000000000000000000000000010000]");
		lexer = new Lexer("MATH DESTONLY 2 R2\nPUSH SUBTRACT R0 R2 R0\nPOP R1\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000001011100001000001, 00000000000000001011110000001110, 00000000000000000001100000111001, "
				+ "00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 2 R2\nPUSH MULT R1 R2 R7\nPOP R3\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000101011100000100001, 00000000000000001011100001000001, 00000000000010001001110011101110, "
				+ "00000000000000000001100001111001, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 1010 R1\nMATH DESTONLY 20 R3\nSTORE R1 R3 R0\nLOAD R1 R0 R2\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000111111001011100000100001, 00000000000001010011100001100001, 00000000000010001101100000010110, "
				+ "00000000000010000001100001010010, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 560 R1\nMATH DESTONLY 25 R2\nMATH DESTONLY 40 R4\nSTORE R4 R2 R1\nMATH DESTONLY 575 R5\nLOAD R5 R2 R3\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000100011000011100000100001, 00000000000001100111100001000001, 00000000000010100011100010000001, "
				+ "00000000001000001001100000110110, 00000000100011111111100010100001, 00000000001010001001100001110010, "
				+ "00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 1000 R1\nMATH DESTONLY 24 R2\nSTORE R0 R2 R1\nPEEK R2 R0 R3\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000111110100011100000100001, 00000000000001100011100001000001, 00000000000000001001100000110110, "
				+ "00000000000100000001100001111010, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 995 R1\nMATH DESTONLY 5 R2\nMATH DESTONLY 19 R3\nSTORE R2 R3 R1\nPEEK R3 R2 R4\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000111110001111100000100001, 00000000000000010111100001000001, 00000000000001001111100001100001, "
				+ "00000000000100001101100000110110, 00000000000110001001100010011010, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 12 R0\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000110011100000000001, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 5 R1\nMATH ADD R1 R1 R2\nMATH ADD R2 R2\nMATH ADD R2 R1 R3\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000010111100000100001, 00000000000010000111100001000010, 00000000000100001011100001000010, "
				+ "00000000000100000111100001100010, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 3 R1\nMATH NOT R1 R2\nMATH AND R1 R2 R3\nMATH OR R1 R2 R4\nMATH XOR R4 R0\nMATH XOR R3 R4 R5\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000001111100000100001, 00000000000010001010110001000010, 00000000000010001010000001100010, "
				+ "00000000000010001010010010000010, 00000000001000000010100000000010, 00000000000110010010100010100010, "
				+ "00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 1 R1\nMATH DESTONLY 2 R2\nMATH AND R1 R2 R3\nMATH XOR R1 R2 R4\nMATH OR R1 R2 R5\nMATH NOT R4 R6\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000000111100000100001, 00000000000000001011100001000001, 00000000000010001010000001100010, "
				+ "00000000000010001010100010000010, 00000000000010001010010010100010, 00000000001000011010110011000010, "
				+ "00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 2 R2\nMATH ADD R1 R2 R3\nMATH MULT R1 R2 R4\nMATH SUBTRACT R2 R1 R5\nMATH SUBTRACT "
		        + "R1 R2 R6\nMATH SHIFT LEFT R1 R2 R7\nMATH RIGHT SHIFT R2 R1 R8\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000101011100000100001, 00000000000000001011100001000001, 00000000000010001011100001100010, "
				+ "00000000000010001001110010000010, 00000000000100000111110010100010, 00000000000010001011110011000010, "
				+ "00000000000010001011000011100010, 00000000000100000111010100000010, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 5 R10\nMATH ADD R0 R10 R20\nMATH MULT R10 R20 R30\nMATH MULT R10 R30 R1\nMATH SUBTRACT R1 R30 R17\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000010111100101000001, 00000000000000101011101010000010, 00000000010101010001111111000010, "
				+ "00000000010101111001110000100010, 00000000000011111011111000100010, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 1 R1\nMATH DESTONLY 2 R2\nMATH LEFT SHIFT R2 R1 R3\nMATH SHIFT RIGHT R2 R1 R4\nMATH OR R3 R4 R5\nMATH OR R2 R5 R6\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000000111100000100001, 00000000000000001011100001000001, 00000000000100000111000001100010, "
				+ "00000000000100000111010010000010, 00000000000110010010010010100010, 00000000000100010110010011000010, "
				+ "00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 0 R1\nMATH DESTONLY 0 R2\nBRANCH EQUALS R1 R2 1\nBRANCH NOTEQUALS R1 R2\nBRANCH LESSTHAN R1 R2 1\n"
				+ "CALLIF GREATERTHAN R1 R2 1\nCALLIF GREATEROREQUAL R1 R2 1\nCALLIF LESSOREQUAL R1 R2 1\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000000011100000100001, 00000000000000000011100001000001, 00000000000010000100000001000111, "
				+ "00000000000010001000010001000110, 00000000000010000100100001000111, 00000000000010000101000001001011, "
				+ "00000000000010000100110001001011, 00000000000010000101010001001011, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 12 R1\nMATH DESTONLY 25 R2\nBRANCH EQUALS R1 R2 1\nBRANCH NOTEQUALS R1 R2\nBRANCH LESSTHAN R1 R2 1\n"
				+ "CALLIF GREATERTHAN R1 R2 1\nCALLIF GREATEROREQUAL R1 R2 1\nCALLIF LESSOREQUAL R1 R2 1\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000000110011100000100001, 00000000000001100111100001000001, 00000000000010000100000001000111, "
				+ "00000000000010001000010001000110, 00000000000010000100100001000111, 00000000000010000101000001001011, "
				+ "00000000000010000100110001001011, 00000000000010000101010001001011, 00000000000000000000000000000000]");
		lexer = new Lexer("MATH DESTONLY 25 R1\nMATH DESTONLY 12 R2\nBRANCH EQUALS R1 R2 1\nBRANCH NOTEQUALS R1 R2\nBRANCH LESSTHAN R1 R2 1\n"
				+ "CALLIF GREATERTHAN R1 R2 1\nCALLIF GREATEROREQUAL R1 R2 1\nCALLIF LESSOREQUAL R1 R2 1\nHALT");
		parser = new Parser(lexer.lex());
		assertEquals(parser.toString(), "[00000000000001100111100000100001, 00000000000000110011100001000001, 00000000000010000100000001000111, "
				+ "00000000000010001000010001000110, 00000000000010000100100001000111, 00000000000010000101000001001011, "
				+ "00000000000010000100110001001011, 00000000000010000101010001001011, 00000000000000000000000000000000]");
	}
	@Test
	public void testDecode() throws Exception{
		Lexer lexer = new Lexer("HALT");
		String toClear = new Word().toString();
		String[] clear = new String[1024];
		for(int i = 0 ; i < 1024; i++)
			clear[i] = toClear;
		Parser parser = new Parser(lexer.lex());
		String[] check = parser.parse();
		MainMemory.load(check);
		Processor process = new Processor();
		process.fetch(); process.decode();
		assertEquals(process.R1, null);
		assertEquals(process.R2, null);
		MainMemory.load(clear);
		process.PC = new Word();
		lexer = new Lexer("MATH DESTONLY 10 R1");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.fetch(); process.decode();
		assertEquals(process.R1.toString(), new Word(10).toString());
		assertEquals(process.R2, null);
		MainMemory.load(clear);
		process.PC = new Word();
		lexer = new Lexer("MATH NOT 12 R1");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.fetch(); process.decode();
		assertEquals(process.R1.toString(), new Word(12).toString());
		assertEquals(process.R2, null);
		MainMemory.load(clear);
		process.PC = new Word();
		lexer = new Lexer("MATH ADD R3 R1");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.fetch(); process.decode();
		assertEquals(process.R1.toString(), new Word().toString());
		assertEquals(process.R2.toString(), new Word().toString());
		MainMemory.load(clear);
		process.PC = new Word();
		lexer = new Lexer("MATH MULT 23 R23 R10");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.fetch(); process.decode();
		assertEquals(process.R1.toString(), new Word().toString());
		assertEquals(process.R2.toString(), new Word(23).toString());
		MainMemory.load(clear);
		process.PC = new Word();
		lexer = new Lexer("MATH ADD R2 R2 R1");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.fetch(); process.decode();
		assertEquals(process.R1.toString(), new Word().toString());
		assertEquals(process.R2.toString(), new Word().toString());
		MainMemory.load(clear);
		process.PC = new Word();
		lexer = new Lexer("MATH SUBTRACT R2 R3 R1");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.fetch(); process.decode();
		assertEquals(process.R1.toString(), new Word().toString());
		assertEquals(process.R2.toString(), new Word().toString());
		MainMemory.load(clear);
		process.PC = new Word();
	}
	@Test
	public void testExecuteAndStore() throws Exception{
		Lexer lexer = new Lexer("HALT");
		String toClear = new Word().toString();
		String[] clear = new String[1024];
		for(int i = 0 ; i < 1024; i++)
			clear[i] = toClear;
		Parser parser = new Parser(lexer.lex());
		String[] check = parser.parse();
		MainMemory.load(check);
		Processor process = new Processor();
		process.fetch(); process.decode(); process.execute();
		assertEquals(process.result, null);
		MainMemory.load(clear);
		process = new Processor();
		lexer = new Lexer("MATH DESTONLY 5 R1\nMATH ADD R1 R1 R2\nMATH ADD R2 R2\nMATH ADD R2 R1 R3\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process.fetch(); process.decode(); process.execute(); process.store();
		assertEquals(process.result.toString(), new Word(5).toString());
		process.fetch(); process.decode(); process.execute(); process.store();
		assertEquals(process.result.toString(), new Word(10).toString());
		process.fetch(); process.decode(); process.execute(); process.store();
		assertEquals(process.result.toString(), new Word(20).toString());
		process.fetch(); process.decode(); process.execute(); process.store();
		assertEquals(process.result.toString(), new Word(25).toString());
		MainMemory.load(clear);
		process = new Processor();
		lexer = new Lexer("MATH DESTONLY 10 R23\nMATH MULT 23 R23 R10");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process.fetch(); process.decode(); process.execute(); process.store();
		assertEquals(process.result.toString(), new Word(10).toString());
		process.fetch(); process.decode(); process.execute(); process.store();
		assertEquals(process.result.toString(), new Word(230).toString());
		MainMemory.load(clear);
		process = new Processor();
		lexer = new Lexer("MATH DESTONLY 12 R0\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process.run();
		assertEquals(process.registers[0].toString(), new Word().toString());
		MainMemory.load(clear);
	}
	public Processor testing(String test) throws Exception{
		Processor process = new Processor();
		Lexer lexer = new Lexer(test);
		Parser parser = new Parser(lexer.lex());
		String[] check = parser.parse();
		MainMemory.load(check);
		return process;
	}
	public void clear() {
		String toClear = new Word().toString();
		String[] clear = new String[1024];
		for(int i = 0 ; i < 1024; i++)
			clear[i] = toClear;
		MainMemory.load(clear);
	}
	@Test
	public void test0Reg() throws Exception{
		Processor p = testing("JUMP 1\nMATH DESTONLY 1 R1\nHALT");
		p.run();
		assertEquals(1, p.registers[1].getSigned());
		clear();
		p = testing("JUMP 2\nMATH DESTONLY 1 R1\nHALT");
		p.run();
		assertEquals(0, p.registers[1].getSigned());
		clear();
		p = testing("CALL 2\nHALT\nMATH DESTONLY 3 R1\nRETURN");
		p.run();
		assertEquals(3, p.registers[1].getSigned());
		clear();
		p = testing("MATH DESTONLY 10 R1\nMATH ADD R0 R1 R2\nCALL 7\nMATH MULT R1 R2\nMATH ADD "
				+ "R3 R2\nMATH ADD R4 R2\nHALT\nMATH DESTONLY 5 R3\nMATH DESTONLY 10 R4\nRETURN");
		p.run();
		assertEquals(10, p.registers[1].getSigned());
		assertEquals(115, p.registers[2].getSigned());
		assertEquals(5, p.registers[3].getSigned());
		assertEquals(10, p.registers[4].getSigned());
		clear();
	}
	@Test
	public void test1Reg() throws Exception{
		Processor p = testing("JUMP 0 R0\nMATH DESTONLY 1 R1\nHALT");
		p.run();
		assertEquals(1, p.registers[1].getSigned());
		clear();
		p = testing("JUMP 1 R0\nMATH DESTONLY 1 R1\nHALT");
		p.run();
		assertEquals(0, p.registers[1].getSigned());
		clear();
		p = testing("CALL 2 R0\nMATH DESTONLY 1 R1\nHALT");
		p.run();
		assertEquals(0, p.registers[1].getSigned());
		clear();
		p = testing("MATH DESTONLY 3 R1\nCALL 0 R1\nMATH DESTONLY 2 R2\nHALT");
		p.run();
		assertEquals(3, p.registers[1].getSigned());
		assertEquals(0, p.registers[2].getSigned());
		clear();
		p = testing("PUSH SUBTRACT 1 R0\nHALT");
		p.run();
		assertEquals(1, p.read(new Word(1023)).getSigned());
		clear();
		p = testing("PUSH SUBTRACT 1 R0\nPOP R1\nHALT");
		p.run();
		assertEquals(1, p.registers[1].getSigned());
		clear();
		p = testing("MATH DESTONLY 5 R1\nMATH DESTONLY 10 R2\nMATH MULT R1 R2\nPUSH ADD 15 R2\nPOP R3\nHALT");
		p.run();
		assertEquals(5, p.registers[1].getSigned());
		assertEquals(50, p.registers[2].getSigned());
		assertEquals(65, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 1000 R1\nSTORE 12 R1\nLOAD 0 R1\nHALT");
		p.run();
		assertEquals(12, p.registers[1].getSigned());
		clear();
		p = testing("MATH DESTONLY 1000 R1\nSTORE 500 R1\nMATH SUBTRACT R1 800 R1\nMATH MULT 4 R1 R1\nLOAD 200 R1\nHALT");
		p.run();
		assertEquals(500, p.registers[1].getSigned());
		clear();
	}
//	@Test
	public void test2Reg() throws Exception{
		Processor p = testing("MATH DESTONLY 10 R1\nMATH DESTONLY 1 R2\nMATH DESTONLY 1 R3\nBRANCH LESSOREQUAL R1 R3 3\nMATH MULT R1 R2\n"
				+ "MATH SUBTRACT R1 1 R1\nJUMP 2\nHALT");
		p.run();
		assertEquals(3628800, p.registers[2].getSigned());
		clear();
		p = testing("MATH DESTONLY 10 R1\nMATH DESTONLY 15 R2\nBRANCH GREATERTHAN R2 R1 1\nMATH DESTONLY 20 R3\nHALT");
		p.run();
		assertEquals(0, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 12 R1\nMATH ADD R0 R1 R2\nCALLIF EQUALS R1 R2 1\nHALT\nMATH MULT R1 R2 R3\nRETURN\n");
		p.run();
		assertEquals(12, p.registers[1].getSigned());
		assertEquals(12, p.registers[2].getSigned());
		assertEquals(144, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 1 R1\nCALLIF NOTEQUALS R0 R1 2\nMATH ADD R1 R2 R3\nHALT\nMATH DESTONLY 25 R2\nRETURN");
		p.run();
		assertEquals(1, p.registers[1].getSigned());
		assertEquals(25, p.registers[2].getSigned());
		assertEquals(26, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 2 R2\nPUSH SUBTRACT R0 R2\nPOP R1\nHALT");
		p.run();
		assertEquals(-2, p.registers[1].getSigned());
		assertEquals(2, p.registers[2].getSigned());
		clear();
		p = testing("MATH DESTONLY 10 R1\nMATH DESTONLY 2 R2\nPUSH MULT R1 R2\nPOP R3\nHALT");
		p.run();
		assertEquals(10, p.registers[1].getSigned());
		assertEquals(2, p.registers[2].getSigned());
		assertEquals(20, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 20 R1\nSTORE R1 1010 R0\nLOAD 1010 R0 R2\nHALT");
		p.run();
		assertEquals(20, p.registers[1].getSigned());
		assertEquals(20, p.registers[2].getSigned());
		clear();
		p = testing("MATH DESTONLY 560 R1\nMATH DESTONLY 25 R2\nSTORE R2 40 R1\nLOAD 575 R2 R3\nHALT");
		p.run();
		assertEquals(560, p.registers[1].getSigned());
		assertEquals(25, p.registers[2].getSigned());
		assertEquals(25, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 1000 R1\nMATH DESTONLY 5 R2\nSTORE 0 R2 R1\nPEEK 24 R0 R3\nHALT");
		p.run();
		assertEquals(1000, p.registers[1].getSigned());
		assertEquals(5, p.registers[2].getSigned());
		assertEquals(5, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 1000 R1\nMATH DESTONLY 5 R2\nSTORE 0 R2 R1\nPEEK 19 R2 R3\nHALT");
		p.run();
		assertEquals(1000, p.registers[1].getSigned());
		assertEquals(5, p.registers[2].getSigned());
		assertEquals(5, p.registers[3].getSigned());
		clear();
	}
//	@Test
	public void test3Reg() throws Exception{
		Processor p = testing("MATH DESTONLY 10 R1\nMATH DESTONLY 1 R2\nMATH DESTONLY 1 R3\nBRANCH LESSOREQUAL R1 R3 R0 3\nMATH MULT R1 R2\n"
				+ "MATH SUBTRACT R1 1 R1\nJUMP 2\nHALT");
		p.run();
		assertEquals(3628800, p.registers[2].getSigned());
		clear();
		p = testing("MATH DESTONLY 10 R1\nMATH DESTONLY 15 R2\nBRANCH GREATERTHAN R2 R1 R7 1\nMATH DESTONLY 20 R3\nHALT");
		p.run();
		assertEquals(0, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 12 R1\nMATH ADD R0 R1 R2\nCALLIF EQUALS R1 R2 R0 4\nHALT\nMATH MULT R1 R2 R3\nRETURN\n");
		p.run();
		assertEquals(12, p.registers[1].getSigned());
		assertEquals(12, p.registers[2].getSigned());
		assertEquals(144, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 1 R1\nCALLIF NOTEQUALS R0 R1 R7 4\nMATH ADD R1 R2 R3\nHALT\nMATH DESTONLY 25 R2\nRETURN");
		p.run();
		assertEquals(1, p.registers[1].getSigned());
		assertEquals(25, p.registers[2].getSigned());
		assertEquals(26, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 2 R2\nPUSH SUBTRACT R0 R2 R0\nPOP R1\nHALT");
		p.run();
		assertEquals(-2, p.registers[1].getSigned());
		assertEquals(2, p.registers[2].getSigned());
		clear();
		p = testing("MATH DESTONLY 10 R1\nMATH DESTONLY 2 R2\nPUSH MULT R1 R2 R7\nPOP R3\nHALT");
		p.run();
		assertEquals(10, p.registers[1].getSigned());
		assertEquals(2, p.registers[2].getSigned());
		assertEquals(20, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 1010 R1\nMATH DESTONLY 20 R3\nSTORE R1 R3 R0\nLOAD R1 R0 R2\nHALT");
		p.run();
		assertEquals(1010, p.registers[1].getSigned());
		assertEquals(20, p.registers[2].getSigned());
		assertEquals(20, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 560 R1\nMATH DESTONLY 25 R2\nMATH DESTONLY 40 R4\nSTORE R4 R2 R1\nMATH DESTONLY 575 R5\nLOAD R5 R2 R3\nHALT");
		p.run();
		assertEquals(560, p.registers[1].getSigned());
		assertEquals(25, p.registers[2].getSigned());
		assertEquals(25, p.registers[3].getSigned());
		assertEquals(40, p.registers[4].getSigned());
		assertEquals(575, p.registers[5].getSigned());
		clear();
		p = testing("MATH DESTONLY 1000 R1\nMATH DESTONLY 24 R2\nSTORE R0 R2 R1\nPEEK R2 R0 R3\nHALT");
		p.run();
		assertEquals(1000, p.registers[1].getSigned());
		assertEquals(24, p.registers[2].getSigned());
		assertEquals(24, p.registers[3].getSigned());
		clear();
		p = testing("MATH DESTONLY 995 R1\nMATH DESTONLY 5 R2\nMATH DESTONLY 19 R3\nSTORE R2 R3 R1\nPEEK R3 R2 R4\nHALT");
		p.run();
		assertEquals(995, p.registers[1].getSigned());
		assertEquals(5, p.registers[2].getSigned());
		assertEquals(19, p.registers[3].getSigned());
		assertEquals(19, p.registers[4].getSigned());
		clear();
	}
	@Test
	public void testBoolean() {
		Word word1 = new Word();
		Word word2 = new Word();
		ALU alu = new ALU(word1, word2);
		Bit[] eq = {new Bit(false), new Bit(false), new Bit(false), new Bit(false)};
		Bit[] ne = {new Bit(false), new Bit(false), new Bit(false), new Bit(true)};
		Bit[] lt = {new Bit(false), new Bit(false), new Bit(true), new Bit(false)};
		Bit[] gt = {new Bit(false), new Bit(true), new Bit(false), new Bit(false)};
		Bit[] le = {new Bit(false), new Bit(true), new Bit(false), new Bit(true)};
		Bit[] ge = {new Bit(false), new Bit(false), new Bit(true), new Bit(true)};
		alu.doOperation(eq);
		assertEquals(true, alu.boolResult.getValue());
		alu.doOperation(ne);
		assertEquals(false, alu.boolResult.getValue());
		alu.doOperation(lt);
		assertEquals(false, alu.boolResult.getValue());
		alu.doOperation(gt);
		assertEquals(false, alu.boolResult.getValue());
		alu.doOperation(ge);
		assertEquals(true, alu.boolResult.getValue());
		alu.doOperation(le);
		assertEquals(true, alu.boolResult.getValue());
		alu.op1 = new Word(12);
		alu.op2 = new Word(25);
		alu.doOperation(eq);
		assertEquals(false, alu.boolResult.getValue());
		alu.doOperation(ne);
		assertEquals(true, alu.boolResult.getValue());
		alu.doOperation(lt);
		assertEquals(true, alu.boolResult.getValue());
		alu.doOperation(gt);
		assertEquals(false, alu.boolResult.getValue());
		alu.doOperation(ge);
		assertEquals(false, alu.boolResult.getValue());
		alu.doOperation(le);
		assertEquals(true, alu.boolResult.getValue());
		alu.op1 = new Word(25);
		alu.op2 = new Word(12);
		alu.doOperation(eq);
		assertEquals(false, alu.boolResult.getValue());
		alu.doOperation(ne);
		assertEquals(true, alu.boolResult.getValue());
		alu.doOperation(lt);
		assertEquals(false, alu.boolResult.getValue());
		alu.doOperation(gt);
		assertEquals(true, alu.boolResult.getValue());
		alu.doOperation(ge);
		assertEquals(true, alu.boolResult.getValue());
		alu.doOperation(le);
		assertEquals(false, alu.boolResult.getValue());
	}
	@Test
	public void testRun() throws Exception{
		Processor process = new Processor();
		Lexer lexer = new Lexer("MATH DESTONLY 12 R0\nHALT");
		String toClear = new Word().toString();
		String[] clear = new String[1024];
		for(int i = 0 ; i < 1024; i++)
			clear[i] = toClear;
		Parser parser = new Parser(lexer.lex());
		String[] check = parser.parse();
		MainMemory.load(check);
		process.run();
		assertEquals(process.registers[0].toString(), new Word().toString());
		MainMemory.load(clear);
		lexer = new Lexer("MATH DESTONLY 5 R1\nMATH ADD R1 R1 R2\nMATH ADD R2 R2\nMATH ADD R2 R1 R3\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.run();
		assertEquals(process.registers[3].toString(), new Word(25).toString());
		assertEquals(process.registers[2].toString(), new Word(20).toString());
		assertEquals(process.registers[1].toString(), new Word(5).toString());
		MainMemory.load(clear);
		lexer = new Lexer("MATH DESTONLY 3 R1\nMATH NOT R1 R2\nMATH AND R1 R2 R3\nMATH OR R1 R2 R4\nMATH XOR R4 R0\nMATH XOR R3 R4 R5\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.run();
		assertEquals(process.registers[0].toString(), new Word(0).toString());
		assertEquals(process.registers[1].toString(), new Word(3).toString());
		assertEquals(process.registers[2].toString(), new Word(3).not().toString());
		assertEquals(process.registers[3].toString(), new Word(0).toString());
		assertEquals(process.registers[4].toString(), new Word(-1).toString());
		assertEquals(process.registers[5].toString(), new Word(-1).toString());
		process = new Processor();
		MainMemory.load(clear);
		lexer = new Lexer("MATH DESTONLY 1 R1\nMATH DESTONLY 2 R2\nMATH AND R1 R2 R3\nMATH XOR R1 R2 R4\nMATH OR R1 R2 R5\nMATH NOT R4 R6\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.run();
		assertEquals(process.registers[1].toString(), new Word(1).toString());
		assertEquals(process.registers[2].toString(), new Word(2).toString());
		assertEquals(process.registers[3].toString(), new Word(0).toString());
		assertEquals(process.registers[4].toString(), new Word(3).toString());
		assertEquals(process.registers[5].toString(), new Word(3).toString());
		assertEquals(process.registers[6].toString(), new Word(3).not().toString());
		process = new Processor();
		MainMemory.load(clear);
		lexer = new Lexer("MATH DESTONLY 10 R1\nMATH DESTONLY 2 R2\nMATH ADD R1 R2 R3\nMATH MULT R1 R2 R4\nMATH SUBTRACT R2 R1 R5\nMATH SUBTRACT "
				+ "R1 R2 R6\nMATH SHIFT LEFT R1 R2 R7\nMATH RIGHT SHIFT R2 R1 R8\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.run();
		assertEquals(process.registers[1].toString(), new Word(10).toString());
		assertEquals(process.registers[2].toString(), new Word(2).toString());
		assertEquals(process.registers[3].toString(), new Word(12).toString());
		assertEquals(process.registers[4].toString(), new Word(20).toString());
		assertEquals(process.registers[5].toString(), new Word(-8).toString());
		assertEquals(process.registers[6].toString(), new Word(8).toString());
		assertEquals(process.registers[7].toString(), new Word(10).leftShift(2).toString());
		assertEquals(process.registers[8].toString(), new Word(2).rightShift(10).toString());
		process = new Processor();
		MainMemory.load(clear);
		lexer = new Lexer("MATH DESTONLY 5 R10\nMATH ADD R0 R10 R20\nMATH MULT R10 R20 R30\nMATH MULT R10 R30 R1\nMATH SUBTRACT R1 R30 R17\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.run();
		assertEquals(process.registers[1].toString(), new Word(125).toString());
		assertEquals(process.registers[10].toString(), new Word(5).toString());
		assertEquals(process.registers[20].toString(), new Word(5).toString());
		assertEquals(process.registers[30].toString(), new Word(25).toString());
		assertEquals(process.registers[17].toString(), new Word(100).toString());
		process = new Processor();
		MainMemory.load(clear);
		lexer = new Lexer("MATH DESTONLY 1 R1\nMATH DESTONLY 2 R2\nMATH LEFT SHIFT R2 R1 R3\nMATH SHIFT RIGHT R2 R1 R4\nMATH OR R3 R4 R5\nMATH OR R2 R5 R6\nHALT");
		parser = new Parser(lexer.lex());
		check = parser.parse();
		MainMemory.load(check);
		process = new Processor();
		process.run();
		assertEquals(process.registers[1].toString(), new Word(1).toString());
		assertEquals(process.registers[2].toString(), new Word(2).toString());
		assertEquals(process.registers[3].toString(), new Word(4).toString());
		assertEquals(process.registers[4].toString(), new Word(1).toString());
		assertEquals(process.registers[5].toString(), new Word(5).toString());
		assertEquals(process.registers[6].toString(), new Word(7).toString());
		MainMemory.load(clear);
	}
	@Test
	public void testProcessorSetupAndFetch() {
		Processor p = new Processor();
		assertEquals(0, p.PC.getSigned());
		assertEquals(1024, p.SP.getSigned());
		p.fetch();
		assertEquals(0, p.currentInstruction.getSigned());
		assertEquals(1, p.PC.getSigned());
		p = new Processor();
		for(int i = 0; i < 10; i++) {
			MainMemory.write(new Word(i), new Word(i));
		}
		Word w;
		for(int i = 0; i < 10; i++) {
			assertEquals(i, p.PC.getSigned());
			p.fetch();
			assertEquals(i, p.currentInstruction.getSigned());
		}
		for(int i = 0; i < 1024; i++) {
			MainMemory.write(new Word(i), new Word());
		}
	}
}	