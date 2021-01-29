package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model2.AdditionalItem;
import de.skymatic.appstore_invoices.model2.Invoicable;
import de.skymatic.appstore_invoices.model2.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GoogleInvoicer {

	private static final String TAX_DESC = "Tax Withheld";
	private static final String TAXREFUND_DESC = "Tax Refunds";
	private static final String FEE_DESC = "Google Fees";
	private static final String REFUNDS_DESC = "Refunds";

	static Invoice mergeProductsAndCreateInvoiceFrom(GoogleSubsidiary subsidiary, YearMonth billingMonth, String currency, Collection<GoogleProductSubsidiaryReport> productSubReports) {
		//if there is only one product, use this description
		String soldUnitsDescription = "MyApp";
		if(productSubReports.size() == 1){
			soldUnitsDescription = productSubReports.stream().findAny().map(GoogleProductSubsidiaryReport::getProductTitle).orElse("MyApp");
		}

		int units = productSubReports.stream().mapToInt(GoogleProductSubsidiaryReport::getUnits).sum();
		BigDecimal sales = productSubReports.stream().map(GoogleProductSubsidiaryReport::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
		BigDecimal tax = productSubReports.stream().map(GoogleProductSubsidiaryReport::getTaxes).reduce(BigDecimal.ZERO,BigDecimal::add);
		BigDecimal taxRefunds = productSubReports.stream().map(GoogleProductSubsidiaryReport::getTaxRefunds).reduce(BigDecimal.ZERO,BigDecimal::add);
		BigDecimal fees = productSubReports.stream().map(GoogleProductSubsidiaryReport::getFees).reduce(BigDecimal.ZERO,BigDecimal::add);
		BigDecimal feeRefunds = productSubReports.stream().map(GoogleProductSubsidiaryReport::getFeeRefunds).reduce(BigDecimal.ZERO,BigDecimal::add);
		BigDecimal refunds = productSubReports.stream().map(GoogleProductSubsidiaryReport::getRefunds).reduce(BigDecimal.ZERO,BigDecimal::add);
		BigDecimal proceeds = sales.add(tax).add(taxRefunds).add(fees).add(feeRefunds).add(refunds);

		HashMap<AdditionalItem, BigDecimal> additionalItems = new HashMap<>();
		additionalItems.put(AdditionalItem.SALES, sales);
		additionalItems.put(AdditionalItem.TAX, tax);
		additionalItems.put(AdditionalItem.TAX_REFUNDS, tax);
		additionalItems.put(AdditionalItem.FEES, fees.add(feeRefunds));
		additionalItems.put(AdditionalItem.REFUNDS, refunds);

		return new Invoice(String.valueOf(subsidiary.ordinal()), subsidiary, billingMonth.atDay(1), billingMonth.atEndOfMonth(), LocalDate.now(), units, currency, proceeds, soldUnitsDescription, additionalItems);
	}

	private static Comparator<String> createGlobalItemOrder() {
		return new GlobalItemsComparator();
	}

	private static class GlobalItemsComparator implements Comparator<String> {

		private static final List<String> order = List.of(TAX_DESC, REFUNDS_DESC, TAXREFUND_DESC, FEE_DESC);

		@Override
		public int compare(String s1, String s2) {
			int i1 = order.indexOf(s1);
			int i2 = order.indexOf(s2);
			if (i1 > -1 && i2 > -1) {
				return Integer.compare(i1, i2);
			} else if (i1 == -1 && i2 > -1) {
				return 1;
			} else if (i1 > -1 && i2 == -1) {
				return -1;
			} else {
				return s1.compareTo(s2);
			}
		}
	}

}
