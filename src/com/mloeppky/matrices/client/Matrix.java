package com.mloeppky.matrices.client;

import static java.util.Arrays.deepEquals;

import com.google.common.base.Objects;
import com.google.gwt.i18n.client.NumberFormat;

public class Matrix {
	private double[][] data;
	private String name = "";
	
	//private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("0.00##");
	private static final NumberFormat DOUBLE_FORMAT = NumberFormat.getFormat("0.####");
	
	public Matrix(int rows, int col) {
		data = new double[rows][col];
	}
	
	public Matrix(double[][] array) {
		data = array;
	}
	
	public String getName() {
		return name;
	}

	public Matrix setName(String name) {
		this.name = name;
		return this;
	}

	public int getRows() {
		return data.length;
	}
	
	public int getCols() {
		if (data.length == 0) {
			return 0;
		}
		return data[0].length;
	}
	
	public double get(int row, int col) {
		if (row >= getRows() || col >= getCols() || row < 0 || col < 0) {
			throw new IllegalArgumentException("The given row and col (" + row + ", " + col + ") are outside the matrix");
		}
		return data[row][col];
	}
	
	public String getFormatted(int row, int col) {
		return DOUBLE_FORMAT.format(get(row, col));
	}
	
	public Matrix set(int row, int col, double value) {
		if (row >= getRows() || col >= getCols() || row < 0 || col < 0) {
			throw new IllegalArgumentException("The given row and col (" + row + ", " + col + ") are outside the matrix");
		}
		data[row][col] = value;
		return this;
	}
	
	@Override
	public String toString() {
		if (getRows() < 1 || getCols() < 1) {
			return "[[]]";
		}
		int maxDigits = maxNumberOfDigits(this);
		String s = "[";
		for (int r = 0; r < getRows(); r++) {
			s = s + "[";
			for (int c = 0; c < getCols(); c++) {
				for (int i = maxDigits; i > numberOfDigits(data[r][c]); i--) {
					s = s + " ";
				}
				s = s + DOUBLE_FORMAT.format(data[r][c]);
				if (c != getCols() - 1) {
					s = s + ", ";
				}
			}
			s = s + "]";
			if (r != getRows() - 1) {
				s = s + ", ";
			}
		}
		return s + "]";
	}
	
	public String toStringMultiLine() {
		String[] lines = toString().split("],");
		if (lines.length > 1) {
			String s = "";
			for (String line : lines) {
				s += line + "],\n";
			}
			return s.substring(0, s.length() - 3);
		}
		else {
			return lines[0];
		}
	}
	
	public boolean equals(Object other) {
		if (other instanceof Matrix) {
			return deepEquals(this.data, ((Matrix) other).data);
		}
		return false;
	}
	
	public int HashCode() {
		return Objects.hashCode(data);
	}
	
	private static int numberOfDigits(double d) {
		return DOUBLE_FORMAT.format(d).length();
	}
	
	private static int maxNumberOfDigits(Matrix m) {
		int max = 0;
		for (int r = 0; r < m.getRows(); r++) {
			for (int c = 0; c < m.getCols(); c++) {
				if (numberOfDigits(m.data[r][c]) > max) {
					max = numberOfDigits(m.data[r][c]);
				}
			}
		}
		return max;
	}
	
	public Matrix duplicate() {
		double[][] newArray = new double[getRows()][getCols()];
		for (int r = 0; r < getRows(); r++) {
			for (int c = 0; c < getCols(); c++) {
				newArray[r][c] = get(r, c);
			}
		}
		return new Matrix(newArray);
	}
	
	public boolean isEchelonForm() {
		for (int r = 0; r < getRows(); r++) {
			int startCol = firstNonZeroCol(r);
			if (startCol != -1 
					&& startCol < getCols() - 1 
					&& get(r, startCol) != 1) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isReducedEchelonForm() {
		if (!isEchelonForm()) {
			return false;
		}
		for (int r = 0; r < getRows(); r++) {
			int startCol = firstNonZeroCol(r);
			if (startCol != -1 && startCol < getCols() - 1) {
				for (int row = 0; row < getRows(); row++) {
					if (get(row, startCol) != 0 && row != r) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static Matrix newIdentityMatrix(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("The given size can't be less than 0");
		}
		Matrix m = new Matrix(size, size);
		for (int i = 0; i < size; i++) {
			m.set(i, i, 1);
		}
		return m;
	}
	
	public int firstNonZeroCol(int row) {
		for (int col = 0; col < getCols(); col++) {
			if (get(row, col) != 0) {
				return col;
			}
		}
		return -1;
	}
	
	public boolean is3DVector() {
		return getRows() == 1 && getCols() == 3;
	}
	
	public boolean isSquare() {
		return getRows() == getCols();
	}
	
	
}


