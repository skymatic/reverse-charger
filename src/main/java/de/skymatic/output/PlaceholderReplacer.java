package de.skymatic.output;

import de.skymatic.model.Invoice;
import de.skymatic.model.MonthlyInvoices;

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

public class PlaceholderReplacer {

	private static final String PLACEHOLDER_START = "{{";
	private static final String PLACEHOLDER_END = "}}";
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

	private final Path templatePath;
	private final Collection<Invoice> invoices;


	public PlaceholderReplacer(Path templatePath, MonthlyInvoices m) {
		this.templatePath = templatePath;
		this.invoices = m.getInvoices();
	}

	public Map<String, StringBuilder> createHTMLInvoices() {
		Map<String, StringBuilder> sbs = new HashMap<>();
		invoices.forEach(i -> sbs.put(i.getNumberString(), new StringBuilder()));

		try (BufferedReader br = Files.newBufferedReader(templatePath)) {
			br.lines().forEach(line -> {
				int pos = line.indexOf(PLACEHOLDER_START);
				if (pos >= 0) {
					int endPos = line.indexOf(PLACEHOLDER_END, pos);
					invoices.forEach(invoice -> {
						sbs.get(invoice.getNumberString()).append(line, 0, pos)
								.append(getReplacement(invoice, line.substring(pos + 2, endPos)))
								.append(line, endPos + 2, line.length());
					});
				} else {
					sbs.forEach((i, sb) -> sb.append(line));
				}
			});
		} catch (IOException e) {
			//TODO: error handling
		}
		return sbs;
	}

	private String getReplacement(Invoice invoice, String placeholder) {
		switch (placeholder.trim()) {
			case "subsidiary_information":
				return Arrays.stream(invoice.getSubsidiary().getAddress()) //
						.reduce("", (address, address_entry) -> address + "<br>" + address_entry);
			case "item_amount":
				return String.valueOf(invoice.getAmount());
			case "invoice_number":
				return String.valueOf(invoice.getNumberString());
			case "item_proceeds":
				return String.valueOf(invoice.sum());
			case "issue_date":
				return invoice.getIssueDate().format(formatter);
			case "sales_period_start":
				return invoice.getStartOfPeriod().format(formatter);
			case "sales_period_end":
				return invoice.getEndOfPeriod().format(formatter);
			default:
				return PLACEHOLDER_START + placeholder + PLACEHOLDER_END; //We don't know it, so we do not touch it
		}

	}

}
