package de.skymatic.appstore_invoices.parser;

import java.text.ParseException;

public class ReportParseException extends ParseException {

	private Throwable cause;

	public ReportParseException(String msg, int offset, Throwable t) {
		super(msg, offset);
		this.cause = t;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}
}
