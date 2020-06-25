package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoice;
import de.skymatic.appstore_invoices.model.InvoiceNumberGenerator;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Collection of GoogleReportOfSubsidiary, for each subsidiary can only exists one object.
 */
public class GoogleReport {

	private final YearMonth billingMonth;
	private final Map<GoogleSubsidiary, GoogleReportOfSubsidiary> reportsOfSubsidiaries;
	private final InvoiceNumberGenerator numberGenerator;

	public GoogleReport(YearMonth billingMonth, GoogleSaleEntry... sales) {
		this.billingMonth = billingMonth;
		this.reportsOfSubsidiaries = new HashMap<>();
		this.numberGenerator = new InvoiceNumberGenerator(1);

		for (var sale : sales) {
			add(sale);
		}
	}

	public GoogleReport(YearMonth billingMonth, int numberingSeed, GoogleSaleEntry... sales) {
		this.billingMonth = billingMonth;
		this.reportsOfSubsidiaries = new HashMap<>();
		this.numberGenerator = new InvoiceNumberGenerator(numberingSeed);

		for (var sale : sales) {
			add(sale);
		}
	}

	public void add(GoogleSaleEntry sale) {
		GoogleSubsidiary subsidiary = GoogleUtility.mapCountryToSubsidiary(sale.getCountry());
		if (reportsOfSubsidiaries.containsKey(subsidiary)) {
			reportsOfSubsidiaries.get(subsidiary).add(sale);
		} else {
			reportsOfSubsidiaries.put(subsidiary, new GoogleReportOfSubsidiary(billingMonth, subsidiary, sale));
		}
	}

	public YearMonth getBillingMonth() {
		return billingMonth;
	}

	public Collection<Invoice> createInvoices() {
		return reportsOfSubsidiaries.values().stream().map(r -> r.toInvoice()).collect(Collectors.toUnmodifiableList());
	}

	public Set<Invoice> createInvoices(LocalDate issueDate) {
		return null;
	}
}
