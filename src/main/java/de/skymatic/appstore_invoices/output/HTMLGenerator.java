package de.skymatic.appstore_invoices.output;

import de.skymatic.appstore_invoices.model.Invoice;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HTMLGenerator {

	private static final String PLACEHOLDER_START = "{{";
	private static final String PLACEHOLDER_END = "}}";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

	public Map<String, StringBuilder> createHTMLInvoices(Path templatePath, Collection<Invoice> invoices) throws IOException {
		Map<String, StringBuilder> sbs = new HashMap<>();
		invoices.forEach(i -> sbs.put(i.getNumberString(), new StringBuilder()));

		try (BufferedReader br = Files.newBufferedReader(templatePath)) {
			br.lines().forEach(line -> {
				int pos = line.indexOf(PLACEHOLDER_START);
				if (pos >= 0) {
					int endPos = line.indexOf(PLACEHOLDER_END, pos);
					String placeholderString = line.substring(pos + 2, endPos).trim().toUpperCase();
					try {
						Placeholder p = Placeholder.valueOf(placeholderString);
						invoices.forEach(invoice -> {
							sbs.get(invoice.getNumberString())
									.append(line, 0, pos)
									.append(getReplacement(invoice, p))
									.append(line, endPos + 2, line.length());
						});
					} catch (IllegalArgumentException e) {
						sbs.forEach((i, sb) -> sb.append(line));
					}
				} else {
					sbs.forEach((i, sb) -> sb.append(line));
				}
			});
		}
		return sbs;
	}

	private String getReplacement(Invoice invoice, Placeholder placeholder) {
		switch (placeholder) {
			case SUBSIDIARY_INFORMATION:
				return Arrays.stream(invoice.getSubsidiary().getAddress()) //
						.reduce("", (address, address_entry) -> address + "<br>" + address_entry);
			case PRODUCT_AMOUNT:
				return String.valueOf(invoice.getAmount());
			case INVOICE_NUMBER:
				return String.valueOf(invoice.getNumberString());
			case PRODUCT_PROCEEDS:
				return String.valueOf(invoice.sum());
			case ISSUE_DATE:
				return invoice.getIssueDate().format(formatter);
			case SALES_PERIOD_START:
				return invoice.getStartOfPeriod().format(formatter);
			case SALES_PERIOD_END:
				return invoice.getEndOfPeriod().format(formatter);
			default:
				throw new IllegalArgumentException(); //NO-OP
		}

	}

}
