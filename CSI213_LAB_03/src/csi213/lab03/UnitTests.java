package csi213.lab03;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 * {@code UnitTests} tests the Lab 3 implementations.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class UnitTests {

	/**
	 * A sample {@code int} array.
	 */
	static int[] a = { 0, 1, 2, 3, 4 };

	/**
	 * Tests the Task 1 implementations.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test1() throws Exception {
		int[] b = new int[4];
		IntArrays.fill(b, -1);
		assertEquals("[-1, -1, -1, -1]", Arrays.toString(b));
		IntArrays.fill(b, -2);
		assertEquals("[-2, -2, -2, -2]", Arrays.toString(b));
		b = new int[3];
		IntArrays.fill(b, -3);
		assertEquals("[-3, -3, -3]", Arrays.toString(b));
	}

	/**
	 * Tests the Task 2 implementations.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test2() throws Exception {
		int[] b = IntArrays.copyOf(a);
		assertEquals(Arrays.toString(a), Arrays.toString(b));
		assertNotEquals(a, b);
		b = new int[2];
		IntArrays.fill(b, -1);
		assertEquals(Arrays.toString(b), Arrays.toString(IntArrays.copyOf(b)));
		assertNotEquals(b, IntArrays.copyOf(b));
	}

	/**
	 * Tests the Task 3 implementations.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test3() throws Exception {
		assertEquals("[0, 1, 2, 3, 4]", Arrays.toString(IntArrays.subarray(a, 0, a.length)));
		assertEquals("[1, 2, 3, 4]", Arrays.toString(IntArrays.subarray(a, 1, a.length)));
		assertEquals("[1, 2, 3]", Arrays.toString(IntArrays.subarray(a, 1, a.length - 1)));
		int[] b = new int[2];
		IntArrays.fill(b, -1);
		assertEquals(Arrays.toString(b), Arrays.toString(IntArrays.subarray(b, 0, b.length)));
	}

}
