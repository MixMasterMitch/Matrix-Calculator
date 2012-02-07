package com.mloeppky.matrices.client;

import static com.mloeppky.matrices.client.MatrixOperations.add;
import static com.mloeppky.matrices.client.MatrixOperations.multiply;
import static com.mloeppky.matrices.client.MatrixOperations.subtract;
import static com.mloeppky.matrices.client.MatrixOperations.transpose;
import static java.lang.Math.sqrt;

public class MatrixFun {

	public static void main(String[] args) {
		Matrix a = new MatrixBuilder()
			.addRow(1, 2)
			.addRow(3, 4)
			.build();
		
		Matrix b = new MatrixBuilder()
			.addRow(1, 3)
			.addRow(1, 4)
			.build();
		
		Matrix c = new MatrixBuilder()
			.addRow(2, 3)
			.addRow(4, 5)
			.build();
		
		Matrix v = new MatrixBuilder()
			.addRow(-1.0/sqrt(6)+6.0/sqrt(161), -6.0/sqrt(161), 4.0/sqrt(45), 0)
			.addRow(2.0/sqrt(6)+10.0/sqrt(161), 10.0/sqrt(161), 5.0/sqrt(45), 3000)
			.addRow(1.0/sqrt(6)+5.0/sqrt(161), -5.0/sqrt(161), -2.0/sqrt(45), 0)
			.build();
		
		Matrix p = new MatrixBuilder()
			.addRow(-.941, .131, .314)
			.build();
		
		Matrix n = new MatrixBuilder()
			.addRow(.231, .923, .308)
			.build();

		System.out.println(subtract(multiply(a, a), multiply(b, b)).toStringMultiLine());
		System.out.println();
		
		System.out.println(transpose(subtract(c, b)).toStringMultiLine());
		System.out.println();
		
		
		System.out.println(v);
		System.out.println();
		Matrix reduced = MatrixOperations.rowReduction(v);
		System.out.println(reduced.toStringMultiLine());

		System.out.println("Is echelon " + reduced.isEchelonForm());
		System.out.println("Is reduced echelon " + reduced.isReducedEchelonForm());
		
	}

}
