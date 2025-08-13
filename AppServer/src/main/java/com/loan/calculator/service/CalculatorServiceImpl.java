package com.loan.calculator.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.loan.calculator.model.LoanRequest;
import com.loan.calculator.model.LoanResult;
import com.loan.calculator.util.CalculatorUtils;
import com.loan.calculator.util.DateUtils;

@Service
public class CalculatorServiceImpl implements CalculatorService {

	private static final int BASE_DAYS = 360;

	@Override
	public List<LoanResult> calculate(LoanRequest loanRequest) throws Exception {
		this.validateNullFields(loanRequest);

		LocalDate initialDate = loanRequest.getInitialDate();
		LocalDate endDate = loanRequest.getEndDate();
		LocalDate firstPayment = loanRequest.getFirstPaymentDate();

		if(endDate.isBefore(initialDate)) {
			throw new Exception("A data final não pode ser anterior a data inicial");
		}

		if(firstPayment.isBefore(initialDate) || firstPayment.isAfter(endDate)) {
			throw new Exception("A data do primeiro pagamento não pode ser anterior a data inicial e nem posterior a data final.");
		}

		double loanValue = loanRequest.getLoanValue();
		double annualRate = loanRequest.getInterestValue() / 100.0;

		if(loanValue <= 0 || annualRate <= 0) {
			throw new Exception("O valor do empréstimo e da taxa de juros não podem ser menor ou igual a zero.");
		}

		List<LocalDate> schedule = this.generateSchedule(initialDate, endDate, firstPayment);

		List<LocalDate> paymentDates = this.generatePaymentDates(firstPayment, endDate);
		int installments = paymentDates.size();

		double amortization = CalculatorUtils.calculateAmortization(loanValue, installments);

		double outstandingBalance = loanValue;
		double accumulatedProvision = 0.0;

		LocalDate previousDate = initialDate.minusDays(1);
		List<LoanResult> results = new ArrayList<>();

		LoanResult initialRow = this.buildRow(initialDate, loanValue);
		results.add(initialRow);

		for (int i = 1; i < schedule.size(); i++) {
			LocalDate current = schedule.get(i);
			long days = DateUtils.daysBetweenDates(previousDate, current);
			double interestPeriod = CalculatorUtils.calculateInterestForPeriod(outstandingBalance, annualRate, days, BASE_DAYS);

			boolean isPayment = DateUtils.isPaymentDate(current, paymentDates, endDate);

			LoanResult result = LoanResult.Builder().date(DateUtils.toDate(current)).loanValue(0.0).debtorBalance(outstandingBalance);

			if (!isPayment) {
				accumulatedProvision += interestPeriod;

				result.consolidated(0.0).total(0.0).amortization(0.0).balance(outstandingBalance).provision(interestPeriod).accumulated(accumulatedProvision).payment(0.0);
			} else {
				double provisionThisPeriod = interestPeriod;
				double totalInterestToPay = accumulatedProvision + provisionThisPeriod;
				double totalPayment = amortization + totalInterestToPay;

				outstandingBalance -= amortization;

				if (outstandingBalance < 1e-8) {					
					outstandingBalance = 0.0;
				}

				result.consolidated((double) DateUtils.getPaymentIndex(current, paymentDates) + 1).total(totalPayment).amortization(amortization).balance(outstandingBalance).provision(provisionThisPeriod).accumulated(0.0).payment(totalInterestToPay);

				accumulatedProvision = 0.0;
			}

			result.debtorBalance(outstandingBalance);
			results.add(result);

			previousDate = current;
		}

		return results;
	}

	private void validateNullFields(Object object) throws Exception {
		for (Field field : object.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (field.get(object) == null) {
				throw new Exception("Campo obrigatório não informado: " + field.getName());
			}
		}
	}

	private LoanResult buildRow(LocalDate initial, double loanValue) {
		LoanResult result = LoanResult.Builder().date(DateUtils.toDate(initial)).loanValue(loanValue).debtorBalance(loanValue).consolidated(0.0).total(0.0).amortization(0.0).balance(loanValue).provision(0.0).accumulated(0.0).payment(0.0);

		return result;
	}

	private List<LocalDate> generateSchedule(LocalDate initialDate, LocalDate endDate, LocalDate firstPayment) {
	    Set<LocalDate> dates = new TreeSet<>();

	    dates.add(initialDate);

	    LocalDate lastDayInitialMonth = initialDate.with(TemporalAdjusters.lastDayOfMonth());
	    if (!lastDayInitialMonth.isBefore(initialDate) && !lastDayInitialMonth.isAfter(endDate)) {
	        dates.add(lastDayInitialMonth);
	    }

	    LocalDate cursor = initialDate.withDayOfMonth(1).plusMonths(1);
	    while (!cursor.isAfter(endDate)) {
	        LocalDate lastDay = cursor.with(TemporalAdjusters.lastDayOfMonth());

	        if (!lastDay.isBefore(initialDate) && !lastDay.isAfter(endDate)) {
	            dates.add(lastDay);
	        }

	        cursor = cursor.plusMonths(1);
	    }

	    dates.addAll(this.generatePaymentDates(firstPayment, endDate));
	    dates.add(endDate);

	    return new ArrayList<>(dates);
	}

	private List<LocalDate> generatePaymentDates(LocalDate firstPayment, LocalDate endDate) {
	    List<LocalDate> dates = new ArrayList<>();

	    LocalDate firstPaymentDate = firstPayment;
	    int monthCounter = 0;

	    while (!firstPaymentDate.isAfter(endDate)) {
	        dates.add(DateUtils.adjustPaymentDate(firstPaymentDate));
	        monthCounter++;
	        firstPaymentDate = DateUtils.addMonthsKeepDayOrLastDay(firstPayment, monthCounter);
	    }

	    Collections.sort(dates);

	    return dates;
	}
}
