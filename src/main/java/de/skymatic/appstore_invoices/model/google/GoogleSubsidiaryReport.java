package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoicable;
import de.skymatic.appstore_invoices.model.Invoice;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class GoogleSubsidiaryReport implements Invoicable {

	private final GoogleSubsidiary subsidiary;
	private final YearMonth billingMonth;
	private final Map<String, GoogleSaleEntry> salesPerProduct;

	public GoogleSubsidiaryReport(YearMonth billingMonth, GoogleSaleEntry sale) {
		this.billingMonth = billingMonth;
		this.salesPerProduct = new HashMap<>();
		this.subsidiary = GoogleUtility.mapCountryToSubsidiary(sale.getCountry());
		add(sale);
	}

	public GoogleSubsidiaryReport(YearMonth billingMonth, GoogleSubsidiary subsidiary, GoogleSaleEntry... sales) {
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


	public void add(GoogleSaleEntry sale) throws IllegalArgumentException {
		final var productTitle = sale.getProductTitle();
		final var country = sale.getCountry();
		if (salesPerProduct.containsKey(productTitle)) {
			throw new IllegalArgumentException("Product " + productTitle + " already exists.");
		} else if (subsidiary != GoogleUtility.mapCountryToSubsidiary(country)) {
			throw new IllegalArgumentException("Sales entry of productTitle " + productTitle + "does not belong to this subsidiary (" + subsidiary + ").");
		} else {
			salesPerProduct.put(productTitle, sale);
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
