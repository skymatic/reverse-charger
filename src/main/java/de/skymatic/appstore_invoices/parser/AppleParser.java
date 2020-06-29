package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.InvoiceCollection;
import de.skymatic.appstore_invoices.model.apple.AppleReport;
import de.skymatic.appstore_invoices.model.apple.AppleSalesEntry;
import de.skymatic.appstore_invoices.model.apple.RegionPlusCurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.YearMonth;
import java.util.Collection;
import java.util.stream.Collectors;

public class AppleParser implements CSVParser, ReportParser {

	private static final int MIN_COLUMN_COUNT = 10;
	private boolean endOfReport = false;

	public ParseResult parseCSV(Path p) throws IOException, OldParseException {
		StringReference lastReadLine = new StringReference();
		try (BufferedReader br = Files.newBufferedReader(p)) {
			String header = br.readLine();
			String[] monthYear = header.substring(header.indexOf('(') + 1, header.indexOf(')')).split(",");
			YearMonth yearMonth = YearMonth.of(Integer.valueOf(monthYear[1].trim()), Month.valueOf(monthYear[0].trim().toUpperCase()));

			Collection<AppleSalesEntry> sales = br.lines().filter(line -> !isLastLine(line) && line.startsWith("\""))
					.map(line -> lastReadLine.copyAndReturn(line))
					.map(line -> line.replaceAll("[\"]", "").split(","))
					.map(splittedLine -> {
						RegionPlusCurrency rpc = getRegionPlusCurrency(splittedLine[0]);
						int units = Integer.parseInt(splittedLine[1]);
						double earned = Double.parseDouble(splittedLine[2]);
						double pretaxSubtotal = Double.parseDouble(splittedLine[3]);
						double inputTax = Double.parseDouble(splittedLine[4]);
						double adjustments = Double.parseDouble(splittedLine[5]);
						double withholdingTax = Double.parseDouble(splittedLine[6]);
						double totalOwned = Double.parseDouble(splittedLine[7]);
						double exchangeRate = Double.parseDouble(splittedLine[8]);
						double proceeds = Double.parseDouble(splittedLine[9]);
						return new AppleSalesEntry(rpc, units, earned, pretaxSubtotal, inputTax, adjustments, withholdingTax, totalOwned, exchangeRate, proceeds);
					}).collect(Collectors.toList());

			return new ParseResult(yearMonth, sales);
		} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new OldParseException("Error parsing line:  " + lastReadLine.get(), e);
		}
	}

	private boolean isLastLine(String line) {
		if (line.contains("Paid to")) endOfReport = true;
		return endOfReport;
	}

	private RegionPlusCurrency getRegionPlusCurrency(String rpcString) {
		return RegionPlusCurrency.valueOf(rpcString.replace(' ', '_')
				.replace('-', '_')
				.replaceAll("[\\(\\)]", "")
				.toUpperCase());
	}

	@Override
	public InvoiceCollection parse(Path p) throws IOException, ReportParseException, IllegalArgumentException {
		try {
			//TODO: this is currently only for compiling purpose. AppleReport needs to be refactored.
			ParseResult result = parseCSV(p);
			return new AppleReport(result.getYearMonth(), "", 1, result.getSales().toArray(new AppleSalesEntry[]{}));
		} catch (OldParseException e) {
			throw new ReportParseException(e.getMessage(), -1, e.getCause());
		}
	}
}
