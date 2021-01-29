package de.skymatic.appstore_invoices.model;

public enum AdditionalItem {

	SALES("Sales"),
	TAX("Tax Withheld"),
	TAX_REFUNDS("TODO"),
	REFUNDS("Refunds"),
	FEES("Store Fees");

	private final String defaultDescription;

	AdditionalItem(String defaultDescription){
		this.defaultDescription = defaultDescription;
	}

}
