package de.skymatic.model;

import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.IntSupplier;

public class MonthlyInvoices {

	private YearMonth yearMonth;
	private Map<Subsidiary, Invoice> invoices;
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

	public boolean existsSubsidiary(Subsidiary subsidiary) {
		return invoices.containsKey(subsidiary);
	}

	public Collection<Invoice> getInvoices() {
		return Collections.unmodifiableCollection(invoices.values());
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
