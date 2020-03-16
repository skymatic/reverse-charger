package de.skymatic.model;

import java.util.Hashtable;
import java.util.Map;

public class Invoice {

	private Subsidiary subsidiary;
	private Map<RegionPlusCurrency, SalesEntry> salesPerCountryPlusCurrency;

	public Invoice(SalesEntry s) {
		this.subsidiary = AppleUtility.mapRegionPlusCurrencyToSubsidiary(s.getRpc());
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

	public void addSales(SalesEntry s) {
		final var rpc = s.getRpc();
		if (salesPerCountryPlusCurrency.containsKey(rpc)) {
			throw new IllegalArgumentException("RegionPlusCurrency already exists!");
		} else if (subsidiary != AppleUtility.mapRegionPlusCurrencyToSubsidiary(rpc)) {
			throw new IllegalArgumentException("RegionPlusCurrency does not belong to this Subsidiary");
		} else {
			salesPerCountryPlusCurrency.put(rpc, s);
		}
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		return sb.append(salesPerCountryPlusCurrency).append("\n").toString();
	}
}
