package de.skymatic.appstore_invoices.model;

import java.util.Collection;

public interface SalesReport {

	Collection<? extends Invoicable> getInvoicables();

}
