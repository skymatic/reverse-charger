package de.skymatic.appstore_invoices.template;

import de.skymatic.appstore_invoices.model.Invoice;

class ConstantSection implements StringBuilderable {

	private final String content;

	ConstantSection(String content) {this.content = content;}

	@Override
	public StringBuilder toStringBuilder(Invoice i) {
		return new StringBuilder(content);
	}
}
