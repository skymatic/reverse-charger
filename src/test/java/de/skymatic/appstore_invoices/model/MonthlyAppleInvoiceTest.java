package de.skymatic.appstore_invoices.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.YearMonth;

public class MonthlyAppleInvoiceTest {

	@Test
	public void testAddingSalesEntryOfNoneExistingInvoiceCreatesIt() {
		AppleMonthlyInvoices appleMonthlyInvoices = new AppleMonthlyInvoices(YearMonth.of(2020, 3), "CVS", 0);
		Assertions.assertEquals(0, appleMonthlyInvoices.getInvoices().size());

		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		appleMonthlyInvoices.addSalesEntry(appleSalesEntry);
		Assertions.assertEquals(1, appleMonthlyInvoices.getInvoices().size());
	}

	@Test
	public void testAddingSalesEntryOfExistingSubsidiaryAddsIt() {
		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		AppleMonthlyInvoices appleMonthlyInvoices = new AppleMonthlyInvoices(YearMonth.of(2020, 3), "CVS", 0, appleSalesEntry);
		int expected = appleMonthlyInvoices.getInvoices().size();

		AppleSalesEntry other = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(other.getRpc()).thenReturn(RegionPlusCurrency.MEXICO_MXN);
		appleMonthlyInvoices.addSalesEntry(other);
		Assertions.assertEquals(expected, appleMonthlyInvoices.getInvoices().size());
	}

	@Test
	public void testDefaultNumberingIncreasesWithNewInvoice() {
		int numberingSeed = 5;
		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		AppleMonthlyInvoices appleMonthlyInvoices = new AppleMonthlyInvoices(YearMonth.of(2020, 3), "CVS", numberingSeed, appleSalesEntry);

		AppleSalesEntry other = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(other.getRpc()).thenReturn(RegionPlusCurrency.JAPAN_JPY);
		appleMonthlyInvoices.addSalesEntry(other);

		Assertions.assertTrue(appleMonthlyInvoices.getInvoices().stream().anyMatch(invoice -> invoice.getNumberString().endsWith(String.valueOf(numberingSeed))));
		Assertions.assertTrue(appleMonthlyInvoices.getInvoices().stream().anyMatch(invoice -> invoice.getNumberString().endsWith(String.valueOf(numberingSeed + 1))));
	}

}
