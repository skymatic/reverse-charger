package de.skymatic.output;

import de.skymatic.model.MonthlyInvoices;

import java.nio.file.Path;
import java.util.Map;

public abstract class InvoiceGenerator {

	public void generateAndWriteInvoices(MonthlyInvoices m, Path templatePath, Path out) {
		//TODO: how to handle the invoice nomber prefix?
		Map<Integer, StringBuilder> sbs = new PlaceholderReplacer(templatePath, m).createHTMLInvoices();
		this.write(out, sbs);

	}

	abstract void write(Path out, Map<Integer, StringBuilder> sbs);

	public static InvoiceGenerator createInvoiceGenerator(OutputFormat o) {
		switch (o) {
			case HTML:
				return new HTMLInvoiceGenerator();
			default:
				throw new IllegalArgumentException("Unknown Outputformat.");
		}
	}

	public enum OutputFormat {
		HTML;
	}

}
