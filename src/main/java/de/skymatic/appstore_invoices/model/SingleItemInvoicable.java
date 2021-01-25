package de.skymatic.appstore_invoices.model;

public interface SingleItemInvoicable extends Invoicable{

	@Override
	default Invoice toInvoice() throws InvoiceGenerationException {
		return toSingleItemInvoice();
	}

	SingleProductInvoice toSingleItemInvoice() throws InvoiceGenerationException;

}
