package de.skymatic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class InvoiceTest {

	@Test
	public void testEmptyInvoiceReturnsZero() {
		SalesEntry s = Mockito.mock(SalesEntry.class);
		Mockito.when(s.getRpc()).thenReturn(RegionPlusCurrency.AMERICAS_USD);
		Invoice i = new Invoice(s);
		Assertions.assertEquals(0, i.sum());
	}
}
