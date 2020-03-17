package de.skymatic.model;

public class AppleUtility {

	public static Subsidiary mapRegionPlusCurrencyToSubsidiary(RegionPlusCurrency rpc) {
		switch (rpc) {
			case CANADA_CAD:
				return Subsidiary.CANADA;
			case AUSTRALIA_AUD:
			case NEW_ZEALAND_NZD:
				return Subsidiary.AUSTRALIA;
			case BRAZIL_BRL:
			case COLOMBIA_COP:
			case MEXICO_MXN:
			case AMERICAS_USD:
				return Subsidiary.AMERICA;
			case JAPAN_JPY:
				return Subsidiary.JAPAN;
			case SWITZERLAND_CHF:
			case EURO_ZONE_EUR:
			//TODO: add rest of RPCs
				return Subsidiary.ROW;
			default:
				throw new IllegalArgumentException("No existing mapping for RegionPlusCurrency: " + rpc.toString());
		}
	}
}
