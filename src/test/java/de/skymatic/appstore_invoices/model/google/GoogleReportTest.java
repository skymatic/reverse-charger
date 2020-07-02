package de.skymatic.appstore_invoices.model.google;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static de.skymatic.appstore_invoices.model.google.GoogleSaleFactory.*;

public class GoogleReportTest {

	@Test
	public void creatingReportWithNoSalesResultsToEmptyInvoiceCollection() {
		YearMonth yearMonth = YearMonth.now();
		GoogleReport report = new GoogleReport(yearMonth);
		Assertions.assertTrue(report.toInvoices().isEmpty());
	}

	@Test
	public void usingVarArgsOnlyConstructorThrowsExceptionWhenEmpty() {
		GoogleSale[] sales = new GoogleSale[]{};
		Assertions.assertThrows(IllegalArgumentException.class, () -> new GoogleReport(sales));
	}

	@Test
	public void addingSaleOfNotExistingSubsidiaryCreatesIt() {
		GoogleReport report = new GoogleReport(YearMonth.now());
		int expectedSize = report.toInvoices().size() + 1;
		report.add(getSale());
		Assertions.assertEquals(expectedSize, report.toInvoices().size());
	}

	@Test
	public void addingSaleOfExistingSubsidiaryDoesNotChangeNumberOfInvoices() {
		GoogleReport report = new GoogleReport(YearMonth.now());
		int expectedSize = report.toInvoices().size() + 1;
		report.add(getSale());
		report.add(getSaleOfDifferentProduct());
		Assertions.assertEquals(expectedSize, report.toInvoices().size());
	}

}
