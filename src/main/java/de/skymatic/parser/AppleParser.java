package de.skymatic.parser;

import de.skymatic.model.MonthlyInvoices;
import de.skymatic.model.RegionPlusCurrency;
import de.skymatic.model.SalesEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.time.YearMonth;
import java.util.Set;
import java.util.stream.Collectors;

public class AppleParser implements CSVParser {

	private static final int MIN_COLUMN_COUNT = 10;

	private YearMonth yearMonth;

	public MonthlyInvoices parseCSV(Path p) throws IOException {
		try (BufferedReader br = Files.newBufferedReader(p)) {
			String header = br.readLine();
			String[] monthYear = header.substring(header.indexOf('(') + 1, header.indexOf(')')).split(",");
			this.yearMonth = YearMonth.of(Integer.valueOf(monthYear[1].trim()), Month.valueOf(monthYear[0].trim().toUpperCase()));

			Set<SalesEntry> sales = br.lines().filter(line -> line.startsWith("\""))
					.map(line -> line.replaceAll("[\"]", "").split(","))
					.filter(splittedLine -> splittedLine.length >= MIN_COLUMN_COUNT) //TODO really necesssary? bufferedReader should have read firstline lines()
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
					}).collect(Collectors.toSet());

			MonthlyInvoices monthlyInvoices = new MonthlyInvoices(yearMonth);
			sales.forEach(salesEntry -> monthlyInvoices.addSalesEntry(salesEntry));
			return monthlyInvoices;
		} catch (IOException e) {
			throw e;
		}
	}

	public RegionPlusCurrency getRegionPlusCurrency(String rpcString) {
		return RegionPlusCurrency.valueOf(rpcString.replace(' ', '_')
				.replace('-', '_')
				.replaceAll("[\\(\\)]", "")
				.toUpperCase());
	}

}
