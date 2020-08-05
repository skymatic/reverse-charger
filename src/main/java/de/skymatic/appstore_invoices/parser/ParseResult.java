package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.apple.AppleSalesEntry;

import java.time.YearMonth;
import java.util.Collection;

public class ParseResult {

	private final YearMonth yearMonth;
	private final Collection<AppleSalesEntry> sales;

	public ParseResult(YearMonth yearMonth, Collection<AppleSalesEntry> sales) {
		this.yearMonth = yearMonth;
		this.sales = sales;
	}

	public YearMonth getYearMonth() {
		return yearMonth;
	}

	public Collection<AppleSalesEntry> getSales() {
		return sales;
	}

}
