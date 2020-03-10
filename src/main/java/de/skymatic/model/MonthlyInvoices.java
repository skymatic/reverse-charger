package de.skymatic.model;

import java.time.YearMonth;
import java.util.Hashtable;
import java.util.Map;

public class MonthlyInvoices {

	private YearMonth yearMonth;
	private Map<Subsidiary, Invoice> invoices;

	public MonthlyInvoices(YearMonth yearMonth, Invoice... invoices) {
		this.yearMonth = yearMonth;
		this.invoices = new Hashtable<>();
		for (var i : invoices) {
			this.invoices.put(i.getSubsidiary(), i);
		}
	}

	public void addEntry(Invoice i) {
		if (invoices.containsKey(i.getSubsidiary())) {
			throw new IllegalArgumentException("Subsidiary already exists!");
		} else {
			invoices.put(i.getSubsidiary(), i);
		}
	}

	public boolean existsSubsidiary(Subsidiary subsidiary) {
		return invoices.containsKey(subsidiary);
	}

}
