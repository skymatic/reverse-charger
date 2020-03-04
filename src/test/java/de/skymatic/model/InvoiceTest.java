package de.skymatic.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvoiceTest {

    @Test
    public void testEmptyInvoiceReturnsZero(){
        Invoice i = new Invoice();
        Assertions.assertEquals(0, i.sum());
    }
}
