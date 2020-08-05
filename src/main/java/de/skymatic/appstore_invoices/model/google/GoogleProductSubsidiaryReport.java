package de.skymatic.appstore_invoices.model.google;

import java.math.BigDecimal;

public class GoogleProductSubsidiaryReport {

	private final String productTitle;

	private int units;
	private BigDecimal amount;
	private BigDecimal taxes;
	private BigDecimal fees;
	private BigDecimal refunds;
	private BigDecimal feeRefunds;
	private BigDecimal taxRefunds;

	public GoogleProductSubsidiaryReport(String productTitle) {
		this.productTitle = productTitle;
		this.units = 0;
		this.amount = BigDecimal.ZERO;
		this.taxes = BigDecimal.ZERO;
		this.fees = BigDecimal.ZERO;
		this.refunds = BigDecimal.ZERO;
		this.feeRefunds = BigDecimal.ZERO;
		this.taxRefunds = BigDecimal.ZERO;
	}

	public GoogleProductSubsidiaryReport(GoogleSale sale) {
		this(sale.getProductTitle());
		update(sale);
	}

	public GoogleProductSubsidiaryReport(String productTitle, int units, BigDecimal amount, BigDecimal taxes, BigDecimal fees, BigDecimal refunds, BigDecimal feeRefunds, BigDecimal taxRefunds) {
		this.productTitle = productTitle;
		this.units = units;
		this.amount = amount;
		this.taxes = taxes;
		this.fees = fees;
		this.refunds = refunds;
		this.feeRefunds = feeRefunds;
		this.taxRefunds = taxRefunds;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public int getUnits() {
		return units;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public BigDecimal getFees() {
		return fees;
	}

	public BigDecimal getRefunds() {
		return refunds;
	}

	public BigDecimal getFeeRefunds() {
		return feeRefunds;
	}

	public BigDecimal getTaxRefunds() {
		return taxRefunds;
	}

	public void update(GoogleSale sale) {
		if (!sale.getProductTitle().equals(this.productTitle)) {
			throw new IllegalArgumentException("Invalid sale entry for update: Wrong ProductTitle.");
		} else {
			final BigDecimal val = sale.getAmountMerchantCurrency();
			switch (sale.getTransactionType()) {
				case CHARGE -> {
					units++;
					amount = amount.add(val);
				}
				case TAX -> taxes = taxes.add(val);
				case GOOGLE_FEE -> fees = fees.add(val);
				case CHARGE_REFUND -> refunds = refunds.add(val);
				case TAX_REFUND -> taxRefunds = taxRefunds.add(val);
				case GOOGLE_FEE_REFUND -> feeRefunds = feeRefunds.add(val);
			}
		}
	}
}
