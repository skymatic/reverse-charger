package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoicable;
import de.skymatic.appstore_invoices.model.Invoice;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class GoogleSubsidiaryReport implements Invoicable {

	private final GoogleSubsidiary subsidiary;
	private final YearMonth billingMonth;
	private final Map<String, GoogleProductSubsidiaryReport> salesPerProduct;

	public GoogleSubsidiaryReport(YearMonth billingMonth, GoogleSubsidiary subsidiary) {
		this.billingMonth = billingMonth;
		this.subsidiary = subsidiary;
		this.salesPerProduct = new HashMap<>();
	}

	public GoogleSubsidiaryReport(GoogleSale sale) {
		this.billingMonth = YearMonth.from(sale.getTransactionDateTime());
		this.subsidiary = sale.getSubsidiary();
		this.salesPerProduct = new HashMap<>();
		add(sale);
	}

	public GoogleSubsidiaryReport(GoogleSale... sales) {
		if (sales.length == 0) {
			throw new IllegalArgumentException("Cannot set subsidiary and billing month: Parameter sales is empty.");
		} else {
			this.billingMonth = YearMonth.from(sales[0].getTransactionDateTime());
			this.subsidiary = sales[0].getSubsidiary();
			this.salesPerProduct = new HashMap<>();

			for (var sale : sales) {
				add(sale);
			}
		}
	}

	/**
	 * Adds a sale entry to this report.
	 *
	 * @param sale
	 * @throws IllegalArgumentException if either the country does not belong to the reports subsidiary or the transaction time does not match the month if the report
	 */
	public void add(GoogleSale sale) throws IllegalArgumentException {
		final var productTitle = sale.getProductTitle();
		final var monthYear = YearMonth.from(sale.getTransactionDateTime());
		if (subsidiary != sale.getSubsidiary()) {
			throw new IllegalArgumentException("Sales entry does not belong to sub report of " + subsidiary + ": Wrong subsidiary.");
		} else if (billingMonth.getMonth() != monthYear.getMonth() || billingMonth.getYear() != monthYear.getYear()) {
			throw new IllegalArgumentException("Sales entry does not belong to sub report of " + billingMonth + ": Wrong Month/Year.");
		} else {
			if (salesPerProduct.containsKey(productTitle)) {
				salesPerProduct.get(productTitle).update(sale);
			} else {
				salesPerProduct.put(productTitle, new GoogleProductSubsidiaryReport(sale));
			}
		}
	}


	public GoogleSubsidiary getSubsidiary() {
		return subsidiary;
	}

	public YearMonth getBillingMonth() {
		return billingMonth;
	}

	@Override
	public Invoice toInvoice() {
		return GoogleSubsidiaryReportInvoicer.createInvoiceFrom(subsidiary, billingMonth, salesPerProduct);
	}
}
