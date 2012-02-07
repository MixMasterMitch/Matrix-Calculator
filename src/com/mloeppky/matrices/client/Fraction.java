package com.mloeppky.matrices.client;

public class Fraction {
	int numerator;
	int denominator;
	
	public Fraction(int n, int d) {
		if (d == 0) {
			throw new IllegalArgumentException("You can't have a 0 denominator");
		}
		if (d < 0) {
			n *= -1;
			d *= -1;
		}
		numerator = n;
		denominator = d;
		reduce(this);
	}
	
	public String toString() {
		String s = "" + numerator;
		if (denominator != 1) {
			s = s + "/" + denominator;
		}
		return s;
	}
	
	public void add(Fraction other) {
		int otherN = other.numerator;
		int otherD = other.denominator;
		this.numerator *= otherD;
		otherN *= this.denominator;
		this.denominator *= otherD;
		otherD = this.denominator;
		this.numerator += otherN;
		reduce(this);
	}

	public static void reduce(Fraction f) {
		for (int i = Math.min(f.numerator, f.denominator); i > 1; i--) {
			if (f.numerator % i == 0 && f.denominator % i == 0) {
				f.numerator /= i;
				f.denominator /= i;
				reduce(f);
				return;
			}
		}
	}

}
