package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.Workflow;

public class ReportParserFactory {

	public static ReportParser createParser(Workflow workflow) {
		return switch (workflow) {
			case APPLE -> new AppleParser();
			case AUTO -> new AutoParser();
			case GOOGLE -> new GoogleParser();
			default -> throw new IllegalArgumentException("No parser implementation found for type." + workflow);
		};
	}

}
