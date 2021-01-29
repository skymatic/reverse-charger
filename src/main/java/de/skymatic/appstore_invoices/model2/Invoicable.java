package de.skymatic.appstore_invoices.model2;

public interface Invoicable {

	Invoice toInvoice() throws InvoiceGenerationException;

	class InvoiceGenerationException extends Exception {

		public InvoiceGenerationException(String msg) {
			super(msg);
		}

		public InvoiceGenerationException(Throwable cause) {
			super(cause);
		}
	}

}
