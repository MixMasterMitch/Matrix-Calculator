package com.mloeppky.matrices;

import static com.mloeppky.matrices.MatrixTest.DEFAULT_COLS;
import static com.mloeppky.matrices.MatrixTest.DEFAULT_MATRIX;
import static com.mloeppky.matrices.MatrixTest.DEFAULT_ROWS;
import static com.mloeppky.matrices.MatrixTest.EMPTY_MATRIX;
import static com.mloeppky.matrices.client.Matrix.newIdentityMatrix;
import static com.mloeppky.matrices.client.MatrixOperations.add;
import static com.mloeppky.matrices.client.MatrixOperations.crossProduct;
import static com.mloeppky.matrices.client.MatrixOperations.dotProduct;
import static com.mloeppky.matrices.client.MatrixOperations.multiply;
import static com.mloeppky.matrices.client.MatrixOperations.rowReduction;
import static com.mloeppky.matrices.client.MatrixOperations.scale;
import static com.mloeppky.matrices.client.MatrixOperations.subtract;
import static com.mloeppky.matrices.client.MatrixOperations.transpose;
import static com.mloeppky.matrices.client.MatrixOperations.unit;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.mloeppky.matrices.client.Matrix;
import com.mloeppky.matrices.client.MatrixBuilder;
import com.mloeppky.matrices.client.MatrixOperations;

public class MatrixOperationsTest {
	public static final Matrix DOUBLE_DEFAULT = new MatrixBuilder()
		.addRow(0, 2)
		.addRow(4, 6)
		.addRow(8, 10)
		.build();
	
	public static final Matrix VECTOR_DEFAULT = new MatrixBuilder()
		.addRow(1, 2, 3)
		.build();
	
	public static final Matrix ZERO_VECTOR = new MatrixBuilder()
		.addRow(0, 0, 0)
		.build();
	
	@Test
	public void testScale() {
		assertEquals(EMPTY_MATRIX, scale(EMPTY_MATRIX, 0));
		
		assertEquals(DOUBLE_DEFAULT, scale(DEFAULT_MATRIX, 2));

		assertEquals(new Matrix(DEFAULT_ROWS, DEFAULT_COLS), scale(DEFAULT_MATRIX, 0));
	}
	
	@Test
	public void testRowReduction() {
		assertEquals(EMPTY_MATRIX, rowReduction(EMPTY_MATRIX));
		
		Matrix toBeReduced = new MatrixBuilder()
			.addRow(1, 1, -1)
			.addRow(-3, -2, 3)
			.build();
		Matrix reduced = new MatrixBuilder()
			.addRow(1, 0, -1)
			.addRow(0, 1, 0)
			.build();
		assertEquals(reduced, rowReduction(toBeReduced));
		
		Matrix reducedDefault = new MatrixBuilder()
			.addRow(0, 1)
			.addRow(1, 1.5)
			.addRow(0, -1)
			.build();
		assertEquals(reducedDefault, rowReduction(DEFAULT_MATRIX));
	}
	
	@Test
	public void testAdd() {
		assertEquals(EMPTY_MATRIX, add(EMPTY_MATRIX, EMPTY_MATRIX));
		
		assertEquals(DEFAULT_MATRIX, add(DEFAULT_MATRIX, new Matrix(DEFAULT_ROWS, DEFAULT_COLS)));
		
		assertEquals(DOUBLE_DEFAULT, add(DEFAULT_MATRIX, DEFAULT_MATRIX));
		
		try {
			add(DEFAULT_MATRIX, EMPTY_MATRIX);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		 	
		try {
			add(DEFAULT_MATRIX, new Matrix(DEFAULT_COLS, DEFAULT_ROWS));
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testSubtract() {
		assertEquals(EMPTY_MATRIX, subtract(EMPTY_MATRIX, EMPTY_MATRIX));
		
		assertEquals(DEFAULT_MATRIX, subtract(DEFAULT_MATRIX, new Matrix(DEFAULT_ROWS, DEFAULT_COLS)));

		assertEquals(DEFAULT_MATRIX, subtract(DOUBLE_DEFAULT, DEFAULT_MATRIX));
		
		try {
			subtract(DEFAULT_MATRIX, EMPTY_MATRIX);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		 	
		try {
			subtract(DEFAULT_MATRIX, new Matrix(DEFAULT_COLS, DEFAULT_ROWS));
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
	}
	
	@Test
	public void testMultiply() {
		assertEquals(EMPTY_MATRIX, multiply(EMPTY_MATRIX, EMPTY_MATRIX));
		
		assertEquals(DEFAULT_MATRIX, multiply(DEFAULT_MATRIX, newIdentityMatrix(DEFAULT_COLS)));

		assertEquals(new Matrix(DEFAULT_ROWS, DEFAULT_ROWS), multiply(DEFAULT_MATRIX, new Matrix(DEFAULT_COLS, DEFAULT_ROWS)));
		
		try {
			multiply(DEFAULT_MATRIX, EMPTY_MATRIX);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		 	
		try {
			multiply(DEFAULT_MATRIX, DEFAULT_MATRIX);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testTranspose() {
		Matrix transposedDefault = new MatrixBuilder()
			.addRow(0, 2, 4)
			.addRow(1, 3, 5)
			.build();
		assertEquals(transposedDefault, transpose(DEFAULT_MATRIX));
		assertEquals(EMPTY_MATRIX, transpose(EMPTY_MATRIX));
	}
	
	@Test
	public void testMagnitude() {
		try {
			unit(DEFAULT_MATRIX);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		assertEquals(new Double(sqrt(14)), new Double(MatrixOperations.magnitude(VECTOR_DEFAULT)));
		assertEquals(new Double(0), new Double(MatrixOperations.magnitude(ZERO_VECTOR)));
	}
	
	@Test
	public void testUnit() {
		try {
			unit(ZERO_VECTOR);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			unit(DEFAULT_MATRIX);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		assertEquals(new MatrixBuilder().addRow(1/sqrt(14), 2/sqrt(14), 3/sqrt(14)).build(), unit(VECTOR_DEFAULT));
	}
	
	@Test
	public void testDotProduct() {
		try {
			dotProduct(DEFAULT_MATRIX, DEFAULT_MATRIX);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		assertEquals(new Double(0), new Double(dotProduct(ZERO_VECTOR, ZERO_VECTOR)));
		assertEquals(new Double(14), new Double(dotProduct(VECTOR_DEFAULT, VECTOR_DEFAULT)));
	}
	
	@Test
	public void testCrossProduct() {
		try {
			crossProduct(DEFAULT_MATRIX, DEFAULT_MATRIX);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		assertEquals(ZERO_VECTOR, crossProduct(ZERO_VECTOR, VECTOR_DEFAULT));

		Matrix vector2 = new MatrixBuilder().addRow(4, 5, 6).build();
		Matrix cross = new MatrixBuilder().addRow(-3, 6, -3).build();
		Matrix negativeCross = new MatrixBuilder().addRow(3, -6, 3).build();
		assertEquals(cross, crossProduct(VECTOR_DEFAULT, vector2));
		assertEquals(negativeCross, crossProduct(vector2, VECTOR_DEFAULT));
	}
}
