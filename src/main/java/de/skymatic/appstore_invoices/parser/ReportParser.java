package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.Invoice;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Collection;

public interface ReportParser {

	Collection<Invoice> parseToInvoice(Path p) throws IOException, ParseException, IllegalArgumentException;

	default Object parseToSpecificModel(Path p) throws IOException, ParseException {
		throw new UnsupportedOperationException("Not implemented.");
	}

	default Collection<Object> parseUnresolved(Path p) throws IOException, ParseException {
		throw new UnsupportedOperationException("Not implemented.");
	}

}
