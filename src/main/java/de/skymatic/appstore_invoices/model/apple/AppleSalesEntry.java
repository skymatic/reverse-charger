package de.skymatic.appstore_invoices.model.apple;

import java.math.BigDecimal;

public class AppleSalesEntry {

	private final RegionNCurrency rpc;
	private final int unitsSold;
	private final BigDecimal earned;
	private final BigDecimal pretaxSubtotal;
	private final BigDecimal inputTax;
	private final BigDecimal adjustments;
	private final BigDecimal withholdingTax;
	private final BigDecimal totalOwned;
	private final BigDecimal exchangeRate;
	private final BigDecimal proceeds;
	private final String bankAccountCurrency;

	public AppleSalesEntry(RegionNCurrency rpc, int unitsSold, BigDecimal earned, BigDecimal pretaxSubtotal, BigDecimal inputTax, BigDecimal adjustments, BigDecimal withholdingTax, BigDecimal totalOwned, BigDecimal exchangeRate, BigDecimal proceeds, String bankAccountCurrency) {
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
		this.bankAccountCurrency = bankAccountCurrency;
	}

	public RegionNCurrency getRpc() {
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
				.append(bankAccountCurrency)
				.toString();

	}

	public BigDecimal getEarned() {
		return earned;
	}

	public BigDecimal getPretaxSubtotal() {
		return pretaxSubtotal;
	}

	public BigDecimal getInputTax() {
		return inputTax;
	}

	public BigDecimal getAdjustments() {
		return adjustments;
	}

	public BigDecimal getWithholdingTax() {
		return withholdingTax;
	}

	public BigDecimal getTotalOwned() {
		return totalOwned;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public String getBankAccountCurrency() {
		return bankAccountCurrency;
	}
}
