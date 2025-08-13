
package com.loan.calculator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loan.calculator.model.LoanRequest;
import com.loan.calculator.model.LoanResult;
import com.loan.calculator.service.CalculatorService;

@RestController
@RequestMapping("/loan")
public class CalculatorController {

	@Autowired
	private CalculatorService calculatorService;

	@PostMapping("/calculate")
	public ResponseEntity<List<LoanResult>> calculate(@RequestBody LoanRequest loanRequest) throws Exception {
		if (loanRequest == null) {
			throw new Exception("Requisição inválida!");
		}

		List<LoanResult> loanResultList = this.calculatorService.calculate(loanRequest);

		return new ResponseEntity<List<LoanResult>>(loanResultList, HttpStatus.OK);
	}
}
