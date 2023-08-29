package csi213.lab05;

import java.util.Arrays;

/**
 * This {@code FloatArrays} class provides methods for manipulating {@code Float} arrays.
 */
public class FloatArrays {

	/**
	 * Sorts the specified array using the bubble sort algorithm.
	 * 
	 * @param a
	 *            an {@code Float} array
	 */
	public static void bubbleSort(float[] a) {
		System.out.println(Arrays.toString(a));
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
		System.out.println(Arrays.toString(a));
	}

	/**
	 * Sorts the specified array using the selection sort algorithm.
	 * 
	 * @param a
	 *            an {@code Float} array
	 * @param out
	 *            a {@code PrintStream} to show the array at the end of each pass
	 */
	public static void selectionSort(float[] a) {
		System.out.println(Arrays.toString(a));
		int index = 0;
		float temporary;
		for (int last = a.length - 1; last >= 1; last--) {
			for(int i = last; i >= 0; i--) {
				if(a[i] > a[index])
					index = i;
			}
			temporary = a[last];
			a[last] = a[index];
			a[index] = temporary;
		}
		System.out.println(Arrays.toString(a));
	}

	/**
	 * Sorts the specified array using the quick sort algorithm.
	 * 
	 * @param a
	 *            an {@code Float} array
	 * @param out
	 *            a {@code PrintStream} to show the array at the end of each pass
	 */
	public static void quickSort(float[] a) {
		System.out.println(Arrays.toString(a));
		quickSort(a, 0, a.length-1);
		System.out.println(Arrays.toString(a));
	}
	public static int partition(float [] a, int low, int high) {
		float pivot = a[(low + high)/2];
		while (true) {
			while (a[low] < pivot)
				low++;
			while (a[high] > pivot)
				high--;
			if (low >= high) 
				return high;
			float temp = a[low];
			a[low] = a[high];
			a[high] = temp;
			low++;
			high--;
		}
	}
	public static void quickSort(float [] a, int low, int high) {
        if (low >= 0 && high >= 0 && low < high) {
            int p = partition (a, low, high);
            quickSort(a,low, p);
            quickSort(a, p+1, high);
        }
    }

	/**
	 * The main method of the {@code FloatArrays} class.
	 * 
	 * @param args
	 *            the program arguments
	 */
	public static void main(String[] args) {
		float[] a = { 5.3F, 3.8F, 1.2F, 2.7F, 4.99F };

		bubbleSort(Arrays.copyOf(a, a.length));
		System.out.println();

		selectionSort(Arrays.copyOf(a, a.length));
		System.out.println();

		quickSort(Arrays.copyOf(a, a.length));
		System.out.println();
	}

}