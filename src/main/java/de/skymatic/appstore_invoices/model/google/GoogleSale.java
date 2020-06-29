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

	public GoogleSale(String description, LocalDateTime transactionDateTime, String taxType, String transactionType, String refundType, String productTitle, String productId, int productType, String skuId, String hardware, Locale.IsoCountryCode buyerCountry, String buyerState, String buyerPostalCode, String buyerCurrency, double amountBuyerCurrency, double currencyConversionRate, String merchantCCurrency, double amountMerchantCurrency) {
		this.description = description;
		this.transactionDateTime = transactionDateTime;
		this.taxType = taxType;
		this.transactionType = transactionType;
		this.refundType = refundType;
		this.productTitle = productTitle;
		this.productId = productId;
		this.productType = productType;
		this.skuId = skuId;
		this.hardware = hardware;
		this.buyerCountry = buyerCountry;
		this.buyerState = buyerState;
		this.buyerPostalCode = buyerPostalCode;
		this.buyerCurrency = buyerCurrency;
		this.amountBuyerCurrency = amountBuyerCurrency;
		this.currencyConversionRate = currencyConversionRate;
		this.merchantCCurrency = merchantCCurrency;
		this.amountMerchantCurrency = amountMerchantCurrency;
	}

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
