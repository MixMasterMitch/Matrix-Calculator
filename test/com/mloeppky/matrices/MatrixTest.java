package com.mloeppky.matrices;

import static com.mloeppky.matrices.client.Matrix.newIdentityMatrix;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.mloeppky.matrices.client.Matrix;
import com.mloeppky.matrices.client.MatrixBuilder;

public class MatrixTest {
	
	public static final int DEFAULT_ROWS = 3;
	public static final int DEFAULT_COLS = 2;
	public static final double[][] DEFAULT_ARRAY;
	static {
		DEFAULT_ARRAY = new double[DEFAULT_ROWS][DEFAULT_COLS];
		double cell = 0;
		for (int r = 0; r < DEFAULT_ROWS; r++) {
			for (int c = 0; c < DEFAULT_COLS; c++) {
				DEFAULT_ARRAY[r][c] = cell;
				cell++;
			}
		}
	}
	public static final Matrix DEFAULT_MATRIX = new Matrix(DEFAULT_ARRAY);
	public static final Matrix EMPTY_MATRIX = new Matrix(0, 0);
	
	@Test
	public void testMatrix() {		
		double[][] array = DEFAULT_ARRAY;
		Matrix m = new Matrix(array);
		for (int r = 0; r < array.length; r++) {
			for (int c = 0; c < array[r].length; c++) {
				assertEquals(array[r][c], m.get(r, c), .1);
			}
		}
		
		m = new Matrix(array.length, array[0].length);
		assertEquals(array.length, m.getRows());
		assertEquals(array[0].length, m.getCols());
	}
	
	@Test
	public void testGetRows() {
		Assert.assertEquals(DEFAULT_ROWS, DEFAULT_MATRIX.getRows());
	}
	
	@Test
	public void testGetCols() {
		Assert.assertEquals(DEFAULT_COLS, DEFAULT_MATRIX.getCols());
	}
	
	@Test
	public void testGet() {
		try {
			DEFAULT_MATRIX.get(-1, -1);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			DEFAULT_MATRIX.get(DEFAULT_ROWS, -1);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			DEFAULT_MATRIX.get(-1, DEFAULT_COLS);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			DEFAULT_MATRIX.get(DEFAULT_ROWS, DEFAULT_COLS);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		double cellNumber = 0;
		for (int r = 0; r < DEFAULT_ROWS; r++) {
			for (int c = 0; c < DEFAULT_COLS; c++) {
				assertEquals(cellNumber, DEFAULT_MATRIX.get(r, c), .1);
				cellNumber++;
			}
		}
	}
	
	@Test
	public void testSet() {
		try {
			DEFAULT_MATRIX.set(-1, -1, 1.5);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			DEFAULT_MATRIX.set(DEFAULT_ROWS, -1, 1.5);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			DEFAULT_MATRIX.set(-1, DEFAULT_COLS, 1.5);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			DEFAULT_MATRIX.set(DEFAULT_ROWS, DEFAULT_COLS, 1.5);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		Matrix m = DEFAULT_MATRIX.duplicate();
		m.set(0, 0, 1.5);
		m.set(DEFAULT_ROWS - 1, DEFAULT_COLS - 1, 2.5);
		assertEquals(1.5, m.get(0, 0), .1);
		assertEquals(2.5, m.get(DEFAULT_ROWS - 1, DEFAULT_COLS - 1), .1);
	}
	
	@Test
	public void testDuplicate() {
		assertEquals(DEFAULT_MATRIX, DEFAULT_MATRIX.duplicate());
	}
	
	@Test
	public void testNewIdentityMatrix() {
		try {
			newIdentityMatrix(-1);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		Matrix m = new MatrixBuilder().build();
		assertEquals(m, newIdentityMatrix(0));

		m = new MatrixBuilder()
			.addRow(1)
			.build();
		assertEquals(m, newIdentityMatrix(1));

		m = new MatrixBuilder()
			.addRow(1, 0)
			.addRow(0, 1)
			.build();
		assertEquals(m, newIdentityMatrix(2));
	}
	
	@Test
	public void testToStringMultiLine() {
		assertEquals("[[]]", new Matrix(0, 0).toStringMultiLine());
		assertEquals("[[]]", new Matrix(1, 0).toStringMultiLine());
		assertEquals("[[]]", new Matrix(0, 1).toStringMultiLine());
		assertEquals("[[0.00]]", new Matrix(1, 1).toStringMultiLine());
		assertEquals("[[ 0.00, 10.00]]", new MatrixBuilder().addRow(0, 10).build().toStringMultiLine());
		assertEquals("[[0.00, 1.00],\n [2.00, 3.00],\n [4.00, 5.00]]", DEFAULT_MATRIX.toStringMultiLine());
	}
	
	@Test
	public void testToString() {
		assertEquals("[[]]", new Matrix(0, 0).toString());
		assertEquals("[[]]", new Matrix(1, 0).toString());
		assertEquals("[[]]", new Matrix(0, 1).toString());
		assertEquals("[[0.00]]", new Matrix(1, 1).toString());
		assertEquals("[[ 0.00, 10.00]]", new MatrixBuilder().addRow(0, 10).build().toString());
		assertEquals("[[0.00, 1.00], [2.00, 3.00], [4.00, 5.00]]", DEFAULT_MATRIX.toString());
	}
	
	@Test
	public void testEquals() {
		assertTrue(DEFAULT_MATRIX.equals(DEFAULT_MATRIX));
		assertFalse(DEFAULT_MATRIX.equals(DEFAULT_ARRAY));
		assertFalse(DEFAULT_MATRIX.equals(new Matrix(0,0)));
		assertFalse(DEFAULT_MATRIX.equals(new Matrix(1,0)));
		assertFalse(DEFAULT_MATRIX.equals(new Matrix(0,1)));
		assertFalse(DEFAULT_MATRIX.equals(new Matrix(1,1)));
		assertFalse(DEFAULT_MATRIX.equals(new Matrix(DEFAULT_ROWS,DEFAULT_COLS)));
	}
	
	@Test
	public void isEchelonAndReducedEchelonForm() {
		assertFalse(DEFAULT_MATRIX.isEchelonForm());
		assertFalse(DEFAULT_MATRIX.isReducedEchelonForm());
		
		Matrix echelon = new MatrixBuilder()
			.addRow(1, 2, 3, 4)
			.addRow(0, 1, 2, 3)
			.addRow(0, 0, 1, 2)
			.build();
		assertTrue(echelon.isEchelonForm());
		assertFalse(echelon.isReducedEchelonForm());
		
		echelon = new MatrixBuilder()
		.addRow(1, 2, 3, 4)
		.addRow(0, 1, 2, 3)
		.addRow(0, 0, 0, 2)
		.build();
		assertTrue(echelon.isEchelonForm());
		assertFalse(echelon.isReducedEchelonForm());
		
		echelon = new MatrixBuilder()
		.addRow(1, 2, 3, 4)
		.addRow(0, 1, 2, 3)
		.addRow(0, 0, 0, 0)
		.build();
		assertTrue(echelon.isEchelonForm());
		assertFalse(echelon.isReducedEchelonForm());
		
		Matrix reducedEchelon = new MatrixBuilder()
		.addRow(1, 0, 0, 4)
		.addRow(0, 1, 0, 3)
		.addRow(0, 0, 1, 2)
		.build();
		assertTrue(reducedEchelon.isEchelonForm());
		assertTrue(reducedEchelon.isReducedEchelonForm());
		
		reducedEchelon = new MatrixBuilder()
		.addRow(1, 0, 3, 4)
		.addRow(0, 1, 2, 3)
		.addRow(0, 0, 0, 2)
		.build();
		assertTrue(reducedEchelon.isEchelonForm());
		assertTrue(reducedEchelon.isReducedEchelonForm());
		
		reducedEchelon = new MatrixBuilder()
		.addRow(1, 0, 3, 4)
		.addRow(0, 1, 2, 3)
		.addRow(0, 0, 0, 0)
		.build();
		assertTrue(reducedEchelon.isEchelonForm());
		assertTrue(reducedEchelon.isReducedEchelonForm());
		
	}
	
	@Test
	public void is3DVector() {
		assertFalse(DEFAULT_MATRIX.is3DVector());
		assertFalse(EMPTY_MATRIX.is3DVector());
		assertTrue(new MatrixBuilder().addRow(0, 1, 2).build().is3DVector());
	}
	
	@Test
	public void testIsSquare() {
		assertFalse(DEFAULT_MATRIX.isSquare());
		assertTrue(EMPTY_MATRIX.isSquare());
		assertTrue(Matrix.newIdentityMatrix(3).isSquare());
	}

}
