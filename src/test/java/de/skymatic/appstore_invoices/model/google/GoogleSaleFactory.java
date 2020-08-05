package de.skymatic.appstore_invoices.model.google;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static de.skymatic.appstore_invoices.model.google.GoogleTransactionType.*;

/**
 * This class is solely for testing purposes. It contains methods to creates different GoogleSale objects.
 */
class GoogleSaleFactory {

	private static LocalDateTime DEFAULT_TIME = LocalDateTime.now();
	private static String DEFAULT_PRODUCT = "MyProduct";
	private static GoogleSubsidiary DEFAULT_SUBSIDIARY = GoogleSubsidiary.ROW;

	private static GoogleSale createSale(LocalDateTime time, GoogleTransactionType type, String product, GoogleSubsidiary subsidiary) {
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
				subsidiary,
				"CC",
				"State",
				"1234",
				"EUR",
				BigDecimal.valueOf(12.34),
				BigDecimal.valueOf(1.0),
				"EUR",
				BigDecimal.valueOf(12.34)
		);
	}

	static GoogleSale getSale() {
		return getChargeSale();
	}

	static GoogleSale getSaleOfDifferentProduct() {
		String otherProduct = "YourProduct";
		assert otherProduct != DEFAULT_PRODUCT;

		return createSale(DEFAULT_TIME, CHARGE, otherProduct, DEFAULT_SUBSIDIARY);
	}

	static GoogleSale getSaleOfDifferentSubsidiary() {
		GoogleSubsidiary otherSubsidiary = GoogleSubsidiary.ASIA;
		assert otherSubsidiary != DEFAULT_SUBSIDIARY;

		return createSale(DEFAULT_TIME, CHARGE, DEFAULT_PRODUCT, otherSubsidiary);
	}

	static GoogleSale getSaleOfDifferentMonth() {
		LocalDateTime otherMonth = LocalDateTime.of(2014,12,31,12,59);
		assert otherMonth.getMonth() != DEFAULT_TIME.getMonth() || otherMonth.getYear() != DEFAULT_TIME.getYear();

		return createSale(otherMonth, CHARGE, DEFAULT_PRODUCT, DEFAULT_SUBSIDIARY);
	}

	static GoogleSale getChargeSale(){
		return createSale(DEFAULT_TIME, CHARGE, DEFAULT_PRODUCT, DEFAULT_SUBSIDIARY);
	}

	static GoogleSale getTaxSale(){
		return createSale(DEFAULT_TIME, TAX, DEFAULT_PRODUCT, DEFAULT_SUBSIDIARY);
	}

	static GoogleSale getFeeSale(){
		return createSale(DEFAULT_TIME, GOOGLE_FEE, DEFAULT_PRODUCT, DEFAULT_SUBSIDIARY);
	}

	static GoogleSale getRefundSale(){
		return createSale(DEFAULT_TIME, CHARGE_REFUND, DEFAULT_PRODUCT, DEFAULT_SUBSIDIARY);
	}
	static GoogleSale getTaxRefundSale(){
		return createSale(DEFAULT_TIME, TAX_REFUND, DEFAULT_PRODUCT, DEFAULT_SUBSIDIARY);
	}
	static GoogleSale getFeeRefundSale(){
		return createSale(DEFAULT_TIME, GOOGLE_FEE_REFUND, DEFAULT_PRODUCT, DEFAULT_SUBSIDIARY);
	}
}
