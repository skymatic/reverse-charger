package de.skymatic.appstore_invoices.model.apple;

import de.skymatic.appstore_invoices.model.Recipient;
import de.skymatic.appstore_invoices.model.Workflow;

public enum AppleSubsidiary implements Recipient {

	ROW(new String[]{"Apple Distribution International Limited", "Hollyhill Industrial Estate", "Hollyhill, Cork", "Republic of Ireland", "VAT ID: IE9700053D"}, true),
	NORTHAMERICA(new String[]{"Apple Inc.", "One Apple Park Way", "Cupertino, CA 95014", "USA"}, false),
	LATINAMERICA(new String[]{"Apple Services LATAM LLC", "One Apple Park Way, MS 169-5CL", "Cupertino, CA 95014", "USA"}, false),
	CANADA(new String[]{"Apple Canada Inc.", "120 Bremner Boulevard, Suite 1600", "Toronto, ON M5J 0A8", "Canada"}, false),
	JAPAN(new String[]{"iTunes K.K.", "〒 106-6140", "6-10-1 Roppongi, Minato-ku, Tokyo", "Japan"}, false),
	AUSTRALIA(new String[]{"Apple Pty Limited", "Level 3", "20 Martin Place", "Sydney NSW 2000", "Australia"}, false);

	private static final Workflow WORKFLOW = Workflow.APPLE;
	private final boolean reverseCharge;

	private String[] address;

	AppleSubsidiary(String[] address, boolean reverseRechargeEligible) {
		this.address = address;
		this.reverseCharge = reverseRechargeEligible;
	}

	@Override
	public String getAbbreviation() {
		return this.name();
	}

	@Override
	public String[] getAddress() {
		return address;
	}

	@Override
	public boolean isReverseChargeEligible(){
		return reverseCharge;
	}

	public static AppleSubsidiary mapFromRegionNCurrency(RegionNCurrency rpc) {
		switch (rpc) {
			case CANADA_CAD:
				return AppleSubsidiary.CANADA;
			case AUSTRALIA_AUD:
			case NEW_ZEALAND_NZD:
				return AppleSubsidiary.AUSTRALIA;
			case AMERICAS_USD:
				return AppleSubsidiary.NORTHAMERICA;
			case PERU_PEN:
			case BRAZIL_BRL:
			case CHILE_CLP:
			case COLOMBIA_COP:
			case MEXICO_MXN:
			case LATIN_AMERICA_AND_THE_CARIBBEAN_USD:
				return AppleSubsidiary.LATINAMERICA;
			case JAPAN_JPY:
				return AppleSubsidiary.JAPAN;
			case SWITZERLAND_CHF:
			case EURO_ZONE_EUR:
			case UNITED_ARAB_EMIRATES_AED:
			case BULGARIA_BGN:
			case CHINA_MAINLAND_CNY:
			case CZECH_REPUBLIC_CZK:
			case DENMARK_DKK:
			case EGYPT_EGP:
			case UNITED_KINGDOM_GBP:
			case HONG_KONG_HKD:
			case CROATIA_HRK:
			case HUNGARY_HUF:
			case INDONESIA_IDR:
			case ISRAEL_ILS:
			case INDIA_INR:
			case REPUBLIC_OF_KOREA_KRW:
			case KAZAKHSTAN_KZT:
			case MALAYSIA_MYR:
			case NIGERIA_NGN:
			case NORWAY_NOK:
			case PHILIPPINES_PHP:
			case PAKISTAN_PKR:
			case POLAND_PLN:
			case QATAR_QAR:
			case ROMANIA_RON:
			case RUSSIA_RUB:
			case SAUDI_ARABIA_SAR:
			case SWEDEN_SEK:
			case SINGAPORE_SGD:
			case THAILAND_THB:
			case TURKEY_TRY:
			case TÜRKIYE_TRY:
			case TAIWAN_TWD:
			case TANZANIA_TZS:
			case VIETNAM_VND:
			case REST_OF_WORLD_USD:
			case SOUTH_AFRICA_ZAR:
				return AppleSubsidiary.ROW;
			default:
				throw new IllegalArgumentException("No existing mapping for RegionPlusCurrency: " + rpc.toString());
		}
	}

}
