package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model2.AdditionalItem;
import de.skymatic.appstore_invoices.model2.Invoicable;
import de.skymatic.appstore_invoices.model2.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GoogleSingleProductInvoicer {

	private static final String TAX_DESC = "Tax Withheld";
	private static final String TAXREFUND_DESC = "Tax Refunds";
	private static final String FEE_DESC = "Google Fees";
	private static final String REFUNDS_DESC = "Refunds";

	static Invoice createInvoiceFrom(GoogleSubsidiary subsidiary, YearMonth billingMonth, GoogleProductSubsidiaryReport productSubReport) throws Invoicable.InvoiceGenerationException {

		HashMap<AdditionalItem, BigDecimal> additionalItems = new HashMap<>();
		additionalItems.put(AdditionalItem.SALES, productSubReport.getAmount());
		additionalItems.put(AdditionalItem.TAX, productSubReport.getTaxes());
		additionalItems.put(AdditionalItem.TAX_REFUNDS, productSubReport.getTaxRefunds());
		additionalItems.put(AdditionalItem.FEES, productSubReport.getFees().add(productSubReport.getFeeRefunds()));
		additionalItems.put(AdditionalItem.REFUNDS, productSubReport.getRefunds());

		BigDecimal proceeds = productSubReport.getAmount().add(productSubReport.getTaxes()).add(productSubReport.getTaxRefunds()).add(productSubReport.getFees()).add(productSubReport.getFeeRefunds()).add(productSubReport.getRefunds());

		return new Invoice(String.valueOf(subsidiary.ordinal()), subsidiary, billingMonth.atDay(1), billingMonth.atEndOfMonth(), LocalDate.now(), productSubReport.getUnits(), productSubReport.getAmountCurrency(), proceeds, productSubReport.getProductTitle(), additionalItems);
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
