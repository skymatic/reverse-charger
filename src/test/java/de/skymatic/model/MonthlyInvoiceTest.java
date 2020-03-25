package de.skymatic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.YearMonth;

public class MonthlyInvoiceTest {

	@Test
	public void testAddingSalesEntryOfNoneExistingInvoiceCreatesIt() {
		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(YearMonth.of(2020, 3), 0);
		SalesEntry salesEntry = Mockito.mock(SalesEntry.class);
		Mockito.when(salesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		monthlyInvoices.addSalesEntry(salesEntry);
		Assertions.assertEquals(1, monthlyInvoices.getInvoices().size());
	}
}
