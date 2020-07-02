package de.skymatic.appstore_invoices.model.google;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.skymatic.appstore_invoices.model.google.GoogleSaleFactory.*;
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
		GoogleSale sale = getTaxSale();
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
		GoogleSale sale = getFeeSale();
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
		GoogleSale sale = getRefundSale();
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
		GoogleSale sale = getTaxRefundSale();
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
		GoogleSale sale = getFeeRefundSale();
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
		GoogleSale sale = getSaleOfDifferentProduct();
		assert sale.getProductTitle() != productReport.getProductTitle();

		Assertions.assertThrows(IllegalArgumentException.class, () -> productReport.update(sale));
	}

}
