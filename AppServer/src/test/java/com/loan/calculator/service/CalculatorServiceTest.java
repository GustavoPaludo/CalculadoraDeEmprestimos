package com.loan.calculator.service;

import com.loan.calculator.model.LoanRequest;
import com.loan.calculator.model.LoanResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorServiceTest {

	private CalculatorServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new CalculatorServiceImpl();
	}

	private LoanRequest createRequest(LocalDate initial, LocalDate end, LocalDate firstPayment, double loanValue, float interest) {
		LoanRequest request = new LoanRequest();
		request.setInitialDate(initial);
		request.setEndDate(end);
		request.setFirstPaymentDate(firstPayment);
		request.setLoanValue(loanValue);
		request.setInterestValue(interest);
		return request;
	}

	@Test
	void testMultipleInstallments() throws Exception {
		LoanRequest request = createRequest(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 8, 31), LocalDate.of(2025, 1, 31), 1000.0, 12.0f);

		List<LoanResult> results = service.calculate(request);

		assertFalse(results.isEmpty());
		assertEquals(0.0, results.get(results.size() - 1).getBalance(), 0.001);
		assertTrue(results.size() > 2);
	}

	@Test
	void testSinglePaymentAtEnd() throws Exception {
		LoanRequest request = createRequest(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 31), LocalDate.of(2025, 1, 31), 500.0, 10.0f);

		List<LoanResult> results = service.calculate(request);

		assertEquals(2, results.size());
		assertEquals(0.0, results.get(results.size() - 1).getBalance(), 0.001);
	}

	@Test
	void testFirstPaymentMidMonth() throws Exception {
		LoanRequest request = createRequest(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 4, 30), LocalDate.of(2025, 2, 15), 2000.0, 8.0f);

		List<LoanResult> results = service.calculate(request);

		assertTrue(results.stream().anyMatch(r -> r.getPayment() > 0.0));
		assertEquals(0.0, results.get(results.size() - 1).getBalance(), 0.001);
	}

	@Test
	void testInitialDateAfterFirstPayment() throws Exception {
		LoanRequest request = this.createRequest(LocalDate.of(2025, 2, 2), LocalDate.of(2025, 2, 15), LocalDate.of(2025, 2, 1), 1000.0, 5.0f);

		try {
			this.service.calculate(request);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "A data do primeiro pagamento não pode ser anterior a data inicial e nem posterior a data final.");
		}
	}

	@Test
	void testEndDateBeforeFirstPayment() throws Exception {
		LoanRequest request = this.createRequest(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 15), LocalDate.of(2025, 2, 1), 1000.0, 5.0f);

		try {
			this.service.calculate(request);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "A data do primeiro pagamento não pode ser anterior a data inicial e nem posterior a data final.");
		}
	}

	@Test
	void testEndDateBeforeInitialDate() throws Exception {
		LoanRequest request = this.createRequest(LocalDate.of(2025, 1, 1), LocalDate.of(2024, 1, 15), LocalDate.of(2025, 1, 2), 1000.0, 5.0f);

		try {
			this.service.calculate(request);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "A data final não pode ser anterior a data inicial");
		}
	}

	@Test
	void testWrongLoanValue() throws Exception {
		LoanRequest request = this.createRequest(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 15), LocalDate.of(2025, 1, 2), -1.0, 5.0f);

		try {
			this.service.calculate(request);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "O valor do empréstimo e da taxa de juros não podem ser menor ou igual a zero.");
		}
	}

	@Test
	void testWrongInterestValue() throws Exception {
		LoanRequest request = this.createRequest(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 15), LocalDate.of(2025, 1, 2), 1000.0, -1f);

		try {
			this.service.calculate(request);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "O valor do empréstimo e da taxa de juros não podem ser menor ou igual a zero.");
		}
	}

	@Test
	void testZeroInterestRate() throws Exception {
		LoanRequest request = createRequest(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 8, 31), LocalDate.of(2025, 1, 31), 900.0, 0.0f);

		try {
			this.service.calculate(request);
		} catch (Exception e) {
			assertEquals(e.getMessage(), "O valor do empréstimo e da taxa de juros não podem ser menor ou igual a zero.");
		}
	}

	@Test
	void testSameDayPayments() throws Exception {
		LoanRequest request = createRequest(LocalDate.of(2025, 1, 15), LocalDate.of(2025, 4, 15), LocalDate.of(2025, 1, 15), 1500.0, 7.0f);

		List<LoanResult> results = service.calculate(request);

		assertEquals(LocalDate.of(2025, 1, 15),	results.get(0).getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
		assertEquals(0.0, results.get(results.size() - 1).getBalance(), 0.001);
	}
}
