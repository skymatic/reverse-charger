package de.skymatic.parser;

import de.skymatic.model.MonthlyInvoices;

import java.io.IOException;
import java.nio.file.Path;

public interface CSVParser {

	MonthlyInvoices parseCSV(Path p) throws IOException;

}
