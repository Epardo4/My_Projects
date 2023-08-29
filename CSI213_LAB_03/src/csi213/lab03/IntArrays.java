package csi213.lab03;

import java.util.Arrays;

/**
 * This {@code IntArrays} class provides methods for manipulating {@code int} arrays.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class IntArrays {

	/**
	 * Assigns the specified {@code int} value to each element of the specified {@code int} array.
	 *
	 * @param a
	 *            the array to be filled
	 * @param val
	 *            the value to be stored in all elements of the array
	 */
	public static void fill(int[] a, int val) {
		for(int i = 0; i < a.length; i++) {
			a[i] = val;
		}
	}

	/**
	 * Constructs a copy of the specified array.
	 * 
	 * @param original
	 *            an {@code int} array
	 * @return the constructed array
	 */
	public static int[] copyOf(int[] original) {
		int[] copy = new int[original.length];
		for(int i = 0; i < original.length; i++) {
			copy[i] = original[i];
		}
		return copy;
	}

	/**
	 * Returns a sub-array of the specified array. The sub-array begins at the specified {@code beginIndex} and extends
	 * to the element at index {@code endIndex - 1} and thus the length of the sub-array is {@code endIndex-beginIndex}.
	 *
	 * @param beginIndex
	 *            the beginning index, inclusive
	 * @param endIndex
	 *            the ending index, exclusive
	 * @return the specified sub-array
	 */
	public static int[] subarray(int[] original, int startIndex, int endIndex) {
		int subIndex = 0;
		int[] sub = new int[original.length - startIndex - (original.length - endIndex)];
		for(int i = startIndex; i < endIndex; i++) {
			sub[subIndex] = original[i];
			subIndex++;
		}
		return sub;
	}

	/**
	 * The main method of the {IntArrays} class.
	 * 
	 * @param args
	 *            the program arguments
	 */
	public static void main(String[] args) {
		int[] a = new int[5];
		for (int i = 0; i < a.length; i++)
			a[i] = i;
		System.out.println(Arrays.toString(a));

		int[] b = new int[4];
		System.out.println(Arrays.toString(b));
		fill(b, -1);
		System.out.println(Arrays.toString(b));

		b = copyOf(a);
		System.out.println(Arrays.toString(b));
		fill(b, -1);
		System.out.println(Arrays.toString(b));
		System.out.println(Arrays.toString(a));
		System.out.println(Arrays.toString(subarray(a, 0, a.length)));
		System.out.println(Arrays.toString(subarray(a, 1, a.length)));
		System.out.println(Arrays.toString(subarray(a, 1, a.length - 1)));
	}

}
