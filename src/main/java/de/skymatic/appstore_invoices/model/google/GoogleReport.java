package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoice;
import de.skymatic.appstore_invoices.model.InvoiceCollection;
import de.skymatic.appstore_invoices.model.InvoiceNumberGenerator;

import java.time.YearMonth;
import java.util.Collection;
import java.util.Comparator;
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

	/**
	 * Creates an empty report with the specified year and month.
	 *
	 * @param billingMonth
	 */
	public GoogleReport(YearMonth billingMonth) {
		this.billingMonth = billingMonth;
		this.reportsOfSubsidiaries = new HashMap<>();
		this.numberGenerator = new InvoiceNumberGenerator(1);
	}

	/**
	 * Creates a report from at least one {@link GoogleSale}.
	 * <p>
	 * Throws IllegalArgumentException, if not all sales belong to the same month.
	 *
	 * @param s
	 * @param furtherSales
	 */
	public GoogleReport(GoogleSale s, GoogleSale... furtherSales) {
		this.billingMonth = YearMonth.from(s.getTransactionDateTime());
		this.reportsOfSubsidiaries = new HashMap<>();
		this.numberGenerator = new InvoiceNumberGenerator(1);

		add(s);
		for (var sale : furtherSales) {
			add(sale);
		}
	}

	public GoogleReport(int numberingSeed, GoogleSale s, GoogleSale... sales) {
		this.billingMonth = YearMonth.from(s.getTransactionDateTime());
		this.reportsOfSubsidiaries = new HashMap<>();
		this.numberGenerator = new InvoiceNumberGenerator(numberingSeed);

		add(s);
		for (var sale : sales) {
			add(sale);
		}
	}

	public GoogleReport(GoogleSale... sales) {
		if (sales.length == 0) {
			throw new IllegalArgumentException("Cannot set subsidiary and billing month: Parameter sales is empty.");
		} else {
			this.billingMonth = YearMonth.from(sales[0].getTransactionDateTime());

			this.reportsOfSubsidiaries = new HashMap<>();
			this.numberGenerator = new InvoiceNumberGenerator(1);

			for (var sale : sales) {
				add(sale);
			}
		}

	}

	public void add(GoogleSale sale) {
		final GoogleSubsidiary subsidiary = sale.getSubsidiary();
		if (reportsOfSubsidiaries.containsKey(subsidiary)) {
			reportsOfSubsidiaries.get(subsidiary).add(sale);
		} else {
			reportsOfSubsidiaries.put(subsidiary, new GoogleSubsidiaryReport(sale));
		}
	}

	public YearMonth getBillingMonth() {
		return billingMonth;
	}

	@Override
	public Collection<Invoice> toInvoices() {
		return reportsOfSubsidiaries.values().stream() //
				.sorted(Comparator.comparingInt(subreport -> subreport.getSubsidiary().ordinal())) //
				.map(r -> {
					Invoice i = r.toInvoice();
					i.setId(String.valueOf(numberGenerator.getAsInt()));
					return i;
				}).collect(Collectors.toUnmodifiableList());
	}
}
