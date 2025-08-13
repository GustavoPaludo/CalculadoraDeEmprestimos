package com.loan.calculator.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class LoanResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date date;
	private double loanValue;
	private double debtorBalance;
	private double consolidated;
	private double total;
	private double amortization;
	private double balance;
	private double provision;
	private double accumulated;
	private double payment;

	public static LoanResult Builder() {
		return new LoanResult();
	}

	public LoanResult date(Date date) {
		this.date = date;
		return this;
	}

	public LoanResult loanValue(double loanValue) {
		this.loanValue = loanValue;
		return this;
	}

	public LoanResult debtorBalance(double debtorBalance) {
		this.debtorBalance = debtorBalance;
		return this;
	}

	public LoanResult consolidated(double consolidated) {
		this.consolidated = consolidated;
		return this;
	}

	public LoanResult total(double total) {
		this.total = total;
		return this;
	}

	public LoanResult amortization(double amortization) {
		this.amortization = amortization;
		return this;
	}

	public LoanResult balance(double balance) {
		this.balance = balance;
		return this;
	}

	public LoanResult provision(double provision) {
		this.provision = provision;
		return this;
	}

	public LoanResult accumulated(double accumulated) {
		this.accumulated = accumulated;
		return this;
	}

	public LoanResult payment(double payment) {
		this.payment = payment;
		return this;
	}

	public Date getDate() {
		return date;
	}

	public double getLoanValue() {
		return loanValue;
	}

	public double getDebtorBalance() {
		return debtorBalance;
	}

	public double getConsolidated() {
		return consolidated;
	}

	public double getTotal() {
		return total;
	}

	public double getAmortization() {
		return amortization;
	}

	public double getBalance() {
		return balance;
	}

	public double getProvision() {
		return provision;
	}

	public double getAccumulated() {
		return accumulated;
	}

	public double getPayment() {
		return payment;
	}
}
