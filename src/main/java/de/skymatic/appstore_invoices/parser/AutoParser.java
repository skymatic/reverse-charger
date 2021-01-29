package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.InvoiceCollection;
import de.skymatic.appstore_invoices.model2.SalesReport;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AutoParser implements ReportParser {

	private static final String APPLE_CSV_NAME = "financial_report.csv";
	private static final String APPLE_CSV_CONTENT_PREFIX = "\"iTunes Connect - Payments and Financial Reports";

	private static final String GOOGLE_CSV_PREFIX = "PlayApps_";
	private static final String GOOGLE_CSV_CONTENT_PREFIX = "Description,Transaction Date,Transaction Time,Tax Type,Transaction Type";

	@Override
	public SalesReport parse(Path p) throws IOException, ReportParseException, IllegalArgumentException {
		//First attempt: Use the file name
		final String fileName = p.getFileName().toString();
		if (fileName == APPLE_CSV_NAME) {
			return new AppleParser().parse(p);
		} else if (fileName.startsWith(GOOGLE_CSV_PREFIX)) {
			return new GoogleParser().parse(p);
		}

		//Ok, as second attempt we read the first line
		String firstLine = "";
		try (BufferedReader reader = Files.newBufferedReader(p)) {
			firstLine = reader.readLine();
		} catch (IOException e) {
			throw new IllegalStateException("IO error while reading file for type detection.", e);
		}
		if (firstLine.startsWith(APPLE_CSV_CONTENT_PREFIX)) {
			return new AppleParser().parse(p);
		} else if (firstLine.startsWith(GOOGLE_CSV_CONTENT_PREFIX)) {
			return new GoogleParser().parse(p);
		}

		//we give up
		throw new IllegalStateException("Could not detect document type to select appropriate parser.");
	}
}
