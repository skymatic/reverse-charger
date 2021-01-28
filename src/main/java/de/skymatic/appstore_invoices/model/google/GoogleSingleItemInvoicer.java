package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoicable;
import de.skymatic.appstore_invoices.model.InvoiceItem;
import de.skymatic.appstore_invoices.model.SingleProductInvoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class GoogleSingleItemInvoicer {

	private static final String TAX_DESC = "Tax Withheld";
	private static final String TAXREFUND_DESC = "Tax Refunds";
	private static final String FEE_DESC = "Google Fees";
	private static final String REFUNDS_DESC = "Refunds";

	static SingleProductInvoice createInvoiceFrom(GoogleSubsidiary subsidiary, YearMonth billingMonth, Map<String, GoogleProductSubsidiaryReport> salesPerProduct) throws Invoicable.InvoiceGenerationException {
		if (salesPerProduct.size() != 1) {
			throw new Invoicable.InvoiceGenerationException("The report contains more than one product. This appplication currently supports only one.");
		}

		var productReport = salesPerProduct.values().stream().findFirst().orElseThrow(IllegalStateException::new);

		SortedMap<String, BigDecimal> globalItems = new TreeMap<>(createGlobalItemOrder());
		globalItems.put(TAX_DESC, productReport.getTaxes());
		globalItems.put(FEE_DESC, productReport.getFees().add(productReport.getFeeRefunds()));
		globalItems.put(REFUNDS_DESC, productReport.getRefunds());
		globalItems.put(TAXREFUND_DESC, productReport.getTaxRefunds());

		var invoiceItem = new InvoiceItem(productReport.getProductTitle(), productReport.getUnits(), productReport.getAmount());
		return new SingleProductInvoice(subsidiary.getAbbreviation(), subsidiary, billingMonth.atDay(1), billingMonth.atEndOfMonth(), LocalDate.now(), productReport.getAmountCurrency(), invoiceItem, globalItems);
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
