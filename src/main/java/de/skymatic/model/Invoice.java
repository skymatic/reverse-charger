package de.skymatic.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Hashtable;
import java.util.Map;

public class Invoice {


	private final Subsidiary subsidiary;
	private final LocalDate startOfPeriod;
	private final LocalDate endOfPeriod;
	private final Map<RegionPlusCurrency, SalesEntry> salesPerCountryPlusCurrency;

	private LocalDate issueDate;
	private String numberString;

	public Invoice(String numberString, YearMonth yearMonth, LocalDate issueDate, SalesEntry s) {
		this.subsidiary = AppleUtility.mapRegionPlusCurrencyToSubsidiary(s.getRpc());
		this.numberString = numberString;
		this.issueDate = issueDate;
		this.startOfPeriod = yearMonth.atDay(1);
		this.endOfPeriod = yearMonth.atEndOfMonth();
		this.salesPerCountryPlusCurrency = new Hashtable<>();
		salesPerCountryPlusCurrency.put(s.getRpc(), s);
	}

	public Subsidiary getSubsidiary() {
		return subsidiary;
	}

	public double sum() {
		return salesPerCountryPlusCurrency.values().stream().mapToDouble(SalesEntry::getProceeds).sum();
	}

	public int getAmount() {
		return salesPerCountryPlusCurrency.values().stream().mapToInt(SalesEntry::getUnitsSold).sum();
	}

	void addSales(SalesEntry s) {
		final var rpc = s.getRpc();
		if (salesPerCountryPlusCurrency.containsKey(rpc)) {
			throw new IllegalArgumentException("RegionPlusCurrency " + rpc.name() + " already exists!");
		} else if (subsidiary != AppleUtility.mapRegionPlusCurrencyToSubsidiary(rpc)) {
			throw new IllegalArgumentException("RegionPlusCurrency " + rpc.name() + " does not belong to subsidiary " + this.subsidiary.name());
		} else {
			salesPerCountryPlusCurrency.put(rpc, s);
		}
	}

	void setNumberString(String numberString) {
		this.numberString = numberString;
	}

	public String getNumberString() {
		return numberString;
	}

	void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public LocalDate getStartOfPeriod() {
		return startOfPeriod;
	}

	public LocalDate getEndOfPeriod() {
		return endOfPeriod;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(salesPerCountryPlusCurrency).append("\n").toString();
	}
}
