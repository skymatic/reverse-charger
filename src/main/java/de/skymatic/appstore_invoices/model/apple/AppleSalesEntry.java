package de.skymatic.appstore_invoices.model.apple;

import java.math.BigDecimal;

public class AppleSalesEntry {

	private final RegionPlusCurrency rpc;
	private final int unitsSold;
	private final BigDecimal earned;
	private final BigDecimal pretaxSubtotal;
	private final BigDecimal inputTax;
	private final BigDecimal adjustments;
	private final BigDecimal withholdingTax;
	private final BigDecimal totalOwned;
	private final BigDecimal exchangeRate;
	private final BigDecimal proceeds;

	public AppleSalesEntry(RegionPlusCurrency rpc, int unitsSold, BigDecimal earned, BigDecimal pretaxSubtotal, BigDecimal inputTax, BigDecimal adjustments, BigDecimal withholdingTax, BigDecimal totalOwned, BigDecimal exchangeRate, BigDecimal proceeds) {
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

	public BigDecimal getProceeds() {
		return proceeds;
	}

	public int getUnitsSold() {
		return unitsSold;
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
