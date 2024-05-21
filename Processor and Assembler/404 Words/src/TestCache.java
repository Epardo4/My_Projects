/*
 * Created by Eli Pardo
 */
import static org.junit.Assert.*;
import org.junit.Test;

public class TestCache {
	public Processor makeProcessor(String code, boolean testWithCache) throws Exception{
		MainMemory.clear();
		Processor processor = new Processor();
		processor.testWithCache = testWithCache;
		Lexer lexer = new Lexer(code);
		Parser parser = new Parser(lexer.lex());
		MainMemory.load(parser.parse());
		return processor;
	}
	@Test
	public void testAddingArrayForwards() throws Exception{
		MainMemory.clear();
		String code = "MATH DESTONLY 300 R1\nMATH DESTONLY 1 R2\nMATH DESTONLY 20 R3\nBRANCH GREATERTHAN R2 R3 4\nSTORE R0 R2 R1\nMATH ADD R1 1 R1\n"
				+ "MATH ADD R2 1 R2\nJUMP 3\nMATH DESTONLY 1 R2\nMATH DESTONLY 300 R1\nBRANCH GREATERTHAN R2 R3 5\nLOAD R0 R1 R4"
				+ "\nMATH ADD R4 R5\nMATH ADD R1 1 R1\nMATH ADD R2 1 R2\nJUMP 10\nHALT";
		Processor p = makeProcessor(code, true);
		System.out.print("Test Array Forwards with Cache: ");
		p.run();
		assertEquals(210, p.registers[5].getSigned());
		p = makeProcessor(code, false);
		System.out.print("Test Array Forwards with MainMemory: ");
		p.run();
		assertEquals(210, p.registers[5].getSigned());
	}
	@Test
	public void testAddingArrayBackwards() throws Exception{
		String code = "MATH DESTONLY 300 R1\nMATH DESTONLY 1 R2\nMATH DESTONLY 20 R3\nBRANCH GREATERTHAN R2 R3 4\nSTORE R0 R2 R1\nMATH ADD R1 1 R1\n"
				+ "MATH ADD R2 1 R2\nJUMP 3\nMATH DESTONLY 1 R2\nMATH DESTONLY 319 R1\nBRANCH GREATERTHAN R2 R3 5\nLOAD R0 R1 R4"
				+ "\nMATH ADD R4 R5\nMATH SUBTRACT R1 1 R1\nMATH ADD R2 1 R2\nJUMP 10\nHALT";
		Processor p = makeProcessor(code, true);
		System.out.print("Test Array Backwards with Cache: ");
		p.run();
		assertEquals(210, p.registers[5].getSigned());
		p = makeProcessor(code, false);
		System.out.print("Test Array Backwards with MainMemory: ");
		p.run();
		assertEquals(210, p.registers[5].getSigned());
	}
	@Test
	public void testAddingLinkedList() throws Exception{
		String code = "MATH DESTONLY 300 R1\nMATH DESTONLY 1 R2\nMATH DESTONLY 1 R4\nMATH DESTONLY 20 R3\nBRANCH GREATERTHAN R2 R3 6\n"
				+ "STORE R0 R2 R1\nMATH ADD R1 R3 R5\nSTORE R4 R5 R1\nMATH ADD R0 R5 R1\nMATH ADD R2 1 R2\nJUMP 4\nMATH DESTONLY 300 R1\n"
				+ "MATH DESTONLY 1 R2\nBRANCH GREATERTHAN R2 R3 6\nLOAD R0 R1 R5\nMATH ADD R5 R6\nLOAD R4 R1 R5\nMATH ADD R0 R5 R1\n"
				+ "MATH ADD R2 1 R2\nJUMP 13\nHALT";
		Processor p = makeProcessor(code, true);
		System.out.print("Test Linked List with Cache: ");
		p.run();
		assertEquals(210, p.registers[6].getSigned());
		p = makeProcessor(code, false);
		System.out.print("Test Linked List with MainMemory: ");
		p.run();
		assertEquals(210, p.registers[6].getSigned());
	}
}
