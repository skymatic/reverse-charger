package de.skymatic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class InvoiceTest {

	@Test
	public void testEmptyInvoiceReturnsZero() {
		SalesEntry s = Mockito.mock(SalesEntry.class);
		Invoice i = new Invoice(RegionPlusCurrency.AMERICAS_USD, s);
		Assertions.assertEquals(0, i.sum());
	}
}
