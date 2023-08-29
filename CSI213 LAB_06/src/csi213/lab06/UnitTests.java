package csi213.lab06;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

public class UnitTests {

	static float[] a = { 5.5f, 3.3f, 1.1f, 2.2f, 4.4f };

	static float[] b = { 7.7f, 6.6f, 5.5f, 1.1f, 2.2f, 3.3f, 4.4f };

	static float[] c = { 1.1f, 2.2f, 3.3f, 4.4f, 5.5f };

	static float[] d = { 1.1f, 3.3f, 5.5f, 7.7f, 9.9f, 11.11f, 13.3f };

	/**
	 * Tests the Task 2 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test2() throws Exception {
		assertEquals(0, FloatArrays.sequentialSearch(c, 1.1f));
		assertEquals(-1, FloatArrays.sequentialSearch(c, 7.7f));
		assertEquals(-1, FloatArrays.sequentialSearch(d, 2.2f));
		assertEquals(3,  FloatArrays.sequentialSearch(d, 7.7f));
	}

	/**
	 * Tests the Task 3 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test3() throws Exception {
		assertNotEquals(-1, FloatArrays.binarySearchRecursive(c, 0, c.length - 1, 1.1f));
		assertEquals(-1, FloatArrays.binarySearchRecursive(c, 0, c.length - 1, 7.7f));
		assertEquals(-1, FloatArrays.binarySearchRecursive(d, 0, d.length - 1, 2.2f));
		assertNotEquals(-1, FloatArrays.binarySearchRecursive(d, 0, d.length - 1, 3.3f));
	}

	/**
	 * Tests the Task 4 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test4() throws Exception {
		assertNotEquals(-1, FloatArrays.binarySearchIterative(c, 1.1f));
		assertEquals(-1, FloatArrays.binarySearchIterative(c, 7.7f));
		assertEquals(-1, FloatArrays.binarySearchIterative(d, 2.2f));
		assertNotEquals(-1, FloatArrays.binarySearchIterative(d, 3.3f));
	}

}
