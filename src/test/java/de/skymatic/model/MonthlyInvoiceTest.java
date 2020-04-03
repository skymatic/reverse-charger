package de.skymatic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.YearMonth;

public class MonthlyInvoiceTest {

	@Test
	public void testAddingSalesEntryOfNoneExistingInvoiceCreatesIt() {
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), "CVS", 0);
		Assertions.assertEquals(0, monthlyInvoices.getInvoices().size());

		SalesEntry salesEntry = Mockito.mock(SalesEntry.class);
		Mockito.when(salesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		monthlyInvoices.addSalesEntry(salesEntry);
		Assertions.assertEquals(1, monthlyInvoices.getInvoices().size());
	}

	@Test
	public void testAddingSalesEntryOfExistingSubsidiaryAddsIt() {
		SalesEntry salesEntry = Mockito.mock(SalesEntry.class);
		Mockito.when(salesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), "CVS", 0, salesEntry);
		int expected = monthlyInvoices.getInvoices().size();

		SalesEntry other = Mockito.mock(SalesEntry.class);
		Mockito.when(other.getRpc()).thenReturn(RegionPlusCurrency.MEXICO_MXN);
		monthlyInvoices.addSalesEntry(other);
		Assertions.assertEquals(expected, monthlyInvoices.getInvoices().size());
	}

	@Test
	public void testChangingNumberOfNotExistingInvoiceThrowsException() {
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), "CVS", 0);
		Assertions.assertThrows(IllegalArgumentException.class, () -> monthlyInvoices.changeSingleInvoiceNumber(Subsidiary.JAPAN, "CVS-333"));
	}

	@Test
	public void testChangingIssueDateOfNotExistingInvoiceThrowsException() {
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), "CVS", 0);
		Assertions.assertThrows(IllegalArgumentException.class, () -> monthlyInvoices.changeSingleIssueDate(Subsidiary.JAPAN, LocalDate.now()));
	}

	@Test
	public void testDefaultNumberingIncreasesWithNewInvoice() {
		int numberingSeed = 5;
		SalesEntry salesEntry = Mockito.mock(SalesEntry.class);
		Mockito.when(salesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), "CVS", numberingSeed, salesEntry);

		SalesEntry other = Mockito.mock(SalesEntry.class);
		Mockito.when(other.getRpc()).thenReturn(RegionPlusCurrency.JAPAN_JPY);
		monthlyInvoices.addSalesEntry(other);

		Assertions.assertTrue(monthlyInvoices.getInvoices().stream().anyMatch(invoice -> invoice.getNumberString().endsWith(String.valueOf(numberingSeed))));
		Assertions.assertTrue(monthlyInvoices.getInvoices().stream().anyMatch(invoice -> invoice.getNumberString().endsWith(String.valueOf(numberingSeed + 1))));
	}

}
