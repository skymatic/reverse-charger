package de.skymatic.appstore_invoices.model.google;

import java.util.regex.Pattern;

public class GoogleUtility {

	public static final Pattern ISO_COUNTRY_CODE = Pattern.compile("[A-Z][A-Z]");

	/**
	 * Maps an ISO 3166-1 alpha-2 country code to a {@link GoogleSubsidiary}.
	 * <p>
	 * The mapping is taken from the Google info <a href=https://support.google.com/googleplay/android-developer/answer/9306917>Supported locations for developer & merchant registration</a>
	 *
	 * @param country String consisting of two letters, representing an ISO 3166-1 alpha-2 country code.
	 * @return the {@link GoogleSubsidiary} responsible for sales in the given country
	 * @throws IllegalArgumentException if either {@code country} is not valid two character country code or if no mapping for the given country exists.
	 */
	public static GoogleSubsidiary mapCountryToSubsidiary(String country) {
		if (!ISO_COUNTRY_CODE.matcher(country).matches()) {
			throw new IllegalArgumentException("Input not a valid ISO 3166-1 alpha-2 country code.");
		} else {
			switch (country) {
				case "AG":
				case "AR":
				case "AW":
				case "BS":
				case "BB":
				case "BZ":
				case "BM":
				case "BO":
				case "BR":
				case "VG":
				case "CA":
				case "CL":
				case "CO":
				case "CR":
				case "DM":
				case "DO":
				case "EC":
				case "SV":
				case "FK":
				case "GD":
				case "GU":
				case "GT":
				case "GY":
				case "HT":
				case "HN":
				case "JM":
				case "MX":
					//TODO: Niederländische Antillen??
				case "NI":
				case "PW":
				case "PA":
				case "PY":
				case "PE":
				case "PR":
				case "KN":
				case "LC":
				case "VC":
				case "SR":
				case "TT":
				case "VI":
				case "US":
				case "UY":
				case "VE":
					return GoogleSubsidiary.AMERICA;
				case "AL":
				case "DZ":
				case "AO":
				case "AM":
				case "AT":
				case "AZ":
				case "BY":
				case "BE":
				case "BJ":
				case "BA":
				case "BW":
				case "BG":
				case "BF":
				case "BU":
				case "CM":
				case "CV":
				case "CF":
				case "CD":
				case "CI":
				case "HR":
				case "CY":
				case "CZ":
				case "DK":
				case "EG":
				case "GQ":
				case "ER":
				case "EE":
				case "SZ":
				case "FO":
				case "FI":
				case "FR":
				case "GA":
				case "GM":
				case "GE":
				case "DE":
				case "GH":
				case "GI":
				case "GR":
				case "GL":
				case "GN":
				case "GW":
				case "HU":
				case "IS":
				case "IL":
				case "IT":
				case "JO":
				case "KZ":
				case "KE":
				case "KW":
				case "LV":
				case "LB":
				case "LS":
				case "LR":
				case "LY":
				case "LI":
				case "LT":
				case "LU":
				case "MW":
				case "ML":
				case "MT":
				case "MR":
				case "MU":
				case "MD":
				case "MC":
				case "ME":
				case "MA":
				case "MZ":
				case "NA":
				case "NL":
				case "NE":
				case "NG":
				case "NO":
					//TODO: Former Yugosl. Republic of Macedonia / Macedonia (FYROM)
				case "OM":
				case "PS":
				case "PL":
				case "PT":
				case "QA":
				case "RO":
				case "RU":
				case "RW":
				case "SM":
				case "SA":
				case "SN":
				case "SC":
				case "SL":
				case "SK":
				case "SI":
				case "SO":
				case "ZA":
				case "ES":
				case "SE":
				case "CH":
				case "TJ":
				case "TZ":
				case "TG":
				case "TN":
				case "TR":
				case "TM":
				case "UG":
				case "UA":
				case "AE":
				case "GB":
				case "UZ":
				case "VA":
				case "YE":
				case "ZM":
				case "ZW":
					return GoogleSubsidiary.ROW;
				case "AS":
				case "AU":
				case "BD":
				case "VU":
				case "IO":
				case "BN":
				case "KH":
				case "CN":
				case "FM":
				case "FJ":
				case "HK":
				case "IN":
				case "ID":
				case "JP":
				case "KI":
				case "LA":
				case "MY":
				case "MV":
				case "MH":
				case "MN":
				case "MM":
				case "NP":
				case "NZ":
				case "MP":
				case "PK":
				case "PH":
				case "SG":
				case "KR":
				case "LK":
				case "TW":
				case "TH":
				case "VN":
					return GoogleSubsidiary.ASIA;
				default:
					throw new IllegalArgumentException("No mapping for country code " + country + " exists.");
			}
		}
	}

}
