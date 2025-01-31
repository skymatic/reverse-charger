package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.google.GoogleReport;
import de.skymatic.appstore_invoices.model.google.GoogleSale;
import de.skymatic.appstore_invoices.model.google.GoogleSubsidiary;
import de.skymatic.appstore_invoices.model.google.GoogleTransactionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

/**
 * Parses CSV files created/exported by the Google Play Store Financial Overview.
 * The CSV file contains the following columns (from left to right):
 * Description
 * Transaction Date
 * Transaction Time
 * Tax Type
 * Transaction Type
 * Refund Type
 * Product Title
 * Product id
 * Product Type
 * Sku Id
 * Hardware
 * Buyer Country
 * Buyer State
 * Buyer Postal Code
 * Buyer Currency
 * Amount (Buyer Currency)
 * Currency Conversion Rate
 * Merchant Currency
 * Amount (Merchant Currency)
 * <p>
 * Date and Time are merged into one field of type {@link LocalDateTime}.
 * The field transaction type is converted into an instance of {@link GoogleTransactionType}.
 * Additionally, the parsed object is extended with a field subsidiary of type {@link GoogleSubsidiary}.
 */
public class GoogleParser implements ReportParser {

	private static final DateTimeFormatter converter = DateTimeFormatter.ofPattern("LLL d, yyyy h:mm:ss a zzz", Locale.US);

	@Override
	public GoogleReport parse(Path p) throws IOException, ReportParseException, IllegalArgumentException {
		Collection<GoogleSale> rawSales = parseInternal(p);
		return new GoogleReport(rawSales.toArray(new GoogleSale[]{}));
	}

	private Collection<GoogleSale> parseInternal(Path p) throws IOException, ReportParseException, IllegalArgumentException {
		StringReference lastReadLine = new StringReference();

		try (BufferedReader br = Files.newBufferedReader(p)) {
			br.readLine(); //first line only contains the csv column headers

			return br.lines()
					.filter(l -> !l.startsWith(",")) //entries without a transaction id are discarded
					.map(lastReadLine::copyAndReturn)
					.map(this::splitWithCSVEscapes)
					.map(splittedLine -> {
						String description = splittedLine[0];
						LocalDateTime transactionDateTime = convertToLocalDateTime(splittedLine[1], splittedLine[2]);
						String taxType = splittedLine[3];
						GoogleTransactionType transactionType = GoogleTransactionType.valueOf(splittedLine[4].toUpperCase().replace(' ', '_'));
						String refundType = splittedLine[5];
						String productTitle = splittedLine[6];
						String productId = splittedLine[7];
						int productType = Integer.parseInt(splittedLine[8]);
						String skuId = splittedLine[9];
						String hardware = splittedLine[10];
						GoogleSubsidiary subsidiary = GoogleSubsidiary.fromISO2CountryCode(splittedLine[11]);
						String buyerCountry = splittedLine[11];
						String buyerState = splittedLine[12];
						String buyerPostalCode = splittedLine[13];
						//TODO: currency NEEDS to be always the same.
						String buyerCurrency = splittedLine[14];
						BigDecimal amountBuyerCurrency = new BigDecimal(splittedLine[15]);
						BigDecimal currencyConversionRate = new BigDecimal(splittedLine[16]);
						String merchantCCurrency = splittedLine[17];
						BigDecimal amountMerchantCurrency = new BigDecimal(splittedLine[18]);

						return new GoogleSale(description,
								transactionDateTime,
								taxType,
								transactionType,
								refundType,
								productTitle,
								productId,
								productType,
								skuId,
								hardware,
								subsidiary,
								buyerCountry,
								buyerState,
								buyerPostalCode,
								buyerCurrency,
								amountBuyerCurrency,
								currencyConversionRate,
								merchantCCurrency,
								amountMerchantCurrency);
					}).toList();

		} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new ReportParseException("Error parsing line: \n\t" + lastReadLine.get(), -1, e);
		}
	}

	private LocalDateTime convertToLocalDateTime(String date, String time) {
		return LocalDateTime.parse(date + " " + time, converter);
	}

	/**
	 * A single CSV entry can contain commas (,) if it is enclosed by quotes (").
	 * Therefore a simple line.split(",") does not do the job, we need something with a state.
	 *
	 * @param line the line to be splitted.
	 * @return an array containing the first 19 comma-separeted values of the csv entry
	 */
	private String[] splitWithCSVEscapes(String line) {
		String[] entriesOfInterest = new String[19];

		int currentPosInLine = 0;
		int entriesOfInteresIndex = 0;
		while(entriesOfInteresIndex < entriesOfInterest.length) {
			boolean entryIsEscaped = line.charAt(currentPosInLine) == '"';
			currentPosInLine = currentPosInLine + (entryIsEscaped? 1:0);
			int delimiter = line.indexOf( entryIsEscaped?'"':',', currentPosInLine); //currentPosInLine up to delimiter-1 is the content

			entriesOfInterest[entriesOfInteresIndex] = line.substring(currentPosInLine, delimiter);
			entriesOfInteresIndex++;

			currentPosInLine = delimiter + (entryIsEscaped? 2:1);
		}
		return entriesOfInterest;
	}

}
