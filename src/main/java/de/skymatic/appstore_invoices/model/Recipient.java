package de.skymatic.appstore_invoices.model;

public interface Recipient {

	String getAbbreviation();

	String [] getAddress();

	boolean isReverseChargeEligible();

}
