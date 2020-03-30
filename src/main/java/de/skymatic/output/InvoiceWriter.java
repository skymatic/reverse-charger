package de.skymatic.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public interface InvoiceWriter {

	default void write(Path outputDirectory, Map<String, StringBuilder> sbs) throws IOException {
		try {
			sbs.forEach((nomber, htmlInvoice) -> {
				Path p = outputDirectory.resolve("invoice-" + nomber + ".html");
				try {
					Files.writeString(p, htmlInvoice, StandardOpenOption.CREATE);
				} catch (IOException e) {
					throw new UncheckedIOException(e);
				}
			});
		} catch (UncheckedIOException e) {
			throw e.getCause();
		}

	}

	static InvoiceWriter createInvoiceGenerator(OutputFormat o) {
		switch (o) {
			case HTML:
				return new HTMLWriter();
			default:
				throw new IllegalArgumentException("Unknown Outputformat.");
		}
	}

	enum OutputFormat {
		HTML;
	}

}
