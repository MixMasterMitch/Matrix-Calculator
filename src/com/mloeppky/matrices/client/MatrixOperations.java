package com.mloeppky.matrices.client;

import static com.mloeppky.matrices.client.Matrix.newIdentityMatrix;
import static com.mloeppky.matrices.client.MatrixOperations.magnitude;
import static com.mloeppky.matrices.client.MatrixOperations.multiply;
import static com.mloeppky.matrices.client.MatrixOperations.rowReduction;
import static java.lang.Math.sqrt;

public class MatrixOperations {
	
private MatrixOperations() {}
	
	public static final PairOperation<Matrix> ADD = new Add();
	public static final PairOperation<Matrix> SUBTRACT = new Subtract();
	public static final PairOperation<Matrix> MULTIPLY = new Multiply();
	public static final PairOperation<Matrix> CROSS_PRODUCT = new CrossProduct();
	public static final ScalarPairOperation<Matrix> DOT_PRODUCT = new DotProduct();
	public static final ScalarSingleOperation<Matrix> MAGNITUDE = new Magnitude();
	public static final SingleOperation<Matrix> ROW_REDUCTION = new RowReduction();
	public static final SingleOperation<Matrix> TRANSPOSE = new Transpose();
	public static final SingleOperation<Matrix> INVERSE = new Inverse();
	public static final SingleOperation<Matrix> UNIT = new Unit();
	public static final ScaleOperation<Matrix> SCALE = new Scale();
	
	/*#####################
	 * Operation wrappers
	 *#####################*/
	public static Matrix scale(Matrix matrix, double scaleFactor) {
		return operate(matrix, scaleFactor, SCALE);
	}
	
	public static Matrix rowReduction(Matrix matrix) {
		return operate(matrix, ROW_REDUCTION);
	}
	
	public static Matrix transpose(Matrix matrix) {
		return operate(matrix, TRANSPOSE);
	}
	
	public static Matrix inverse(Matrix matrix) {
		return operate(matrix, INVERSE);
	}
	
	public static Matrix unit(Matrix matrix) {
		return operate(matrix, UNIT);
	}
	
	public static Matrix add(Matrix a, Matrix b) {
		return operate(a, b, ADD);
	}
	
	public static Matrix subtract(Matrix a, Matrix b) {
		return operate(a, b, SUBTRACT);
	}
	
	public static Matrix multiply(Matrix a, Matrix b) {
		return operate(a, b, MULTIPLY);
	}
	
	public static Matrix crossProduct(Matrix a, Matrix b) {
		return operate(a, b, CROSS_PRODUCT);
	}
	
	public static double dotProduct(Matrix a, Matrix b) {
		return operate(a, b, DOT_PRODUCT);
	}
	
	public static double magnitude(Matrix m) {
		return operate(m, MAGNITUDE);
	}
	
	public static Matrix multiply(Matrix...matracies) {
		Matrix product = matracies[0];
		for (int i = 1; i < matracies.length; i++) {
			product = operate(product, matracies[i], MULTIPLY);
		}
		return product;
	}
	
	/*#####################
	 * Operate methods
	 *#####################*/
	public static Matrix operate(Matrix matrix, double scaleFactor, ScaleOperation<Matrix> operation) {
		return operation.operate(matrix, scaleFactor);
	}
	
	public static Matrix operate(Matrix matrix, SingleOperation<Matrix> operation) {
		return operation.operate(matrix);
	}
	
	public static double operate(Matrix matrix, ScalarSingleOperation<Matrix> operation) {
		if (!operation.correctDimensions(matrix)) {
			throw new IllegalArgumentException("The given matrix is not of the correct dimensions for this opperation");
		}
		return operation.operate(matrix);
	}
	
	public static double operate(Matrix a, Matrix b, ScalarPairOperation<Matrix> operation) {
		if (!operation.correctDimensions(a, b)) {
			throw new IllegalArgumentException("The given matricies are not of the correct dimensions for this opperation");
		}
		return operation.operate(a, b);
	}

	public static Matrix operate(Matrix a, Matrix b, PairOperation<Matrix> operation) {
		if (!operation.correctDimensions(a, b)) {
			throw new IllegalArgumentException("The given matricies are not of the correct dimensions for this opperation");
		}
		Matrix newMatrix = new Matrix(a.getRows(), b.getCols());
		for (int r = 0; r < newMatrix.getRows(); r++) {
			for (int c = 0; c < newMatrix.getCols(); c++) {
				newMatrix.set(r, c, operation.operate(r, c, a, b));
			}
		}
		return newMatrix;
	}

}

/*#####################
 * Operation interfaces
 * and implementing classes
 *#####################*/
interface ScaleOperation<T> {
	T operate(T obj1, double scaleFactor);
}

final class Scale implements ScaleOperation<Matrix> {

	@Override
	public Matrix operate(Matrix matrix, double scaleFactor) {
		Matrix m = matrix.duplicate();
		for (int r = 0; r < matrix.getRows(); r++) {
			for (int c = 0; c < matrix.getCols(); c++) {
				m.set(r, c, m.get(r, c) * scaleFactor);
			}
		}
		return m;
	}

}


interface SingleOperation<T> {
	T operate(T obj1);
}

class RowReduction implements SingleOperation<Matrix> {

	@Override
	public Matrix operate(Matrix matrix) {
		Matrix m = matrix.duplicate();
		for (int row = 0; row < m.getRows(); row++) {
			int startCol = m.firstNonZeroCol(row);
			if (startCol == -1 || startCol == m.getCols() - 1) {
				continue;
			}
			double startColValue = m.get(row, startCol);
			for (int col = startCol; col < m.getCols(); col++) {
				m.set(row, col, m.get(row, col) / startColValue);
			}
			for (int r = 0; r < m.getRows(); r++) {
				if (r != row) {
					double scale = m.get(r, startCol);
					for (int c = startCol; c < m.getCols(); c++) {
						m.set(r, c, m.get(r, c) - (scale * m.get(row, c)));
					}
				}
			}
		}
		return m;
	}
	
}

class Transpose implements SingleOperation<Matrix> {

	@Override
	public Matrix operate(Matrix matrix) {
		Matrix newMatrix = new Matrix(matrix.getCols(), matrix.getRows());
		for (int r = 0; r < matrix.getRows(); r++) {
			for (int c = 0; c < matrix.getCols(); c++) {
				newMatrix.set(c, r, matrix.get(r, c));
			}
		}
		return newMatrix;
	}
	
}

class Inverse implements SingleOperation<Matrix> {

	@Override
	public Matrix operate(Matrix m) {
		//Verify that m is square
		if (!m.isSquare()) {
			notInvertable(m);
		}
		
		//Create augmented matrix: [m|I]
		Matrix augmented = new Matrix(m.getRows(), m.getCols() * 2);
		for (int r = 0; r < augmented.getRows(); r++) {
			for (int c = 0; c < augmented.getRows() / 2; c++) {
				augmented.set(r, c, m.get(r, c));
			}
			augmented.set(r, r + augmented.getRows() / 2, 1);
		}
		
		//Apply row reduction
		Matrix reduced = rowReduction(augmented);
		
		//Get the inverse section of the reduced matrix;
		Matrix inverse = new Matrix(m.getRows(), m.getCols());
		for (int r = 0; r < reduced.getRows(); r++) {
			for (int c = m.getCols(); c < reduced.getRows(); c++) {
				inverse.set(r, c - m.getCols(), reduced.get(r, c));
			}
		}
		
		//Verify that inverse is actually an inverse
		if (!multiply(inverse, m).equals(newIdentityMatrix(m.getRows()))) {
			notInvertable(m);
		}
		
		return inverse;
	}
	
	public static void notInvertable(Matrix m) {
		throw new IllegalArgumentException("The matrix " + m + " is not invertable.");
	}
	
}

class Unit implements SingleOperation<Matrix> {

	@Override
	public Matrix operate(Matrix matrix) {
		if (!matrix.is3DVector()) {
			throw new IllegalArgumentException("Matrix " + matrix + " is not a 3D vector");
		}
		if (matrix.get(0, 0) == 0 && matrix.get(0, 1) == 0 && matrix.get(0, 2) == 0) {
			throw new IllegalArgumentException("Matrix " + matrix + " is the 0 vector and doesn't have a unit");
		}
		return new Scale().operate(matrix, 1.0 / magnitude(matrix));
	}
	
}

interface PairOperation<T> {
	
	public double operate(int row, int col, T a, T b);
	
	public boolean correctDimensions(T a, T b);
	
}

class Add implements PairOperation<Matrix> {

	public double operate(int row, int col, Matrix a, Matrix b) {
		return a.get(row, col) + b.get(row, col);
	}
	
	public boolean correctDimensions(Matrix a, Matrix b) {
		return a.getRows() == b.getRows() && a.getCols() == b.getCols();
	}

}

class Subtract implements PairOperation<Matrix> {

	public double operate(int row, int col, Matrix a, Matrix b) {
		return a.get(row, col) - b.get(row, col);
	}
	
	public boolean correctDimensions(Matrix a, Matrix b) {
		return a.getRows() == b.getRows() && a.getCols() == b.getCols();
	}

}

class Multiply implements PairOperation<Matrix> {

	public double operate(int row, int col, Matrix a, Matrix b) {
		double value = 0;
		for (int i = 0; i < a.getCols(); i++) {
			value += (a.get(row, i) * b.get(i, col));
		}
		return value;
	}
	
	public boolean correctDimensions(Matrix a, Matrix b) {
		return a.getCols() == b.getRows();
	}
}

class CrossProduct implements PairOperation<Matrix> {

	@Override
	public double operate(int row, int col, Matrix a, Matrix b) {
		return a.get(row, plusOne(col)) * b.get(row, minusOne(col)) - a.get(row, minusOne(col)) * b.get(row, plusOne(col));
	}
	
	//We always want our column value to be from 0-2
	private int minusOne(int value) {
		value--;
		if (value < 0) {
			value += 3;
		}
		return value;
	}
	
	private int plusOne(int value) {
		value++;
		if (value > 2) {
			value -= 3;
		}
		return value;
	}

	@Override
	public boolean correctDimensions(Matrix a, Matrix b) {
		return a.is3DVector() && b.is3DVector();
	}
	
}

interface ScalarPairOperation<T> {
	
	public double operate(Matrix a, Matrix b);
	
	public boolean correctDimensions(Matrix a, Matrix b);
}

class DotProduct implements ScalarPairOperation<Matrix> {

	@Override
	public double operate(Matrix a, Matrix b) {
		double result = 0.0;
		for (int c = 0; c < a.getCols(); c++) {
			result += a.get(0, c) * b.get(0, c);
		}
		return result;
	}

	@Override
	public boolean correctDimensions(Matrix a, Matrix b) {
		return a.is3DVector() && b.is3DVector();
	}
	
}

interface ScalarSingleOperation<T> {
	
	public double operate(Matrix m);
	
	public boolean correctDimensions(Matrix m);
}

class Magnitude implements ScalarSingleOperation<Matrix> {

	@Override
	public double operate(Matrix m) {
		double result = 0.0;
		for (int c = 0; c < m.getCols(); c++) {
			result += m.get(0, c) * m.get(0, c);
		}
		return sqrt(result);
	}

	@Override
	public boolean correctDimensions(Matrix m) {
		return m.is3DVector();
	}
	
}
