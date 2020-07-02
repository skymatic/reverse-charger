package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoicable;
import de.skymatic.appstore_invoices.model.Invoice;
import de.skymatic.appstore_invoices.model.InvoiceItem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GoogleSubsidiaryReport implements Invoicable {

	private final GoogleSubsidiary subsidiary;
	private final YearMonth billingMonth;
	private final Map<String, GoogleProductSubsidiaryReport> salesPerProduct;

	public GoogleSubsidiaryReport(YearMonth billingMonth, GoogleSubsidiary subsidiary){
		this.billingMonth = billingMonth;
		this.subsidiary = subsidiary;
		this.salesPerProduct = new HashMap<>();
	}

	public GoogleSubsidiaryReport(GoogleSale sale) {
		this.billingMonth = YearMonth.from(sale.getTransactionDateTime());
		this.subsidiary = GoogleUtility.mapCountryToSubsidiary(sale.getBuyerCountry());
		this.salesPerProduct = new HashMap<>();
		add(sale);
	}

	public GoogleSubsidiaryReport(GoogleSale... sales) {
		if (sales.length == 0) {
			throw new IllegalArgumentException("Cannot set subsidiary and billing month: Parameter sales is empty.");
		} else {
			this.billingMonth = YearMonth.from(sales[0].getTransactionDateTime());
			this.subsidiary = GoogleUtility.mapCountryToSubsidiary(sales[0].getBuyerCountry());
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
		final var country = sale.getBuyerCountry();
		final var monthYear = YearMonth.from(sale.getTransactionDateTime());
		if (subsidiary != GoogleUtility.mapCountryToSubsidiary(country)) {
			throw new IllegalArgumentException("Sales entry does not belong to sub report of " + subsidiary + ": Wrong country.");
		} else if (billingMonth.getMonth() != monthYear.getMonth() || billingMonth.getYear() != monthYear.getYear()){
			throw new IllegalArgumentException("Sales entry does not belong to sub report of " + subsidiary + ": Wrong Month/Year.");
		} else{
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
		//it's ugly and I know it
		TempContainer tmp = new TempContainer();
		salesPerProduct.values().stream().forEach(sale -> {
			tmp.taxes += sale.getTaxes();
			tmp.fees += sale.getFees();
			tmp.refunds += sale.getRefunds();
			tmp.taxRefunds += sale.getTaxRefunds();
			tmp.feeRefunds += sale.getFeeRefunds();
		});
		Map<String, Double> globalItems = new HashMap<>();
		globalItems.put("TAXES", tmp.taxes);
		globalItems.put("FEES", tmp.fees);
		globalItems.put("REFUNDS", tmp.refunds);
		globalItems.put("TAX_REFUNDS", tmp.taxRefunds);
		globalItems.put("FEE_REFUNDS", tmp.feeRefunds);

		Collection<InvoiceItem> items = new ArrayList<>();
		salesPerProduct.forEach((key, sale) -> items.add(new InvoiceItem(sale.getProductTitle(), sale.getUnits(), sale.getAmount())));

		return new Invoice("TEST", subsidiary, billingMonth.atDay(1), billingMonth.atEndOfMonth(), LocalDate.now(), items, globalItems);
	}

	private class TempContainer {
		double taxes;
		double fees;
		double refunds;
		double taxRefunds;
		double feeRefunds;
	}
}
