package de.skymatic.model;

public class AppleUtility {

	public static Subsidiary mapRegionPlusCurrencyToSubsidiary(RegionPlusCurrency rpc) {
		switch (rpc) {
			//TODO:
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
			default:
				return Subsidiary.ROW;
		}
	}
}
