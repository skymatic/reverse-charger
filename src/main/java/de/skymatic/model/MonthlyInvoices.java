package de.skymatic.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.IntSupplier;

public class MonthlyInvoices {

	private static final LocalDate CURRENT_TIME = LocalDate.now();

	private final YearMonth yearMonth;
	private final Map<Subsidiary, Invoice> invoices;
	private final InvoiceNumberGenerator invoiceNumberGenerator;

	private String numberPrefix;

	public MonthlyInvoices(YearMonth yearMonth, String numberPrefix, int numberingSeed, SalesEntry... sales) {
		//TODO: change numberingSeed to optional such that one can decide to use automatic numbering
		// 	and check, if  numberStringPrefix needs to be refactored to!
		this.yearMonth = yearMonth;
		this.numberPrefix = numberPrefix;
		this.invoiceNumberGenerator = new InvoiceNumberGenerator(numberingSeed);
		this.invoices = new Hashtable<>();
		for (var sale : sales) {
			addSalesEntry(sale);
		}
	}

	public void addSalesEntry(SalesEntry salesEntry) {
		Subsidiary subsidiary = AppleUtility.mapRegionPlusCurrencyToSubsidiary(salesEntry.getRpc());
		if (invoices.containsKey(subsidiary)) {
			invoices.get(subsidiary).addSales(salesEntry);
		} else {
			var numberString = numberPrefix + String.valueOf(invoiceNumberGenerator.getAsInt());
			invoices.put(subsidiary, new Invoice(numberString, yearMonth, CURRENT_TIME, salesEntry));
		}
	}

	public Collection<Invoice> getInvoices() {
		return Collections.unmodifiableCollection(invoices.values());
	}

	public YearMonth getYearMonth() {
		return yearMonth;
	}

	public void changeSingleInvoiceNumber(Subsidiary subsidiary, String invoiceNumber) {
		if (invoices.containsKey(subsidiary)) {
			invoices.get(subsidiary).setNumberString(invoiceNumber);
		} else {
			throw new IllegalArgumentException("Invoice for Subsidiary does not exists.");
		}
	}

	public void changeSingleIssueDate(Subsidiary subsidiary, LocalDate issueDate) {
		if (invoices.containsKey(subsidiary)) {
			invoices.get(subsidiary).setIssueDate(issueDate);
		} else {
			throw new IllegalArgumentException("Invoice for Subsidiary does not exists.");
		}
	}

	public int getNextInvoiceNumber() {
		return invoiceNumberGenerator.next;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(yearMonth)
				.append("\n")
				.append(invoices);
		return sb.toString();
	}

	private class InvoiceNumberGenerator implements IntSupplier {

		int next;

		InvoiceNumberGenerator(int seed) {
			next = seed;
		}

		@Override
		public int getAsInt() {
			var current = next;
			next += 1;
			return current;
		}
	}
}
