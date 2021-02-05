package de.skymatic.appstore_invoices.model;

public enum AdditionalItem {

	SALES("Sales"),
	TAX_WITHHELD("Tax Withheld"),
	TAX_REFUNDS("Tax Refunds"),
	REFUNDS("Refunds"),
	FEES("Distribution Fees");

	private final String defaultDescription;

	AdditionalItem(String defaultDescription){
		this.defaultDescription = defaultDescription;
	}

}
