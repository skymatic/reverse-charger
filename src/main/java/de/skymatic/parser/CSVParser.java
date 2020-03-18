package de.skymatic.parser;

import java.io.IOException;
import java.nio.file.Path;

public interface CSVParser {

	ParseResult parseCSV(Path p) throws IOException, ParseException;

}
