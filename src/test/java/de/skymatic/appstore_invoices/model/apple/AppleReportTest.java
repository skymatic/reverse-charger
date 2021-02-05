package de.skymatic.appstore_invoices.model.apple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.YearMonth;

public class AppleReportTest {

	@Test
	public void testAddingSalesEntryOfNoneExistingInvoiceCreatesIt() {
		AppleReport appleReport = new AppleReport(YearMonth.of(2020, 3));
		Assertions.assertEquals(0, appleReport.getSubReports().size());

		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionNCurrency.AMERICAS_USD);
		appleReport.addSalesEntry(appleSalesEntry);
		Assertions.assertEquals(1, appleReport.getSubReports().size());
	}

	@Test
	public void testAddingSalesEntryOfExistingSubsidiaryAddsIt() {
		AppleSalesEntry appleSalesEntry = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(appleSalesEntry.getRpc()).thenReturn(RegionNCurrency.PERU_PEN);
		AppleReport appleReport = new AppleReport(YearMonth.of(2020, 3), appleSalesEntry);
		int expected = appleReport.getSubReports().size();

		AppleSalesEntry other = Mockito.mock(AppleSalesEntry.class);
		Mockito.when(other.getRpc()).thenReturn(RegionNCurrency.MEXICO_MXN);
		appleReport.addSalesEntry(other);
		Assertions.assertEquals(expected, appleReport.getSubReports().size());
	}

}
