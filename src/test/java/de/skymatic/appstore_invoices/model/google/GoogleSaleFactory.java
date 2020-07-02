package de.skymatic.appstore_invoices.model.google;

import java.time.LocalDateTime;

import static de.skymatic.appstore_invoices.model.google.GoogleTransactionType.*;

/**
 * This class is solely for testing purposes. It contains methods to creates different GoogleSale objects.
 */
class GoogleSaleFactory {

	private static LocalDateTime DEFAULT_TIME = LocalDateTime.now();
	private static String DEFAULT_PRODUCT = "MyProduct";
	private static String DEFAULT_COUNTRY = "DE";

	private static GoogleSale createSale(LocalDateTime time, GoogleTransactionType type, String product, String country) {
		return new GoogleSale(
				"1234",
				time,
				"",
				type,
				"",
				product,
				"my.company",
				0,
				"",
				"ultraPhone",
				country,
				"State",
				"1234",
				"EUR",
				12.34,
				1.0,
				"EUR",
				12.34
		);
	}

	static GoogleSale getSale() {
		return getChargeSale();
	}

	static GoogleSale getSaleOfDifferentProduct() {
		String otherProduct = "YourProduct";
		assert otherProduct != DEFAULT_PRODUCT;

		return createSale(DEFAULT_TIME, CHARGE, otherProduct, DEFAULT_COUNTRY);
	}

	static GoogleSale getSaleOfDifferentSubsidiary() {
		String otherCountry = "US";
		assert GoogleUtility.mapCountryToSubsidiary(otherCountry) != GoogleUtility.mapCountryToSubsidiary(DEFAULT_COUNTRY);

		return createSale(DEFAULT_TIME, CHARGE, DEFAULT_PRODUCT, otherCountry);
	}

	static GoogleSale getSaleOfDifferentMonth() {
		LocalDateTime otherMonth = LocalDateTime.of(2014,12,31,12,59);
		assert otherMonth.getMonth() != DEFAULT_TIME.getMonth() || otherMonth.getYear() != DEFAULT_TIME.getYear();

		return createSale(otherMonth, CHARGE, DEFAULT_PRODUCT, DEFAULT_COUNTRY);
	}

	static GoogleSale getChargeSale(){
		return createSale(DEFAULT_TIME, CHARGE, DEFAULT_PRODUCT, DEFAULT_COUNTRY);
	}

	static GoogleSale getTaxSale(){
		return createSale(DEFAULT_TIME, TAX, DEFAULT_PRODUCT, DEFAULT_COUNTRY);
	}

	static GoogleSale getFeeSale(){
		return createSale(DEFAULT_TIME, GOOGLE_FEE, DEFAULT_PRODUCT, DEFAULT_COUNTRY);
	}

	static GoogleSale getRefundSale(){
		return createSale(DEFAULT_TIME, REFUND, DEFAULT_PRODUCT, DEFAULT_COUNTRY);
	}
}
