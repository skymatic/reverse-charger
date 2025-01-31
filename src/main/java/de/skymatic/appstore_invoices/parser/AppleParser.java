package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.apple.AppleReport;
import de.skymatic.appstore_invoices.model.apple.AppleSalesEntry;
import de.skymatic.appstore_invoices.model.apple.RegionNCurrency;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.YearMonth;
import java.util.Collection;

public class AppleParser implements ReportParser {

	private boolean endOfReport = false;

	@Override
	public AppleReport parse(Path p) throws IOException, ReportParseException, IllegalArgumentException {
		StringReference lastReadLine = new StringReference();
		try (BufferedReader br = Files.newBufferedReader(p)) {
			String header = br.readLine();
			String[] monthYear = header.substring(header.indexOf('(') + 1, header.indexOf(')')).split(",");
			YearMonth yearMonth = YearMonth.of(Integer.parseInt(monthYear[1].trim()), Month.valueOf(monthYear[0].trim().toUpperCase()));

			Collection<AppleSalesEntry> sales = br.lines() //
					.filter(line -> !isLastLine(line) && line.startsWith("\"")) //
					.map(lastReadLine::copyAndReturn) //
					.map(line -> line.replaceAll("[\"]", "").split(",")) //
					.map(splittedLine -> {
						RegionNCurrency rpc = getRegionPlusCurrency(splittedLine[0]);
						int units = Integer.parseInt(splittedLine[1]);
						BigDecimal earned = new BigDecimal(splittedLine[2]);
						BigDecimal pretaxSubtotal = new BigDecimal(splittedLine[3]);
						BigDecimal inputTax = new BigDecimal(splittedLine[4]);
						BigDecimal adjustments = new BigDecimal(splittedLine[5]);
						BigDecimal withholdingTax = new BigDecimal(splittedLine[6]);
						BigDecimal totalOwned = new BigDecimal(splittedLine[7]);
						BigDecimal exchangeRate = new BigDecimal(splittedLine[8]);
						BigDecimal proceeds = new BigDecimal(splittedLine[9]);
						String bankAccountCurrency = splittedLine[10];
						return new AppleSalesEntry(rpc, units, earned, pretaxSubtotal, inputTax, adjustments, withholdingTax, totalOwned, exchangeRate, proceeds, bankAccountCurrency);
					}).toList();

			return new AppleReport(yearMonth, sales.toArray(new AppleSalesEntry[]{}));
		} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new ReportParseException("Error parsing line:  " + lastReadLine.get(), -1, e);
		}
	}

	private boolean isLastLine(String line) {
		if (line.contains("Paid to")) endOfReport = true;
		return endOfReport;
	}

	private RegionNCurrency getRegionPlusCurrency(String rpcString) {
		return RegionNCurrency.valueOf(rpcString.replace(' ', '_').replace('-', '_').replaceAll("[\\(\\)]", "").toUpperCase());
	}

}
