package csi213.lab02;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@code Tests2022F1} tests the Fall 2022 Programming Assignment 1 implementations.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class UnitTests {

	/**
	 * Tests the Task 1 implementations.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test1() throws Exception {
		assertEquals("John", "" + new Person("John"));
		assertEquals("Emily", "" + new Person("Emily"));
	}

	/**
	 * Tests the Task 2 implementations.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test2() throws Exception {
		assertEquals("Chloy, 1234","" + new Student("Chloy", 1234));
		assertEquals("Ken, 2345","" + new Student("Ken", 2345));
	}

	/**
	 * Tests the Task 3 implementations.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void test3() throws Exception {
		assertEquals(3, new Student("Tom", 3456).studentCount());
		assertEquals(4, new Student("Jenny", 4567).studentCount());
	}

}
