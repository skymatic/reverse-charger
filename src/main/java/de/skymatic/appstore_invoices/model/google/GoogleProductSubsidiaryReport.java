package de.skymatic.appstore_invoices.model.google;

public class GoogleProductSubsidiaryReport {

	private final String productTitle;

	private int units;
	private double amount;
	private double taxes;
	private double fees;
	private double refunds;
	private double feeRefunds;
	private double taxRefunds;

	public GoogleProductSubsidiaryReport(GoogleSale sale) {
		this.productTitle = sale.getProductTitle();
		initialize();
		update(sale);
	}

	public GoogleProductSubsidiaryReport(String productTitle) {
		this.productTitle = productTitle;
		initialize();
	}

	private void initialize() {
		this.units = 0;
		this.amount = 0;
		this.taxes = 0;
		this.fees = 0;
		this.refunds = 0;
		this.feeRefunds = 0;
		this.taxRefunds = 0;
	}

	public GoogleProductSubsidiaryReport(String productTitle, int units, double amount, double taxes, double fees, double refunds, double feeRefunds, double taxRefunds) {
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

	public double getAmount() {
		return amount;
	}

	public double getTaxes() {
		return taxes;
	}

	public double getFees() {
		return fees;
	}

	public double getRefunds() {
		return refunds;
	}

	public double getFeeRefunds() {
		return feeRefunds;
	}

	public double getTaxRefunds() {
		return taxRefunds;
	}

	public void update(GoogleSale sale) {
		if (sale.getProductTitle() != this.productTitle) {
			throw new IllegalArgumentException("Invalid sale entry for update: Wrong ProductTitle.");
		} else {
			final double val = sale.getAmountMerchantCurrency();
			switch (sale.getTransactionType()) {
				case CHARGE -> {
					units++;
					amount += val;
				}
				case TAX -> taxes += val;
				case GOOGLE_FEE -> fees += val;
				case REFUND -> refunds += val;
				case TAX_REFUND -> taxRefunds += val;
				case GOOGLE_FEE_REFUND -> feeRefunds += val;
			}
		}
	}
}
