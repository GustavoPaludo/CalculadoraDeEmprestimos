package com.loan.calculator.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class DateUtils {

	public static long daysBetweenDates(LocalDate a, LocalDate b) {
		return ChronoUnit.DAYS.between(a, b);
	}

	public static Date toDate(LocalDate ld) {
		if (ld == null) {
			return null;
		}

		return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate addMonthsKeepDayOrLastDay(LocalDate date, int monthsToAdd) {
		int day = date.getDayOfMonth();

		LocalDate target = date.plusMonths(monthsToAdd);

		int lastDayOfTargetMonth = YearMonth.from(target).lengthOfMonth();

		if (day > lastDayOfTargetMonth) {
			return target.withDayOfMonth(lastDayOfTargetMonth);
		}

		return target.withDayOfMonth(day);
	}

	public static LocalDate adjustPaymentDate(LocalDate date) {
		int day = date.getDayOfMonth();

		YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());

		int last = yearMonth.lengthOfMonth();

		if (day > last) {
			return LocalDate.of(date.getYear(), date.getMonth(), last);
		}

		return date;
	}

	public static boolean isPaymentDate(LocalDate date, List<LocalDate> paymentDates, LocalDate finalDate) {
		if (date == null) {
			return false;
		}

		return paymentDates.contains(date) || date.equals(finalDate);
	}

	public static int getPaymentIndex(LocalDate date, List<LocalDate> paymentDates) {
		return paymentDates.indexOf(date);
	}
}
