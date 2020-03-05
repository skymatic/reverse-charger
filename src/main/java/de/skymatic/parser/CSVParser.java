package de.skymatic.parser;

import de.skymatic.model.MonthlyInvoices;

import java.nio.file.Path;

public interface CSVParser {

	MonthlyInvoices parseCSV(Path p);

}
