package de.skymatic.appstore_invoices.model.apple;

import de.skymatic.appstore_invoices.model.Invoice;
import de.skymatic.appstore_invoices.model.InvoiceCollection;
import de.skymatic.appstore_invoices.model.InvoiceNumberGenerator;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Collection of {@link AppleSubsidiaryReport}s of a single month.
 */
public class AppleReport implements InvoiceCollection {

	private static final LocalDate CURRENT_TIME = LocalDate.now();

	private final YearMonth yearMonth;
	private final Map<AppleSubsidiary, AppleSubsidiaryReport> invoices;
	private final InvoiceNumberGenerator invoiceNumberGenerator;

	private String numberPrefix;

	public AppleReport(YearMonth yearMonth, String numberPrefix, int numberingSeed, AppleSalesEntry... sales) {
		this.yearMonth = yearMonth;
		this.numberPrefix = numberPrefix;
		this.invoiceNumberGenerator = new InvoiceNumberGenerator(numberingSeed);
		this.invoices = new Hashtable<>();
		for (var sale : sales) {
			addSalesEntry(sale);
		}
	}

	public void addSalesEntry(AppleSalesEntry appleSalesEntry) {
		AppleSubsidiary appleSubsidiary = AppleUtility.mapRegionPlusCurrencyToSubsidiary(appleSalesEntry.getRpc());
		if (invoices.containsKey(appleSubsidiary)) {
			invoices.get(appleSubsidiary).addSales(appleSalesEntry);
		} else {
			var numberString = numberPrefix + appleSubsidiary.ordinal()+1;
			invoices.put(appleSubsidiary, new AppleSubsidiaryReport(numberString, yearMonth, CURRENT_TIME, appleSalesEntry));
		}
	}

	public Collection<AppleSubsidiaryReport> getInvoices() {
		return Collections.unmodifiableCollection(invoices.values());
	}

	public YearMonth getYearMonth() {
		return yearMonth;
	}

	public int getNextInvoiceNumber() {
		return invoiceNumberGenerator.peek();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(yearMonth).append("\n").append(invoices);
		return sb.toString();
	}

	@Override
	public Collection<Invoice> toInvoices() {
		return invoices.values().stream() //
				.sorted(Comparator.comparingInt(subreport -> subreport.getAppleSubsidiary().ordinal())) //
				.map(subReport -> subReport.toInvoice()) //
				.collect(Collectors.toUnmodifiableList());
	}
}
