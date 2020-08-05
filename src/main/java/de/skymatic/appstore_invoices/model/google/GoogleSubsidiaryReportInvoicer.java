package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoice;
import de.skymatic.appstore_invoices.model.InvoiceItem;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GoogleSubsidiaryReportInvoicer {

	private static final String TAX_DESC = "Taxes Withheld";
	private static final String TAXREFUND_DESC = "Tax Refunds";
	private static final String FEE_DESC = "Google Fees";
	private static final String REFUNDS_DESC = "Refunds";

	static Invoice createInvoiceFrom(GoogleSubsidiary subsidiary, YearMonth billingMonth, Map<String, GoogleProductSubsidiaryReport> salesPerProduct){
		Aggregator agg = new Aggregator();
		salesPerProduct.values().stream().forEach(sale -> {
			agg.taxes += sale.getTaxes();
			agg.fees += sale.getFees();
			agg.refunds += sale.getRefunds();
			agg.taxRefunds += sale.getTaxRefunds();
			agg.feeRefunds += sale.getFeeRefunds();
		});
		Map<String, Double> globalItems = new TreeMap<>(createGlobalItemOrder());
		globalItems.put(TAX_DESC, agg.taxes);
		globalItems.put(FEE_DESC, agg.fees + agg.feeRefunds);
		globalItems.put(REFUNDS_DESC, agg.refunds);
		globalItems.put(TAXREFUND_DESC, agg.taxRefunds);

		Collection<InvoiceItem> items = new ArrayList<>();
		salesPerProduct.forEach((key, sale) -> items.add(new InvoiceItem(sale.getProductTitle(), sale.getUnits(), sale.getAmount())));

		return new Invoice(subsidiary.getAbbreviation(), subsidiary, billingMonth.atDay(1), billingMonth.atEndOfMonth(), LocalDate.now(), items, globalItems);
	}

	private static Comparator<String> createGlobalItemOrder(){
		return new GlobalItemsComparator();
	}

	private static class Aggregator {
		double taxes;
		double fees;
		double refunds;
		double taxRefunds;
		double feeRefunds;
	}

	private static class GlobalItemsComparator implements Comparator<String> {

		private final List<String> order = List.of(TAX_DESC,REFUNDS_DESC,TAXREFUND_DESC,FEE_DESC);

		@Override
		public int compare(String s1, String s2) {
			int i1 = order.indexOf(s1);
			int i2 = order.indexOf(s2);
			if(i1 > -1 && i2 > -1){
				return Integer.compare(i1,i2);
			} else if(i1 == -1 && i2 > -1){
				return 1;
			} else if( i1 > -1 && i2 == -1){
				return -1;
			} else {
				return s1.compareTo(s2);
			}
		}
	}

}
