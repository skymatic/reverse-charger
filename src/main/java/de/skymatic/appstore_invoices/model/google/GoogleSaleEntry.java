package de.skymatic.appstore_invoices.model.google;

import java.util.Locale;

public class GoogleSaleEntry {

	private Locale.IsoCountryCode country;

	private String productTitle;

	private double charges;
	private double taxes;
	private double fees;
	private double refunds;
	private double feeRefunds;
	private double taxRefunds;

	public GoogleSaleEntry(Locale.IsoCountryCode country, String productTitle){
		this.country = country;
		this.productTitle = productTitle;
		this.charges = 0;
		this.taxes = 0;
		this.fees = 0;
		this.refunds = 0;
		this.feeRefunds = 0;
		this.taxRefunds = 0;
	}

	public GoogleSaleEntry(Locale.IsoCountryCode country, String productTitle, double charges, double taxes, double fees, double refunds, double feeRefunds, double taxRefunds) {
		this.country = country;
		this.productTitle = productTitle;
		this.charges = charges;
		this.taxes = taxes;
		this.fees = fees;
		this.refunds = refunds;
		this.feeRefunds = feeRefunds;
		this.taxRefunds = taxRefunds;
	}

	public Locale.IsoCountryCode getCountry() {
		return country;
	}

	public String getProductTitle() {
		return productTitle;
	}

	public double getCharges() {
		return charges;
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
}
