package de.skymatic.appstore_invoices.model.google;

import java.util.Locale;

public class GoogleUtility {

	public static GoogleSubsidiary mapCountryToSubsidiary(Locale.IsoCountryCode country){
		switch (country){
			//TODO
			default -> throw new IllegalArgumentException("Unknown country.");
		}
	}

}
