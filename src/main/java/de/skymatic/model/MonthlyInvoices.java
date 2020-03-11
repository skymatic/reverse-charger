package de.skymatic.model;

import java.time.YearMonth;
import java.util.Hashtable;
import java.util.Map;

public class MonthlyInvoices {

	private YearMonth yearMonth;
	private Map<Subsidiary, Invoice> invoices;

	public MonthlyInvoices(YearMonth yearMonth, SalesEntry... sales) {
		this.yearMonth = yearMonth;
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
			invoices.put(subsidiary, new Invoice(salesEntry));
		}
	}

	public boolean existsSubsidiary(Subsidiary subsidiary) {
		return invoices.containsKey(subsidiary);
	}

}
