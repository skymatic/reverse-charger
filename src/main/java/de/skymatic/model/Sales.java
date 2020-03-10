package de.skymatic.model;

public class Sales {

	private int unitsSold;
	private double earned;
	private double pretaxSubtotal;
	private double inputTax;
	private double adjustments;
	private double withholdingTax;
	private double totalOwned;
	private double exchangeRate;
	private double proceeds;

	public Sales(int unitsSold, double earned, double pretaxSubtotal, double inputTax, double adjustments, double withholdingTax, double totalOwned, double exchangeRate, double proceeds) {
		this.unitsSold = unitsSold;
		this.earned = earned;
		this.pretaxSubtotal = pretaxSubtotal;
		this.inputTax = inputTax;
		this.adjustments = adjustments;
		this.withholdingTax = withholdingTax;
		this.totalOwned = totalOwned;
		this.exchangeRate = exchangeRate;
		this.proceeds = proceeds;
	}

	public double getProceeds() {
		return proceeds;
	}
}
