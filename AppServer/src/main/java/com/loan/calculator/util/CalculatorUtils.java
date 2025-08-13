package com.loan.calculator.util;

public class CalculatorUtils {

	public static double calculateAmortization(double loanValue, int installments) {
		if (installments <= 0) {
			return 0.0;
		}

		return loanValue / installments;
	}

	public static double calculateInterestForPeriod(double balance, double annualRate, long days, int baseDays) {
		if (balance <= 0 || annualRate <= 0 || days <= 0) {
			return 0.0;
		}

		return balance * annualRate * ((double) days / baseDays);
	}
}
