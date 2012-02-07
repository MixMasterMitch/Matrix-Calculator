package com.mloeppky.matrices.client;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class MatrixBuilder {
	
	private List<List<Double>> matrix;
	
	public MatrixBuilder() {
		matrix = newArrayList();
	}
	
	public MatrixBuilder addCol(double...values) {
		if (matrix.isEmpty()) {
			for (double v : values) {
				matrix.add(newArrayList(v));
			}
		} else {
			if (matrix.size() != values.length) {
				throw new IllegalStateException("This column does not have the same number elements as the previous column or a row was added.");
			}
			for (int i = 0; i < values.length; i++) {
				matrix.get(i).add(values[i]);
			}
		}
		return this;
	}
	
	public MatrixBuilder addRow(double...values) {
		List<Double> newRow = newArrayList();
		for (double v : values) {
			newRow.add(v);
		}
		matrix.add(newRow);
		return this;
	}
	
	public Matrix build() {
		if (matrix == null) {
			return null;
		}
		if (matrix.isEmpty()) {
			return new Matrix(0, 0);
		}
		if (constantColumnCount()) {
			Matrix m = new Matrix(matrix.size(), matrix.get(0).size());
				for (int r = 0; r < matrix.size(); r++)	 {
					for (int c = 0; c < matrix.get(r).size(); c++) {
						m.set(r, c, matrix.get(r).get(c).doubleValue());
					}
				}
			return m;
		}
		throw new IllegalStateException("The rows don't all have the same number of values: " + matrix);
	}

	private boolean constantColumnCount() {
		int prev = matrix.get(0).size();
		for (int i = 1; i < matrix.size(); i++) {
			int cur = matrix.get(i).size();
			if (prev != cur) {
				return false;
			}
			prev = cur;
		}
		return true;
	}
}
