package de.skymatic.appstore_invoices.model;

import java.util.Collection;

public interface InvoiceCollection {

	Collection<? extends Invoice> toInvoices();

	default Collection<? extends SingleProductInvoice> toInvoicesOfSingleProduct(){
		throw new UnsupportedOperationException();
	}

}
