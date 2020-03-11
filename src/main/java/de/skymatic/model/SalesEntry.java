package de.skymatic.model;

public class SalesEntry {

	private final RegionPlusCurrency rpc;
	private final int unitsSold;
	private final double earned;
	private final double pretaxSubtotal;
	private final double inputTax;
	private final double adjustments;
	private final double withholdingTax;
	private final double totalOwned;
	private final double exchangeRate;
	private final double proceeds;

	public SalesEntry(RegionPlusCurrency rpc, int unitsSold, double earned, double pretaxSubtotal, double inputTax, double adjustments, double withholdingTax, double totalOwned, double exchangeRate, double proceeds) {
		this.rpc = rpc;
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

	public RegionPlusCurrency getRpc() {
		return rpc;
	}

	public double getProceeds() {
		return proceeds;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(unitsSold).append(" ")
				.append(earned).append(" ")
				.append(pretaxSubtotal).append(" ")
				.append(inputTax).append(" ")
				.append(adjustments).append(" ")
				.append(withholdingTax).append(" ")
				.append(totalOwned).append(" ")
				.append(exchangeRate).append(" ")
				.append(proceeds).append(" ")
				.toString();

	}

}
