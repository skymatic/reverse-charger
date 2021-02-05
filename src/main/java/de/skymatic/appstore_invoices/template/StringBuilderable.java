package de.skymatic.appstore_invoices.template;

import de.skymatic.appstore_invoices.model.Invoice;

public interface StringBuilderable {

	StringBuilder toStringBuilder(Invoice i);
}
