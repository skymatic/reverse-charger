package de.skymatic.appstore_invoices.parser;

class StringReference {

	private String s = "";

	public String copyAndReturn(String s) {
		this.s = s;
		return s;
	}

	public String get() {
		return s;
	}
}
