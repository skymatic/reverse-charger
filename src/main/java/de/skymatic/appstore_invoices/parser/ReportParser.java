package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model2.SalesReport;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Collection;

public interface ReportParser {

	SalesReport parse(Path p) throws IOException, ReportParseException, IllegalArgumentException;

	default Collection<Object> parseUnresolved(Path p) throws IOException, ParseException {
		throw new UnsupportedOperationException("Not implemented.");
	}

}
