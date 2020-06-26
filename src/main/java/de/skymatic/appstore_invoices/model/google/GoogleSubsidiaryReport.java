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

	public GoogleSubsidiaryReport(YearMonth billingMonth, GoogleSale sale) {
		this.billingMonth = billingMonth;
		this.salesPerProduct = new HashMap<>();
		this.subsidiary = GoogleUtility.mapCountryToSubsidiary(sale.getBuyerCountry());
		add(sale);
	}

	public GoogleSubsidiaryReport(YearMonth billingMonth, GoogleSubsidiary subsidiary, GoogleSale... sales) {
		this.billingMonth = billingMonth;
		this.subsidiary = subsidiary;
		this.salesPerProduct = new HashMap<>();

		for (var sale : sales) {
			try {
				add(sale);
			} catch (IllegalArgumentException e) {
				//TODO;
			}
		}
	}

	public void add(GoogleSale sale) throws IllegalArgumentException {
		final var productTitle = sale.getProductTitle();
		final var country = sale.getBuyerCountry();
		if (subsidiary != GoogleUtility.mapCountryToSubsidiary(country)) {
			throw new IllegalArgumentException("Sales entry of productTitle " + productTitle + "does not belong to this subsidiary (" + subsidiary + ").");
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
		return null; //TODO
	}
}
