package de.skymatic.model;

import java.util.Map;

public class Invoice {

	private Map<RegionPlusCurrency,Sales> salesPerCountryPlusCurrency;

	public double sum() {
		return salesPerCountryPlusCurrency.values().stream().mapToDouble(Sales::getProceeds).sum();
	}
}
