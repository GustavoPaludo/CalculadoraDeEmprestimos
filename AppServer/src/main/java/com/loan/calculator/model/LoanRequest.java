package com.loan.calculator.model;

import java.io.Serializable;
import java.time.LocalDate;

public class LoanRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private LocalDate initialDate;
	private LocalDate endDate;
	private LocalDate firstPaymentDate;
	private Double loanValue;
	private Float interestValue;

	public LocalDate getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(LocalDate initialDate) {
		this.initialDate = initialDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public void setFirstPaymentDate(LocalDate firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	public Double getLoanValue() {
		return loanValue;
	}

	public void setLoanValue(Double loanValue) {
		this.loanValue = loanValue;
	}

	public Float getInterestValue() {
		return interestValue;
	}

	public void setInterestValue(Float interestValue) {
		this.interestValue = interestValue;
	}
}
