package de.skymatic.appstore_invoices.template;

import de.skymatic.appstore_invoices.model2.Invoice;

public interface StringBuilderable {

	StringBuilder toStringBuilder(Invoice i);
}
