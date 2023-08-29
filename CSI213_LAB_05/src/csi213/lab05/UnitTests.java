package csi213.lab05;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

/**
 * {@code UnitTests} tests the Lab 5 implementations.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu), Michael Phipps
 * 
 */
public class UnitTests {

	static float[] a = { 5, 3, 1, 2, 4 };

	static float[] b = { 7, 6, 5, 1, 2, 3, 4 };

	/**
	 * Tests the Task 1 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test1() throws Exception {
		float[] a1 = Arrays.copyOf(a,a.length);
		FloatArrays.bubbleSort(a1);
		compare(a,a1);

		float[] b1 = Arrays.copyOf(b,b.length);
		FloatArrays.bubbleSort(b1);
		compare(b,b1);
	}

	/**
	 * Tests the Task 2 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test2() throws Exception {
		float[] a1 = Arrays.copyOf(a,a.length);
		FloatArrays.selectionSort(a1);
		compare(a,a1);

		float[] b1 = Arrays.copyOf(b,b.length);
		FloatArrays.selectionSort(b1);
		compare(b,b1);
	}

	/**
	 * Tests the Task 3 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test3() throws Exception {
		float[] a1 = Arrays.copyOf(a,a.length);
		FloatArrays.quickSort(a1);
		compare(a,a1);

		float[] b1 = Arrays.copyOf(b,b.length);
		FloatArrays.quickSort(b1);
		compare(b,b1);
	}

	/**
	 * Compares the two specified {@code float} arrays.
	 * 
	 * @param a
	 *            a {@code float} array
	 * @param b
	 *            a {@code float} array
	 */
	protected void compare(float[] original, float[] sorted) {
		assertEquals(original.length, sorted.length);
		float[] temp = Arrays.copyOf(original,original.length);
		Arrays.sort(temp);
		for (int i = 0; i < original.length; i++)
			assertEquals(temp[i], sorted[i],.001f);
	}

}