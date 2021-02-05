package de.skymatic.appstore_invoices.template;

public enum ReverseChargeInfo {

	RC_INFO_EN("According to the reverse-charge regulation, tax liability transfers to the recipient of services."),
	RC_INFO_DE("Auf die Steuerschuldnerschaft des Leistungsempfängers gemäß § 13b UStG wird hingewiesen.");

	private final String text;

	ReverseChargeInfo(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
}
