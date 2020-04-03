package de.skymatic.parser;

import de.skymatic.model.RegionPlusCurrency;
import de.skymatic.model.SalesEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.YearMonth;
import java.util.Collection;
import java.util.stream.Collectors;

public class AppleParser implements CSVParser {

	private static final int MIN_COLUMN_COUNT = 10;

	public ParseResult parseCSV(Path p) throws IOException, ParseException {
		StringReference lastReadLine = new StringReference();
		try (BufferedReader br = Files.newBufferedReader(p)) {
			String header = br.readLine();
			String[] monthYear = header.substring(header.indexOf('(') + 1, header.indexOf(')')).split(",");
			YearMonth yearMonth = YearMonth.of(Integer.valueOf(monthYear[1].trim()), Month.valueOf(monthYear[0].trim().toUpperCase()));

			Collection<SalesEntry> sales = br.lines().filter(line -> line.startsWith("\""))
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
						return new SalesEntry(rpc, units, earned, pretaxSubtotal, inputTax, adjustments, withholdingTax, totalOwned, exchangeRate, proceeds);
					}).collect(Collectors.toList());

			return new ParseResult(yearMonth, sales);
		} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new ParseException("Error parsing line:  " + lastReadLine.get(), e);
		}
	}

	private RegionPlusCurrency getRegionPlusCurrency(String rpcString) {
		return RegionPlusCurrency.valueOf(rpcString.replace(' ', '_')
				.replace('-', '_')
				.replaceAll("[\\(\\)]", "")
				.toUpperCase());
	}

	private class StringReference {

		private String s = "";

		public String copyAndReturn(String s) {
			this.s = s;
			return s;
		}

		public String get() {
			return s;
		}
	}

}
