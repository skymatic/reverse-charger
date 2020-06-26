package de.skymatic.appstore_invoices.model.google;

import java.time.LocalDateTime;
import java.util.Locale;

public class GoogleSale {

	private String description;
	private LocalDateTime transactionDateTime;
	private String taxType;
	private String transactionType;
	private String refundType;
	private String productTitle;
	private String productId;
	private int productType;
	private String skuId;
	private String hardware;
	private Locale.IsoCountryCode buyerCountry;
	private String buyerState;
	private String buyerPostalCode;
	private String buyerCurrency;
	private double amountBuyerCurrency;
	private double currencyConversionRate;
	private String merchantCCurrency;
	private double amountMerchantCurrency;

	public String getDescription() {
		return description;
	}

	public LocalDateTime getTransactionDateTime() {
		return transactionDateTime;
	}

	public String getTaxType() {
		return taxType;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public String getRefundType() {
		return refundType;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public String getProductId() {
		return productId;
	}

	public int getProductType() {
		return productType;
	}

	public String getSkuId() {
		return skuId;
	}

	public String getHardware() {
		return hardware;
	}

	public Locale.IsoCountryCode getBuyerCountry() {
		return buyerCountry;
	}

	public String getBuyerState() {
		return buyerState;
	}

	public String getBuyerPostalCode() {
		return buyerPostalCode;
	}

	public String getBuyerCurrency() {
		return buyerCurrency;
	}

	public double getAmountBuyerCurrency() {
		return amountBuyerCurrency;
	}

	public double getCurrencyConversionRate() {
		return currencyConversionRate;
	}

	public String getMerchantCCurrency() {
		return merchantCCurrency;
	}

	public double getAmountMerchantCurrency() {
		return amountMerchantCurrency;
	}
}
