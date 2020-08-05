package de.skymatic.appstore_invoices.output;

import de.skymatic.appstore_invoices.model.apple.AppleSubsidiaryReport;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

	private static final NumberFormat numFormatter;

	static {
		NumberFormat tmp = NumberFormat.getInstance();
		if(tmp instanceof DecimalFormat){
			((DecimalFormat) tmp).applyPattern("#,##0.0#;(#)");
			numFormatter = tmp;
		} else {
			numFormatter = new DecimalFormat("#,##0.0#;(#)");
		}
	}

	public Map<String, StringBuilder> createHTMLInvoices(Path templatePath, Collection<AppleSubsidiaryReport> invoices) throws IOException {
		Map<String, StringBuilder> sbs = new HashMap<>();
		invoices.forEach(i -> sbs.put(i.getNumberString(), new StringBuilder()));

		try (BufferedReader br = Files.newBufferedReader(templatePath)) {
			br.lines().forEach(line -> {
				int pos = line.indexOf(PLACEHOLDER_START);
				int lastEndPosPlus2 = 0;
				while (pos >= 0) {
					int currentEndPos = line.indexOf(PLACEHOLDER_END, pos);
					String placeholderString = line.substring(pos + 2, currentEndPos).trim().toUpperCase();
					try {
						Placeholder p = Placeholder.valueOf(placeholderString);
						for (AppleSubsidiaryReport i : invoices) {
							sbs.get(i.getNumberString())
									.append(line, lastEndPosPlus2, pos)
									.append(getReplacement(i, p));
						}
					} catch (IllegalArgumentException e) {
						sbs.forEach((i, sb) -> sb.append(line));
					}
					pos = line.indexOf(PLACEHOLDER_START, currentEndPos);
					lastEndPosPlus2 = currentEndPos + 2;
				}
				for (AppleSubsidiaryReport i : invoices) {
					sbs.get(i.getNumberString()).append(line, lastEndPosPlus2, line.length());
					sbs.get(i.getNumberString()).append("\n");
				}

			});
		}
		return sbs;
	}

	private String getReplacement(AppleSubsidiaryReport invoice, Placeholder placeholder) {
		switch (placeholder) {
			case SUBSIDIARY_INFORMATION:
				return Arrays.stream(invoice.getAppleSubsidiary().getAddress()) //
						.reduce("", (address, address_entry) -> address + "<br>" + address_entry);
			case PRODUCT_AMOUNT:
				return String.valueOf(invoice.getAmount());
			case INVOICE_NUMBER:
				return String.valueOf(invoice.getNumberString());
			case PRODUCT_PROCEEDS:
				return numFormatter.format(invoice.sum());
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
