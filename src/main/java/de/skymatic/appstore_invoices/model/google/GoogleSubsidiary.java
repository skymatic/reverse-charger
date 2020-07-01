package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Recipient;
import de.skymatic.appstore_invoices.model.Workflow;

/**
 * Enumeration of real subsidiaries of Google Inc.
 */
public enum GoogleSubsidiary implements Recipient {

	ROW(new String[]{"Google Commerce Limited", "Gordon House, Barrow Street", "Dublin 4", "Ireland"}),
	AMERICA(new String[]{"Google LLC", "1600 Amphitheatre Parkway", "Mountain View, CA 94043", "USA"}),
	ASIA(new String[]{"Google Asia Pacific Pte. Limited", "70 Pasir Panjang Road, #03-71", "Mapletree Business City", "Singapore 117371"});

	private static final Workflow WORKFLOW = Workflow.GOOGLE;

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
