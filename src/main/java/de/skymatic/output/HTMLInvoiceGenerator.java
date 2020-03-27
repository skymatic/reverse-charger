package de.skymatic.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class HTMLInvoiceGenerator extends InvoiceGenerator {

	@Override
	void write(Path outputDir, Map<String, StringBuilder> sbs) {
		sbs.forEach((nomber, htmlInvoice) -> {
			Path p = outputDir.resolve("invoice-" + nomber + ".html");
			try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
				bw.append(htmlInvoice);
			} catch (IOException e) {
				//TODO
			}
		});

	}
}
