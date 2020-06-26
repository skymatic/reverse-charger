package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Recipient;
import de.skymatic.appstore_invoices.model.Type;

public enum GoogleSubsidiary implements Recipient {

	//TODO: Give'em good names!
	SUB1(new String[]{"Google Commerce Limited", "Gordon House, Barrow Street",	"Dublin 4",	"Ireland"}),
	SUB2(new String[]{"Google LLC", "1600 Amphitheatre Parkway", "Mountain View, CA 94043", "USA"}),
	SUB3(new String[]{"Google Asia Pacific Pte. Limited", "70 Pasir Panjang Road, #03-71", "Mapletree Business City", "Singapore 117371"});

	private static final Type type = Type.GOOGLE;

	private String[] address;

	GoogleSubsidiary(String[] address) {
		this.address = address;
	}

	@Override
	public String getAbbreviation() {
		return this.name();
	}

	@Override
	public String[] getAddress() {
		return address;
	}
}
