package com.loan.calculator.service;

import java.util.List;

import com.loan.calculator.model.LoanRequest;
import com.loan.calculator.model.LoanResult;

public interface CalculatorService {
    public List<LoanResult> calculate(LoanRequest loanRequest) throws Exception;
}
