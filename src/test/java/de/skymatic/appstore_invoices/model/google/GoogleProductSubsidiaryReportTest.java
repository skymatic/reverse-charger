package de.skymatic.appstore_invoices.model.google;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static de.skymatic.appstore_invoices.model.google.GoogleSaleFactory.*;

/**
 * Unittests for {@link GoogleProductSubsidiaryReport}.
 *
 * @Note: Even thou it does not seem so, this class is threadsafe because every thread executing methods recivies its own instance.
 */
public class GoogleProductSubsidiaryReportTest {

	private GoogleProductSubsidiaryReport productReport;
	private int expectedUnits;
	private BigDecimal expectedAmount;
	private BigDecimal expectedTaxes;
	private BigDecimal expectedFees;
	private BigDecimal expectedRefunds;
	private BigDecimal expectedTaxRefunds;
	private BigDecimal expectedFeeRefunds;

	@BeforeEach
	public void init() {
		productReport = new GoogleProductSubsidiaryReport(getSale());
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
		GoogleSale sale = getChargeSale();
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedAmount = sale.getAmountMerchantCurrency().add(productReport.getAmount());
		expectedUnits = 1 + productReport.getUnits();

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(0, expectedAmount.compareTo(productReport.getAmount()));
		Assertions.assertEquals(0, expectedTaxes.compareTo(productReport.getTaxes()));
		Assertions.assertEquals(0, expectedFees.compareTo( productReport.getFees()));
		Assertions.assertEquals(0, expectedRefunds.compareTo( productReport.getRefunds()));
		Assertions.assertEquals(0, expectedTaxRefunds.compareTo( productReport.getTaxRefunds()));
		Assertions.assertEquals(0, expectedFeeRefunds.compareTo( productReport.getFeeRefunds()));
	}

	@Test
	public void updateWithTaxSaleDoesItsJob() {
		GoogleSale sale = getTaxSale();
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedTaxes = sale.getAmountMerchantCurrency().add(productReport.getTaxes());

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(0, expectedAmount.compareTo( productReport.getAmount()));
		Assertions.assertEquals(0, expectedTaxes.compareTo( productReport.getTaxes()));
		Assertions.assertEquals(0, expectedFees.compareTo( productReport.getFees()));
		Assertions.assertEquals(0, expectedRefunds.compareTo( productReport.getRefunds()));
		Assertions.assertEquals(0, expectedTaxRefunds.compareTo( productReport.getTaxRefunds()));
		Assertions.assertEquals(0, expectedFeeRefunds.compareTo( productReport.getFeeRefunds()));
	}

	@Test
	public void updateWithFeeSaleDoesItsJob() {
		GoogleSale sale = getFeeSale();
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedFees = sale.getAmountMerchantCurrency().add(productReport.getFees());

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(0, expectedAmount.compareTo( productReport.getAmount()));
		Assertions.assertEquals(0, expectedTaxes.compareTo(productReport.getTaxes()));
		Assertions.assertEquals(0, expectedFees.compareTo( productReport.getFees()));
		Assertions.assertEquals(0, expectedRefunds.compareTo( productReport.getRefunds()));
		Assertions.assertEquals(0, expectedTaxRefunds.compareTo( productReport.getTaxRefunds()));
		Assertions.assertEquals(0, expectedFeeRefunds.compareTo( productReport.getFeeRefunds()));
	}

	@Test
	public void updateWithRefundSaleDoesItsJob() {
		GoogleSale sale = getRefundSale();
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedRefunds = sale.getAmountMerchantCurrency().add( productReport.getRefunds());

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(0, expectedAmount.compareTo( productReport.getAmount()));
		Assertions.assertEquals(0, expectedTaxes.compareTo( productReport.getTaxes()));
		Assertions.assertEquals(0, expectedFees.compareTo( productReport.getFees()));
		Assertions.assertEquals(0, expectedRefunds.compareTo( productReport.getRefunds()));
		Assertions.assertEquals(0, expectedTaxRefunds.compareTo( productReport.getTaxRefunds()));
		Assertions.assertEquals(0, expectedFeeRefunds.compareTo( productReport.getFeeRefunds()));
	}

	@Test
	public void updateWithRefundTaxSaleDoesItsJob() {
		GoogleSale sale = getTaxRefundSale();
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedTaxRefunds = sale.getAmountMerchantCurrency().add( productReport.getTaxRefunds());

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(0, expectedAmount.compareTo( productReport.getAmount()));
		Assertions.assertEquals(0, expectedTaxes.compareTo( productReport.getTaxes()));
		Assertions.assertEquals(0, expectedFees.compareTo( productReport.getFees()));
		Assertions.assertEquals(0, expectedRefunds.compareTo( productReport.getRefunds()));
		Assertions.assertEquals(0, expectedTaxRefunds.compareTo( productReport.getTaxRefunds()));
		Assertions.assertEquals(0, expectedFeeRefunds.compareTo( productReport.getFeeRefunds()));
	}

	@Test
	public void updateWithRefundFeeSaleDoesItsJob() {
		GoogleSale sale = getFeeRefundSale();
		assert sale.getProductTitle() == productReport.getProductTitle();

		expectedFeeRefunds = sale.getAmountMerchantCurrency().add(productReport.getFeeRefunds());

		productReport.update(sale);

		Assertions.assertEquals(expectedUnits, productReport.getUnits());
		Assertions.assertEquals(0, expectedAmount.compareTo( productReport.getAmount()));
		Assertions.assertEquals(0, expectedTaxes.compareTo( productReport.getTaxes()));
		Assertions.assertEquals(0, expectedFees.compareTo( productReport.getFees()));
		Assertions.assertEquals(0, expectedRefunds.compareTo( productReport.getRefunds()));
		Assertions.assertEquals(0, expectedTaxRefunds.compareTo( productReport.getTaxRefunds()));
		Assertions.assertEquals(0, expectedFeeRefunds.compareTo( productReport.getFeeRefunds()));
	}

	@Test
	public void updateWithWrongProductTitleThrowsException() {
		GoogleSale sale = getSaleOfDifferentProduct();
		assert sale.getProductTitle() != productReport.getProductTitle();

		Assertions.assertThrows(IllegalArgumentException.class, () -> productReport.update(sale));
	}

}
