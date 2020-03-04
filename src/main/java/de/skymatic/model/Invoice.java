package de.skymatic.model;

import java.util.Map;

public class Invoice {

	private Map<RegionPlusCurrency,Sales> salesPerCountry;

	public double sum() {
		return salesPerCountry.values().stream().mapToDouble(s -> s.getTotalAmount()).sum();
	}
}
