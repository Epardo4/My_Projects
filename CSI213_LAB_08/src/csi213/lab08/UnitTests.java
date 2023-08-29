package csi213.lab08;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnitTests {

	static Integer[] a = { 23, 42, 77, 13, 99, 22, 56, 33 };

	static Float[] b = { 7.7f, 6.6f, 5.5f, 1.1f, 2.2f, 3.3f, 4.4f };

	static Double[] c = { 1.1, 2.2, 3.3, 4.4, 5.5 };

	@Test
	public void testInteger() {
		GenericArray<Integer> ga = new GenericArray<>(a.length);
		for (int i=0;i<a.length;i++) {
			ga.setElement(i,a[i]);
		}
		assertEquals(42, ga.getElement(1).intValue());
		assertEquals(4, ga.sequentialSearch(99));
		ga.bubbleSort();
		assertEquals(7, ga.sequentialSearch(99));
		assertEquals(7, ga.binarySearchRecursive(99));

		GenericArray<Integer> ga2 = new GenericArray<>(a);
		assertEquals(42, ga2.getElement(1).intValue());
		assertEquals(4, ga2.sequentialSearch(99));
		ga2.selectionSort();
		assertEquals(7, ga2.sequentialSearch(99));
		assertEquals(7, ga2.binarySearchRecursive(99));
	}

	@Test
	public void testFloat() {
		GenericArray<Float> gb = new GenericArray<>(b.length);
		for (int i=0;i<b.length;i++) {
			gb.setElement(i,b[i]);
		}
		assertEquals(6.6f, gb.getElement(1),.0001f);
		assertEquals(4, gb.sequentialSearch(2.2f));
		gb.bubbleSort();
		assertEquals(6, gb.sequentialSearch(7.7f));
		assertEquals(6, gb.binarySearchRecursive(7.7f));

		GenericArray<Float> gb2 = new GenericArray<>(b);
		assertEquals(6.6f, gb2.getElement(1),0001f);
		assertEquals(4, gb2.sequentialSearch(2.2f));
		gb2.selectionSort();
		assertEquals(6, gb2.sequentialSearch(7.7f));
		assertEquals(6, gb2.binarySearchRecursive(7.7f));
	}

	@Test
	public void testDouble() {
		GenericArray<Double> gc = new GenericArray<>(c.length);
		for (int i=0;i<c.length;i++) {
			gc.setElement(i,c[i]);
		}
		assertEquals(2.2, gc.getElement(1),.0001);
		assertEquals(3, gc.sequentialSearch(4.4));
		gc.bubbleSort();
		assertEquals(4, gc.sequentialSearch(5.5));
		assertEquals(4, gc.binarySearchRecursive(5.5));

		GenericArray<Double> gc2 = new GenericArray<>(c);
		assertEquals(2.2, gc2.getElement(1),.0001);
		assertEquals(3, gc2.sequentialSearch(4.4));
		gc2.selectionSort();
		assertEquals(4, gc2.sequentialSearch(5.5));
		assertEquals(4, gc2.binarySearchRecursive(5.5));
	}
	@Test
	public void testString() {
		GenericArray<String> gs = new GenericArray<>("the quick brown fox jumped over the lazy dog".split(" "));
		assertEquals(gs.getElement(4).compareTo("jumped"),0);
		gs.bubbleSort();
		assertNotEquals(-1,gs.binarySearchRecursive("over"));
		assertEquals(gs.getElement(0).compareTo("brown"),0);
	}
}
