package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoicable;
import de.skymatic.appstore_invoices.model.InvoiceItem;
import de.skymatic.appstore_invoices.model.SingleProductInvoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
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
		Aggregator agg = new Aggregator();
		salesPerProduct.values().stream().forEach(sale -> {
			agg.taxes = agg.taxes.add(sale.getTaxes());
			agg.fees = agg.fees.add(sale.getFees());
			agg.refunds = agg.refunds.add(sale.getRefunds());
			agg.taxRefunds = agg.taxRefunds.add(sale.getTaxRefunds());
			agg.feeRefunds = agg.feeRefunds.add(sale.getFeeRefunds());
		});
		SortedMap<String, BigDecimal> globalItems = new TreeMap<>(createGlobalItemOrder());
		globalItems.put(TAX_DESC, agg.taxes);
		globalItems.put(FEE_DESC, agg.fees.add(agg.feeRefunds));
		globalItems.put(REFUNDS_DESC, agg.refunds);
		globalItems.put(TAXREFUND_DESC, agg.taxRefunds);

		Collection<InvoiceItem> items = new ArrayList<>();
		salesPerProduct.forEach((key, sale) -> items.add(new InvoiceItem(sale.getProductTitle(), sale.getUnits(), sale.getAmount())));
		if(items.size() != 1){
			throw new Invoicable.InvoiceGenerationException("The report contains more than one product. This appplication currently supports only one.");
		} else {
			return new SingleProductInvoice(subsidiary.getAbbreviation(), subsidiary, billingMonth.atDay(1), billingMonth.atEndOfMonth(), LocalDate.now(), items.stream().findFirst().get(), globalItems);
		}
	}

	private static Comparator<String> createGlobalItemOrder() {
		return new GlobalItemsComparator();
	}

	private static class Aggregator {

		BigDecimal taxes = BigDecimal.ZERO;
		BigDecimal fees = BigDecimal.ZERO;
		BigDecimal refunds = BigDecimal.ZERO;
		BigDecimal taxRefunds = BigDecimal.ZERO;
		BigDecimal feeRefunds = BigDecimal.ZERO;
	}

	private static class GlobalItemsComparator implements Comparator<String> {

		private final List<String> order = List.of(TAX_DESC, REFUNDS_DESC, TAXREFUND_DESC, FEE_DESC);

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
