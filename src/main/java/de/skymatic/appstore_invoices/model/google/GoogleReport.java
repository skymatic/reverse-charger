package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoice;
import de.skymatic.appstore_invoices.model.InvoiceCollection;
import de.skymatic.appstore_invoices.model.InvoiceNumberGenerator;

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Collection of {@link GoogleSubsidiaryReport}s of a single month. For each subsidiary only one report can exist.
 */
public class GoogleReport implements InvoiceCollection {

	private final YearMonth billingMonth;
	private final Map<GoogleSubsidiary, GoogleSubsidiaryReport> reportsOfSubsidiaries;
	private final InvoiceNumberGenerator numberGenerator;

	public GoogleReport(YearMonth billingMonth, GoogleSale... sales) {
		this.billingMonth = billingMonth;
		this.reportsOfSubsidiaries = new HashMap<>();
		this.numberGenerator = new InvoiceNumberGenerator(1);

		for (var sale : sales) {
			add(sale);
		}
	}

	public GoogleReport(YearMonth billingMonth, int numberingSeed, GoogleSale... sales) {
		this.billingMonth = billingMonth;
		this.reportsOfSubsidiaries = new HashMap<>();
		this.numberGenerator = new InvoiceNumberGenerator(numberingSeed);

		for (var sale : sales) {
			add(sale);
		}
	}

	public void add(GoogleSale sale) {
		GoogleSubsidiary subsidiary = GoogleUtility.mapCountryToSubsidiary(sale.getBuyerCountry());
		if (reportsOfSubsidiaries.containsKey(subsidiary)) {
			reportsOfSubsidiaries.get(subsidiary).add(sale);
		} else {
			reportsOfSubsidiaries.put(subsidiary, new GoogleSubsidiaryReport(billingMonth, subsidiary, sale));
		}
	}

	public YearMonth getBillingMonth() {
		return billingMonth;
	}

	@Override
	public Collection<Invoice> toInvoices() {
		return reportsOfSubsidiaries.values().stream().map(r -> r.toInvoice()).collect(Collectors.toUnmodifiableList());
	}
}
