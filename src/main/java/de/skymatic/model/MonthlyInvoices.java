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
	private final String numberPrefix;
	private final Map<Subsidiary, Invoice> invoices;
	private final IntSupplier invoiceNumberGenerator;

	public MonthlyInvoices(YearMonth yearMonth, String numberPrefix, int numberingSeed, SalesEntry... sales) {
		this.yearMonth = yearMonth;
		this.numberPrefix = numberPrefix;
		this.invoiceNumberGenerator = new IntSupplier() {
			private int current = numberingSeed;

			@Override
			public int getAsInt() {
				var old = current;
				current += 1;
				return old;
			}
		};
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

	public void changeInvoiceNumber(Subsidiary subsidiary, String invoiceNumber) {
		if (invoices.containsKey(subsidiary)) {
			invoices.get(subsidiary).setNumberString(invoiceNumber);
		} else {
			throw new IllegalArgumentException("Invoice for Subsidiary does not exists.");
		}
	}

	public void changeIssueDate(Subsidiary subsidiary, LocalDate issueDate) {
		if (invoices.containsKey(subsidiary)) {
			invoices.get(subsidiary).setIssueDate(issueDate);
		} else {
			throw new IllegalArgumentException("Invoice for Subsidiary does not exists.");
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(yearMonth)
				.append("\n")
				.append(invoices);
		return sb.toString();
	}

}
