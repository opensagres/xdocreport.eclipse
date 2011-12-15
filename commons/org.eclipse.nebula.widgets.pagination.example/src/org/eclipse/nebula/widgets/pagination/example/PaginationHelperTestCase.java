package org.eclipse.nebula.widgets.pagination.example;

import junit.framework.TestCase;

import org.eclipse.nebula.widgets.pagination.PaginationHelper;

public class PaginationHelperTestCase extends TestCase {

	public void testname() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(0, 403, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, 1, 2, 3, 4, 5, 6, 7, -1, 402 };
		assertEquals(display(expected), display(indexes));
	}

	public void test2name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(10, 403, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, -1, 7, 8, 9, 10, 11, 12, -1, 402 };
		assertEquals(display(expected), display(indexes));
	}

	public void test22name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(402, 403, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, -1, 395, 396, 397, 398, 399, 400, 401, 402 };
		assertEquals(display(expected), display(indexes));
	}

	public void test23name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(396, 403, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, -1, 393, 394, 395, 396, 397, 398, -1, 402 };
		assertEquals(display(expected), display(indexes));
	}

	public void test24name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(395, 403, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, -1, 392, 393, 394, 395, 396, 397, -1, 402 };
		assertEquals(display(expected), display(indexes));
	}

	public void test3name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(0, 4, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, 1, 2, 3 };
		assertEquals(display(expected), display(indexes));
	}

	public void test4name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(1, 4, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, 1, 2, 3 };
		assertEquals(display(expected), display(indexes));
	}

	public void test5name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(2, 4, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, 1, 2, 3 };
		assertEquals(display(expected), display(indexes));
	}

	public void test6name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(3, 4, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, 1, 2, 3 };
		assertEquals(display(expected), display(indexes));
	}

	public void test7name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(7, 20, 10);
		System.err.println(display(indexes));

		int[] expected = { 0, 1, 2, 3, 4, 5, 6, 7, -1, 19 };
		assertEquals(display(expected), display(indexes));
	}

	public void test8name() throws Exception {
		int[] indexes = PaginationHelper.getPageIndexes(8, 20, 10);
		System.err.println(display(indexes));

		int[] expected = { 0,-1,5,6,7,8,9,10,-1,19 };
		assertEquals(display(expected), display(indexes));
	}

	private static String display(int[] indexes) {
		StringBuilder result = new StringBuilder();
		result.append("[");
		for (int i = 0; i < indexes.length; i++) {
			if (i > 0) {
				result.append(",");
			}
			result.append(indexes[i]);
		}
		result.append("]");
		return result.toString();
	}
}
