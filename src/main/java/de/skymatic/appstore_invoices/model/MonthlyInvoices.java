package de.skymatic.appstore_invoices.model;

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
	private final Map<AppleSubsidiary, AppleInvoice> invoices;
	private final InvoiceNumberGenerator invoiceNumberGenerator;

	private String numberPrefix;

	public MonthlyInvoices(YearMonth yearMonth, String numberPrefix, int numberingSeed, SalesEntry... sales) {
		this.yearMonth = yearMonth;
		this.numberPrefix = numberPrefix;
		this.invoiceNumberGenerator = new InvoiceNumberGenerator(numberingSeed);
		this.invoices = new Hashtable<>();
		for (var sale : sales) {
			addSalesEntry(sale);
		}
	}

	public void addSalesEntry(SalesEntry salesEntry) {
		AppleSubsidiary appleSubsidiary = AppleUtility.mapRegionPlusCurrencyToSubsidiary(salesEntry.getRpc());
		if (invoices.containsKey(appleSubsidiary)) {
			invoices.get(appleSubsidiary).addSales(salesEntry);
		} else {
			var numberString = numberPrefix + String.valueOf(invoiceNumberGenerator.getAsInt());
			invoices.put(appleSubsidiary, new AppleInvoice(numberString, yearMonth, CURRENT_TIME, salesEntry));
		}
	}

	public Collection<AppleInvoice> getInvoices() {
		return Collections.unmodifiableCollection(invoices.values());
	}

	public YearMonth getYearMonth() {
		return yearMonth;
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
