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

}
