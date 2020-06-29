package de.skymatic.appstore_invoices.model.google;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.YearMonth;

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
		//TODO: Fails due to unknown country
		GoogleReport report = new GoogleReport(YearMonth.now());
		Assertions.assertTrue(report.toInvoices().isEmpty());
		report.add(getValidSale());
		Assertions.assertEquals(1, report.toInvoices().size());
	}

	@Test
	public void addingSaleOfExistingSubsidiaryDoesNotChangeNumberOfInvoices() {
		//TODO: Fails due to unknown country
		GoogleReport report = new GoogleReport(YearMonth.now());
		Assertions.assertTrue(report.toInvoices().isEmpty());
		report.add(getValidSale());
		report.add(getValidSaleOfSameSubsidiary());
		Assertions.assertEquals(1, report.toInvoices().size());
	}

	/**
	 * TODO: refactor to its own CLass ( the functionality of GoogleUtility is tested!)
	 */
	@Test
	public void addingSaleOfUnknownCountryThrowsException() {
		GoogleReport report = new GoogleReport(YearMonth.now());
		Assertions.assertThrows(IllegalArgumentException.class, () -> report.add(getInvalidCountrySale()));
	}


	private static GoogleSale getValidSale() {
		return new GoogleSale("1234",
				LocalDateTime.now(),
				"tax",
				"transaction",
				"refund",
				"MyProduct",
				"my.company",
				0,
				"",
				"ultraPhone",
				"DE",
				"Bavaria",
				"80333",
				"EUR",
				1337.0,
				1.0,
				"EUR",
				1337.0
		);
	}

	private static GoogleSale getValidSaleOfSameSubsidiary() {
		return new GoogleSale("1236",
				LocalDateTime.now(),
				"tax",
				"transaction",
				"refund",
				"MyProduct",
				"my.company",
				0,
				"",
				"ultraPhone",
				"DE",
				"Bavaria",
				"80333",
				"EUR",
				1337.0,
				1.0,
				"EUR",
				1337.0
		);
	}

	private static GoogleSale getInvalidCountrySale() {
		return new GoogleSale("1234",
				LocalDateTime.now(),
				"tax",
				"transaction",
				"refund",
				"MyProduct",
				"my.company",
				0,
				"",
				"ultraPhone",
				"XX",
				"Bavaria",
				"80333",
				"EUR",
				1337.0,
				1.0,
				"EUR",
				1337.0
		);
	}
}
