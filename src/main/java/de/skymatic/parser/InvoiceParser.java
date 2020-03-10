package de.skymatic.parser;

import de.skymatic.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Month;
import java.util.Hashtable;
import java.util.Map;

import static de.skymatic.model.AppleUtility.mapRegionPlusCurrencyToSubsidiary;

public class InvoiceParser implements CSVParser {

	private final static int MAX_MONTH_CHAR_LENGTH = 9;

	public MonthlyInvoices parseCSV(Path p) throws IOException {
		String csvFile = p.toString();
		BufferedReader br;
		String line;
		String csvSplitBy = ",";
		Month currentMonth = null;
		Map<Subsidiary, Invoice> invoices = new Hashtable<>();

		//MonthlyInvoices monthlyInvoices = null; //brought into scope for Exception Handling
		br = new BufferedReader(new FileReader(csvFile));
		//get the month
		if ((line = br.readLine()) != null) {
			String[] headline = line.split(csvSplitBy);
			String monthString = headline[0].substring(headline[0].length() - MAX_MONTH_CHAR_LENGTH); //"September" is the longest Month with 9 chars
			currentMonth = getMonth(monthString);
		}

		MonthlyInvoices monthlyInvoices = new MonthlyInvoices(currentMonth);
		//Skipping empty line and column descriptions
		br.readLine();
		br.readLine();

		while ((line = br.readLine()) != null) {
			String[] invoice = line.replaceAll("[\"]", "").split(csvSplitBy);
			//Not ideal

			if (invoice.length >= 1) { // skip over lines without any invoices

				RegionPlusCurrency rpc = getRegionPlusCurrency(invoice[0]);

				int units = Integer.parseInt(invoice[1]);
				double earned = Double.parseDouble(invoice[2]);
				double pretaxSubtotal = Double.parseDouble(invoice[3]);
				double inputTax = Double.parseDouble(invoice[4]);
				double adjustments = Double.parseDouble(invoice[5]);
				double withholdingTax = Double.parseDouble(invoice[6]);
				double totalOwned = Double.parseDouble(invoice[7]);
				double exchangeRate = Double.parseDouble(invoice[8]);
				double proceeds = Double.parseDouble(invoice[9]);
				Sales sales = new Sales(units, earned, pretaxSubtotal, inputTax, adjustments, withholdingTax, totalOwned, exchangeRate, proceeds);

				Subsidiary subsidiary = mapRegionPlusCurrencyToSubsidiary(rpc);
				if (invoices.containsKey(subsidiary)) {
					invoices.get(subsidiary).addSales(rpc, sales);
				} else invoices.put(subsidiary, new Invoice(rpc, sales));
			} else break;
		}
		invoices.forEach((s, i) -> monthlyInvoices.addEntry(i));
		return monthlyInvoices;
	}

	public Month getMonth(String monthString) {
		return Month.valueOf(monthString.substring(monthString.indexOf('(') + 1).toUpperCase());
	}

	public RegionPlusCurrency getRegionPlusCurrency(String rpcString) {
		return RegionPlusCurrency.valueOf(rpcString.replace(' ', '_')
				.replace('-', '_')
				.replaceAll("[\\(\\)]", "")
				.toUpperCase());
	}
}
