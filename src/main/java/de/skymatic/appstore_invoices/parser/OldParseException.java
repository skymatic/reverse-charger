package de.skymatic.appstore_invoices.parser;

@Deprecated
public class OldParseException extends Exception {

	private final Throwable cause;
	private final String message;

	public OldParseException(String message, Throwable cause) {
		this.message = message;
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}

	public String getMessage() {
		return message;
	}

}
