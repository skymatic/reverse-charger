package de.skymatic.model;

import java.util.Hashtable;
import java.util.Map;

public class Invoice {

	private Subsidiary subsidiary;
	private Map<RegionPlusCurrency,Sales> salesPerCountryPlusCurrency;

	public Invoice(RegionPlusCurrency rpc, Sales s){
		this.subsidiary = AppleUtility.mapRegionPlusCurrencyToSubsidiary(rpc);
		this.salesPerCountryPlusCurrency = new Hashtable<>();
		salesPerCountryPlusCurrency.put(rpc, s);
	}

	public Subsidiary getSubsidiary(){
		return subsidiary;
	}

	public double sum() {
		return salesPerCountryPlusCurrency.values().stream().mapToDouble(Sales::getProceeds).sum();
	}
}
