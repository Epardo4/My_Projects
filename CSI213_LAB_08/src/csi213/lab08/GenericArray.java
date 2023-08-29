package csi213.lab08;

public class GenericArray <T extends Comparable<T>>{
	private Object[] genericArray; 
	public GenericArray(int arraySize) {
		genericArray = new Object[arraySize];
	}
	public GenericArray( Object[] existingArray) {
		genericArray = new Object[existingArray.length];
		for(int i = 0; i < genericArray.length; i++) {
			genericArray[i] = existingArray[i];
		}
	}

	/**
	 * Gets the Nth element of the array.
	 * 
	 * @param n
	 *            the index of the item to return
	 * @return the nth item in the array
	 */
	public T getElement(int n) {
		return (T)genericArray[n];
	}

	/**
	 * Sets the Nth element of the array.
	 * 
	 * @param n
	 *            the index of the item to return
	 * @param value
	 *            the value to set the array element to
	 */
	public void setElement(int n, T value) {
		genericArray[n] = value;
	}

	/**
	 * Finds the location of the specified value (the index at which the specified value is stored) in the specified
	 * array by examining each element until the value is found.
	 * 
	 * @param x
	 *            a value to search for
	 * @return the location of the specified value (the index at which the specified value is stored) in the specified
	 *         array; {@code -1} if the specified value is not in the array
	 */
	public int sequentialSearch(T x) {
		for(int i = 0; i < genericArray.length; i++) {
			if(genericArray[i].equals(x))
				return i;
		}
		return -1;
	}

	/**
	 * Finds the location of the specified value (the index at which the specified value is stored) in the specified
	 * sorted array using the recursive binary search algorithm.
	 * 
	 * @param findMe
	 *            the value to search for
	 * @return the location of the specified value (the index at which the specified value is stored) in the specified
	 *         array; {@code -1} if the specified value is not in the array
	 */
	public int binarySearchRecursive( T findMe) {
		bubbleSort();
		return binarySearchRecursiveInternal(0, genericArray.length, findMe);
	}

	private int binarySearchRecursiveInternal(int lower, int higher, T x) {
		int halfWay = (lower + higher) / 2;
		if(lower >= higher)
			return -1;
		if(genericArray[halfWay].equals(x))
			return halfWay;
		if(((T)genericArray[halfWay]).compareTo(((T)x)) > 0)
			return binarySearchRecursiveInternal(lower, halfWay, x);
		return binarySearchRecursiveInternal(halfWay + 1, higher, x);
	}	

	/**
	 * Sorts the array using the bubble sort algorithm
	 */
	public void bubbleSort() {
		Object temporary;
		for(int i = 0; i < genericArray.length; i++) 
			for(int j = 0; j < genericArray.length-1; j++) {
				if(((T) (genericArray[j])).compareTo((T)(genericArray[j+1])) > 0) {
					temporary = genericArray[j];
					setElement(j, (T)genericArray[j+1]);
					setElement(j+1, (T)temporary);
				}
		}
	}
	
	/**
	 * Sorts the array using the selection sort algorithm
	 */
	public void selectionSort() {
		Object temporary;
		int index;
		for(int i = 0; i < genericArray.length; i++) {
			index = i;
			for(int j = i; j < genericArray.length; j++)
				if(((T)genericArray[j]).compareTo((T)genericArray[index]) < 0)
					index = j;
			temporary = (T)genericArray[index];
			genericArray[index] = genericArray[i];
			genericArray[i] = temporary;
		}
	}
}
