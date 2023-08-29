package csi213.lab04;

/**
 * {@code Fibonacci} demonstrates Fibonacci series based on recursion and iteration.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class Fibonacci {

	/**
	 * Returns the specified term in the Fibonacci series.
	 * 
	 * @param n
	 *            the index of the term in the Fibonacci series
	 * @return the specified term in the Fibonacci series
	 */
	public static int fibonacci(int n) {
		if(n <= 0)
			return 0;
		else if(n == 1)
			return 1;
		return fibonacci(n - 2) + fibonacci(n - 1);
	}

	/**
	 * Returns the specified term in the Fibonacci series.
	 * 
	 * @param n
	 *            the index of the term in the Fibonacci series
	 * @return the specified term in the Fibonacci series
	 */
	public static int fibonacci_iterative(int n) {
		int fib1 = 1;
		int fib2 = 0;
		int fibTotal = 0;
		for(int i = 0; i < n; i++) {
			fibTotal = fib1 + fib2;
			fib1 = fib2;
			fib2 = fibTotal;
		}
		return fibTotal;
	}

	/**
	 * Returns the specified term in the Fibonacci series.
	 * 
	 * @param n
	 *            the index of the term in the Fibonacci series
	 * @param solutions
	 *            an {@code int} array holding finbonacci terms calculated previously
	 * @return the specified term in the Fibonacci series
	 */
	public static int fibonacci_dp(int n, int[] solutions) {
		solutions = new int[n + 2];
		solutions[0] = 0;
		solutions[1] = 1;
		for(int i = 2; i < solutions.length; i++) {
			solutions[i] = solutions[i - 1] + solutions[i - 2];
		}
		return solutions[n];
	}

	/**
	 * The main method of the {@code Fibonacci} class.
	 * 
	 * @param args
	 *            the program arguments
	 */
	public static void main(String[] args) {
		for (int i = 0; i <= 6; i++)
			System.out.println("fibonacci(" + i + ") = " + fibonacci(i));
		System.out.println();
		for (int i = 0; i <= 6; i++)
			System.out.println("fibonacci(" + i + ") = " + fibonacci_iterative(i));
		System.out.println();
		for (int i = 0; i <= 6; i++)
			System.out.println("fibonacci(" + i + ") = " + fibonacci_dp(i, new int[i + 1]));
		System.out.println();

		test(() -> fibonacci(45), "recursive implementation");
		test(() -> fibonacci_iterative(45), "iterative implementation");
		test(() -> fibonacci_dp(45, new int[46]), "dynamic programming");
	}

	/**
	 * Tests the specified {@code IntMethod}.
	 * 
	 * @param method
	 *            an {@code IntMethod}
	 * @param label
	 *            a string representing the {@code IntMethod} to test
	 */
	private static void test(IntMethod method, String label) {
		long t = System.nanoTime();
		System.out.println("fibonacci(45) = " + String.format("%,d", method.run())
				+ String.format(" # " + label + " (%.6f seconds)", 1e-9 * (System.nanoTime() - t)));
	}

	/**
	 * An {@code IntMethod} represents a method returning an {@code int} value.
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 * 
	 */
	static interface IntMethod {

		/**
		 * Returns an {@code int} value.
		 * 
		 * @return an {@code int} value
		 */
		int run();
	}
}