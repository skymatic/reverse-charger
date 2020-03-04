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

	public double getProceeds(){
		return proceeds;
	}
}
