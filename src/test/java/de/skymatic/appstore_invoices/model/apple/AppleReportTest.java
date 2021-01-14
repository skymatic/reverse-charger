package de.skymatic.appstore_invoices.model.apple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.YearMonth;

public class AppleReportTest {

	@Test
	public void testAddingSalesEntryOfNoneExistingInvoiceCreatesIt() {
		AppleReport appleReport = new AppleReport(YearMonth.of(2020, 3), "CVS", 0);
		Assertions.assertEquals(0, appleReport.getInvoices().size());

		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		appleReport.addSalesEntry(appleSalesEntry);
		Assertions.assertEquals(1, appleReport.getInvoices().size());
	}

	@Test
	public void testAddingSalesEntryOfExistingSubsidiaryAddsIt() {
		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionPlusCurrency.PERU_PEN);
		AppleReport appleReport = new AppleReport(YearMonth.of(2020, 3), "CVS", 0, appleSalesEntry);
		int expected = appleReport.getInvoices().size();

		AppleSalesEntry other = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(other.getRpc()).thenReturn(RegionPlusCurrency.MEXICO_MXN);
		appleReport.addSalesEntry(other);
		Assertions.assertEquals(expected, appleReport.getInvoices().size());
	}

}
