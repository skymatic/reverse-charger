package de.skymatic.appstore_invoices.model.google;

import java.time.LocalDateTime;

public class GoogleSale {

	private final String description;
	private final LocalDateTime transactionDateTime;
	private final String taxType;
	private final GoogleTransactionType transactionType;
	private final String refundType;
	private final String productTitle;
	private final String productId;
	private final int productType;
	private final String skuId;
	private final String hardware;
	private final GoogleSubsidiary subsidiary;
	private final String buyerCountry;
	private final String buyerState;
	private final String buyerPostalCode;
	private final String buyerCurrency;
	private final double amountBuyerCurrency;
	private final double currencyConversionRate;
	private final String merchantCCurrency;
	private final double amountMerchantCurrency;

	public GoogleSale(String description, LocalDateTime transactionDateTime, String taxType, GoogleTransactionType transactionType, String refundType, String productTitle, String productId, int productType, String skuId, String hardware, GoogleSubsidiary subsidiary, String buyerCountry, String buyerState, String buyerPostalCode, String buyerCurrency, double amountBuyerCurrency, double currencyConversionRate, String merchantCCurrency, double amountMerchantCurrency) {
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
		this.subsidiary = subsidiary;
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

	public GoogleTransactionType getTransactionType() {
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

	public String getBuyerCountry() {
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

	public GoogleSubsidiary getSubsidiary() {
		return subsidiary;
	}
}
