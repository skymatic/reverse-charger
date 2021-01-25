package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoicable;
import de.skymatic.appstore_invoices.model.Invoice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;

import static de.skymatic.appstore_invoices.model.google.GoogleSaleFactory.*;

public class GoogleSubsidiaryReportTest {

	@Test
	public void emptyVarArgsConstructorThrowsException() {
		GoogleSale[] empty = new GoogleSale[]{};
		Assertions.assertThrows(IllegalArgumentException.class, () -> new GoogleSubsidiaryReport(empty));
	}

	@Test
	public void addingSalesOfDifferentSubsidiariesThrowsException() {
		GoogleSale s1 = getSale();
		GoogleSale s2 = getSaleOfDifferentSubsidiary();
		Assertions.assertThrows(IllegalArgumentException.class, () -> new GoogleSubsidiaryReport(s1, s2));

		GoogleSubsidiaryReport subReport = new GoogleSubsidiaryReport(s1);
		Assertions.assertThrows(IllegalArgumentException.class, () -> subReport.add(s2));
	}

	@Test
	public void addingSalesOfDifferentYearMonthThrowsException() {
		GoogleSale s1 = getSale();
		GoogleSale s2 = getSaleOfDifferentMonth();
		Assertions.assertThrows(IllegalArgumentException.class, () -> new GoogleSubsidiaryReport(s1, s2));

		GoogleSubsidiaryReport subReport = new GoogleSubsidiaryReport(s1);
		Assertions.assertThrows(IllegalArgumentException.class, () -> subReport.add(s2));
	}

	@Test
	public void countOfProductsDoesNotChangeWhenTwoEntriesOfSameProduct() throws Invoicable.InvoiceGenerationException {
		GoogleSale s1 = getSale();
		GoogleSale s2 = getSale();

		GoogleSubsidiaryReport subReport = new GoogleSubsidiaryReport(s1);
		assert subReport.toInvoice().size() == 1;

		subReport.add(s2);
		Assertions.assertEquals(1, subReport.toInvoice().size());
	}

	@Test
	public void addingSaleOfNewProductTypeIncreasesCountOfProducts() throws Invoicable.InvoiceGenerationException {
		GoogleSale s1 = getSale();
		GoogleSale s2 = getSaleOfDifferentProduct();

		GoogleSubsidiaryReport subReport = new GoogleSubsidiaryReport(s1);
		assert subReport.toInvoice().size() == 1;

		subReport.add(s2);
		Assertions.assertEquals(2, subReport.toInvoice().size());
	}

	@Test
	public void returnedBillingMonthMatchesWithFirstAddedSale() {
		GoogleSale sale = getSale();
		GoogleSale[] sales = new GoogleSale[]{sale};
		YearMonth expected = YearMonth.from(sale.getTransactionDateTime());

		GoogleSubsidiaryReport subReport1 = new GoogleSubsidiaryReport(sale);
		GoogleSubsidiaryReport subReport2 = new GoogleSubsidiaryReport(sales);

		Assertions.assertEquals(expected, subReport1.getBillingMonth());
		Assertions.assertEquals(expected, subReport2.getBillingMonth());
		//TODO: and it isn't overwritten
	}

	@Test
	public void returnedSubsidiaryMatchesWithFirstAddedSale() {
		GoogleSale sale = getSale();
		GoogleSale[] sales = new GoogleSale[]{sale};
		GoogleSubsidiary expected = sale.getSubsidiary();

		GoogleSubsidiaryReport subReport1 = new GoogleSubsidiaryReport(sale);
		GoogleSubsidiaryReport subReport2 = new GoogleSubsidiaryReport(sales);

		Assertions.assertEquals(expected, subReport1.getSubsidiary());
		Assertions.assertEquals(expected, subReport2.getSubsidiary());
		//TODO: and it isn't overwritten
	}


	@Test
	public void generatedInvoiceContainsTheData() throws Invoicable.InvoiceGenerationException {
		GoogleSale[] sales = new GoogleSale[]{
				getChargeSale(),
				getTaxSale(),
				getFeeSale(),
				getRefundSale(),
				getSaleOfDifferentProduct(),
		};
		Invoice invoice = new GoogleSubsidiaryReport(sales).toInvoice();

		BigDecimal expectedProceeds = Arrays.stream(sales).map(GoogleSale::getAmountMerchantCurrency).reduce(BigDecimal.ZERO,BigDecimal::add);

		Assertions.assertEquals(2, invoice.size());
		Assertions.assertEquals(0, expectedProceeds.compareTo(invoice.proceeds()));
	}
}
