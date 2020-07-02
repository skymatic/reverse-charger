package de.skymatic.appstore_invoices.parser;

import de.skymatic.appstore_invoices.model.google.GoogleReport;
import de.skymatic.appstore_invoices.model.google.GoogleSale;
import de.skymatic.appstore_invoices.model.google.GoogleSubsidiary;
import de.skymatic.appstore_invoices.model.google.GoogleTransactionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Parses CSV files created/exported by the Google Play Store Financial Overview.
 * The CSV file contains the following columns (from left to right):
 * Description
 * Transaction
 * Date
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
 *
 * Date and Time are merged into one field of type {@link LocalDateTime}.
 * The field transaction type is converted into an instance of {@link GoogleTransactionType}.
 * Additionally the parsed object is extended with a field subsidiary of type {@link GoogleSubsidiary}.
 */
public class GoogleParser implements ReportParser {

	private static final DateTimeFormatter converter = DateTimeFormatter.ofPattern("LLL d yyyy h:mm:ss a zzz", Locale.US);

	@Override
	public GoogleReport parse(Path p) throws IOException, ReportParseException, IllegalArgumentException {
		Collection<GoogleSale> rawSales = parseInternal(p);
		YearMonth yM = YearMonth.from(rawSales.iterator().next().getTransactionDateTime());
		return new GoogleReport(rawSales.toArray(new GoogleSale[]{}));
	}

	private Collection<GoogleSale> parseInternal(Path p) throws IOException, ReportParseException, IllegalArgumentException {
		StringReference lastReadLine = new StringReference();

		try (BufferedReader br = Files.newBufferedReader(p)) {
			br.readLine(); //first line only contains the csv column headers

			return br.lines()
					.map(line -> lastReadLine.copyAndReturn(line))
					.map(line -> line.split(","))
					.map(splittedLine -> {
						String description = splittedLine[0];
						LocalDateTime transactionDateTime = convertToLocalDateTime(splittedLine[1], splittedLine[2], splittedLine[3]);
						String taxType = splittedLine[4];
						GoogleTransactionType transactionType = GoogleTransactionType.valueOf(splittedLine[5].toUpperCase().replace(' ','_'));
						String refundType = splittedLine[6];
						String productTitle = splittedLine[7];
						String productId = splittedLine[8];
						int productType = Integer.parseInt(splittedLine[9]);
						String skuId = splittedLine[10];
						String hardware = splittedLine[11];
						GoogleSubsidiary subsidiary = GoogleSubsidiary.fromISO2CountryCode(splittedLine[12]);
						String buyerCountry = splittedLine[12];
						String buyerState = splittedLine[13];
						String buyerPostalCode = splittedLine[14];
						String buyerCurrency = splittedLine[15];
						double amountBuyerCurrency = Double.valueOf(splittedLine[16]);
						double currencyConversionRate = Double.valueOf(splittedLine[17]);
						String merchantCCurrency = splittedLine[18];
						double amountMerchantCurrency = Double.valueOf(splittedLine[19]);

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
					}).collect(Collectors.toList());

		} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new ReportParseException("Error parsing line: \n\t" + lastReadLine.get(), -1, e);
		}
	}

	private LocalDateTime convertToLocalDateTime(String monthAndDay, String year, String time) {
		return LocalDateTime.parse(monthAndDay.substring(1) + year.substring(0,year.length()-1) + " " + time, converter);
	}

}
