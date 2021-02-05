package de.skymatic.appstore_invoices.model.apple;

import de.skymatic.appstore_invoices.model.Invoicable;
import de.skymatic.appstore_invoices.model.SalesReport;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

/**
 * Collection of {@link AppleSubsidiaryReport}s of a single month.
 */
public class AppleReport implements SalesReport {

	private static final LocalDate CURRENT_TIME = LocalDate.now();

	private final YearMonth billingMonth;
	private final Map<AppleSubsidiary, AppleSubsidiaryReport> subReports;

	public AppleReport(YearMonth billingMonth, AppleSalesEntry... sales) {
		this.billingMonth = billingMonth;
		this.subReports = new Hashtable<>();
		for (var sale : sales) {
			addSalesEntry(sale);
		}
	}

	public void addSalesEntry(AppleSalesEntry appleSalesEntry) {
		var appleSubsidiary = AppleSubsidiary.mapFromRegionNCurrency(appleSalesEntry.getRpc());
		if (subReports.containsKey(appleSubsidiary)) {
			subReports.get(appleSubsidiary).addSales(appleSalesEntry);
		} else {
			subReports.put(appleSubsidiary, new AppleSubsidiaryReport(billingMonth, CURRENT_TIME, appleSalesEntry.getBankAccountCurrency(), appleSalesEntry));
		}
	}

	public Collection<AppleSubsidiaryReport> getSubReports() {
		return Collections.unmodifiableCollection(subReports.values());
	}

	public YearMonth getBillingMonth() {
		return billingMonth;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(billingMonth).append("\n").append(subReports);
		return sb.toString();
	}

	@Override
	public Collection<? extends Invoicable> getInvoicables() {
		return subReports.values();
	}

}
