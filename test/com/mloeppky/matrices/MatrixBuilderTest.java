package com.mloeppky.matrices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.mloeppky.matrices.client.Matrix;
import com.mloeppky.matrices.client.MatrixBuilder;

public class MatrixBuilderTest {
	@Test
	public void testMatrixBuilder() {
		assertEquals(new Matrix(0,0), new MatrixBuilder().build());
		assertEquals(MatrixTest.DEFAULT_MATRIX, new MatrixBuilder()
			.addRow(0, 1)
			.addRow(2, 3)
			.addRow(4, 5)
			.build());
		assertEquals(MatrixTest.DEFAULT_MATRIX, new MatrixBuilder()
			.addCol(0, 2, 4)
			.addCol(1, 3, 5)
			.build());
		assertEquals(MatrixTest.DEFAULT_MATRIX, new MatrixBuilder()
			.addRow(0)
			.addRow(2)
			.addRow(4)
			.addCol(1, 3, 5)
			.build());
		
		try {
			new MatrixBuilder()
				.addRow(0, 1)
				.addCol(2, 3, 4)
				.build();
			fail();
		} catch (IllegalStateException e) {
			assertTrue(true);
		}
		
		try {
			new MatrixBuilder()
				.addRow(0, 1)
				.addRow(2, 3, 4)
				.build();
			fail();
		} catch (IllegalStateException e) {
			assertTrue(true);
		}
	}
}
