package de.skymatic.appstore_invoices.model;

public interface Invoicable {

	Invoice toInvoice() throws InvoiceGenerationException;

	class InvoiceGenerationException extends Exception {

		public InvoiceGenerationException(String msg){
			super(msg);
		}
	}

}
