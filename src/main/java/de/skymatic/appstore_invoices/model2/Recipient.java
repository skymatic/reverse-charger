package de.skymatic.appstore_invoices.model2;

public interface Recipient {

	String getAbbreviation();

	String [] getAddress();

	boolean isReverseChargeEligible();

}
