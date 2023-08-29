package csi213.lab04;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@code UnitTests} tests the Lab 4 implementations.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class UnitTests {

	/**
	 * Tests the Task 1 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test1() throws Exception {
		assertEquals(1, Factorial.factorial(0));
		assertEquals(6, Factorial.factorial(3));
		assertEquals(24, Factorial.factorial(4));
	}

	/**
	 * Tests the Task 2 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test2() throws Exception {
		assertEquals(1, Factorial.factorial_iterative(0));
		assertEquals(6, Factorial.factorial_iterative(3));
		assertEquals(24, Factorial.factorial_iterative(4));
	}

	/**
	 * Tests the Task 3 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test3() throws Exception {
		assertEquals(0, Fibonacci.fibonacci(0));
		assertEquals(5, Fibonacci.fibonacci(5));
		assertEquals(8, Fibonacci.fibonacci(6));
		assertEquals(13, Fibonacci.fibonacci(7));
	}

	/**
	 * Tests the Task 4 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test4() throws Exception {
		assertEquals(0, Fibonacci.fibonacci_iterative(0));
		assertEquals(5, Fibonacci.fibonacci_iterative(5));
		assertEquals(8, Fibonacci.fibonacci_iterative(6));
		assertEquals(13, Fibonacci.fibonacci_iterative(7));
	}

	/**
	 * Tests the Task 5 implementation.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test5() throws Exception {
		assertEquals(0, Fibonacci.fibonacci_dp(0, new int[1]));
		assertEquals(5, Fibonacci.fibonacci_dp(5, new int[6]));
		assertEquals(8, Fibonacci.fibonacci_dp(6, new int[7]));
		assertEquals(13, Fibonacci.fibonacci_dp(7, new int[8]));
		long t = System.currentTimeMillis();
		assertEquals(1134903170, Fibonacci.fibonacci_dp(45, new int[46]));
		assertTrue(System.currentTimeMillis() - t < 1000);
	}

}
