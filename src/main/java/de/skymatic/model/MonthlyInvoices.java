package de.skymatic.model;

import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.IntSupplier;

public class MonthlyInvoices {

	private final YearMonth yearMonth;
	private final Map<Subsidiary, Invoice> invoices;
	private final IntSupplier invoiceNumberGenerator;

	public MonthlyInvoices(YearMonth yearMonth, int numberingSeed, SalesEntry... sales) {
		this.yearMonth = yearMonth;
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
			invoices.put(subsidiary, new Invoice(invoiceNumberGenerator.getAsInt(), salesEntry));
		}
	}

	public Collection<Invoice> getInvoices() {
		return Collections.unmodifiableCollection(invoices.values());
	}

	public void setInvoiceNumber(Subsidiary subsidiary, int number) {
		if (invoices.containsKey(subsidiary)) {
			invoices.get(subsidiary).setNumber(number);
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
