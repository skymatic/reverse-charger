package de.skymatic.appstore_invoices.model.google;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static de.skymatic.appstore_invoices.model.google.GoogleTransactionType.CHARGE;
import static de.skymatic.appstore_invoices.model.google.GoogleTransactionType.GOOGLE_FEE;
import static de.skymatic.appstore_invoices.model.google.GoogleTransactionType.GOOGLE_FEE_REFUND;
import static de.skymatic.appstore_invoices.model.google.GoogleTransactionType.REFUND;
import static de.skymatic.appstore_invoices.model.google.GoogleTransactionType.TAX;
import static de.skymatic.appstore_invoices.model.google.GoogleTransactionType.TAX_REFUND;

/**
 * Unittests for {@link GoogleProductSubsidiaryReport}.
 *
 * @Note: Even thou it does not seem so, this class is threadsafe because every thread executing methods recivies its own instance.
 */
public class GoogleProductSubsidiaryReportTest {

	private GoogleProductSubsidiaryReport productReport;
	private int expectedUnits;
	private double expectedAmount;
	private double expectedTaxes;
	private double expectedFees;
	private double expectedRefunds;
	private double expectedTaxRefunds;
	private double expectedFeeRefunds;

	@BeforeEach
	public void init() {
		productReport = new GoogleProductSubsidiaryReport("MyProduct");
		expectedUnits = productReport.getUnits();
		expectedAmount = productReport.getAmount();
		expectedTaxes = productReport.getTaxes();
		expectedFees = productReport.getFees();
		expectedRefunds = productReport.getRefunds();
		expectedTaxRefunds = productReport.getTaxRefunds();
		expectedFeeRefunds = productReport.getFeeRefunds();
	}

	@Test
	public void updateWithChargeSaleDoesItsJob() {
		GoogleSale sale = createSale(productReport.getProductTitle(), CHARGE);
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedAmount = sale.getAmountBuyerCurrency() + productReport.getAmount();
		expectedUnits = 1 + productReport.getUnits();

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(expectedAmount, productReport.getAmount());
		Assertions.assertEquals(expectedTaxes, productReport.getTaxes());
		Assertions.assertEquals(expectedFees, productReport.getFees());
		Assertions.assertEquals(expectedRefunds, productReport.getRefunds());
		Assertions.assertEquals(expectedTaxRefunds, productReport.getTaxRefunds());
		Assertions.assertEquals(expectedFeeRefunds, productReport.getFeeRefunds());
	}

	@Test
	public void updateWithTaxSaleDoesItsJob() {
		GoogleSale sale = createSale(productReport.getProductTitle(), TAX);
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedTaxes = sale.getAmountBuyerCurrency() + productReport.getAmount();

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(expectedAmount, productReport.getAmount());
		Assertions.assertEquals(expectedTaxes, productReport.getTaxes());
		Assertions.assertEquals(expectedFees, productReport.getFees());
		Assertions.assertEquals(expectedRefunds, productReport.getRefunds());
		Assertions.assertEquals(expectedTaxRefunds, productReport.getTaxRefunds());
		Assertions.assertEquals(expectedFeeRefunds, productReport.getFeeRefunds());
	}

	@Test
	public void updateWithFeeSaleDoesItsJob() {
		GoogleSale sale = createSale(productReport.getProductTitle(), GOOGLE_FEE);
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedFees = sale.getAmountBuyerCurrency() + productReport.getFees();

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(expectedAmount, productReport.getAmount());
		Assertions.assertEquals(expectedTaxes, productReport.getTaxes());
		Assertions.assertEquals(expectedFees, productReport.getFees());
		Assertions.assertEquals(expectedRefunds, productReport.getRefunds());
		Assertions.assertEquals(expectedTaxRefunds, productReport.getTaxRefunds());
		Assertions.assertEquals(expectedFeeRefunds, productReport.getFeeRefunds());
	}

	@Test
	public void updateWithRefundSaleDoesItsJob() {
		GoogleSale sale = createSale(productReport.getProductTitle(), REFUND);
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedRefunds = sale.getAmountBuyerCurrency() + productReport.getRefunds();

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(expectedAmount, productReport.getAmount());
		Assertions.assertEquals(expectedTaxes, productReport.getTaxes());
		Assertions.assertEquals(expectedFees, productReport.getFees());
		Assertions.assertEquals(expectedRefunds, productReport.getRefunds());
		Assertions.assertEquals(expectedTaxRefunds, productReport.getTaxRefunds());
		Assertions.assertEquals(expectedFeeRefunds, productReport.getFeeRefunds());
	}

	@Test
	public void updateWithRefundTaxSaleDoesItsJob() {
		GoogleSale sale = createSale(productReport.getProductTitle(), TAX_REFUND);
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedTaxRefunds = sale.getAmountBuyerCurrency() + productReport.getTaxRefunds();

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(expectedAmount, productReport.getAmount());
		Assertions.assertEquals(expectedTaxes, productReport.getTaxes());
		Assertions.assertEquals(expectedFees, productReport.getFees());
		Assertions.assertEquals(expectedRefunds, productReport.getRefunds());
		Assertions.assertEquals(expectedTaxRefunds, productReport.getTaxRefunds());
		Assertions.assertEquals(expectedFeeRefunds, productReport.getFeeRefunds());
	}

	@Test
	public void updateWithRefundFeeSaleDoesItsJob() {
		GoogleSale sale = createSale(productReport.getProductTitle(), GOOGLE_FEE_REFUND);
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedFeeRefunds = sale.getAmountBuyerCurrency() + productReport.getFeeRefunds();

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(expectedAmount, productReport.getAmount());
		Assertions.assertEquals(expectedTaxes, productReport.getTaxes());
		Assertions.assertEquals(expectedFees, productReport.getFees());
		Assertions.assertEquals(expectedRefunds, productReport.getRefunds());
		Assertions.assertEquals(expectedTaxRefunds, productReport.getTaxRefunds());
		Assertions.assertEquals(expectedFeeRefunds, productReport.getFeeRefunds());
	}

	@Test
	public void updateWithWrongProductTitleThrowsException() {
		GoogleSale sale = createSale("OtherProduct", CHARGE);
		assert sale.getProductTitle() != productReport.getProductTitle();

		Assertions.assertThrows(IllegalArgumentException.class, () -> productReport.update(sale));
	}

	private static GoogleSale createSale(String productTitle, GoogleTransactionType type) {
		return new GoogleSale("1236",
				LocalDateTime.now(),
				"tax",
				type,
				"",
				productTitle,
				"my.company",
				0,
				"",
				"ultraPhone",
				"DE",
				"Bavaria",
				"80333",
				"EUR",
				1337.0,
				1.0,
				"EUR",
				1337.0
		);
	}
}
