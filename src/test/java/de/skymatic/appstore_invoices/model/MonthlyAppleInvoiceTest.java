package de.skymatic.appstore_invoices.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.YearMonth;

public class MonthlyAppleInvoiceTest {

	@Test
	public void testAddingSalesEntryOfNoneExistingInvoiceCreatesIt() {
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), "CVS", 0);
		Assertions.assertEquals(0, monthlyInvoices.getInvoices().size());

		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		monthlyInvoices.addSalesEntry(appleSalesEntry);
		Assertions.assertEquals(1, monthlyInvoices.getInvoices().size());
	}

	@Test
	public void testAddingSalesEntryOfExistingSubsidiaryAddsIt() {
		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), "CVS", 0, appleSalesEntry);
		int expected = monthlyInvoices.getInvoices().size();

		AppleSalesEntry other = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(other.getRpc()).thenReturn(RegionPlusCurrency.MEXICO_MXN);
		monthlyInvoices.addSalesEntry(other);
		Assertions.assertEquals(expected, monthlyInvoices.getInvoices().size());
	}

	@Test
	public void testDefaultNumberingIncreasesWithNewInvoice() {
		int numberingSeed = 5;
		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), "CVS", numberingSeed, appleSalesEntry);

		AppleSalesEntry other = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(other.getRpc()).thenReturn(RegionPlusCurrency.JAPAN_JPY);
		monthlyInvoices.addSalesEntry(other);

		Assertions.assertTrue(monthlyInvoices.getInvoices().stream().anyMatch(invoice -> invoice.getNumberString().endsWith(String.valueOf(numberingSeed))));
		Assertions.assertTrue(monthlyInvoices.getInvoices().stream().anyMatch(invoice -> invoice.getNumberString().endsWith(String.valueOf(numberingSeed + 1))));
	}

}
