package de.skymatic.appstore_invoices.model.apple;

import de.skymatic.appstore_invoices.model.Recipient;
import de.skymatic.appstore_invoices.model.Type;

public enum AppleSubsidiary implements Recipient {

	AMERICA(new String[]{"Apple Inc.", "One Apple Park Way", "Cupertino, CA 95014", "USA"}),
	AUSTRALIA(new String[]{"Apple Pty Limited", "Level 3", "20 Martin Place", "Sydney NSW 2000", "Australia"}),
	CANADA(new String[]{"Apple Canada Inc.", "120 Bremner Boulevard, Suite 1600", "Toronto, ON M5J 0A8", "Canada"}),
	JAPAN(new String[]{"iTunes K.K.", "〒 106-6140", "6-10-1 Roppongi, Minato-ku, Tokyo", "Japan"}),
	ROW(new String[]{"Apple Distribution International Limited", "Hollyhill Industrial Estate", "Hollyhill", "Cork", "Ireland"});

	private static final Type type = Type.APPLE;

	private String[] address;

	AppleSubsidiary(String[] address) {
		this.address = address;
	}

	@Override
	public String[] getAddress() {
		return address;
	}

}
