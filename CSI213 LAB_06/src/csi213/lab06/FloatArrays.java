package csi213.lab06;

import java.util.Arrays;

/**
 * This {@code FloatArrays} class provides methods for manipulating {@code float} arrays.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class FloatArrays {

	/**
	 * Finds the location of the specified value (the index at which the specified value is stored) in the specified
	 * array by examining each element until the value is found.
	 * 
	 * @param a
	 *            an {@code float} array
	 * @param x
	 *            an {@code float} value
	 * @return the location of the specified value (the index at which the specified value is stored) in the specified
	 *         array; {@code -1} if the specified value is not in the array
	 */
	public static int sequentialSearch(float[] a, float x) {
		// TODO: add some code here
		for(int i = 0; i < a.length; i++)
			if(x == a[i])
				return i;
		return -1;
	}

	/**
	 * Finds the location of the specified value (the index at which the specified value is stored) in the specified
	 * sorted array using the recursive binary search algorithm.
	 * 
	 * @param a
	 *            an {@code float} array that is known to be sorted
	 * @param lower
	 *            an index representing the lower bound of the binary search
	 * @param upper
	 *            an index representing the upper bound of the binary search
	 * @param x
	 *            an {@code float} value
	 * @return the location of the specified value (the index at which the specified value is stored) in the specified
	 *         array; {@code -1} if the specified value is not in the array
	 */
	public static int binarySearchRecursive(float[] a, int lower, int upper, float x) {
		// TODO: add some code here
		sortingAlgorithm(a);
		int answer = -1;
		int halfWay = (upper + lower)/2;
		if(a[halfWay] == x) {
			answer = halfWay;
		}
		if(lower < upper) {
			if(a[halfWay] < x) {
				answer = binarySearchRecursive(a,halfWay + 1,upper,x);
			}
			else if(a[halfWay] > x) {
				answer = binarySearchRecursive(a,lower,halfWay - 1,x);
			}
		}
			return answer;
	}

	/**
	 * Finds the location of the specified value (the index at which the specified value is stored) in the specified
	 * sorted array using the iterative binary search algorithm.
	 * 
	 * @param a
	 *            an {@code float} array that is known to be sorted
	 * @param x
	 *            an {@code float} value
	 * @return the location of the specified value (the index at which the specified value is stored) in the specified
	 *         array; {@code -1} if the specified value is not in the array
	 */
	public static float[] sortingAlgorithm(float[]a) {
		float temporary = 0;
		for (int last = a.length - 1; last >= 1; last--) {
			for(int i = a.length - 1; i >= 1; i--) {
				if(a[i] < a[i-1]) {
					temporary = a[i];
					a[i] = a[i-1];
					a[i-1] = temporary;
				}
			}
		}
		return a;
	}
	public static int binarySearchIterative(float[] a, float x) {
		// TODO: add some code here
		sortingAlgorithm(a);
		int first = 0;
		int last = a.length - 1;
		int halfWay = (last + first)/2;
		while(first < last) {
			if(a[halfWay] > x) 
				last = halfWay - 1;
			else if(a[halfWay] < x)
				first = halfWay + 1;
			else
				return halfWay;
			halfWay = (first + last)/2;
		}
		return -1;
	}

	/**
	 * The main method of the {@code IntArrays} class.
	 * 
	 * @param args
	 *            the program arguments
	 */
	public static void main(String[] args) {
		float[] a = { 5.1f, 3.2f, 1.7f, 2.6f, 4.4f };
		float[] b = { 1.4f, 3.5f, 5.2f, 7.2f, 9.2f, 11.2f, 13.8f };

		/**System.out.println(sequentialSearch(a, 2.6f));
		System.out.println(sequentialSearch(a, 7.2f));
		System.out.println(sequentialSearch(b, 2.3f));
		System.out.println(sequentialSearch(b, 7.2f));
		System.out.println();*/

		System.out.println(binarySearchRecursive(a, 0, a.length - 1, 2.6f));
		System.out.println("\n");
		System.out.println(binarySearchRecursive(a, 0, a.length - 1, 7.2f));
		System.out.println("\n");
		System.out.println(binarySearchRecursive(b, 0, b.length - 1, 2.3f));
		System.out.println("\n");
		System.out.println(binarySearchRecursive(b, 0, b.length - 1, 7.2f));
		System.out.println();

		/**System.out.println(binarySearchIterative(a, 2.6f));
		System.out.println(binarySearchIterative(a, 7.2f));
		System.out.println(binarySearchIterative(b, 2.3f));
		System.out.println(binarySearchIterative(b, 7.2f));
		System.out.println();*/
	}

}
