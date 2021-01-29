package de.skymatic.appstore_invoices.model2;

import java.util.Collection;

public interface SalesReport {

	Collection<? extends Invoicable> getInvoicables();

}
