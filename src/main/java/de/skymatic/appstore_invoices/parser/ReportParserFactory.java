package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.Type;

public class ReportParserFactory {

	public static ReportParser createParser(Type type) {
		return switch (type) {
			case APPLE -> new AppleParser();
			case GOOGLE -> new GoogleParser();
			default -> throw new IllegalArgumentException("No parser implementation found for type." + type);
		};
	}

}
